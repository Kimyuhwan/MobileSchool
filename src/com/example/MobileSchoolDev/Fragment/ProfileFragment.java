package com.example.MobileSchoolDev.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.MobileSchoolDev.BaseMethod;
import com.example.MobileSchoolDev.Communication.AjaxCallSender;
import com.example.MobileSchoolDev.Communication.PushSender;
import com.example.MobileSchoolDev.Utils.GlobalApplication;
import com.example.MobileSchoolDev.Utils.AccountManager;
import com.example.MobileSchoolDev.R;
import com.example.MobileSchoolDev.Utils.Constants;
import org.json.JSONObject;

/**
 * User: yuhwan
 * Date: 13. 10. 16
 * Time: 오후 11:15
 */
public class ProfileFragment extends Fragment implements BaseMethod {
    private String TAG = Constants.TAG;
    private String title = "Profile";

    private GlobalApplication globalApplication;
    private AccountManager accountManager;
    private PushSender pushSender;
    private AjaxCallSender ajaxCallSender;

    private TextView typeTextView;
    private TextView nameTextView;
    private TextView phoneNumberView;
    private TextView callInformationTextView;

    private Handler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "ProfileFragment : onCreateView");

        globalApplication = (GlobalApplication) getActivity().getApplication();
        accountManager = globalApplication.getAccountManager();
        pushSender = new PushSender(getActivity().getApplicationContext());
        ajaxCallSender = new AjaxCallSender(getActivity().getApplicationContext(), this);

        getActivity().setTitle(title);
        View rootView = inflater.inflate(R.layout.disp_profile, container, false);

        if(globalApplication.isClassConnected() && globalApplication.getPartnerInfo() != null) {
            if(accountManager.isStudent())
                _initUIForStudent(rootView);
            else
                _initUIForTeacher(rootView);
        }
        _initFont(rootView);
        return rootView;
    }

    private void _initFont(View rootView) {
        ViewGroup container = (LinearLayout) rootView.findViewById(R.id.profile_layout_root);
        globalApplication.setAppFont(container);
    }

    private void _initUIForStudent(View rootView) {
        typeTextView = (TextView) rootView.findViewById(R.id.profile_textView_profile_type);
        nameTextView = (TextView) rootView.findViewById(R.id.profile_textView_profile_name);
        phoneNumberView = (TextView) rootView.findViewById(R.id.profile_textView_profile_phoneNumber);
        callInformationTextView = (TextView) rootView.findViewById(R.id.profile_textView_call_information) ;

        typeTextView.setText(globalApplication.getPartnerInfo().getType());
        nameTextView.setText(globalApplication.getPartnerInfo().getName());
        phoneNumberView.setText(globalApplication.getPartnerInfo().getPhoneNumber());
        callInformationTextView.setText(R.string.profile_textView_call_information_student_msg);
        ajaxCallSender.ready();
    }

    private void _initUIForTeacher(View rootView) {
        typeTextView = (TextView) rootView.findViewById(R.id.profile_textView_profile_type);
        nameTextView = (TextView) rootView.findViewById(R.id.profile_textView_profile_name);
        phoneNumberView = (TextView) rootView.findViewById(R.id.profile_textView_profile_phoneNumber);
        callInformationTextView = (TextView) rootView.findViewById(R.id.profile_textView_call_information) ;

        typeTextView.setText(globalApplication.getPartnerInfo().getType());
        nameTextView.setText(globalApplication.getPartnerInfo().getName());
        phoneNumberView.setText(globalApplication.getPartnerInfo().getPhoneNumber());
        callInformationTextView.setText(R.string.profile_textView_call_information_teacher_msg);

    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void handlePush(JSONObject object) {
        Log.d(TAG, "ProfileFragment : " + object);
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
