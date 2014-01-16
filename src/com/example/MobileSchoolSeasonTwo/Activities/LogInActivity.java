package com.example.MobileSchoolSeasonTwo.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.bugsense.trace.BugSenseHandler;
import com.example.MobileSchoolSeasonTwo.BaseMethod;
import com.example.MobileSchoolSeasonTwo.Communication.AjaxCallSender;
import com.example.MobileSchoolSeasonTwo.Model.MyInfo;
import com.example.MobileSchoolSeasonTwo.R;
import com.example.MobileSchoolSeasonTwo.SchoolActivity;
import com.example.MobileSchoolSeasonTwo.Utils.AccountManager;
import com.example.MobileSchoolSeasonTwo.Utils.Constants;
import com.example.MobileSchoolSeasonTwo.Utils.GlobalApplication;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * User: yuhwan
 * Date: 13. 10. 19
 * Time: 오후 4:47
 */
public class LogInActivity extends Activity implements BaseMethod{
    private String TAG = Constants.TAG;

    private GlobalApplication globalApplication;
    private AjaxCallSender ajaxCallSender;
    private AccountManager accountManager;

    private LinearLayout rootLinearLayout;
    private EditText idEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registrationButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.disp_login);
        Log.d(TAG, "LogInActivity : onCreate");

        globalApplication = (GlobalApplication) getApplication();
        ajaxCallSender = new AjaxCallSender(getApplicationContext(), this);
        accountManager = globalApplication.getAccountManager();

        // Bug Sense
        BugSenseHandler.initAndStartSession(this, Constants.BUGSENSE_KEY);


        _initUI();
        _initFont();
    }

    private void _initFont() {
        ViewGroup container = (LinearLayout) findViewById(R.id.login_layout_root);
        globalApplication.setAppFont(container);
    }

    private void _initUI() {
        rootLinearLayout = (LinearLayout) findViewById(R.id.login_layout_root);
        idEditText = (EditText) findViewById(R.id.login_editText_id);
        passwordEditText = (EditText) findViewById(R.id.login_editText_password);
        loginButton = (Button) findViewById(R.id.login_button_login);
        registrationButton = (Button) findViewById(R.id.login_button_registration);

        rootLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(getCurrentFocus());
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_isValid())
                    ajaxCallSender.login(idEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });
    }

    private boolean _isValid() {
        boolean result = true;
        String id = idEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if(id.equals("")) {
            Toast toast = Toast.makeText(this, "아이디를 입력해 주세요",Toast.LENGTH_SHORT);
            toast.show();
            return false;
        } else if(password.equals("")) {
            Toast toast = Toast.makeText(this, "비밀번호를 입력해 주세요",Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return  result;
    }

    protected void hideSoftKeyboard(View view) {
        InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void handleSocketMessage(String message) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {
        Log.d(TAG, "LogInActivity : handleAjaxCallBack Object => " + object);
        try {
            String status = object.getString("status");
            if(status.equals(Constants.RESPONSE_LOGIN_FAIL_WRONG_ID)) {
                Toast toast = Toast.makeText(this, R.string.toast_login_wrong_id,Toast.LENGTH_SHORT);
                toast.show();
            } else if(status.equals(Constants.RESPONSE_LOGIN_FAIL_WRONG_PASSWORD)) {
                Toast toast = Toast.makeText(this, R.string.toast_login_wrong_password, Toast.LENGTH_SHORT);
                toast.show();
            } else if(status.equals(Constants.RESPONSE_LOGIN_SUCCESS)){
                _handleSuccess(object);
            }
        } catch (JSONException e) { e.printStackTrace(); }
    }

    @Override
    public void handlePush(JSONObject object) {
    }

    private void _handleSuccess(JSONObject object) {
        try {
            JSONObject user;
            String type = "";
            if(!object.isNull("student")) {
                user = object.getJSONObject("student");
                type = "student";
            } else {
                user = object.getJSONObject("teacher");
                type = "teacher";
            }
            MyInfo myInfo = new MyInfo(object.getString("account_id"), user.getString("uid"),user.getString("name"),user.getString("phone"), user.getInt("age"), user.getString("gender"), type);
            //Check phoneNumber
            TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String phoneNumber = mgr.getLine1Number();
            if(phoneNumber.contains("+"))
                phoneNumber = "0" + phoneNumber.substring(3, phoneNumber.length());

            if(user.getString("phone").equals(phoneNumber)) {
                accountManager.saveMyInfo(myInfo);
                // Set Push Notification Framework (Parse)
                globalApplication.addSubscribe();
                globalApplication.setVersion();
                Intent intent = new Intent(getApplicationContext(), SchoolActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intent);
                finish();
            } else {
                Toast toast = Toast.makeText(this, "이 핸드폰 번호와 맞지 않는 아이디입니다.",Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (JSONException e) { e.printStackTrace(); }
    }
}
