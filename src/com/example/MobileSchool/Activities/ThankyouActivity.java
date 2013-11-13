package com.example.MobileSchool.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import com.bugsense.trace.BugSenseHandler;
import com.example.MobileSchool.Model.MyInfo;
import com.example.MobileSchool.R;
import com.example.MobileSchool.SchoolActivity;
import com.example.MobileSchool.Utils.AccountManager;
import com.example.MobileSchool.Utils.AutoUpdateApk;
import com.example.MobileSchool.Utils.Constants;
import com.example.MobileSchool.Utils.GlobalApplication;

/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 11.
 * Time: 오후 4:43
 */
public class ThankyouActivity extends Activity {
    private String TAG = Constants.TAG;

    private Handler mHandler;
    private Runnable mRunnable;
    private GlobalApplication globalApplication;
    private AccountManager accountManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.disp_thankyou);
        Log.d(TAG, "IntroActivity : onCreate");

        globalApplication = (GlobalApplication) getApplication();
        accountManager = globalApplication.getAccountManager();

        _initFont();
    }
    private void _initFont() {
        ViewGroup container = (LinearLayout) findViewById(R.id.thankyou_layout_root);
        globalApplication.setAppFont(container);
    }

}
