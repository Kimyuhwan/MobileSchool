package com.example.MobileSchoolDev.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.example.MobileSchoolDev.BaseMethod;
import com.example.MobileSchoolDev.Communication.AjaxCallSender;
import com.example.MobileSchoolDev.Communication.PushSender;
import com.example.MobileSchoolDev.Utils.GlobalApplication;
import com.example.MobileSchoolDev.Utils.AccountManager;
import com.example.MobileSchoolDev.R;
import com.example.MobileSchoolDev.Utils.Constants;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 13.
 * Time: 오후 7:19
 */

public class ContactFragment extends Fragment implements BaseMethod {

    private String TAG = Constants.TAG;
    private String title = "Contact";

    private GlobalApplication globalApplication;
    private AccountManager accountManager;
    private PushSender pushSender;
    private AjaxCallSender ajaxCallSender;

    public ContactFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.disp_contact, container, false);
        getActivity().setTitle(title);

        globalApplication = (GlobalApplication) getActivity().getApplication();
        accountManager = globalApplication.getAccountManager();
        pushSender = new PushSender(getActivity().getApplicationContext());
        ajaxCallSender = new AjaxCallSender(getActivity().getApplicationContext(), this);

        _initFragment();
        _initFont(rootView);

        return rootView;
    }

    private void _initFont(View rootView) {
        ViewGroup container = (LinearLayout) rootView.findViewById(R.id.contact_layout_root);
        globalApplication.setAppFont(container);
    }

    private void _initFragment() {
        globalApplication.setFragment("Contact", this);
    }

    @Override
    public void handleSocketMessage(String message) {
    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void handlePush(JSONObject object) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
