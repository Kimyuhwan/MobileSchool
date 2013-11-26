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
import com.example.MobileSchoolDev.Activities.LogInActivity;
import com.example.MobileSchoolDev.BaseMethod;
import com.example.MobileSchoolDev.Communication.AjaxCallSender;
import com.example.MobileSchoolDev.Communication.PushSender;
import com.example.MobileSchoolDev.Communication.SocketCommunication;
import com.example.MobileSchoolDev.Utils.GlobalApplication;
import com.example.MobileSchoolDev.Utils.AccountManager;
import com.example.MobileSchoolDev.R;
import com.example.MobileSchoolDev.Utils.Constants;
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
    private Button startingPointButton;

    private Button temporalButton;

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
        globalApplication.setFragment("Home", new HomeFragment());
    }

    private void _initFont(View rootView) {
        ViewGroup container = (LinearLayout) rootView.findViewById(R.id.home_layout_root);
        globalApplication.setAppFont(container);
    }

    private void _initUI(View rootView) {
        startingPointButton = (Button) rootView.findViewById(R.id.home_button_starting_point);
        startingPointTextView = (TextView) rootView.findViewById(R.id.home_textView_starting_point);

        if(accountManager.isStudent()) {
            startingPointButton.setText(R.string.home_button_starting_point_student);
            startingPointTextView.setText(R.string.home_textView_starting_point_student);
            startingPointButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "HomeFragment starting point button : click (student)");
                    ajaxCallSender.start();
                }
            });
        } else {
            startingPointButton.setText(R.string.home_button_starting_point_teacher);
            startingPointTextView.setText(R.string.home_textView_starting_point_teacher);
            startingPointButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "HomeFragment starting point button : click (teacher)");
                    // Available
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
            if(result.equals("success") && code.equals(Constants.CODE_STUDENT_START)) {
                globalApplication.setWaitingTime(object.getInt(Constants.PUSH_KEY_WAITING) * 1000);
                globalApplication.setFragment("Guide", new GuideFragment());
                globalApplication.setDrawerType(R.array.Waiting_menu_array);
                globalApplication.getSchoolActivity().initDrawer();
                globalApplication.getSchoolActivity().initFragment();
                Log.d(TAG, "Student homefragment initDrawer()");
            }
        } catch (JSONException e) { e.printStackTrace(); }
    }

    @Override
    public void handlePush(JSONObject object) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
