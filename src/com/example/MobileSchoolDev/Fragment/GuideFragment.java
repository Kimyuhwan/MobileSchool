package com.example.MobileSchoolDev.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.MobileSchoolDev.BaseMethod;
import com.example.MobileSchoolDev.Communication.AjaxCallSender;
import com.example.MobileSchoolDev.Communication.PushSender;
import com.example.MobileSchoolDev.Model.PartnerInfo;
import com.example.MobileSchoolDev.Utils.GlobalApplication;
import com.example.MobileSchoolDev.Utils.AccountManager;
import com.example.MobileSchoolDev.R;
import com.example.MobileSchoolDev.Utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * User: yuhwan
 * Date: 13. 10. 16
 * Time: 오후 11:15
 */
public class GuideFragment extends Fragment implements BaseMethod {
    private String TAG = Constants.TAG;
    private String title = "Guide";

    private GlobalApplication globalApplication;
    private AccountManager accountManager;
    private PushSender pushSender;
    private AjaxCallSender ajaxCallSender;
    private Handler mHandler;

    private TextView connectingTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "GuideFragment : onCreateView");
        View rootView;
        globalApplication = (GlobalApplication) getActivity().getApplication();
        accountManager = globalApplication.getAccountManager();
        pushSender = new PushSender(getActivity().getApplicationContext());
        ajaxCallSender = new AjaxCallSender(getActivity().getApplicationContext(), this);

        getActivity().setTitle(title);

        _initFragment();
        if(accountManager.isStudent()) {
            rootView = inflater.inflate(R.layout.disp_guide_student, container, false);
            if(globalApplication.isSession_connected())
                _initUIForStudentConnected(rootView);
            else
                _initUIForStudentUnconnected(rootView);
            _initFontForStudent(rootView);
        }
        else {
            rootView = inflater.inflate(R.layout.disp_guide_teacher, container, false);
            if(globalApplication.isSession_connected())
                _initUIForTeacherConnected(rootView);
            else
                _initUIForTeacherUnconnected(rootView);
            _initFontForTeacher(rootView);
        }
        return rootView;
    }

    private void _initFragment() {
        globalApplication.setFragment("Guide", this);
    }

    private void _initFontForStudent(View rootView) {
        ViewGroup container = (LinearLayout) rootView.findViewById(R.id.guide_student_layout_root);
        globalApplication.setAppFont(container);
    }

    private void _initFontForTeacher(View rootView) {
        ViewGroup container = (LinearLayout) rootView.findViewById(R.id.guide_teacher_layout_root);
        globalApplication.setAppFont(container);
    }

    private void _initUIForStudentUnconnected(View rootView) {
        connectingTextView = (TextView) rootView.findViewById(R.id.guide_textView_connecting);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        connectingTextView.startAnimation(anim);
        if(globalApplication.getSession_type().equals(Constants.CODE_SESSION_PASSIVE))
            ajaxCallSender.sConfirm();
    }

    private void _initUIForStudentConnected(View rootView) {
        connectingTextView = (TextView) rootView.findViewById(R.id.guide_textView_connecting);
        connectingTextView.setText(R.string.guide_textView_connected);
    }

    private void _initUIForTeacherUnconnected(View rootView) {
        connectingTextView = (TextView) rootView.findViewById(R.id.guide_textView_connecting);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        connectingTextView.startAnimation(anim);
        if(globalApplication.getSession_type().equals(Constants.CODE_SESSION_PASSIVE))
            ajaxCallSender.tConfirm();
    }

    private void _initUIForTeacherConnected(View rootView) {
        connectingTextView = (TextView) rootView.findViewById(R.id.guide_textView_connecting);
        connectingTextView.setText(R.string.guide_textView_connected);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void handleSocketMessage(String message) {
        Log.d(TAG, "message : " + message);
    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {
        try {
            Log.d(TAG, "GuideFragment : handleAjaxCallBack Object => " + object);
            String result = object.getString(Constants.PUSH_KEY_RESULT);
            String code = object.getString(Constants.PUSH_KEY_CODE);
            if(result.equals("success") && (code.equals(Constants.CODE_TEACHER_CONFIRM) || code.equals(Constants.CODE_STUDENT_CONFIRM))) {
                connectingTextView.setText(R.string.guide_textView_connected);
                connectingTextView.clearAnimation();
                // 학생 정보 저장
                PartnerInfo partnerInfo;
                if(!accountManager.isStudent()) {
                    JSONObject studentInfo = object.getJSONObject(Constants.PUSH_KEY_STUDENT_INFO);
                    partnerInfo = new PartnerInfo(studentInfo.getString("sid"), studentInfo.getString("name"), studentInfo.getString("phoneNumber"), studentInfo.getInt("age"),studentInfo.getInt("gender"), studentInfo.getString("type"));
                } else {
                    JSONObject teacherInfo = object.getJSONObject(Constants.PUSH_KEY_TEACHER_INFO);
                    partnerInfo = new PartnerInfo(teacherInfo.getString("tid"), teacherInfo.getString("name"), teacherInfo.getString("phoneNumber"), teacherInfo.getInt("age"),teacherInfo.getInt("gender"), teacherInfo.getString("type"));
                }

                globalApplication.setPartnerInfo(partnerInfo);
                globalApplication.setSession_connected(true);
                if(globalApplication.isSchoolActivityFront()) {
                    globalApplication.setFragment("Profile", new ProfileFragment());
                    globalApplication.getSchoolActivity().initFragment();
                }
            } else if(result.equals("success") && code.equals(Constants.CODE_NO_STUDENT)) {
                connectingTextView.clearAnimation();
                connectingTextView.setText(R.string.guide_textView_no_class);

                if(globalApplication.isSchoolActivityFront()) {
                    globalApplication.setFragment("Home", new HomeFragment());
                    globalApplication.getSchoolActivity().initFragment();
                }
            }
        } catch (JSONException e) { e.printStackTrace(); }
    }

    @Override
    public void handlePush(JSONObject object) {
        try{
            Log.d(TAG, "GuideFragment : handlePush Object => " + object);
            String code = object.getString(Constants.PUSH_KEY_CODE);
            if(code.equals(Constants.CODE_PUSH_TEACHER_INFO) || code.equals(Constants.CODE_PUSH_STUDENT_INFO)) {
                JSONObject msg = object.getJSONObject(Constants.PUSH_TYPE_MESSAGE);
                PartnerInfo partnerInfo;

                if(accountManager.isStudent()) {
                    JSONObject teacherJson = msg.getJSONObject(Constants.PUSH_KEY_TEACHER_INFO);
                    partnerInfo = new PartnerInfo(teacherJson.getString("tid"), teacherJson.getString("name"), teacherJson.getString("phoneNumber"), teacherJson.getInt("age"),teacherJson.getInt("gender"), teacherJson.getString("type"));
                } else {
                    JSONObject studentJson = msg.getJSONObject(Constants.PUSH_KEY_STUDENT_INFO);
                    partnerInfo = new PartnerInfo(studentJson.getString("sid"), studentJson.getString("name"), studentJson.getString("phoneNumber"), studentJson.getInt("age"),studentJson.getInt("gender"), studentJson.getString("type"));
                }

                globalApplication.setPartnerInfo(partnerInfo);
                globalApplication.setSession_connected(true);
                connectingTextView.setText(R.string.guide_textView_connected);
                connectingTextView.clearAnimation();
                globalApplication.setFragment("Profile",new ProfileFragment());
                globalApplication.getSchoolActivity().initFragment();
            }
        } catch (JSONException e) { e.printStackTrace();}
    }

    private void _makeToast() {
        LayoutInflater inflater = (LayoutInflater) globalApplication.getSchoolActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_layout, null);
        TextView textView = (TextView) layout.findViewById(R.id.toast_textView_notification);
        textView.setText(R.string.toast_textView_warning);
        Toast toast = new Toast(globalApplication.getSchoolActivity().getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
