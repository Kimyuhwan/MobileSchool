package com.example.MobileSchoolSeasonTwo.Communication;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.example.MobileSchoolSeasonTwo.Model.MyInfo;
import com.example.MobileSchoolSeasonTwo.Utils.AccountManager;
import com.example.MobileSchoolSeasonTwo.Utils.Constants;
import com.example.MobileSchoolSeasonTwo.Utils.GlobalApplication;
import com.example.MobileSchoolSeasonTwo.Utils.ServerMessage;
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
        globalApplication = (GlobalApplication) context.getApplicationContext();
        accountManager = globalApplication.getAccountManager();
    }

    public AjaxCallSender(Context context, Fragment fragment) {
        this.context = context;
        aq = new AQuery(context);
        ajaxCallBack = new AjaxCallBackReceiver(context, fragment);
        globalApplication = (GlobalApplication) context.getApplicationContext();
        accountManager = globalApplication.getAccountManager();
    }

    public AjaxCallSender(Context context, Activity activity) {
        this.context = context;
        aq = new AQuery(context);
        ajaxCallBack = new AjaxCallBackReceiver(context, activity);
        globalApplication = (GlobalApplication) context.getApplicationContext();
        accountManager = globalApplication.getAccountManager();
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

    public void sStart() {
        String url = ServerMessage.URL_SSTART + accountManager.getUniqueId();
        aq.ajax(url, JSONObject.class, ajaxCallBack);
        Log.d(TAG, "AjaxCall sStart : " + url);
    }

    // Experiment
    public void sStart(String experiment_type) {
        String url = ServerMessage.URL_SSTART + accountManager.getUniqueId() + "/" + experiment_type;
        aq.ajax(url, JSONObject.class, ajaxCallBack);
        Log.d(TAG, "AjaxCall sStart : " + url);
    }

    public void tStart() {
        String url = ServerMessage.URL_TSTART + accountManager.getUniqueId();
        aq.ajax(url, JSONObject.class, ajaxCallBack);
        Log.d(TAG, "AjaxCall sStart : " + url);
    }

    public void tConfirm() {
        String url = ServerMessage.URL_TCONFIRM + accountManager.getUniqueId();
        aq.ajax(url, JSONObject.class, ajaxCallBack);
        Log.d(TAG, "AjaxCall tConfirm : " + url);
    }

    public void sConfirm() {
        String url = ServerMessage.URL_SCONFIRM + "sid/" + accountManager.getUniqueId() + "/tid/" + globalApplication.getSender_id();
        aq.ajax(url, JSONObject.class, ajaxCallBack);
        Log.d(TAG, "AjaxCall tConfirm : " + url);
    }

    public void answer() {
        String url = ServerMessage.URL_ANSWER + accountManager.getUniqueId() + "/from/" + globalApplication.getPartnerInfo().getUnique_id();
        aq.ajax(url, JSONObject.class, ajaxCallBack);
        Log.d(TAG, "AjaxCall answer : " + url);
    }

    public void select(String type, String id) {
        String url = ServerMessage.URL_SELECT_SELECT + id + "/teacher/";

        if(accountManager.isStudent())
            url += globalApplication.getPartnerInfo().getUnique_id() + "/student/" + accountManager.getMyInfo().getUnique_id();
        else
            url += accountManager.getMyInfo().getUnique_id() + "/student/" + globalApplication.getPartnerInfo().getUnique_id();

        aq.ajax(url, JSONObject.class, ajaxCallBack);
        Log.d(TAG, "AjaxCall select : " + url);
    }

    public void recall(String url) {
        aq.ajax(url, JSONObject.class, ajaxCallBack);
    }

    public void ready() {
       String url = ServerMessage.URL_READY +  globalApplication.getPartnerInfo().getUnique_id();
       aq.ajax(url, JSONObject.class, ajaxCallBack);
       Log.d(TAG, "AjaxCall ready : " + url);
    }

    public void finish() {
        String url = ServerMessage.URL_FINISH + globalApplication.getSession_id();
        aq.ajax(url, JSONObject.class, ajaxCallBack);
        Log.d(TAG, "AjaxCall finish : " + url);
    }
    public void sessionend()
    {
        String url = ServerMessage.URL_SESSIONEND + globalApplication.getSession_id();
        aq.ajax(url, JSONObject.class, ajaxCallBack);
        Log.d(TAG, "AjaxCall SessionEnd : "+ url);
    }
}
