package com.example.MobileSchool.BroadCastReceiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import com.example.MobileSchool.SchoolActivity;
import com.example.MobileSchool.Communication.PushReceiver;
import com.example.MobileSchool.Utils.AccountManager;
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
    private BroadcastReceiver telephonyBroadcastReceiver;
    private BroadcastReceiver notificationClickBroadcastReceiver;

    private AccountManager accountManager;

    @Override
    public void onCreate() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Initialize AccountManager
        accountManager = new AccountManager(this);

        // Set Push Notification Framework (Parse)
        Parse.initialize(this, Constants.PARSE_APPLICATION_ID, Constants.PARSE_CLIENT_KEY);
        PushService.setDefaultPushCallback(this, SchoolActivity.class);
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        List<String> channels = new LinkedList<String>();
        channels.add(accountManager.getUserId());  // Will be updated to user Id
        installation.put("channels", channels);
        installation.saveInBackground();

        // Set broadcastReceiver
        screenStatusBroadcastReceiver = new DeviceStatusBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_USER_PRESENT);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenStatusBroadcastReceiver, intentFilter);

        // Set PushReceiver
        pushReceiver = new PushReceiver();
        registerReceiver(pushReceiver, new IntentFilter(Constants.PUSH_CUSTOM_INTENT));

        // Set CallBroadCastReceiver
        telephonyBroadcastReceiver = new TelephonyBroadcastReceiver();
        registerReceiver(telephonyBroadcastReceiver, new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED));

        // Set NotificationClickBroadcastReceiver
        notificationClickBroadcastReceiver = new NotificationClickBroadcastReceiver();
        registerReceiver(notificationClickBroadcastReceiver, new IntentFilter(Constants.PUSH_CUSTOM_NOTIFICATION_EVENT));

        return START_STICKY; // Continue running until it is explicitly stopped.
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(screenStatusBroadcastReceiver);
        unregisterReceiver(pushReceiver);
        unregisterReceiver(telephonyBroadcastReceiver);
        unregisterReceiver(notificationClickBroadcastReceiver);
    }
}