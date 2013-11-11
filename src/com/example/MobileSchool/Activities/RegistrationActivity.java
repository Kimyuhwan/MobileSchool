package com.example.MobileSchool.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
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
 * Date: 13. 10. 25
 * Time: 오후 9:26
 */
public class RegistrationActivity extends Activity implements BaseMethod, RadioGroup.OnCheckedChangeListener {
    private String TAG = Constants.TAG;

    private GlobalApplication globalApplication;
    private AjaxCallSender ajaxCallSender;
    private AccountManager accountManager;

    private Button confirmButton;
    private EditText phoneEditText;
    private RadioGroup genderRadioGroup;
    private RadioGroup typeRadioGroup;

    private LinearLayout rootLinearLayout;
    private EditText idEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private TextView msgTextView;

    private String gender = Constants.REGISTRATION_GENDER_MALE;
    private String type = Constants.REGISTRATION_TYPE_STUDENT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.disp_registration);
        Log.d(TAG, "RegistrationActivity : onCreate");

        globalApplication = (GlobalApplication) getApplication();
        accountManager = globalApplication.getAccountManager();
        ajaxCallSender = new AjaxCallSender(getApplicationContext(), this);


        _initUI();
    }

    private void _initUI() {
       rootLinearLayout = (LinearLayout) findViewById(R.id.registration_layout_root);
       confirmButton = (Button) findViewById(R.id.registration_button_confirm);
       phoneEditText = (EditText) findViewById(R.id.registration_editText_phone);
       genderRadioGroup = (RadioGroup) findViewById(R.id.registration_radioGroup_gender);
       typeRadioGroup = (RadioGroup) findViewById(R.id.registration_radioGroup_type);
       idEditText = (EditText) findViewById(R.id.registration_editText_id);
       passwordEditText = (EditText) findViewById(R.id.registration_editText_password);
       nameEditText = (EditText) findViewById(R.id.registration_editText_name);
       msgTextView = (TextView) findViewById(R.id.registration_textView_msg);

       genderRadioGroup.setOnCheckedChangeListener(this);
       typeRadioGroup.setOnCheckedChangeListener(this);

       rootLinearLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                hideSoftKeyboard(getCurrentFocus());
           }
       });

       confirmButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              MyInfo myInfo = _getMyInfo();
               if(_checkValid())
                 ajaxCallSender.register(myInfo);
           }
       });

       TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
       String phoneNumber = mgr.getLine1Number();
       if(phoneNumber.contains("+"))
        phoneEditText.setText("0" + phoneNumber.substring(3, phoneNumber.length()));
       else
        phoneEditText.setText(phoneNumber);
    }

    private MyInfo _getMyInfo() {
        MyInfo myInfo  = new MyInfo(idEditText.getText().toString(), passwordEditText.getText().toString(), nameEditText.getText().toString(), gender, type, phoneEditText.getText().toString());
        return myInfo;
    }

    private boolean _checkValid() {
        String id = idEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String name = nameEditText.getText().toString();
        if(id.equals("") || password.equals("") || name.equals("")) {
            if(name.equals(""))
                nameEditText.requestFocus();
            if(password.equals(""))
                passwordEditText.requestFocus();
            if(id.equals(""))
                idEditText.requestFocus();
            msgTextView.setText("채워지지 않은 항목이 있습니다.");
            return false;
        } else
            return true;
    }

    protected void hideSoftKeyboard(View view) {
        InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId == R.id.registration_radioGroup_male)
            gender = Constants.REGISTRATION_GENDER_MALE;
        else if(checkedId == R.id.registration_radioGroup_female)
            gender = Constants.REGISTRATION_GENDER_FEMALE;

        else if(checkedId == R.id.registration_radioGroup_student)
            type = Constants.REGISTRATION_TYPE_STUDENT;
        else
            type = Constants.REGISTRATION_TYPE_TEACHER;
    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {
        Log.d(TAG, "RegistrationActivity : handleAjaxCallBack Object => " + object);
        try {
            String status = object.getString("status");
            if(status.equals(Constants.RESPONSE_REGISTRATION_STUDENT_SUCCESS) || status.equals(Constants.RESPONSE_REGISTRATION_TEACHER_SUCCESS)) {
                if(globalApplication.isSchoolActivityFront()) _makeToast("Registration Success");
                startActivity(new Intent(this, LogInActivity.class));
                finish();
            } else {
                Log.d(TAG, "Sign up fail : " + status);
                msgTextView.setText("존재하는 ID 입니다.");
                idEditText.setText("");
                idEditText.requestFocus();
            }

        } catch (JSONException e) { e.printStackTrace(); }
    }

    @Override
    public void handlePush(JSONObject object) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private void _makeToast(String message) {
        LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View layout = inflater.inflate(R.layout.toast_layout, null);
        TextView textView = (TextView) layout.findViewById(R.id.toast_textView_notification);
        textView.setText(message);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}