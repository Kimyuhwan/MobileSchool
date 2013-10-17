package com.example.MobileSchool.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * User: yuhwan
 * Date: 13. 10. 16
 * Time: 오후 11:15
 */
public class GuideFragment extends Fragment implements FragmentMethod {
    private String TAG = Constants.TAG;
    private String title = "Guide";

    private GlobalApplication globalApplication;
    private AccountManager accountManager;
    private PushSender pushSender;
    private AjaxCallSender ajaxCallSender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "GuideFragment : onCreateView");
        View rootView = inflater.inflate(R.layout.guide_fragment, container, false);
        getActivity().setTitle(title);

        globalApplication = (GlobalApplication) getActivity().getApplication();
        accountManager = new AccountManager(getActivity().getApplicationContext());
        pushSender = new PushSender(getActivity().getApplicationContext());
        ajaxCallSender = new AjaxCallSender(getActivity().getApplicationContext(), this);
        _initFragment();

        if(accountManager.isStudent())
            _initUIForStudent();
        else
            _initUIForTeacher();

        return rootView;
    }

    private void _initFragment() {
        globalApplication.setFragment("Home", new HomeFragment());
    }

    private void _initUIForStudent() {

    }

    private void _initUIForTeacher() {
        ajaxCallSender.confirm();
    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {
        try {
            Log.d(TAG, "GuideFragment : handleAjaxCallBack Object => " + object);
            String result = object.getString(Constants.PUSH_KEY_RESULT);
            String code = object.getString(Constants.PUSH_KEY_CODE);
            if(result.equals("success") && code.equals(Constants.PUSH_CODE_TEACHER_CONFIRM)) {
                JSONObject studentInfo = object.getJSONObject(Constants.PUSH_KEY_STUDENT_INFO);
                String sid = studentInfo.getString(Constants.PUSH_VALUE_STUDENT_ID);
                globalApplication.setTargetStudentId(sid);
                globalApplication.setFragment("Profile",new ProfileFragment());
                globalApplication.getSchoolActivity().initFragment();
            }
        } catch (JSONException e) { e.printStackTrace(); }
    }

}
