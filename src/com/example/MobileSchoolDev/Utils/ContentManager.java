package com.example.MobileSchoolDev.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.MobileSchoolDev.Model.Content;
import com.google.gson.Gson;
import org.joda.time.LocalDate;

/**
 * User: yuhwan
 * Date: 13. 10. 26
 * Time: 오전 12:06
 */
public class ContentManager {
    private String TAG = Constants.TAG;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public ContentManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(Constants.TAG, 0);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public boolean existTodayDialog() {
        LocalDate localDate = new LocalDate();
        String json = sharedPreferences.getString(Constants.CONTENTS + localDate, Constants.SHAREDPREFERENCES_EMPTY);
        if(json.equals(Constants.SHAREDPREFERENCES_EMPTY))
            return false;
        else
            return true;
    }

    public void saveTodayContent(Content content) {
        LocalDate localDate = new LocalDate();
        String json = gson.toJson(content);
        editor.putString(Constants.CONTENTS + localDate, json);
        editor.commit();
    }

    public Content getTodayContent() {
        LocalDate localDate = new LocalDate();
        String json = sharedPreferences.getString(Constants.CONTENTS + localDate, Constants.SHAREDPREFERENCES_EMPTY);
        if(json.equals(Constants.SHAREDPREFERENCES_EMPTY))
            return null;
        else
            return gson.fromJson(json, Content.class);
    }

}
