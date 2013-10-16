package com.example.MobileSchool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.example.MobileSchool.Utils.Constants;

/**
 * User: yuhwan
 * Date: 13. 10. 16
 * Time: 오후 2:53
 */
public class ClassEntryActivity extends Activity {
    private String TAG = Constants.TAG;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "ClassEntryActivity: onCreate");

        Intent intent = new Intent(ClassEntryActivity.this, EntryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
