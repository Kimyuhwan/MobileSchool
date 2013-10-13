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
import com.example.MobileSchool.Activities.StudentWaitingActivity;
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

    private Switch rightNowSwitch;
    private Button newClassButton;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        getActivity().setTitle(Constants.FRAGMENT_TITLE_HOME);
        Log.d(TAG, "HomeFragment : onCreateView");

        _initUI(rootView);

        return rootView;
    }

    private void _initUI(View rootView) {
        rightNowSwitch = (Switch) rootView.findViewById(R.id.btn_rightNow);
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

        newClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getActivity().startActivity(new Intent(getActivity(), StudentWaitingActivity.class));
            }
        });


    }

}
