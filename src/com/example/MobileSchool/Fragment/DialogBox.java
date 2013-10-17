package com.example.MobileSchool.Fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import com.example.MobileSchool.EntryActivity;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.Constants;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 14.
 * Time: 오전 9:43
 *
 * Under construction...
 *
 */

public class DialogBox extends Activity {

    private String TAG = Constants.TAG;

    private Button dialogCheckButton;
    private Button dialogCancelButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "DialogBox : onCreate");
        setContentView(R.layout.dialog_box);
        _initFlag();
        _initUI();

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                DialogBox.this.finish();
                timer.cancel();
            }
        }, 5000);
    }

    private void _initFlag() {
        getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                           | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                           | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    private void _initUI() {
        dialogCheckButton = (Button) findViewById(R.id.btn_check_dialog);
        dialogCancelButton = (Button) findViewById(R.id.btn_cancel_dialog);

        dialogCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EntryActivity.class));
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.cancel(Constants.PUSH_NOTIFICATION_UNIQUE_ID);
            }
        });

        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogBox.this.finish();
            }
        });
    }
}
