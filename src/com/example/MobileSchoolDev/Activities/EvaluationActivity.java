package com.example.MobileSchoolDev.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.bugsense.trace.BugSenseHandler;
import com.example.MobileSchoolDev.BaseMethod;
import com.example.MobileSchoolDev.R;
import com.example.MobileSchoolDev.Utils.AccountManager;
import com.example.MobileSchoolDev.Utils.Constants;
import com.example.MobileSchoolDev.Utils.GlobalApplication;
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
