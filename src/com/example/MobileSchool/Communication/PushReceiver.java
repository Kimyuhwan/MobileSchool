package com.example.MobileSchool.Communication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.example.MobileSchool.Activities.EntryActivity;
import com.example.MobileSchool.Fragment.ProfileFragment;
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
            if(object.getString("type").equals(Constants.PUSH_TYPE_NOTIFICATION)) {
                Log.d(TAG, "PushNotification notification : " + object);
                _makeNotification(context);
                _makeToast(context);
            }
            else if(object.getString("type").equals(Constants.PUSH_TYPE_MESSAGE))
                _handleMessage(object, context);
        } catch (JSONException e) {e.printStackTrace();}
    }

    private void _makeNotification(Context context) {
        Intent confirmIntent = new Intent(Constants.PUSH_CUSTOM_NOTIFICATION_CONFIRM_EVENT);
        PendingIntent confirmPendingIntent = PendingIntent.getBroadcast(context, 0, confirmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("The time is now")
                .setContentText("새로운 수업을 시작해주세요!")
                .setContentIntent(confirmPendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 500, 250, 500});

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Constants.PUSH_NOTIFICATION_UNIQUE_ID, mBuilder.build());
    }

    private void _makeToast(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View layout = inflater.inflate(R.layout.toast_layout, null);
        Toast toast = new Toast(context.getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
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
               // 여기에서 선생님 정보 저장

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
               Intent intent = new Intent(context, EntryActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
               context.startActivity(intent);
            }
        } catch (JSONException e) { e.printStackTrace();}
    }


}
