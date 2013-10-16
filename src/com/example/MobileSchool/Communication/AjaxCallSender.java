package com.example.MobileSchool.Communication;

import android.content.Context;
import android.util.Log;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.example.MobileSchool.Manager.AccountManager;
import com.example.MobileSchool.Utils.Constants;
import com.example.MobileSchool.Utils.ServerMessage;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

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

    public AjaxCallSender(Context context) {
        this.context = context;
        aq = new AQuery(context);
        ajaxCallBack = new AjaxCallBackReceiver(context);
        accountManager = new AccountManager(context);
    }

    // School Call
    public void deviceStatusUpdate(boolean isScreenOn) {
        if(isScreenOn) {
            String url = ServerMessage.URL_SCREEN_MONITORING + accountManager.getUserId() + "/on/";
            aq.ajax(url, JSONObject.class, ajaxCallBack);
            Log.d(TAG, "DeviceStatusUpdate URL : " + url);
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

    // Matching Call
    public void matchMake() {
        // Teacher
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tid", accountManager.getUserSId()));   // sid should be changed into id
        params.add(new BasicNameValuePair("time", "500000")); // redundant
        String parameter = URLEncodedUtils.format(params, "UTF-8");

        String url = ServerMessage.URL_MATCH_MAKE + "?" + parameter;
        Log.d(TAG, "Match Make url : " + url);
        aq.ajax(url, JSONObject.class, ajaxCallBack);
    }

    public void matchFind() {
        // Student
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sid", accountManager.getUserSId()));
        params.add(new BasicNameValuePair("cname", "8"));
        String parameter = URLEncodedUtils.format(params, "UTF-8");

        String url = ServerMessage.URL_MATCH_FIND + "?" + parameter;
        Log.d(TAG, "Match Make url : " + url);
        aq.ajax(url, JSONObject.class, ajaxCallBack);
    }

    public void matchJoin() {
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tid", accountManager.getUserSId()));
        params.add(new BasicNameValuePair("qid", accountManager.getQueueID()));
        String parameter = URLEncodedUtils.format(params, "UTF-8");

        String url = ServerMessage.URL_MATCH_JOIN + "?" + parameter;
        Log.d(TAG, "Match Join url : " + url);
        aq.ajax(url, JSONObject.class, ajaxCallBack);
    }

}
