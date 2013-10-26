package com.example.MobileSchool.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.MobileSchool.BaseMethod;
import com.example.MobileSchool.Communication.AjaxCallSender;
import com.example.MobileSchool.Communication.PushSender;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.AccountManager;
import com.example.MobileSchool.Utils.Constants;
import com.example.MobileSchool.Utils.GlobalApplication;
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

        return rootView;
    }

    private void _initFragment() {
        globalApplication.setFragment("Home", new HomeFragment());
    }

    private void _initUI(View rootView) {
       typeTextView = (TextView) rootView.findViewById(R.id.myinfo_textView_profile_type);
       nameTextView = (TextView) rootView.findViewById(R.id.myinfo_textView_profile_name);
       phoneNumberTextView = (TextView) rootView.findViewById(R.id.myinfo_textView_profile_phoneNumber);

       typeTextView.setText(accountManager.getMyInfo().getType());
       nameTextView.setText(accountManager.getMyInfo().getName());
       phoneNumberTextView.setText(accountManager.getMyInfo().getPhoneNumber());
    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
