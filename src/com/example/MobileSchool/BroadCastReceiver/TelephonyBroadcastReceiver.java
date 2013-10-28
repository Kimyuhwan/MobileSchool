package com.example.MobileSchool.BroadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.example.MobileSchool.Activities.EntryActivity;
import com.example.MobileSchool.Activities.EvaluationActivity;
import com.example.MobileSchool.Communication.AjaxCallSender;
import com.example.MobileSchool.Fragment.HomeFragment;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.AccountManager;
import com.example.MobileSchool.Utils.Constants;
import com.example.MobileSchool.Utils.GlobalApplication;

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

    private GlobalApplication globalApplication;

    private String incomingNumber = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        globalApplication = (GlobalApplication) context.getApplicationContext();
        accountManager = globalApplication.getAccountManager();
        ajaxCallSender = new AjaxCallSender(context);

        // If the classReady is on .. (not always!)

        String telephonyState = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
        extras = intent.getExtras();

        Log.d(TAG, "TelephonyBroadcastReceiver receive stat : " + telephonyState);
        if(globalApplication.isClassConnected())
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
        Log.d(TAG, "TelephonyBroadcastReceiver start : onCalling");
        if(accountManager.isStudent()) { // && isIncomingCall equals to oppositeNumber
            Thread thread = new Thread(){
                @Override
                public void run() {
                    // Answer
                    ajaxCallSender.answer();
                    // Start Activity
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
        Log.d(TAG, "TelephonyBroadcastReceiver start : onRinging from " + incomingNumber);
        this.incomingNumber = incomingNumber;
    }

    private void _onIdle() {
        Thread thread = new Thread(){
            @Override
            public void run() {
                globalApplication.setClassConnected(false);
                globalApplication.getSchoolActivity().finish();
                Intent intent = new Intent(context, EvaluationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                context.startActivity(intent);
            }
        };
        thread.start();
    }
}
