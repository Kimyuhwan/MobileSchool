package com.example.MobileSchoolDev;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.bugsense.trace.BugSenseHandler;
import com.example.MobileSchoolDev.BroadCastReceiver.ManagerRegistrationService;
import com.example.MobileSchoolDev.Fragment.*;
import com.example.MobileSchoolDev.Communication.AjaxCallSender;
import com.example.MobileSchoolDev.Model.Content;
import com.example.MobileSchoolDev.Utils.*;
import com.parse.ParseAnalytics;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 13.
 * Time: 오후 4:36
 */

public class SchoolActivity extends FragmentActivity implements BaseMethod{

    private String TAG = Constants.TAG;

    private String[] menuTitles;
    private CharSequence title;

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private AjaxCallSender ajaxCallSender;
    private GlobalApplication globalApplication;
    private AccountManager accountManager;
    private ContentManager contentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);
        Log.d(TAG, "SchoolActivity : onCreate");
        globalApplication = (GlobalApplication) getApplicationContext();
        globalApplication.setSchoolActivity(this);
        accountManager = globalApplication.getAccountManager();
        contentManager = globalApplication.getContentManager();
        ajaxCallSender = new AjaxCallSender(getApplicationContext(), this);

        ajaxCallSender.appOnUpdate();

        // Parse Analytics
        ParseAnalytics.trackAppOpened(getIntent());

        // Bug Sense
        BugSenseHandler.initAndStartSession(this, Constants.BUGSENSE_KEY);

        //Check Service
        if(ManagerRegistrationService.managerRegistrationService == null)
            startService(new Intent(getApplicationContext(), ManagerRegistrationService.class));

        initDrawer();
        initFragment();
        _initFont();
    }

    private void _initFont() {
        ViewGroup container = (DrawerLayout) findViewById(R.id.drawer_layout);
        globalApplication.setAppFont(container);
    }

    public void initDrawer() {
        int drawerType = globalApplication.getDrawerType();

        menuTitles = getResources().getStringArray(drawerType);

        ListView.OnItemClickListener onItemClickListener;
        if(drawerType == R.array.Home_menu_array)
            onItemClickListener = new HomeDrawerItemClickListener();
        else if(drawerType == R.array.Waiting_menu_array)
            onItemClickListener = new WaitingDrawerItemClickListener();
        else
            onItemClickListener = new ClassDrawerItemClickListener();

        _setDrawer(onItemClickListener);
    }

    private void _setDrawer(ListView.OnItemClickListener onItemClickListener) {
        // Set Drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.item_drawer, menuTitles));
        drawerList.setOnItemClickListener(onItemClickListener);

        // Set ActionBar
        title = getTitle();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    public void initFragment() {
        _changeFragment(globalApplication.getFragmentPosition(), globalApplication.getFragment());
    }

    @Override
    public void onBackPressed() {
        String fragmentName = globalApplication.getFragmentName();

        if(globalApplication != null && fragmentName != null && (fragmentName.equals("Guide") || fragmentName.equals("Profile"))) {
            globalApplication.setDrawerType(R.array.Home_menu_array);
            globalApplication.setFragment("Home", new HomeFragment());
            globalApplication.getSchoolActivity().initDrawer();
            globalApplication.getSchoolActivity().initFragment();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app main_icon touch event
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        globalApplication.freeFragment();
        super.onStop();
    }

    @Override
    public void onResume() {
        globalApplication.setSchoolActivityFront(true);
        super.onResume();
    }

    @Override
    public void onPause() {
        globalApplication.setSchoolActivityFront(false);
        globalApplication.setFragment("Home", new HomeFragment());
        globalApplication.setDrawerType(R.array.Home_menu_array);
        super.onPause();
    }

    private void _changeFragment(int position, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        drawerList.setItemChecked(position, true);
        setTitle(menuTitles[position]);
        drawerLayout.closeDrawer(drawerList);
    }

    @Override
    public void handleSocketMessage(String message) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {
        Log.d(TAG, "SchoolActivity : handleAjaxCallBack Object => " + object);
        try {
            if(!object.isNull("code")) {
                String code = object.getString("code");
                if(code.equals(Constants.CODE_DAILY_DIALOGUE)) {
                    Content content = new Content(object.getString("expression"), object.getString("script"), object.getString("tip"));
                    contentManager.saveTodayContent(content);
                }
            }
        } catch (JSONException e) { e.printStackTrace(); }
    }

    @Override
    public void handlePush(JSONObject object) {

    }

    private class HomeDrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0: _changeFragment(position, new HomeFragment()); break;
                case 1: _changeFragment(position, new MyInfoFragment()); break;
                case 2: _changeFragment(position, new ContactFragment()); break;
                default: Log.d(TAG, "DrawerItemClickListener : other click"); break;
            }
        }
    }

    private class WaitingDrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0: _changeFragment(position, new GuideFragment()); break;
                case 1: _changeFragment(position, new ProfileFragment()); break;
                default: Log.d(TAG, "DrawerItemClickListener : other click"); break;
            }
        }
    }

    private class ClassDrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0: _changeFragment(position, new ScriptFragment()); break;
                default: Log.d(TAG, "DrawerItemClickListener : other click"); break;
            }
        }
    }

}
