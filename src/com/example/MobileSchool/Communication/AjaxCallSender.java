package com.example.MobileSchool.Communication;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.example.MobileSchool.Model.MyInfo;
import com.example.MobileSchool.Utils.AccountManager;
import com.example.MobileSchool.Utils.Constants;
import com.example.MobileSchool.Utils.GlobalApplication;
import com.example.MobileSchool.Utils.ServerMessage;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 11.
 * Time: 오후 4:45
 */
public class AjaxCallSender {
    private String TAG = Constants.TAG;

    private AQuery aq;
    private AjaxCallback ajaxCallBack;

    private AccountManager accountManager;
    private Context context;
    private GlobalApplication globalApplication;

    public AjaxCallSender(Context context) {
        this.context = context;
        aq = new AQuery(context);
        ajaxCallBack = new AjaxCallBackReceiver(context);
        accountManager = new AccountManager(context);
        globalApplication = (GlobalApplication) context.getApplicationContext();
    }

    public AjaxCallSender(Context context, Fragment fragment) {
        this.context = context;
        aq = new AQuery(context);
        ajaxCallBack = new AjaxCallBackReceiver(context, fragment);
        accountManager = new AccountManager(context);
        globalApplication = (GlobalApplication) context.getApplicationContext();
    }

    public AjaxCallSender(Context context, Activity activity) {
        this.context = context;
        aq = new AQuery(context);
        ajaxCallBack = new AjaxCallBackReceiver(context, activity);
        accountManager = new AccountManager(context);
        globalApplication = (GlobalApplication) context.getApplicationContext();
    }

    public void deviceStatusUpdate(boolean isScreenOn) {
        if(isScreenOn) {
            String url = ServerMessage.URL_SCREEN_MONITORING + accountManager.getUniqueId() + "/on/";
            aq.ajax(url, JSONObject.class, ajaxCallBack);
            Log.d(TAG, "DeviceStatusUpdate URL: " + url);
        }
        else {
            String url = ServerMessage.URL_SCREEN_MONITORING + accountManager.getUniqueId() + "/off/";
            aq.ajax(url, JSONObject.class, ajaxCallBack);
            Log.d(TAG, "DeviceStatusUpdate URL: " + url);
        }
    }

    public void appOnUpdate() {
        String url = ServerMessage.URL_APP_MONITORING + accountManager.getUniqueId() + "/on/";
        aq.ajax(url, JSONObject.class, ajaxCallBack);
        Log.d(TAG, "AppOnUpdate URL : " + url);
    }

    public void login(String id, String password) {
        String url = ServerMessage.URL_LOGIN;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("pw", password);
        aq.ajax(url, params, JSONObject.class, ajaxCallBack);
        Log.d(TAG, "AppOnUpdate URL : " + url + " ( " + id + ", " + password + " ) ");
    }

    public void register(MyInfo myInfo) {
        String url = "";
        if(myInfo.getType().equals(Constants.REGISTRATION_TYPE_STUDENT))
          url = ServerMessage.URL_STUDENT_REGISTER;
        else
          url = ServerMessage.URL_TEACHER_REGISTER;

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_id", myInfo.getAccount_id());
        params.put("pw", myInfo.getPassword());
        params.put("name", myInfo.getName());
        params.put("gender", myInfo.getGender());
        params.put("age", myInfo.getAge());
        params.put("phone", myInfo.getPhoneNumber());
        params.put("email", myInfo.getAccount_id() + "@nclab.kaist.ac.kr");

        aq.ajax(url, params, JSONObject.class, ajaxCallBack);
        Log.d(TAG, "AppOnUpdate URL : " + url + " ( " + myInfo.getAccount_id() + ", " + myInfo.getPassword() + ", " + myInfo.getName() + ", " + myInfo.getGender() + ", " + myInfo.getAge() + ", " + myInfo.getPhoneNumber() + ", " + myInfo.getType() + ")" );
    }

    public void getDialogue() {
        String url = "";
        if(accountManager.isStudent())
            url = ServerMessage.URL_DIALOGUE_STUDENT;
        else
            url = ServerMessage.URL_DIALOGUE_TEACHER;
        aq.ajax(url, JSONObject.class, ajaxCallBack);
        Log.d(TAG, "AjaxCall getDialogue : " + url);
    }

    public void start() {
        String url = ServerMessage.URL_START + accountManager.getUniqueId();
        aq.ajax(url, JSONObject.class, ajaxCallBack);
        Log.d(TAG, "AjaxCall start : " + url);
    }

    public void confirm() {
        String url = ServerMessage.URL_CONFIRM + accountManager.getUniqueId();
        aq.ajax(url, JSONObject.class, ajaxCallBack);
        Log.d(TAG, "AjaxCall confirm : " + url);
    }

    public void answer() {
        String url = ServerMessage.URL_ANSWER + accountManager.getUniqueId() + "/from/" + globalApplication.getPartnerInfo().getUnique_id();
        aq.ajax(url, JSONObject.class, ajaxCallBack);
        Log.d(TAG, "AjaxCall answer : " + url);
    }

}
