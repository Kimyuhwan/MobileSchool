package com.example.MobileSchool.BroadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 11.
 * Time: 오후 4:44
 */
public class BootCompleteBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // When boot complete, restart Manager Registration Service
        context.startService(new Intent(context, ManagerRegistrationService.class));
    }
}
