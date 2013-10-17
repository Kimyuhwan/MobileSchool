package com.example.MobileSchool.BroadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.example.MobileSchool.Fragment.GuideFragment;
import com.example.MobileSchool.Utils.GlobalApplication;
import com.example.MobileSchool.Utils.Constants;

/**
 * User: yuhwan
 * Date: 13. 10. 17
 * Time: 오후 4:04
 */
public class NotificationClickBroadcastReceiver extends BroadcastReceiver {
    private String TAG = Constants.TAG;

    private GlobalApplication globalApplication;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "NotificationClickBroadcastReceiver : click");
        globalApplication = (GlobalApplication) context.getApplicationContext();
        globalApplication.setFragment("Guide", new GuideFragment());
    }
}
