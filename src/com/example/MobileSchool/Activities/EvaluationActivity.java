package com.example.MobileSchool.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import com.bugsense.trace.BugSenseHandler;
import com.example.MobileSchool.BaseMethod;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.AccountManager;
import com.example.MobileSchool.Utils.Constants;
import com.example.MobileSchool.Utils.GlobalApplication;
import org.json.JSONObject;

/**
 * User: yuhwan
 * Date: 13. 10. 26
 * Time: 오후 9:44
 */
public class EvaluationActivity extends Activity implements BaseMethod {
    private String TAG = Constants.TAG;

    private GlobalApplication globalApplication;
    private AccountManager accountManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disp_evaluation);
        Log.d(TAG, "EvaluationActivity : onCreate");

        globalApplication = (GlobalApplication) getApplication();
        accountManager = globalApplication.getAccountManager();

        // Bug Sense
        BugSenseHandler.initAndStartSession(this, "66fd741b");


        _initUI();
    }

    private void _initUI() {
    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void handlePush(JSONObject object) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
