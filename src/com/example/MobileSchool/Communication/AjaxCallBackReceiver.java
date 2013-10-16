package com.example.MobileSchool.Communication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.MobileSchool.Activities.Teacher.TeacherProfileActivity;
import com.example.MobileSchool.Manager.AccountManager;
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
public class AjaxCallBackReceiver extends AjaxCallback<JSONObject> {
    private String TAG = Constants.TAG;
    private Context context;
    private AccountManager accountManager;

    public AjaxCallBackReceiver(Context context) {
        this.context = context;
        accountManager = new AccountManager(context);
    }

    @Override
    public void callback(String url, JSONObject object, AjaxStatus status) {
        Log.d(TAG, "AjaxCallBackReceiver : JSON -> " + object + ", Status -> " + status);

        if(!object.isNull("status"))
            _handleResult(object);
    }

    private void _handleResult(JSONObject object) {
        try {
            String status = object.getString("status");
            if(!object.isNull("queue_id") && status.equals(ServerMessage.RESPONSE_MATCH_MAKE)) {
               accountManager.setQueueId(object.getString("queue_id"));
            } else if(!object.isNull("student") && status.equals(ServerMessage.RESPONSE_MATCH_JOIN)) {
               JSONObject student = object.getJSONObject("student");
               accountManager.setOppositePhoneNumber(student.getString("phone"));
               Log.d(TAG, "Match JOIN opposite phone number : " + student.getString("phone"));
               Intent intent = new Intent(context, TeacherProfileActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
