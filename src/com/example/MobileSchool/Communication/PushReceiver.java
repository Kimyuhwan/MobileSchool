package com.example.MobileSchool.Communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.example.MobileSchool.Utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 11.
 * Time: 오후 4:45
 */
public class PushReceiver extends BroadcastReceiver {

    private String TAG = Constants.TAG;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "PushReceiver : onReceive");
        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            String pushMessage = json.getString("message");
            _handleMessage(pushMessage, intent);
            Log.d(TAG, "PushReceiver pushMessage : " + pushMessage);
        } catch (JSONException e) {e.printStackTrace();}
    }

    private void _handleMessage(String pushMessage, Intent intent) {

    }
}
