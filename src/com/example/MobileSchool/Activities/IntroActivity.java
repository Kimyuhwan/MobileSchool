package com.example.MobileSchool.Activities;

import com.bugsense.trace.BugSenseHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import com.example.MobileSchool.Fragment.DialogueStudentFragment;
import com.example.MobileSchool.Fragment.DialogueTeacherFragment;
import com.example.MobileSchool.Model.MyInfo;
import com.example.MobileSchool.R;
import com.example.MobileSchool.SchoolActivity;
import com.example.MobileSchool.Utils.AccountManager;
import com.example.MobileSchool.Utils.Constants;
import com.example.MobileSchool.Utils.GlobalApplication;
import com.example.MobileSchool.Utils.AutoUpdateApk;
import org.joda.time.Hours;

/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 11.
 * Time: 오후 4:43
 */
public class IntroActivity extends Activity {
    private String TAG = Constants.TAG;

    private Handler mHandler;
    private Runnable mRunnable;
    private GlobalApplication globalApplication;
    private AccountManager accountManager;

    private AutoUpdateApk aua;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.disp_intro);
        Log.d(TAG, "IntroActivity : onCreate");

        // Bug Sense
        BugSenseHandler.initAndStartSession(IntroActivity.this, "66fd741b");

        // Auto Update
        aua = new AutoUpdateApk(getApplicationContext());
        aua.setUpdateInterval(1 * AutoUpdateApk.HOURS);

        globalApplication = (GlobalApplication) getApplication();
        accountManager = globalApplication.getAccountManager();

        MyInfo myInfo = accountManager.getMyInfo();
        Log.d(TAG, "IntroActivity myInfo : " + myInfo);
        _setHandler(myInfo);
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 3000);
    }

    private void _setHandler(MyInfo myInfo) {

        final Intent intent;
        if(myInfo == null) intent = new Intent(getApplicationContext(), LogInActivity.class);
        else intent = new Intent(getApplicationContext(), SchoolActivity.class);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent);
                finish();
            }
        };
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }
}
