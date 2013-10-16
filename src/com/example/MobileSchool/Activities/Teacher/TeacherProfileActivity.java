package com.example.MobileSchool.Activities.Teacher;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.example.MobileSchool.Manager.AccountManager;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.Constants;

/**
 * User: yuhwan
 * Date: 13. 10. 14
 * Time: 오후 10:55
 */
public class TeacherProfileActivity extends Activity {
    private String TAG = Constants.TAG;

    private AccountManager accountManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_profile_activity);
        accountManager = new AccountManager(this);
        Log.d(TAG, "TeacherProfileActivity : onCreate");

        // Call
        Log.d(TAG, "TeacherProfileActivity StudentPhoneNumber : " + accountManager.getOppositePhoneNubmer());
        Intent callIntent = new Intent(Intent.ACTION_CALL);
//        callIntent.setData(Uri.parse("tel:" + accountManager.getOppositePhoneNubmer()));
        callIntent.setData(Uri.parse("tel:" + "01034240163"));
        startActivity(callIntent);
    }
}
