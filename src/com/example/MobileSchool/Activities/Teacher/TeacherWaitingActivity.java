package com.example.MobileSchool.Activities.Teacher;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.example.MobileSchool.Communication.AjaxCallSender;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.Constants;

/**
 * User: yuhwan
 * Date: 13. 10. 14
 * Time: 오후 6:05
 */
public class TeacherWaitingActivity extends Activity {
    private String TAG = Constants.TAG;
    private AjaxCallSender ajaxCallSender;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_waiting_activity);
        Log.d(TAG, "TeacherWaitingActivity : onCreate");

        ajaxCallSender = new AjaxCallSender(this);
        ajaxCallSender.matchMake();
    }
}
