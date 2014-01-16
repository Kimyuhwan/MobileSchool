package com.example.MobileSchoolSeasonTwo.Communication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.MobileSchoolSeasonTwo.Activities.EntryActivity;
import com.example.MobileSchoolSeasonTwo.BaseMethod;
import com.example.MobileSchoolSeasonTwo.Model.DialogueItem;
import com.example.MobileSchoolSeasonTwo.R;
import com.example.MobileSchoolSeasonTwo.Utils.CallRecorder;
import com.example.MobileSchoolSeasonTwo.Utils.Constants;
import com.example.MobileSchoolSeasonTwo.Utils.GlobalApplication;
import org.json.JSONArray;
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
    private CallRecorder callRecorder;

    private TextView connectingTextView;

    @Override
    public void onReceive(Context context, Intent intent) {
        globalApplication = (GlobalApplication) context.getApplicationContext();
        callRecorder = globalApplication.getCallRecorder();

        try {
            JSONObject object = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            if(object.getString("type").equals(Constants.PUSH_TYPE_NOTIFICATION)) {
                Log.d(TAG, "PushNotification notification : " + object);
                if(!_isStudying()) {
                    // 수업 시작이 되지 않았을 때만.
                    String code = object.getString("code");
                    JSONObject msg = object.getJSONObject("msg");
                    if(code.equals(Constants.CODE_PUSH_TEACHER))
                         globalApplication.setSender_id(msg.getString("sender"));
                    else if(code.equals(Constants.CODE_PUSH_STUDENT))
                         globalApplication.setSender_id(msg.getString("sender"));

                    _makeNotification(context);
                    _makeToast(context);
                }
            }
            else if(object.getString("type").equals(Constants.PUSH_TYPE_MESSAGE))
                _handleMessage(object, context);
        } catch (JSONException e) {e.printStackTrace();}
    }

    private boolean _isStudying() {
        String currentFragmentName = globalApplication.getFragmentName();
        String[] studyingFragments = new String[]{"DialogueStudent","DialogueTeacher","Guide","Profile","Script","Experiment"};
        for(String fragmentName : studyingFragments) {
            if(fragmentName.equals(currentFragmentName))
                return true;
        }
        return false;
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

        notificationManager.cancel(Constants.PUSH_NOTIFICATION_UNIQUE_ID);
    }

    private void _makeToast(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View layout = inflater.inflate(R.layout.toast_layout, null);
        TextView textView = (TextView) layout.findViewById(R.id.toast_textView_notification);
        textView.setText(R.string.toast_textView_notification);
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
            if(code.equals(Constants.CODE_PUSH_ROOT_QUESTION)) {
               JSONObject msg = object.getJSONObject(Constants.PUSH_TYPE_MESSAGE);
               JSONArray topics = msg.getJSONArray(Constants.PUSH_TYPE_TOPICS);
               String session_id = msg.getString("session_id");
               DialogueItem[] entryItems = new DialogueItem[topics.length()];
               for(int index = 0; index < topics.length(); index++) {
                   JSONObject topic = topics.getJSONObject(index);
                   DialogueItem dialogueItem = new DialogueItem(topic.getString("type"), topic.getString("context"), topic.getString("id"), topic.getString("successor"));
                   entryItems[index] = dialogueItem;
               }
               globalApplication.setEntryItems(entryItems);
               globalApplication.setSession_id(session_id);

               // Start recorder
//               callRecorder.startRecording();

               // Class Entry Start
               Intent intent = new Intent(context, EntryActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
               context.startActivity(intent);

            } else if(code.equals(Constants.CODE_PUSH_STUDENT_READY)) {
               Log.d(TAG, "PSR");
                if(globalApplication.isSchoolActivityFront()) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + globalApplication.getPartnerInfo().getPhoneNumber()));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(callIntent);
                }
            }
            else {
                ((BaseMethod) globalApplication.getFragment()).handlePush(object);
            }
        } catch (JSONException e) { e.printStackTrace();}
    }


}
