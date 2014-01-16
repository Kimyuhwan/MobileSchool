package com.example.MobileSchoolSeasonTwo.Fragment;


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
import com.example.MobileSchoolSeasonTwo.Activities.LogInActivity;
import com.example.MobileSchoolSeasonTwo.BaseMethod;
import com.example.MobileSchoolSeasonTwo.Communication.AjaxCallSender;
import com.example.MobileSchoolSeasonTwo.Communication.PushSender;
import com.example.MobileSchoolSeasonTwo.Utils.GlobalApplication;
import com.example.MobileSchoolSeasonTwo.Utils.AccountManager;
import com.example.MobileSchoolSeasonTwo.R;
import com.example.MobileSchoolSeasonTwo.Utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by 정필 on 14. 1. 14.
 */

public class RetryFragment extends Fragment implements BaseMethod {

    private String TAG = Constants.TAG;

    private GlobalApplication globalApplication;
    private AccountManager accountManager;
    private PushSender pushSender;
    private AjaxCallSender ajaxCallSender;

    private TextView startingPointTextView;
    private TextView sub_title;
    private TextView main_string;
    private Button startingPointButton;
    // Experiment
    private Button startingPointButton_A;
    private Button startingPointButton_B;

    private Boolean isStartClicked = false;

    public RetryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "RetryFragment : onCreateView");
        View rootView = inflater.inflate(R.layout.disp_searching, container, false);
        getActivity().setTitle(Constants.FRAGMENT_TITLE_HOME);

        globalApplication = (GlobalApplication) getActivity().getApplication();
        accountManager = globalApplication.getAccountManager();
        pushSender = new PushSender(getActivity().getApplicationContext());
        ajaxCallSender = new AjaxCallSender(getActivity().getApplicationContext(), this);

        //Initialize
        globalApplication.setSession_connected(false);
        globalApplication.setPartnerInfo(null);

        if(!globalApplication.isValidVersion())  {
            globalApplication.removeSubscribe();
            Intent intent = new Intent(getActivity().getApplicationContext(), LogInActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        if(!_isLogged()) {
            Intent intent = new Intent(getActivity().getApplicationContext(), LogInActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        _initFragment();
        _initUI(rootView);
        _initFont(rootView);

        return rootView;
    }

    private boolean _isLogged() {
        if(accountManager.getMyInfo() != null)
            return true;
        else
            return false;
    }

    private void _initFragment() {
        globalApplication.setFragment("Retry", this);
    }

    private void _initFont(View rootView) {
        ViewGroup container = (LinearLayout) rootView.findViewById(R.id.search_layout_root);
        globalApplication.setAppFont(container);
    }

    private void _initUI(View rootView) {

        if(!accountManager.isStudent()){
            startingPointButton = (Button) rootView.findViewById(R.id.search_button);
            startingPointTextView = (TextView) rootView.findViewById(R.id.search_text);
            startingPointButton.setText("Retry");
            startingPointTextView.setText("Start New Class");


            startingPointButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isStartClicked) {
                        Log.d(TAG, "RetryFragment starting point button : click (teacher)");

                        ajaxCallSender.tStart();
                        isStartClicked = true;

                    } else {
                        Log.d(TAG, "Start Button is already clicked");
                    }
                }
            });
        }
    }

    @Override
    public void handleSocketMessage(String message) {
        Log.d(TAG, "RetryFragment message : " + message);
    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {
        try {
            Log.d(TAG, "RetryFragment : handleAjaxCallBack Object => " + object);
            // For Start New Class
            String result = object.getString(Constants.PUSH_KEY_RESULT);
            String code = object.getString(Constants.PUSH_KEY_CODE);
            if(result.equals("success") && (code.equals(Constants.CODE_STUDENT_START) || code.equals(Constants.CODE_TEACHER_START))) {
                globalApplication.setSession_type(Constants.CODE_SESSION_ACTIVE);
                globalApplication.setWaitingTime(object.getInt(Constants.PUSH_KEY_WAITING) * 1000);
                globalApplication.setFragment("Guide", new GuideFragment());
                globalApplication.setDrawerType(R.array.Waiting_menu_array);
                globalApplication.getSchoolActivity().initDrawer();
                globalApplication.getSchoolActivity().initFragment();
                Log.d(TAG, "RetryFragment initDrawer() code by : " + code);
            }
        } catch (JSONException e) { e.printStackTrace(); }
    }

    @Override
    public void handlePush(JSONObject object) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
