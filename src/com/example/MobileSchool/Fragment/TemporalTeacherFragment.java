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
public class TemporalTeacherFragment extends Fragment implements BaseMethod, RadioGroup.OnCheckedChangeListener {

    private String TAG = Constants.TAG;
    private String title = "Temporal";

    private GlobalApplication globalApplication;
    private AccountManager accountManager;
    private PushSender pushSender;
    private AjaxCallSender ajaxCallSender;

    private View rootView;
    private LinearLayout rootLinearLayout;
    private LinearLayout topLinearLayout;
    private Button confirmButton;

    private int itemIndex;
    private View currentChoiceView = null;
    private DialogueItem[] currentItems;
    private DialogueItem chosenItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "TemporalTeacherFragment : onCreateView");
        rootView = inflater.inflate(R.layout.disp_temporal, container, false);
        getActivity().setTitle(title);

        globalApplication = (GlobalApplication) getActivity().getApplication();
        accountManager = globalApplication.getAccountManager();
        pushSender = new PushSender(getActivity().getApplicationContext());
        ajaxCallSender = new AjaxCallSender(getActivity().getApplicationContext(), this);

        currentItems = globalApplication.getEntryItems();
        _initUI();
        return rootView;
    }

    private void _initUI() {
        rootLinearLayout = (LinearLayout) rootView.findViewById(R.id.temporal_layout_root);
        currentChoiceView = _getTemporalItem(rootLinearLayout);
        rootLinearLayout.addView(currentChoiceView);

        confirmButton = (Button) rootView.findViewById(R.id.temporal_button_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "TemporalTeacherFragment Confirm : " + itemIndex);
                chosenItem = currentItems[itemIndex];
                ajaxCallSender.select(chosenItem.getType(), chosenItem.getId());
            }
        });
    }

    private void _changeItem() {
        Log.d(TAG, "_changeItem : " + currentItems[0].getType() + "\t" + currentItems[0].getBody() + "\t" + currentItems[0].getId());
        topLinearLayout = (LinearLayout) rootView.findViewById(R.id.temporal_layout_top);
        topLinearLayout.addView(_getDialogueItem(topLinearLayout, chosenItem.getType(), chosenItem.getBody()));

        rootLinearLayout = (LinearLayout) rootView.findViewById(R.id.temporal_layout_root);
        rootLinearLayout.removeAllViewsInLayout();
        currentChoiceView = _getTemporalItem(rootLinearLayout);
        rootLinearLayout.addView(currentChoiceView);
    }


    private View _getTemporalItem(LinearLayout rootLinearLayout) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_temporal, rootLinearLayout, false);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.temporal_radioGroup_choices);
        radioGroup.setOnCheckedChangeListener(this);

        RadioButton radioButton1 = (RadioButton) view.findViewById(R.id.temporal_radioGroup_first_choice);
        RadioButton radioButton2 = (RadioButton) view.findViewById(R.id.temporal_radioGroup_second_choice);
        radioButton1.setText(currentItems[0].getBody());
        radioButton2.setText(currentItems[1].getBody());

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
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId == R.id.temporal_radioGroup_first_choice)
            itemIndex = Constants.DIALOGUE_CHOICE_0;
        else if(checkedId == R.id.temporal_radioGroup_second_choice)
            itemIndex = Constants.DIALOGUE_CHOICE_1;
        else
            itemIndex = Constants.DIALOGUE_CHOICE_2;
    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {
        Log.d(TAG, "TemporalTeacherFragment : handleAjaxCallBack Object => " + object);
        try {
            if(object.getString("type").equals("answer")) {
                JSONArray answer_list = object.getJSONArray("answer_list");
                DialogueItem[] answerItems = new DialogueItem[answer_list.length()];
                for(int index = 0; index < answer_list.length(); index++) {
                    JSONObject entryObject = answer_list.getJSONObject(index);
                    DialogueItem dialogueItem = new DialogueItem(entryObject.getString("type"), entryObject.getString("body"), entryObject.getString("id"));
                    answerItems[index] = dialogueItem;
                }
                currentItems = answerItems;
            } else {
                JSONArray question_list = object.getJSONArray("question_list");
                DialogueItem[] questionItems = new DialogueItem[question_list.length()];
                for(int index = 0; index < question_list.length(); index++) {
                    JSONObject entryObject = question_list.getJSONObject(index);
                    DialogueItem dialogueItem = new DialogueItem(entryObject.getString("type"), entryObject.getString("body"), entryObject.getString("id"));
                    questionItems[index] = dialogueItem;
                }
                currentItems = questionItems;
            }
            _changeItem();
        } catch (JSONException e) { e.printStackTrace(); }
    }

    @Override
    public void handlePush(JSONObject object) {
        Log.d(TAG, "TemporalTeacherFragment handlePush : " + object);
        try {
            String code = object.getString("code");
            if(code.equals("PQA")) {

            }
        } catch (JSONException e) { e.printStackTrace(); }
    }
}
