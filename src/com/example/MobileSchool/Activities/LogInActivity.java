package com.example.MobileSchool.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.MobileSchool.BaseMethod;
import com.example.MobileSchool.Communication.AjaxCallSender;
import com.example.MobileSchool.Model.MyInfo;
import com.example.MobileSchool.R;
import com.example.MobileSchool.SchoolActivity;
import com.example.MobileSchool.Utils.AccountManager;
import com.example.MobileSchool.Utils.Constants;
import com.example.MobileSchool.Utils.GlobalApplication;
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

    private EditText idEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.disp_login);
        Log.d(TAG, "LogInActivity : onCreate");

        globalApplication = (GlobalApplication) getApplication();
        ajaxCallSender = new AjaxCallSender(getApplicationContext(), this);
        accountManager = new AccountManager(this);
        _initUI();
    }

    private void _initUI() {
        idEditText = (EditText) findViewById(R.id.login_editText_id);
        passwordEditText = (EditText) findViewById(R.id.login_editText_password);
        loginButton = (Button) findViewById(R.id.login_button_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 ajaxCallSender.login(idEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });
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
            accountManager.saveMyInfo(myInfo);
            Intent intent = new Intent(getApplicationContext(), SchoolActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);
            finish();
        } catch (JSONException e) { e.printStackTrace(); }
    }
}
