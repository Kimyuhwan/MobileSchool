package com.example.MobileSchool.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.Constants;

/**
 * User: yuhwan
 * Date: 13. 10. 11
 * Time: 오후 8:32
 */
public class TeacherRoomActivity extends Activity {

    private String TAG = Constants.TAG;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacherroom);
        Log.d(TAG, "TeacherRoomActivity: onCreate");

    }

}
