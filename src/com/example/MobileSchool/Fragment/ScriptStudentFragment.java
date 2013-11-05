package com.example.MobileSchool.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.MobileSchool.BaseMethod;
import com.example.MobileSchool.Communication.AjaxCallSender;
import com.example.MobileSchool.Communication.PushSender;
import com.example.MobileSchool.Model.DialogueItem;
import com.example.MobileSchool.R;
import com.example.MobileSchool.Utils.AccountManager;
import com.example.MobileSchool.Utils.Constants;
import com.example.MobileSchool.Utils.GlobalApplication;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * User: yuhwan
 * Date: 13. 10. 31
 * Time: 오후 2:29
 */
public class ScriptStudentFragment extends Fragment implements BaseMethod {

    private String TAG = Constants.TAG;
    private String title = "Temporal";

    private GlobalApplication globalApplication;
    private AccountManager accountManager;
    private PushSender pushSender;
    private AjaxCallSender ajaxCallSender;

    private View rootView;
    private LinearLayout rootLinearLayout;
    private LinearLayout topLinearLayout;

    private View currentChoiceView = null;
    private DialogueItem[] answerItems;
    private DialogueItem questionItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "ScriptStudentFragment : onCreateView");
        rootView = inflater.inflate(R.layout.disp_script_student, container, false);
        getActivity().setTitle(title);

        globalApplication = (GlobalApplication) getActivity().getApplication();
        accountManager = globalApplication.getAccountManager();
        pushSender = new PushSender(getActivity().getApplicationContext());
        ajaxCallSender = new AjaxCallSender(getActivity().getApplicationContext(), this);

        _initUIForStudent();
        return rootView;
    }

    private void _initUIForStudent() {
    }

    private void _changeItemPQA() {
        topLinearLayout = (LinearLayout) rootView.findViewById(R.id.temporal_layout_top);
        topLinearLayout.addView(_getDialogueItem(topLinearLayout, questionItem.getType(), questionItem.getBody()));

        rootLinearLayout = (LinearLayout) rootView.findViewById(R.id.temporal_layout_root);
        rootLinearLayout.removeAllViewsInLayout();
        currentChoiceView = _getTemporalItem(rootLinearLayout);
        rootLinearLayout.addView(currentChoiceView);
    }

    private void _changeItemPA() {
        topLinearLayout = (LinearLayout) rootView.findViewById(R.id.temporal_layout_top);
        topLinearLayout.addView(_getDialogueItem(topLinearLayout, questionItem.getType(), questionItem.getBody()));

        rootLinearLayout = (LinearLayout) rootView.findViewById(R.id.temporal_layout_root);
        rootLinearLayout.removeAllViewsInLayout();
    }


    private View _getTemporalItem(LinearLayout rootLinearLayout) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_temporal_student, rootLinearLayout, false);

        TextView textView1 = (TextView) view.findViewById(R.id.temporal_student_textView_first_choice);
        TextView textView2 = (TextView) view.findViewById(R.id.temporal_student_textView_second_choice);
        textView1.setText(answerItems[0].getBody());
        textView2.setText(answerItems[1].getBody());

        return view;
    }

    private View _getDialogueItem(LinearLayout rootLinearLayout, String type, String script) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_script, rootLinearLayout, false);
        TextView textView = (TextView) view.findViewById(R.id.script_item_textView);
        ImageView imageView = (ImageView) view.findViewById(R.id.script_item_imageView);

        textView.setText(script);
        if(type.equals("answer")) {
            imageView.setImageResource(R.drawable.profile_icon_blue);
            textView.setBackgroundResource(R.drawable.ninepatch_blue);
        }
        else if(type.equals("question")){
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
        Log.d(TAG, "ScriptStudentFragment handlePush : " + object);
        try {
            String code = object.getString("code");
            if(code.equals("PQA")) {
                JSONObject msg = object.getJSONObject("msg");
                JSONObject questionObject = msg.getJSONObject("question");
                questionItem = new DialogueItem(questionObject.getString("type"), questionObject.getString("body"), questionObject.getString("id"));
                JSONArray answer_list = msg.getJSONArray("answer_list");
                DialogueItem[] items = new DialogueItem[answer_list.length()];
                for(int index = 0; index < answer_list.length(); index++) {
                    JSONObject entryObject = answer_list.getJSONObject(index);
                    DialogueItem dialogueItem = new DialogueItem(entryObject.getString("type"), entryObject.getString("body"), entryObject.getString("id"));
                    items[index] = dialogueItem;
                }
                answerItems = items;
                _changeItemPQA();
            } else if(code.equals("PA")) {
                JSONObject msg = object.getJSONObject("msg");
                JSONObject answer = msg.getJSONObject("answer");
                questionItem = new DialogueItem(answer.getString("type"), answer.getString("body"), answer.getString("id"));
                _changeItemPA();
            }
        } catch (JSONException e) { e.printStackTrace(); }
    }
}
