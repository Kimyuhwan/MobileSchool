package com.example.MobileSchoolDev.BroadCastReceiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import com.bugsense.trace.BugSenseHandler;
import com.example.MobileSchoolDev.Communication.PushReceiver;
import com.example.MobileSchoolDev.Utils.AccountManager;
import com.example.MobileSchoolDev.Utils.Constants;
import com.example.MobileSchoolDev.Utils.GlobalApplication;

/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 11.
 * Time: 오후 4:45
 */
public class ManagerRegistrationService extends Service {

    public static ManagerRegistrationService managerRegistrationService = null;

    private String TAG = Constants.TAG;
    private BroadcastReceiver screenStatusBroadcastReceiver;
    private BroadcastReceiver pushReceiver;
    private BroadcastReceiver telephonyBroadcastReceiver;
    private BroadcastReceiver notificationClickBroadcastReceiver;

    private AccountManager accountManager;
    private GlobalApplication globalApplication;

    @Override
    public void onCreate() {
       managerRegistrationService = this;
       // Bug Sense
       BugSenseHandler.initAndStartSession(this, Constants.BUGSENSE_KEY);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        globalApplication = (GlobalApplication) getApplication();
        accountManager = globalApplication.getAccountManager();

        // Set broadcastReceiver
        screenStatusBroadcastReceiver = new DeviceStatusBroadcastReceiver();
        IntentFilter screenintentFilter = new IntentFilter(Intent.ACTION_USER_PRESENT);
        screenintentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenStatusBroadcastReceiver, screenintentFilter);

        // Set PushReceiver
        pushReceiver = new PushReceiver();
        registerReceiver(pushReceiver, new IntentFilter(Constants.PUSH_CUSTOM_INTENT));

        // Set CallBroadCastReceiver
        telephonyBroadcastReceiver = new TelephonyBroadcastReceiver();
        registerReceiver(telephonyBroadcastReceiver, new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED));

        // Set NotificationClickBroadcastReceiver
        notificationClickBroadcastReceiver = new NotificationClickBroadcastReceiver();
        IntentFilter notifiIntentFilter = new IntentFilter(Constants.PUSH_CUSTOM_NOTIFICATION_CONFIRM_EVENT);
        notifiIntentFilter.addAction(Constants.PUSH_CUSTOM_NOTIFICATION_CANCEL_EVENT);
        registerReceiver(notificationClickBroadcastReceiver, notifiIntentFilter);

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
