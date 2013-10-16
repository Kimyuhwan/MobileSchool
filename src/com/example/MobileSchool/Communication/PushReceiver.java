package com.example.MobileSchool.Communication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.support.v4.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.example.MobileSchool.Activities.DialogBox;
import com.example.MobileSchool.Activities.Teacher.TeacherWaitingActivity;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.Constants;
import com.example.MobileSchool.Utils.ServerMessage;
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
    private AjaxCallSender ajaxCallSender;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "PushReceiver : onReceive");
        ajaxCallSender = new AjaxCallSender(context);

        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            String type = json.getString("type");
            Log.d(TAG, "PushReceiver  type :" + type);
            if(type.equals("notification")) {
                _makeNotification(json, context);
//                _makeDialog(json, context);  // Will be used
            }else if(type.equals("command"))
                _handleCommand(json, context);
            else if(type.equals("message"))
                _handleMessage(json, context);

        } catch (JSONException e) {e.printStackTrace();}
    }

    private void _makeNotification(JSONObject jsonObject, Context context) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Test")
                .setContentText("Test")
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 500, 250, 500});

        Intent schoolIntent = new Intent(context, TeacherWaitingActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(TeacherWaitingActivity.class);
        stackBuilder.addNextIntent(schoolIntent);
        PendingIntent schoolPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(schoolPendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Constants.PUSH_NOTIFICATION_UNIQUE_ID, mBuilder.build());
    }

    private void _makeDialog(JSONObject jsonObject, Context context) {
        Log.d(TAG, "PushReceiver//MakeDialog : Test");
        Intent intent = new Intent(context, DialogBox.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void _handleCommand(JSONObject jsonObject, Context context) {
        try {
            String pushCommand = jsonObject.getString("command");
            Log.d(TAG, "PushReceiver//HandleCommand : " + pushCommand);

            //MSG_FIND
            if(pushCommand.equals(ServerMessage.COMMUNICATION_MSG_FIND)) {
                 ajaxCallSender.matchJoin();
            }

        } catch (JSONException e) { e.printStackTrace();}
    }

    private void _handleMessage(JSONObject jsonObject, Context context) {

        try {
            String pushMessage = jsonObject.getString("message");
            Log.d(TAG, "PushReceiver//HandleMessage : " + pushMessage);

        } catch (JSONException e) { e.printStackTrace();}

    }
}
