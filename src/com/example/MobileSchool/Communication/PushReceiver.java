package com.example.MobileSchool.Communication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.support.v4.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import com.example.MobileSchool.ClassEntryActivity;
import com.example.MobileSchool.Fragment.DialogBox;
import com.example.MobileSchool.Fragment.ProfileFragment;
import com.example.MobileSchool.SchoolActivity;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.Constants;
import com.example.MobileSchool.Utils.GlobalApplication;
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

    private GlobalApplication globalApplication;

    @Override
    public void onReceive(Context context, Intent intent) {
        globalApplication = (GlobalApplication) context.getApplicationContext();
        try {
            JSONObject object = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            if(object.getString("type").equals(Constants.PUSH_TYPE_NOTIFICATION))
                _makeNotification(object, context);                  //_makeDialog(json, context);
            else if(object.getString("type").equals(Constants.PUSH_TYPE_MESSAGE))
                _handleMessage(object, context);
        } catch (JSONException e) {e.printStackTrace();}
    }

    private void _makeNotification(JSONObject object, Context context) {
        Log.d(TAG, "PushNotification notification : " + object);

        RemoteViews remoteViews = new RemoteViews("com.example.MobileSchool", R.layout.notification);
        remoteViews.setTextViewText(R.id.title, "Notification Test");
        remoteViews.setTextViewText(R.id.text, "NotificationTest");
        remoteViews.setImageViewResource(R.id.imagenotileft, R.drawable.ic_launcher);

        Intent clickIntent = new Intent(Constants.PUSH_CUSTOM_NOTIFICATION_EVENT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.imagenotileft, pendingIntent);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("MobileSchool")
                .setContentText("Do you want join us?")
                .setContent(remoteViews)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 500, 250, 500});

        Intent schoolIntent = new Intent(context, SchoolActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(SchoolActivity.class);
        stackBuilder.addNextIntent(schoolIntent);
        PendingIntent schoolPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(schoolPendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Constants.PUSH_NOTIFICATION_UNIQUE_ID, mBuilder.build());
    }

    private void _handleMessage(JSONObject object, Context context) {
        Log.d(TAG, "PushNotification message : " + object);

        try {
            String code = object.getString(Constants.PUSH_KEY_CODE);
            if(code.equals(Constants.PUSH_CODE_PUSH_TEACHER_INFO)) {
               JSONObject msg = object.getJSONObject(Constants.PUSH_TYPE_MESSAGE);
               JSONObject teacherInfo = msg.getJSONObject(Constants.PUSH_KEY_TEACHER_INFO);
               String teacherId = teacherInfo.getString(Constants.PUSH_VALUE_TEACHER_ID);
               Log.d(TAG, "Teacher Id : " + teacherId);
               globalApplication.setTargetTeacherId(teacherId);

               // Change to Profile Page
               globalApplication.setFragment("Profile",new ProfileFragment());
               globalApplication.getSchoolActivity().initFragment();
            }
            else if(code.equals(Constants.PUSH_CODE_PUSH_STUDENT_ANSWER)) {
               JSONObject msg = object.getJSONObject(Constants.PUSH_TYPE_MESSAGE);
               JSONObject studentInfo = msg.getJSONObject(Constants.PUSH_KEY_STUDENT_INFO);
               String studentId = studentInfo.getString(Constants.PUSH_VALUE_STUDENT_ID);
               Log.d(TAG, "Student Id : " + studentId);

               // Class Entry Start
               Intent intent = new Intent(context, ClassEntryActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
               context.startActivity(intent);
            }
        } catch (JSONException e) { e.printStackTrace();}
    }

    private void _makeDialog(JSONObject jsonObject, Context context) {
        Intent intent = new Intent(context, DialogBox.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
