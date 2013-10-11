package com.example.MobileSchool.Manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.example.MobileSchool.EntryActivity;
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
        Log.d(TAG,"AccountManager current context : " + context);
        this.sharedPreferences = context.getSharedPreferences(Constants.TAG, 0);
    }

    public void saveAccount(String id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHAREDPREFERENCES_USER_ID, id);
        editor.commit();
    }

    public String getUserId() {
        return sharedPreferences.getString(Constants.SHAREDPREFERENCES_USER_ID, Constants.ACCOUNT_EMPTY);
    }
}
