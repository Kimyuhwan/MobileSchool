package com.example.MobileSchool.Activities.SchoolFragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.example.MobileSchool.Activities.Student.StudentWaitingActivity;
import com.example.MobileSchool.Communication.PushSender;
import com.example.MobileSchool.Manager.AccountManager;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 13.
 * Time: 오후 7:19
 */

public class HomeFragment extends Fragment {

    private String TAG = Constants.TAG;

    private AccountManager accountManager;
    private PushSender pushSender;

    private Switch rightNowSwitch;
    private Button sendNotificationButton;
    private Button newClassButton;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        getActivity().setTitle(Constants.FRAGMENT_TITLE_HOME);
        Log.d(TAG, "HomeFragment : onCreateView");

        accountManager = new AccountManager(getActivity().getApplicationContext());
        pushSender = new PushSender(getActivity().getApplicationContext());
        _initUI(rootView);

        return rootView;
    }

    private void _initUI(View rootView) {
        rightNowSwitch = (Switch) rootView.findViewById(R.id.btn_rightNow);
        sendNotificationButton = (Button) rootView.findViewById(R.id.btn_sendNotification);
        newClassButton = (Button) rootView.findViewById(R.id.btn_newClass);

        rightNowSwitch.setChecked(false);
        rightNowSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                    Log.d(TAG, "Right Now! : On");
                else
                    Log.d(TAG, "Right Now! : Off");
            }
        });

        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Temporal Function
                Log.d(TAG, "Send Notification");
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
                // Start Next Activity
                getActivity().startActivity(new Intent(getActivity(), StudentWaitingActivity.class));
            }
        });


    }

}
