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
public class DeviceStatusBroadcastReceiver extends BroadcastReceiver {
    private String TAG = Constants.TAG;
    private AjaxCallSender ajaxCallSender;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            ajaxCallSender = new AjaxCallSender(context);
            ajaxCallSender.deviceStatusUpdate(true);
            Log.d(TAG, "UserPresent");
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            ajaxCallSender = new AjaxCallSender(context);
            ajaxCallSender.deviceStatusUpdate(false);
            Log.d(TAG, "Screen Off");
        }
    }
}
