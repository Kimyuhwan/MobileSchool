package com.example.MobileSchool.Utils;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.example.MobileSchool.Fragment.HomeFragment;
import com.example.MobileSchool.Model.DialogueItem;
import com.example.MobileSchool.Model.PartnerInfo;
import com.example.MobileSchool.R;
import com.example.MobileSchool.SchoolActivity;

/**
 * User: yuhwan
 * Date: 13. 10. 16
 * Time: 오후 11:35
 */
public class GlobalApplication extends Application {

    private String TAG = Constants.TAG;

    // Data Object
    private SchoolActivity schoolActivity;
    private AccountManager accountManager;
    private ContentManager contentManager;
    private PartnerInfo partnerInfo;

    // Status Check Variable
    private boolean isSchoolActivityFront;
    private String fragmentName = null;
    private Fragment fragment = null;
    private boolean classConnected = false;
    private int drawerType = -1;

    // Temporal storage
    private int waitingTime;
    private DialogueItem[] entryItems;

    @Override
    public void onCreate() {
        super.onCreate();
        if(accountManager == null)
            accountManager = new AccountManager(this);
        if(contentManager == null)
            contentManager = new ContentManager(this);
    }


    // Data Object Functions
    public void setSchoolActivity(SchoolActivity schoolActivity) {
        this.schoolActivity = schoolActivity;
    }

    public SchoolActivity getSchoolActivity() {
        return schoolActivity;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public ContentManager getContentManager() {
        return contentManager;
    }

    public void setPartnerInfo(PartnerInfo partnerInfo) {
        this.partnerInfo = partnerInfo;
    }

    public PartnerInfo getPartnerInfo() {
        return this.partnerInfo;
    }


    // Status Check Functions
    public boolean isSchoolActivityFront() {
        return isSchoolActivityFront;
    }

    public void setSchoolActivityFront(boolean schoolActivityFront) {
        isSchoolActivityFront = schoolActivityFront;
    }

    public void setFragment(String fragmentName, Fragment fragment) {
        this.fragmentName = fragmentName;
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        if(fragment == null)
            return new HomeFragment();
        else
            return fragment;
    }

    public int getFragmentPosition() {
        if(fragmentName == null)
            return 0;
        else
            return _getPosition(fragmentName);
    }

    private int _getPosition(String fragmentName) {
        Log.d(TAG, "Position fragmentName : " + fragmentName);
        String[] menu_array = getResources().getStringArray(getDrawerType());
        int index = 0;
        for(String menu : menu_array) {
            Log.d(TAG, "Position drawerType : " + menu);
            if(fragmentName.equals(menu))
                return index;
            index++;
        }
        return 0;
    }

    public void freeFragment() {
        fragmentName = null;
        fragment = null;
    }

    public void setDrawerType(int type) {
        this.drawerType = type;
    }

    public int getDrawerType() {
        if(drawerType == -1)
            return R.array.Home_menu_array;
        else
            return drawerType;
    }

    public boolean isClassConnected() {
        return classConnected;
    }

    public void setClassConnected(boolean classConnected) {
        this.classConnected = classConnected;
    }


    //  Temporal storage Functions
    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public DialogueItem[] getEntryItems() {
        return entryItems;
    }

    public void setEntryItems(DialogueItem[] entryItems) {
        this.entryItems = entryItems;
    }


}
