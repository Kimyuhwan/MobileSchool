package com.example.MobileSchool.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.example.MobileSchool.Model.MyInfo;
import com.google.gson.Gson;

/**
 * User: yuhwan
 * Date: 13. 10. 11
 * Time: 오후 9:24
 */
public class AccountManager {

    private String TAG = Constants.TAG;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public AccountManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(Constants.TAG, 0);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public void saveMyInfo(MyInfo myInfo) {
        String json = gson.toJson(myInfo);
        editor.putString("myInfo", json);
        editor.commit();
    }

    public void removeMyInfo() {
        editor.remove("myInfo");
        editor.commit();
    }

    public MyInfo getMyInfo() {
        String json = sharedPreferences.getString("myInfo", Constants.SHAREDPREFERENCES_EMPTY);
        if(json.equals(Constants.SHAREDPREFERENCES_EMPTY))
            return null;
        else
            return gson.fromJson(json, MyInfo.class);
    }

    public String getUniqueId() {
        MyInfo myInfo = getMyInfo();
        if(myInfo == null)
            return Constants.SHAREDPREFERENCES_EMPTY;
        else {
            String userId = getMyInfo().getUnique_id();
            Log.d(TAG, "GetUserId : " + userId);
            return userId;
        }
    }

    public boolean isStudent() {
        MyInfo myInfo = getMyInfo();
        if(myInfo.getType().equals("teacher"))
            return false;
        else
            return true;
    }
}
