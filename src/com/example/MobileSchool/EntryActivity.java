package com.example.MobileSchool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.example.MobileSchool.Fragment.ScriptFragment;
import com.example.MobileSchool.Utils.Constants;
import com.example.MobileSchool.Utils.GlobalApplication;

/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 11.
 * Time: 오후 4:43
 */
public class EntryActivity extends Activity {
    private String TAG = Constants.TAG;

    private GlobalApplication globalApplication;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_activity);
        Log.d(TAG, "EntryActivity : onCreate");
        globalApplication = (GlobalApplication) getApplication();

        _nextActivity(SchoolActivity.class);
    }

    private void _nextActivity(Class next) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {e.printStackTrace();}
        startActivity(new Intent(this, next));
    }
}
