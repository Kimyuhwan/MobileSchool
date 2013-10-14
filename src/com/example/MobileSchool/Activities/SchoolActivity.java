package com.example.MobileSchool.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.MobileSchool.Activities.SchoolFragment.HistoryFragment;
import com.example.MobileSchool.Activities.SchoolFragment.HomeFragment;
import com.example.MobileSchool.Activities.SchoolFragment.SettingFragment;
import com.example.MobileSchool.Communication.AjaxCallSender;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.Constants;


/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 13.
 * Time: 오후 4:36
 */

public class SchoolActivity extends Activity {

    private String TAG = Constants.TAG;

    private String[] planetTitles;
    private CharSequence title;

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private AjaxCallSender ajaxCallSender;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_activity);
        Log.d(TAG, "SchoolActivity : onCreate");
        ajaxCallSender = new AjaxCallSender(this);
        ajaxCallSender.appOnUpdate();

        _initDrawer();
        _changeFragment(0, new HomeFragment());
    }

    private void _initDrawer() {

        // Set Drawer
        planetTitles = getResources().getStringArray(R.array.planets_array);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, planetTitles));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

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

            public void onDrawerClosed(View view) {getActionBar().setTitle(title);}
            public void onDrawerOpened(View drawerView) {getActionBar().setTitle(title);}
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
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
        // true, then it has handled the app icon touch event
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0: _changeFragment(position, new HomeFragment()); break;
                case 1: _changeFragment(position, new HistoryFragment()); break;
                case 2: _changeFragment(position, new SettingFragment()); break;
                default: Log.d(TAG, "DrawerItemClickListener : other click"); break;
            }
        }
    }

    private void _changeFragment(int position, Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        drawerList.setItemChecked(position, true);
        setTitle(planetTitles[position]);
        drawerLayout.closeDrawer(drawerList);
    }

}
