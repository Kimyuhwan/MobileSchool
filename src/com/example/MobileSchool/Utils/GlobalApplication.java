package com.example.MobileSchool.Utils;

import android.app.Application;
import android.support.v4.app.Fragment;
import com.example.MobileSchool.Fragment.HomeFragment;
import com.example.MobileSchool.R;
import com.example.MobileSchool.SchoolActivity;

/**
 * User: yuhwan
 * Date: 13. 10. 16
 * Time: 오후 11:35
 */
public class GlobalApplication extends Application {

    private SchoolActivity schoolActivity;

    private String fragmentName = null;
    private Fragment fragment = null;

    private int drawerType = -1;

    private String targetStudentId;
    private String targetTeacherId;

    public void setSchoolActivity(SchoolActivity schoolActivity) {
        this.schoolActivity = schoolActivity;
    }

    public SchoolActivity getSchoolActivity() {
        return schoolActivity;
    }

    public void setFragment(String fragmentName, Fragment fragment) {
        this.fragmentName = fragmentName;
        this.fragment = fragment;
    }

    public void freeFragment() {
        fragmentName = null;
        fragment = null;
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

    public void setDrawerType(int type) {
        drawerType = type;
    }

    public int getDrawerType() {
        if(drawerType == -1)
            return R.array.Home_menu_array;
        else
            return drawerType;
    }

    private int _getPosition(String fragmentName) {
        String[] menu_array = getResources().getStringArray(getDrawerType());
        int index = 0;
        for(String menu : menu_array) {
            if(fragmentName.equals(menu))
                 return index;
            index++;
        }
        return 0;
    }

    // Target Information
    public void setTargetStudentId(String oppositeId) {
        this.targetStudentId = oppositeId;
    }

    public String getTargetStudentId() {
        return targetStudentId;
    }

    public void setTargetTeacherId(String oppositeId) {
        this.targetTeacherId = oppositeId;
    }

    public String getTargetTeacherId() {
        return targetTeacherId;
    }
}
