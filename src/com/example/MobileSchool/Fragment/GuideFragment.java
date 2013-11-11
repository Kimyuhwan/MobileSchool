package com.example.MobileSchool.Fragment;

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
import android.widget.TextView;
import android.widget.Toast;
import com.example.MobileSchool.BaseMethod;
import com.example.MobileSchool.Communication.AjaxCallSender;
import com.example.MobileSchool.Communication.PushSender;
import com.example.MobileSchool.Model.PartnerInfo;
import com.example.MobileSchool.Utils.GlobalApplication;
import com.example.MobileSchool.Utils.AccountManager;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.Constants;
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
        if(accountManager.isStudent()) {
            rootView = inflater.inflate(R.layout.disp_guide_student, container, false);
            if(globalApplication.isClassConnected())
                _initUIForStudentConnected(rootView);
            else
                _initUIForStudentUnconnected(rootView);
        }
        else {
            rootView = inflater.inflate(R.layout.disp_guide_teacher, container, false);
            if(globalApplication.isClassConnected())
                _initUIForTeacherConnected(rootView);
            else
                _initUIForTeacherUnconnected(rootView);
        }
        return rootView;
    }

    private void _initUIForStudentUnconnected(View rootView) {
        connectingTextView = (TextView) rootView.findViewById(R.id.guide_textView_connecting);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        connectingTextView.startAnimation(anim);

//        // 60초 후에 안오면 돌아가기.
//        mHandler = new Handler();
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(globalApplication.isSchoolActivityFront()) {
//                    _makeToast();
//                    globalApplication.setFragment("Home", new HomeFragment());
//                    globalApplication.setDrawerType(R.array.Home_menu_array);
//                    globalApplication.getSchoolActivity().initDrawer();
//                    globalApplication.getSchoolActivity().initFragment();
//                }
//            }
//        }, globalApplication.getWaitingTime());
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
        ajaxCallSender.confirm();
    }

    private void _initUIForTeacherConnected(View rootView) {
        connectingTextView = (TextView) rootView.findViewById(R.id.guide_textView_connecting);
        connectingTextView.setText(R.string.guide_textView_connected);
    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {
        try {
            Log.d(TAG, "GuideFragment : handleAjaxCallBack Object => " + object);
            String result = object.getString(Constants.PUSH_KEY_RESULT);
            String code = object.getString(Constants.PUSH_KEY_CODE);
            // Teacher
            if(result.equals("success") && code.equals(Constants.CODE_TEACHER_CONFIRM)) {
                connectingTextView.setText(R.string.guide_textView_connected);
                connectingTextView.clearAnimation();

                // 학생 정보 저장
                JSONObject studentInfo = object.getJSONObject(Constants.PUSH_KEY_STUDENT_INFO);
                PartnerInfo partnerInfo = new PartnerInfo(studentInfo.getString("sid"), studentInfo.getString("name"), studentInfo.getString("phoneNumber"), studentInfo.getInt("age"),studentInfo.getInt("gender"), studentInfo.getString("type"));
                globalApplication.setPartnerInfo(partnerInfo);

                // Connection 완료 저장
                globalApplication.setClassConnected(true);
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
            String code = object.getString(Constants.PUSH_KEY_CODE);
            if(code.equals(Constants.CODE_PUSH_TEACHER_INFO)) {
                JSONObject msg = object.getJSONObject(Constants.PUSH_TYPE_MESSAGE);
                JSONObject teacherJson = msg.getJSONObject(Constants.PUSH_KEY_TEACHER_INFO);
                PartnerInfo partnerInfo = new PartnerInfo(teacherJson.getString("tid"), teacherJson.getString("name"), teacherJson.getString("phoneNumber"), teacherJson.getInt("age"),teacherJson.getInt("gender"), teacherJson.getString("type"));
                globalApplication.setPartnerInfo(partnerInfo);
                globalApplication.setClassConnected(true);

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
