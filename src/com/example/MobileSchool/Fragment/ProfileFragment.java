package com.example.MobileSchool.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.MobileSchool.BaseMethod;
import com.example.MobileSchool.Communication.AjaxCallSender;
import com.example.MobileSchool.Communication.PushSender;
import com.example.MobileSchool.Utils.GlobalApplication;
import com.example.MobileSchool.Utils.AccountManager;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.Constants;
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
        accountManager = new AccountManager(getActivity().getApplicationContext());
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
        return rootView;
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

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent callIntent = new Intent(Intent.ACTION_CALL);
////        callIntent.setData(Uri.parse("tel:" + accountManager.getOppositePhoneNubmer()));
//                callIntent.setData(Uri.parse("tel:" + "01090145180"));
//                startActivity(callIntent);
//                getActivity().finish();
            }
        }, 3000);

    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
