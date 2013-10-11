package com.example.MobileSchool.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import com.example.MobileSchool.Activities.MyActivity;
import com.example.MobileSchool.BroadCastReceiver.ScreenStatusBroadcastReceiver;
import com.example.MobileSchool.Communication.PushReceiver;
import com.example.MobileSchool.Utils.Constants;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 11.
 * Time: 오후 4:45
 */
public class ManagerRegistrationService extends Service {

    private String TAG = Constants.TAG;
    private BroadcastReceiver screenStatusBroadcastReceiver;
    private BroadcastReceiver pushReceiver;

    @Override
    public void onCreate() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Set Push Notification Framework (Parse)
        Parse.initialize(this, Constants.PARSE_APPLICATION_ID, Constants.PARSE_CLIENT_KEY);
        PushService.setDefaultPushCallback(this, MyActivity.class);
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        List<String> channels = new LinkedList<String>();
        channels.add("prototype");  // Will be updated to user Id
        installation.put("channels",channels);
        installation.saveInBackground();

        // Set broadcastReceiver
        screenStatusBroadcastReceiver = new ScreenStatusBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenStatusBroadcastReceiver, intentFilter);

        // Set PushReceiver
        pushReceiver = new PushReceiver();
        registerReceiver(pushReceiver, new IntentFilter(Constants.PUSH_CUSTOM_INTENT));

        return START_STICKY; // Continue running until it is explicitly stopped.
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(screenStatusBroadcastReceiver);
        unregisterReceiver(pushReceiver);
    }
}