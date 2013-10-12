package com.example.MobileSchool.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.MobileSchool.Communication.PushSender;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.Constants;
import com.parse.PushService;

/**
 * User: yuhwan
 * Date: 13. 10. 11
 * Time: 오후 8:30
 */
public class StudentActivity extends Activity {

    private String TAG = Constants.TAG;

    private Button sendNotificationButton;

    private PushSender pushSender;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student);
        Log.d(TAG, "StudentActivity: onCreate");
        pushSender = new PushSender(this);

        _initUI();
    }

    private void _initUI() {
        sendNotificationButton = (Button) findViewById(R.id.btn_sendNotification);

        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Now Because of send notification test, student sends push to teacher directly, but later student will call server with ajax and server will send notification to teacher

            }
        });
    }
}
