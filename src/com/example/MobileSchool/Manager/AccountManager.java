package com.example.MobileSchool.Manager;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.MobileSchool.Utils.Constants;

/**
 * User: yuhwan
 * Date: 13. 10. 11
 * Time: 오후 9:24
 */
public class AccountManager {

    private String TAG = Constants.TAG;

    private SharedPreferences sharedPreferences;

    public AccountManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(Constants.TAG, 0);
    }

    public void saveAccount(String id, String SId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHAREDPREFERENCES_USER_ID, id);
        editor.putString(Constants.SHAREDPREFERENCES_USER_SID, SId);
        editor.commit();
    }

    public void setQueueId(String queueId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHAREDPREFERENCES_QUEUE_ID, queueId);
        editor.commit();
    }

    public void setOppositePhoneNumber(String phoneNumber) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHAREDPREFERENCES_PHONE_NUMBER, phoneNumber);
        editor.commit();
    }

    public String getUserId() {
        return sharedPreferences.getString(Constants.SHAREDPREFERENCES_USER_ID, Constants.ACCOUNT_EMPTY);
    }

    public String getUserSId() {
        return sharedPreferences.getString(Constants.SHAREDPREFERENCES_USER_SID, Constants.ACCOUNT_EMPTY);
    }

    public String getQueueID() {
        return sharedPreferences.getString(Constants.SHAREDPREFERENCES_QUEUE_ID, Constants.ACCOUNT_EMPTY);
    }

    public String getOppositePhoneNubmer() {
        return sharedPreferences.getString(Constants.SHAREDPREFERENCES_PHONE_NUMBER, Constants.ACCOUNT_EMPTY);
    }

    public boolean isStudent() {
        String userId = sharedPreferences.getString(Constants.SHAREDPREFERENCES_USER_ID, Constants.ACCOUNT_EMPTY);
        if(userId.contains("t"))
            return false;
        else
            return true;
    }
}
