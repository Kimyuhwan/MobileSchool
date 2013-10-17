package com.example.MobileSchool.Communication;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.example.MobileSchool.Utils.AccountManager;
import com.example.MobileSchool.Utils.Constants;
import com.example.MobileSchool.Utils.GlobalApplication;
import com.example.MobileSchool.Utils.ServerMessage;
import org.json.JSONObject;

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

    public void deviceStatusUpdate(boolean isScreenOn) {
        if(isScreenOn) {
            String url = ServerMessage.URL_SCREEN_MONITORING + accountManager.getUserId() + "/on/";
            aq.ajax(url, JSONObject.class, ajaxCallBack);
        }
        else {
            String url = ServerMessage.URL_SCREEN_MONITORING + accountManager.getUserId() + "/off/";
            aq.ajax(url, JSONObject.class, ajaxCallBack);
            Log.d(TAG, "DeviceStatusUpdate URL: " + url);
        }
    }

    public void appOnUpdate() {
        String url = ServerMessage.URL_APP_MONITORING + accountManager.getUserId() + "/on/";
        aq.ajax(url, JSONObject.class, ajaxCallBack);
        Log.d(TAG, "AppOnUpdate URL : " + url);
    }

    public void start() {
        String url = ServerMessage.URL_START + accountManager.getUserId();
        aq.ajax(url, JSONObject.class, ajaxCallBack);
        Log.d(TAG, "AjaxCall start : " + url);
    }

    public void confirm() {
        String url = ServerMessage.URL_CONFIRM + accountManager.getUserId();
        aq.ajax(url, JSONObject.class, ajaxCallBack);
        Log.d(TAG, "AjaxCall confirm : " + url);
    }

    public void answer() {
        String url = ServerMessage.URL_ANSWER + accountManager.getUserId() + "/from/" + globalApplication.getTargetTeacherId();
        aq.ajax(url, JSONObject.class, ajaxCallBack);
        Log.d(TAG, "AjaxCall answer : " + url);
    }

}
