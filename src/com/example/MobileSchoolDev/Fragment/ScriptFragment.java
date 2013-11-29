package com.example.MobileSchoolDev.Fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.example.MobileSchoolDev.BaseMethod;
import com.example.MobileSchoolDev.Communication.AjaxCallSender;
import com.example.MobileSchoolDev.Communication.PushSender;
import com.example.MobileSchoolDev.Model.DialogueItem;
import com.example.MobileSchoolDev.R;
import com.example.MobileSchoolDev.Utils.AccountManager;
import com.example.MobileSchoolDev.Utils.Constants;
import com.example.MobileSchoolDev.Utils.ContentManager;
import com.example.MobileSchoolDev.Utils.GlobalApplication;
import org.json.JSONObject;

import java.util.List;

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

    private LinearLayout rootLinearLayout;

    private List<DialogueItem> dialogueList;
    private Typeface font;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.disp_script, container, false);
        getActivity().setTitle(title);
        Log.d(TAG, "ScriptFragment : onCreate");

        globalApplication = (GlobalApplication) getActivity().getApplication();
        accountManager = globalApplication.getAccountManager();
        contentManager = globalApplication.getContentManager();
        pushSender = new PushSender(getActivity().getApplicationContext());
        ajaxCallSender = new AjaxCallSender(getActivity().getApplicationContext(), this);
        dialogueList = globalApplication.getDialogueList();
        font = Typeface.createFromAsset(getActivity().getAssets(), "Applemint.ttf");

        _initFragment();
        _initUI(rootView);
        _initFont(rootView);
        return rootView;
    }

    private void _initFragment() {
        globalApplication.setFragment("Script", this);
    }

    private void _initFont(View rootView) {
        ViewGroup container = (ScrollView) rootView.findViewById(R.id.script_layout_root);
        globalApplication.setAppFont(container);
    }

    private void _initUI(View rootView) {
        rootLinearLayout = (LinearLayout) rootView.findViewById(R.id.script_layout_dialogue_container);
        for(DialogueItem dialogueItem : dialogueList) {
            if(!dialogueItem.getType().equals("R"))
                rootLinearLayout.addView(_getScriptItem(rootLinearLayout, dialogueItem.getType(), dialogueItem.getBody()));
        }
    }

    private View _getScriptItem(LinearLayout rootLinearLayout, String type, String script) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_script, rootLinearLayout, false);
        TextView textView = (TextView) view.findViewById(R.id.script_item_textView);
        ImageView imageView = (ImageView) view.findViewById(R.id.script_item_imageView);

        if(type.equals("S")) {
            textView.setText(script);
            imageView.setImageResource(R.drawable.profile_icon_blue);
            textView.setBackgroundResource(R.drawable.ninepatch_blue);
            textView.setTypeface(font);
        }
        else if(type.equals("T")){
            textView.setText(script);
            imageView.setImageResource(R.drawable.profile_icon_pink);
            textView.setBackgroundResource(R.drawable.ninepatch_pink);
            textView.setTypeface(font);
        }

        return view;
    }

    @Override
    public void handleSocketMessage(String message) {
    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {

    }

    @Override
    public void handlePush(JSONObject object) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
