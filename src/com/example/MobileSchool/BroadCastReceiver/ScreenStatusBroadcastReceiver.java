package com.example.MobileSchool.BroadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.example.MobileSchool.Communication.AjaxCallSender;
import com.example.MobileSchool.Utils.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 11.
 * Time: 오후 4:44
 */
public class ScreenStatusBroadcastReceiver extends BroadcastReceiver {
    private String TAG = Constants.TAG;
    private AjaxCallSender ajaxCallSender;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            ajaxCallSender = new AjaxCallSender(context);
            ajaxCallSender.screenStatusUpdate(true);
            Log.d(TAG, "ScreenStatusBroadcastReceiver: Screen On");
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            ajaxCallSender = new AjaxCallSender(context);
            ajaxCallSender.screenStatusUpdate(false);
            Log.d(TAG, "ScreenStatusBroadcastReceiver: Screen Off");
        }
    }
}
