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
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 13.
 * Time: 오후 7:19
 */

public class HomeFragment extends Fragment implements BaseMethod {

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

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "HomeFragment : onCreateView");
        View rootView = inflater.inflate(R.layout.disp_home, container, false);
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
        globalApplication.setFragment("Home", this);
    }

    private void _initFont(View rootView) {
        ViewGroup container = (LinearLayout) rootView.findViewById(R.id.home_layout_root);
        globalApplication.setAppFont(container);
    }

    private void _initUI(View rootView) {

        if(accountManager.isStudent()) {
            startingPointButton = (Button) rootView.findViewById(R.id.home_button_starting_point);
            startingPointTextView = (TextView) rootView.findViewById(R.id.home_textView_starting_point);
            startingPointButton.setText(" ");
            startingPointTextView.setText("-");
            startingPointButton.setAlpha(0.0f);
        }
        if(!accountManager.isStudent()){
            startingPointButton = (Button) rootView.findViewById(R.id.home_button_starting_point);
            startingPointTextView = (TextView) rootView.findViewById(R.id.home_textView_starting_point);
            startingPointButton.setText(R.string.home_button_starting_point_teacher);
            startingPointTextView.setText("Start New Class");
            sub_title = (TextView)rootView.findViewById(R.id.home_subtitle);
            main_string = (TextView)rootView.findViewById(R.id.home_main_string);


            startingPointButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isStartClicked) {
                        Log.d(TAG, "HomeFragment starting point button : click (teacher)");

                        sub_title.setText("Searching....");
                        main_string.setText(" ");
                        startingPointButton.setAlpha(0.0f);
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
        Log.d(TAG, "HomeFragment message : " + message);
    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {
        try {
            Log.d(TAG, "HomeFragment : handleAjaxCallBack Object => " + object);
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
                Log.d(TAG, "HomeFragment initDrawer() code by : " + code);
            }
        } catch (JSONException e) { e.printStackTrace(); }
    }

    @Override
    public void handlePush(JSONObject object) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
