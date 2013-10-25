package com.example.MobileSchool.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import com.example.MobileSchool.BroadCastReceiver.ManagerRegistrationService;
import com.example.MobileSchool.Fragment.GuideFragment;
import com.example.MobileSchool.Model.MyInfo;
import com.example.MobileSchool.R;
import com.example.MobileSchool.SchoolActivity;
import com.example.MobileSchool.Utils.AccountManager;
import com.example.MobileSchool.Utils.Constants;
import com.example.MobileSchool.Utils.GlobalApplication;

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
    private AccountManager accountManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.disp_intro);
        Log.d(TAG, "IntroActivity : onCreate");

        accountManager = new AccountManager(this);
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
