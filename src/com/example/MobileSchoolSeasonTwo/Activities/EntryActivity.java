package com.example.MobileSchoolSeasonTwo.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.actionbarsherlock.app.SherlockActivity;
import com.bugsense.trace.BugSenseHandler;
import com.example.MobileSchoolSeasonTwo.Fragment.DialogueStudentFragment;
import com.example.MobileSchoolSeasonTwo.Fragment.DialogueTeacherFragment;
import com.example.MobileSchoolSeasonTwo.R;
import com.example.MobileSchoolSeasonTwo.SchoolActivity;
import com.example.MobileSchoolSeasonTwo.Utils.Constants;
import com.example.MobileSchoolSeasonTwo.Utils.GlobalApplication;

/**
 * User: yuhwan
 * Date: 13. 10. 16
 * Time: 오후 2:53
 */
public class EntryActivity extends SherlockActivity {
    // FakeActivity
    private String TAG = Constants.TAG;
    private GlobalApplication globalApplication;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "EntryActivity: onCreate");

        // Bug Sense
        BugSenseHandler.initAndStartSession(this, Constants.BUGSENSE_KEY);

        globalApplication = (GlobalApplication) getApplication();
        if(globalApplication.getAccountManager().isStudent()) {
            globalApplication.setFragment("DialogueStudent",new DialogueStudentFragment());
        }
        else {
            globalApplication.setFragment("DialogueTeacher", new DialogueTeacherFragment());
        }
        globalApplication.setDrawerType(R.array.Class_menu_array);
        finish();

        Intent intent = new Intent(EntryActivity.this, SchoolActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
