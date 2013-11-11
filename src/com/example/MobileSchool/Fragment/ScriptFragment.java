package com.example.MobileSchool.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.MobileSchool.BaseMethod;
import com.example.MobileSchool.Communication.AjaxCallSender;
import com.example.MobileSchool.Communication.PushSender;
import com.example.MobileSchool.Model.Content;
import com.example.MobileSchool.Model.DialogueItem;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.AccountManager;
import com.example.MobileSchool.Utils.Constants;
import com.example.MobileSchool.Utils.ContentManager;
import com.example.MobileSchool.Utils.GlobalApplication;
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

        _initUI(rootView);

        return rootView;
    }

    private void _initUI(View rootView) {
        rootLinearLayout = (LinearLayout) rootView.findViewById(R.id.script_layout_root);
        for(DialogueItem dialogueItem : dialogueList) {
            rootLinearLayout.addView(_getScriptItem(rootLinearLayout, dialogueItem.getType(), dialogueItem.getBody()));
        }
    }

    private View _getScriptItem(LinearLayout rootLinearLayout, String type, String script) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_script, rootLinearLayout, false);
        TextView textView = (TextView) view.findViewById(R.id.script_item_textView);
        ImageView imageView = (ImageView) view.findViewById(R.id.script_item_imageView);

        textView.setText(script);
        if(type.equals("A")) {
            imageView.setImageResource(R.drawable.profile_icon_blue);
            textView.setBackgroundResource(R.drawable.ninepatch_blue);
        }
        else {
            imageView.setImageResource(R.drawable.profile_icon_pink);
            textView.setBackgroundResource(R.drawable.ninepatch_pink);
        }

        return view;
    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {

    }

    @Override
    public void handlePush(JSONObject object) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
