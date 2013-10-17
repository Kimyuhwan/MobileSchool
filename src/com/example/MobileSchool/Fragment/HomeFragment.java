package com.example.MobileSchool.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.example.MobileSchool.SchoolActivity;
import com.example.MobileSchool.Communication.AjaxCallSender;
import com.example.MobileSchool.Communication.PushSender;
import com.example.MobileSchool.Utils.GlobalApplication;
import com.example.MobileSchool.Utils.AccountManager;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 13.
 * Time: 오후 7:19
 */

public class HomeFragment extends Fragment implements FragmentMethod{

    private String TAG = Constants.TAG;

    private GlobalApplication globalApplication;
    private AccountManager accountManager;
    private PushSender pushSender;
    private AjaxCallSender ajaxCallSender;

    private Switch rightNowSwitch;
    private Button sendNotificationButton;
    private Button newClassButton;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "HomeFragment : onCreateView");
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        getActivity().setTitle(Constants.FRAGMENT_TITLE_HOME);

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
        rightNowSwitch = (Switch) rootView.findViewById(R.id.btn_rightNow);
        rightNowSwitch.setChecked(false);
        sendNotificationButton = (Button) rootView.findViewById(R.id.btn_sendNotification);
        newClassButton = (Button) rootView.findViewById(R.id.btn_newClass);

        // Set Listener
        rightNowSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) Log.d(TAG, "Right Now! : On");
                else Log.d(TAG, "Right Now! : Off");
            }
        });

        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Send Notification");
                // Temporal Function
                // Now Because of send notification test, student sends push to teacher directly, but later student will call server with ajax and server will send notification to teacher
                String myId = accountManager.getUserId();
                String teacherId = "tkkk";
                String message = "2013.10.14 send Notification Test";
                pushSender.pushToDevice(myId, teacherId, message);
            }
        });

        newClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Start new Class");
                ajaxCallSender.start();
            }
        });
    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {
        try {
            Log.d(TAG, "HomeFragment : handleAjaxCallBack Object => " + object);
            // For Start New Class
            String result = object.getString(Constants.PUSH_KEY_RESULT);
            String code = object.getString(Constants.PUSH_KEY_CODE);
            if(result.equals("success") && code.equals(Constants.PUSH_CODE_STUDENT_START)) {
                globalApplication.setFragment("Guide",new GuideFragment());
                globalApplication.getSchoolActivity().initFragment();
            }
        } catch (JSONException e) { e.printStackTrace(); }
    }

}
