package com.example.MobileSchool.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.MobileSchool.BaseMethod;
import com.example.MobileSchool.Communication.AjaxCallSender;
import com.example.MobileSchool.Communication.PushSender;
import com.example.MobileSchool.Utils.GlobalApplication;
import com.example.MobileSchool.Utils.AccountManager;
import com.example.MobileSchool.R;
import com.example.MobileSchool.BroadCastReceiver.ManagerRegistrationService;
import com.example.MobileSchool.Utils.Constants;
import org.json.JSONObject;


/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 13.
 * Time: 오후 6:28
 */

public class SettingFragment extends Fragment implements BaseMethod {

    private String TAG = Constants.TAG;

    private GlobalApplication globalApplication;
    private AccountManager accountManager;
    private PushSender pushSender;
    private AjaxCallSender ajaxCallSender;

    private Button teacherActivityButton;
    private Button studentActivityButton;
    private Button startServiceButton;
    private Button stopServiceButton;
    private Button sendNotificationButton;

    public SettingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.setting_fragment, container, false);
        getActivity().setTitle(Constants.FRAGMENT_TITLE_SETTING);
        Log.d(TAG, "SettingFragment : onCreateView");

        globalApplication = (GlobalApplication) getActivity().getApplication();
        accountManager = new AccountManager(getActivity().getApplicationContext());
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

        teacherActivityButton = (Button) rootView.findViewById(R.id.btn_teacherAccount);
        studentActivityButton = (Button) rootView.findViewById(R.id.btn_studentAccount);
        startServiceButton = (Button) rootView.findViewById(R.id.btn_startService);
        stopServiceButton = (Button) rootView.findViewById(R.id.btn_stopSerive);
        sendNotificationButton = (Button) rootView.findViewById(R.id.btn_sendNotification);

        teacherActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Click: teacher Account Button");
                // Teacher Test Account
                accountManager.saveAccount("tkkk","93");
            }
        });

        studentActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Click: student Account Button");
                // Student Test Account
                accountManager.saveAccount("kk","43");
            }
        });

        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Click: startServiceButton");
                getActivity().startService(new Intent(getActivity().getApplicationContext(), ManagerRegistrationService.class));
            }
        });

        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Click: stopServiceButton");
                getActivity().stopService(new Intent(getActivity().getApplicationContext(), ManagerRegistrationService.class));
            }
        });

        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
