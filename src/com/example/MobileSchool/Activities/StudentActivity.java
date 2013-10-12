package com.example.MobileSchool.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.MobileSchool.Communication.PushSender;
import com.example.MobileSchool.Manager.AccountManager;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.Constants;

/**
 * User: yuhwan
 * Date: 13. 10. 11
 * Time: 오후 8:30
 */
public class StudentActivity extends Activity {

    private String TAG = Constants.TAG;

    private Button sendNotificationButton;

    private PushSender pushSender;
    private AccountManager accountManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student);
        Log.d(TAG, "StudentActivity: onCreate");

        pushSender = new PushSender(this);
        accountManager = new AccountManager(this);

        _initUI();
    }

    private void _initUI() {
        sendNotificationButton = (Button) findViewById(R.id.btn_sendNotification);

        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Send Notification");
                // Now Because of send notification test, student sends push to teacher directly, but later student will call server with ajax and server will send notification to teacher
                String myId = accountManager.getUserId();
                String teacherId = "teacher";
                String message = "2013.10.12 send Notification Test";
                pushSender.pushToDevice(myId, teacherId, message);
            }
        });
    }
}
