package com.example.MobileSchool.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.MobileSchool.BaseMethod;
import com.example.MobileSchool.Communication.AjaxCallSender;
import com.example.MobileSchool.Communication.PushSender;
import com.example.MobileSchool.Model.Content;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.AccountManager;
import com.example.MobileSchool.Utils.Constants;
import com.example.MobileSchool.Utils.ContentManager;
import com.example.MobileSchool.Utils.GlobalApplication;
import org.json.JSONException;
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
    private ContentManager contentManager;
    private PushSender pushSender;
    private AjaxCallSender ajaxCallSender;

    private Content todayContent;
    private TextView expressionTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.disp_script, container, false);
        getActivity().setTitle(title);
        Log.d(TAG, "ScriptFragment : onCreate");

        globalApplication = (GlobalApplication) getActivity().getApplication();
        accountManager = new AccountManager(getActivity().getApplicationContext());
        contentManager = new ContentManager(getActivity().getApplicationContext());
        pushSender = new PushSender(getActivity().getApplicationContext());
        ajaxCallSender = new AjaxCallSender(getActivity().getApplicationContext(), this);

        todayContent = contentManager.getTodayContent();

        _initUI();

        return rootView;
    }

    private void _initUI() {

        expressionTextView = (TextView) getActivity().findViewById(R.id.script_textView_expression);
        expressionTextView.setText(todayContent.getExpression());

        if(accountManager.isStudent()) {

        } else {

        }
    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {

    }

}
