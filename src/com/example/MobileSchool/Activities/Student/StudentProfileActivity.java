package com.example.MobileSchool.Activities.Student;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.Constants;

/**
 * User: yuhwan
 * Date: 13. 10. 14
 * Time: 오후 10:55
 */
public class StudentProfileActivity extends Activity {
    private String TAG = Constants.TAG;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_waiting_activity);
        Log.d(TAG, "StudentProfileActivity: onCreate");

    }
}
