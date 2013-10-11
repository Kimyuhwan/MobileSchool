package com.example.MobileSchool.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.Constants;

/**
 * User: yuhwan
 * Date: 13. 10. 11
 * Time: 오후 8:30
 */
public class LogInActivity extends Activity {
    private String TAG = Constants.TAG;

    private Button teacherActivityButton;
    private Button studentActivityButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        _initUI();
    }

    private void _initUI() {
        teacherActivityButton = (Button) findViewById(R.id.btn_teacherActivity);
        studentActivityButton = (Button) findViewById(R.id.btn_studentActivity);

        teacherActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Click: teacherActivityButton");
                // Teacher Test Account
                _saveAccount("teacher");
                startActivity(new Intent(getApplicationContext(), TeacherActivity.class));
            }
        });

        studentActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Click: studentActivityButton");
                // Student Test Account
                _saveAccount("student");
                startActivity(new Intent(getApplicationContext(), StudentActivity.class));
            }
        });
    }

    private void _saveAccount(String id) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.TAG, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHAREDPREFERENCES_USER_ID, id);
        editor.commit();
    }


}
