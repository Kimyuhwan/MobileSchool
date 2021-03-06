package com.example.MobileSchoolDev.BroadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.example.MobileSchoolDev.Activities.EntryActivity;
import com.example.MobileSchoolDev.Activities.ThankyouActivity;
import com.example.MobileSchoolDev.Communication.AjaxCallSender;
import com.example.MobileSchoolDev.Utils.AccountManager;
import com.example.MobileSchoolDev.Utils.CallRecorder;
import com.example.MobileSchoolDev.Utils.Constants;
import com.example.MobileSchoolDev.Utils.GlobalApplication;

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
    private AjaxCallSender ajaxCallSender;
    private CallRecorder callRecorder;

    private GlobalApplication globalApplication;

    private String incomingNumber = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        globalApplication = (GlobalApplication) context.getApplicationContext();
        accountManager = globalApplication.getAccountManager();
        callRecorder = globalApplication.getCallRecorder();
        ajaxCallSender = new AjaxCallSender(context);

        // If the classReady is on .. (not always!)
        String telephonyState = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
        extras = intent.getExtras();

        Log.d(TAG, "TelephonyBroadcastReceiver receive stat : " + telephonyState);
        Log.d(TAG, "Connected Status : " + globalApplication.isSession_connected());
        if(globalApplication.isSession_connected())
            _handleState(telephonyState);
    }

    private void _handleState(String telephonyState) {
        if(telephonyState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
             _onCalling();
        else if(telephonyState.equals(TelephonyManager.EXTRA_STATE_RINGING))
            _onRinging();
        else if(telephonyState.equals(TelephonyManager.EXTRA_STATE_IDLE))
            _onIdle();
    }

    private void _onCalling() {
        if(accountManager.isStudent()) {
            Thread thread = new Thread(){
                @Override
                public void run() {
                    // Start Activity
                    Log.d(TAG, "TelephonyBroadcastReceiver onCalling");
                    globalApplication.getSchoolActivity().finish();
                    Intent intent = new Intent(context, EntryActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(intent);
                }
            };
            thread.start();
        }
    }

    private void _onRinging() {
        String incomingNumber = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
        Log.d(TAG, "TelephonyBroadcastReceiver sStart : onRinging from " + incomingNumber);
        this.incomingNumber = incomingNumber;
    }

    private void _onIdle() {
        Thread thread = new Thread(){
            @Override
            public void run() {
                Log.d(TAG, "TelephonyReceiver : onIdle");
                if(!accountManager.isStudent())
                    ajaxCallSender.finish();
                globalApplication.setSession_connected(false);
                globalApplication.getSchoolActivity().finish();
                Intent intent = new Intent(context, ThankyouActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                context.startActivity(intent);
            }
        };
        thread.start();
    }
}
