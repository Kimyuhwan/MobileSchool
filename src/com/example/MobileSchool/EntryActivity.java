package com.example.MobileSchool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.example.MobileSchool.Activities.MyActivity;
import com.example.MobileSchool.Utils.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 11.
 * Time: 오후 4:43
 */
public class EntryActivity extends Activity {
    private String TAG = Constants.TAG;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);
        Log.d(TAG, "EntryActivity : onCreate");

        _nextActivity(MyActivity.class);
    }

    private void _nextActivity(Class next) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {e.printStackTrace();}
        startActivity(new Intent(this, next));
    }
}
