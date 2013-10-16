package com.example.MobileSchool.BroadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.example.MobileSchool.ClassEntryActivity;
import com.example.MobileSchool.Manager.AccountManager;
import com.example.MobileSchool.Utils.Constants;

/**
 * User: yuhwan
 * Date: 13. 10. 16
 * Time: 오후 2:19
 */
public class TelephonyBroadcastReceiver extends BroadcastReceiver {

    private String TAG = Constants.TAG;

    private Context context;
    private Bundle extras;
    private AccountManager accountManager;

    private String incomingNumber = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        accountManager = new AccountManager(context);

        // If the classReady is on .. (not always!)

        String telephonyState = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
        extras = intent.getExtras();

        Log.d(TAG, "TelephonyBroadcastReceiver receive stat : " + telephonyState);
        _handleState(telephonyState);
    }

    private void _handleState(String telephonyState) {
        if(telephonyState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
             _onCalling();
        else if(telephonyState.equals(TelephonyManager.EXTRA_STATE_RINGING))
            _onRinging();
    }

    private void _onCalling() {
        Log.d(TAG, "TelephonyBroadcastReceiver start : onCalling");
        if(accountManager.isStudent()) { // && isIncomingCall equals to oppositeNumber
            Thread thread = new Thread(){
                @Override
                public void run() {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) { e.printStackTrace(); }
                        Intent intent = new Intent(context, ClassEntryActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        context.startActivity(intent);
                }
            };
            thread.start();
        }
    }

    private void _onRinging() {
        String incomingNumber = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
        Log.d(TAG, "TelephonyBroadcastReceiver start : onRinging from " + incomingNumber);
        this.incomingNumber = incomingNumber;
    }
}
