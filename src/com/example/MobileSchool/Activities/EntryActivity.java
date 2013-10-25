package com.example.MobileSchool.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.example.MobileSchool.Fragment.HomeFragment;
import com.example.MobileSchool.Fragment.ScriptFragment;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.Constants;
import com.example.MobileSchool.Utils.GlobalApplication;

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

        globalApplication = (GlobalApplication) getApplication();

        globalApplication.setFragment("Script",new ScriptFragment());
        globalApplication.setDrawerType(R.array.Class_menu_array);

        Intent intent = new Intent(EntryActivity.this, IntroActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
