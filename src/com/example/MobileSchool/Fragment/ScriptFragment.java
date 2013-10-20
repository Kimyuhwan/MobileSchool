package com.example.MobileSchool.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Date: 13. 10. 17
 * Time: 오후 9:01
 */
public class ScriptFragment extends Fragment implements BaseMethod {
    private String TAG = Constants.TAG;
    private String title = "Script";

    private GlobalApplication globalApplication;
    private AccountManager accountManager;
    private PushSender pushSender;
    private AjaxCallSender ajaxCallSender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.script_fragment, container, false);
        getActivity().setTitle(title);
        Log.d(TAG, "ScriptFragment : onCreate");

        globalApplication = (GlobalApplication) getActivity().getApplication();
        accountManager = new AccountManager(getActivity().getApplicationContext());
        pushSender = new PushSender(getActivity().getApplicationContext());
        ajaxCallSender = new AjaxCallSender(getActivity().getApplicationContext(), this);
        _initFragment();

        return rootView;
    }

    private void _initFragment() {
        globalApplication.setFragment("Home", new HomeFragment());
    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
