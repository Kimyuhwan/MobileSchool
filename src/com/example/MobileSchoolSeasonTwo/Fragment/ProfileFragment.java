package com.example.MobileSchoolSeasonTwo.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.example.MobileSchoolSeasonTwo.BaseMethod;
import com.example.MobileSchoolSeasonTwo.Communication.AjaxCallSender;
import com.example.MobileSchoolSeasonTwo.Communication.PushSender;
import com.example.MobileSchoolSeasonTwo.Utils.GlobalApplication;
import com.example.MobileSchoolSeasonTwo.Utils.AccountManager;
import com.example.MobileSchoolSeasonTwo.R;
import com.example.MobileSchoolSeasonTwo.Utils.Constants;
import org.json.JSONObject;

/**
 * User: yuhwan
 * Date: 13. 10. 16
 * Time: 오후 11:15
 */
public class ProfileFragment extends SherlockFragment implements BaseMethod {
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

        _initFragment();
        if(globalApplication.getPartnerInfo() != null) {
            if(accountManager.isStudent())
                _initUIForStudent(rootView);
            else
                _initUIForTeacher(rootView);
        }
        _initFont(rootView);
        return rootView;
    }

    private void _initFragment() {
        globalApplication.setFragment("Profile", this);
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
        if(globalApplication.getSession_type().equals(Constants.CODE_SESSION_ACTIVE))
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

        if(globalApplication.getSession_type().equals(Constants.CODE_SESSION_ACTIVE)) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + globalApplication.getPartnerInfo().getPhoneNumber()));
            startActivity(callIntent);
        }
    }

    @Override
    public void handleSocketMessage(String message) {
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
