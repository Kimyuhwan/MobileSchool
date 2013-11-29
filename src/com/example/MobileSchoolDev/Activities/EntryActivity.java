package com.example.MobileSchoolDev.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.bugsense.trace.BugSenseHandler;
import com.example.MobileSchoolDev.Fragment.DialogueStudentFragment;
import com.example.MobileSchoolDev.Fragment.DialogueTeacherFragment;
import com.example.MobileSchoolDev.R;
import com.example.MobileSchoolDev.SchoolActivity;
import com.example.MobileSchoolDev.Utils.Constants;
import com.example.MobileSchoolDev.Utils.GlobalApplication;

/**
 * User: yuhwan
 * Date: 13. 10. 16
 * Time: 오후 2:53
 */
public class EntryActivity extends Activity {
    // FakeActivity
    private String TAG = Constants.TAG;
    private GlobalApplication globalApplication;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "EntryActivity: onCreate");

        // Bug Sense
        BugSenseHandler.initAndStartSession(this, Constants.BUGSENSE_KEY);

        globalApplication = (GlobalApplication) getApplication();
        if(globalApplication.getAccountManager().isStudent())
            globalApplication.setFragment("DialogueStudent",new DialogueStudentFragment());
        else
            globalApplication.setFragment("DialogueTeacher", new DialogueTeacherFragment());
        globalApplication.setDrawerType(R.array.Class_menu_array);
        finish();

        Intent intent = new Intent(EntryActivity.this, SchoolActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
