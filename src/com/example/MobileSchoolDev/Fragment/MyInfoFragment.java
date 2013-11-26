package com.example.MobileSchoolDev.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.MobileSchoolDev.Activities.IntroActivity;
import com.example.MobileSchoolDev.BaseMethod;
import com.example.MobileSchoolDev.Communication.AjaxCallSender;
import com.example.MobileSchoolDev.Communication.PushSender;
import com.example.MobileSchoolDev.R;
import com.example.MobileSchoolDev.Utils.AccountManager;
import com.example.MobileSchoolDev.Utils.Constants;
import com.example.MobileSchoolDev.Utils.GlobalApplication;
import org.json.JSONObject;

/**
 * User: yuhwan
 * Date: 13. 10. 22
 * Time: 오후 7:48
 */
public class MyInfoFragment extends Fragment implements BaseMethod {

    private String TAG = Constants.TAG;
    private String title = "My Information";

    private GlobalApplication globalApplication;
    private AccountManager accountManager;
    private PushSender pushSender;
    private AjaxCallSender ajaxCallSender;

    private TextView typeTextView;
    private TextView nameTextView;
    private TextView phoneNumberTextView;

    private Button logOutButton;

    public MyInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.disp_myinfo, container, false);
        getActivity().setTitle(title);

        globalApplication = (GlobalApplication) getActivity().getApplication();
        accountManager = globalApplication.getAccountManager();
        pushSender = new PushSender(getActivity().getApplicationContext());
        ajaxCallSender = new AjaxCallSender(getActivity().getApplicationContext(), this);
        _initFragment();
        _initUI(rootView);
        _initFont(rootView);
        return rootView;
    }

    private void _initFont(View rootView) {
        ViewGroup container = (LinearLayout) rootView.findViewById(R.id.myinfo_layout_root);
        globalApplication.setAppFont(container);
    }

    private void _initFragment() {
        globalApplication.setFragment("Home", new HomeFragment());
    }

    private void _initUI(View rootView) {
       typeTextView = (TextView) rootView.findViewById(R.id.myinfo_textView_profile_type);
       nameTextView = (TextView) rootView.findViewById(R.id.myinfo_textView_profile_name);
       phoneNumberTextView = (TextView) rootView.findViewById(R.id.myinfo_textView_profile_phoneNumber);
       logOutButton = (Button) rootView.findViewById(R.id.myinfo_button_logout);

       typeTextView.setText(accountManager.getMyInfo().getType());
       nameTextView.setText(accountManager.getMyInfo().getName());
       phoneNumberTextView.setText(accountManager.getMyInfo().getPhoneNumber());
       logOutButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Log.d(TAG, "MyInfoFragment : log out");
               globalApplication.removeSubscribe();
               accountManager.removeMyInfo();
               Intent intent = new Intent(getActivity(), IntroActivity.class);
               startActivity(intent);
               getActivity().finish();
           }
       });
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
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
