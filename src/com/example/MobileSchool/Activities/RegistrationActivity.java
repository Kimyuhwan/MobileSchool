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

    private EditText idEditText;
    private EditText passwordEditText;
    private EditText nameEditText;

    private String gender = Constants.REGISTRATION_GENDER_MALE;
    private String type = Constants.REGISTRATION_TYPE_STUDENT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.disp_registration);
        Log.d(TAG, "RegistrationActivity : onCreate");

        globalApplication = (GlobalApplication) getApplication();
        ajaxCallSender = new AjaxCallSender(getApplicationContext(), this);
        accountManager = new AccountManager(this);
        _initUI();
    }

    private void _initUI() {
       confirmButton = (Button) findViewById(R.id.registration_button_confirm);
       phoneEditText = (EditText) findViewById(R.id.registration_editText_phone);
       genderRadioGroup = (RadioGroup) findViewById(R.id.registration_radioGroup_gender);
       typeRadioGroup = (RadioGroup) findViewById(R.id.registration_radioGroup_type);

       genderRadioGroup.setOnCheckedChangeListener(this);
       typeRadioGroup.setOnCheckedChangeListener(this);

       confirmButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              MyInfo myInfo = _getMyInfo();
              if(myInfo != null)
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
        MyInfo myInfo = null;
        // Check Valid!
        idEditText = (EditText) findViewById(R.id.registration_editText_id);
        passwordEditText = (EditText) findViewById(R.id.registration_editText_password);
        nameEditText = (EditText) findViewById(R.id.registration_editText_name);

        myInfo = new MyInfo(idEditText.getText().toString(), passwordEditText.getText().toString(), nameEditText.getText().toString(), gender, type, phoneEditText.getText().toString());
        return myInfo;
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
                if(globalApplication.isSchoolActivityFront()) _makeToast("The ID already exists");
            }

        } catch (JSONException e) { e.printStackTrace(); }
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