package com.example.MobileSchool.Activities.Student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.example.MobileSchool.Communication.AjaxCallSender;
import com.example.MobileSchool.Communication.PushSender;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.Constants;

/**
 * User: yuhwan
 * Date: 13. 10. 11
 * Time: 오후 8:32
 */
public class StudentWaitingActivity extends Activity {
    private String TAG = Constants.TAG;

    private AjaxCallSender ajaxCallSender;
    private PushSender pushSender;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_waiting_activity);
        Log.d(TAG, "StudentWaitingActivity: onCreate");

        // Match Find to Server
        ajaxCallSender = new AjaxCallSender(this);
        ajaxCallSender.matchFind();

        // Push Teacher STD_FIND
        pushSender = new PushSender(this);
        pushSender.pushStudentFind("tkkk"); // Will be changed

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {e.printStackTrace();}

        // Next Activity
        startActivity(new Intent(this, StudentProfileActivity.class));
    }
}
