package com.example.MobileSchool.Communication;

import android.content.Context;
import android.util.Log;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.example.MobileSchool.Utils.Constants;
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

    public AjaxCallSender(Context context) {
        aq = new AQuery(context);
        ajaxCallBack = new AjaxCallBackReceiver();
    }

    public void screenStatusUpdate(boolean isScreenOn) {
        if(isScreenOn) {
            String url = ServerMessage.URL_SCREEN_MONITORING + "yuhwan/on/";
            aq.ajax(url, JSONObject.class, ajaxCallBack);
            Log.d(TAG, "ScreenStatusUpdate URL : " + url);
        }
        else {
            String url = ServerMessage.URL_SCREEN_MONITORING + "yuhwan/off/";
            aq.ajax(url, JSONObject.class, ajaxCallBack);
            Log.d(TAG, "ScreenStatusUpdate URL: " + url);
        }
    }
}
