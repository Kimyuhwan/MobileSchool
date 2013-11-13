package com.example.MobileSchool.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
public class DialogueTeacherFragment extends Fragment implements BaseMethod {

    private String TAG = Constants.TAG;
    private String title = "Dialogue";

    private GlobalApplication globalApplication;
    private AccountManager accountManager;
    private PushSender pushSender;
    private AjaxCallSender ajaxCallSender;

    private View rootView;
    private LinearLayout dialogue_teacher_question_root_layout;
    private LinearLayout dialogue_teacher_answer_root_layout;
    private LinearLayout topLinearLayout;
    private Button confirmButton;
    private TextView msgTextView;

    private int itemIndex = -1;
    private View currentChoiceView = null;
    private DialogueItem[] currentItems = null;
    private DialogueItem chosenItem;

    private boolean isConfirmClicked = false;
    private Typeface font;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "DialogueTeacherFragment : onCreateView");
        rootView = inflater.inflate(R.layout.disp_dialogue_teacher, container, false);
        getActivity().setTitle(title);

        globalApplication = (GlobalApplication) getActivity().getApplication();
        accountManager = globalApplication.getAccountManager();
        pushSender = new PushSender(getActivity().getApplicationContext());
        ajaxCallSender = new AjaxCallSender(getActivity().getApplicationContext(), this);
        font = Typeface.createFromAsset(getActivity().getAssets(), "Applemint.ttf");

        currentItems = globalApplication.getEntryItems();
        _initUI();
        _initFont(rootView);
        return rootView;
    }

    private void _initFont(View rootView) {
        ViewGroup container = (LinearLayout) rootView.findViewById(R.id.dialogue_teacher_layout_root);
        globalApplication.setAppFont(container);
    }

    private void _initUI() {
        dialogue_teacher_question_root_layout = (LinearLayout) rootView.findViewById(R.id.dialogue_teacher_question_root_layout);
        msgTextView = (TextView) rootView.findViewById(R.id.dialogue_teacher_textView_msg);
        confirmButton = (Button) rootView.findViewById(R.id.dialogue_teacher_button_confirm);

        int index = 0;
        for(DialogueItem dialogueItem :  currentItems) {
            currentChoiceView = _getQuestion(dialogue_teacher_question_root_layout, dialogueItem, index);
            dialogue_teacher_question_root_layout.addView(currentChoiceView, index);
            index++;
        }
        _setMsgText();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(itemIndex == -1) {
                        msgTextView.setText("아무것도 선택되지 않았습니다.");
                    } else {
                        Log.d(TAG, "DialogueTeacherFragment Confirm : " + itemIndex);
                        if(!isConfirmClicked) {  // check
                            chosenItem = currentItems[itemIndex];
                            ajaxCallSender.select(chosenItem.getType(), chosenItem.getId());
                            globalApplication.addDialogue(chosenItem);
                            isConfirmClicked = true;
                        }
                    }
                }
//            }
        });
    }

    private View _getQuestion(LinearLayout linearLayout, DialogueItem dialogueItem, int index) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_question, linearLayout, false);
        TextView questionNumberTextView = (TextView) view.findViewById(R.id.item_question_textView_question_number);
        TextView bodyTextView = (TextView) view.findViewById(R.id.item_question_textView_body);
        if(index != -1) {
            view.setId(index);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "DialogueTeacherFragment Item click : " + v.getId());
                    itemIndex = v.getId();
                    _setMsgText();
                }
            });
            questionNumberTextView.setText("Q" + (index + 1));
        } else {
            questionNumberTextView.setText("Q");
        }
        questionNumberTextView.setTypeface(font);
        bodyTextView.setText(dialogueItem.getBody());
        bodyTextView.setTypeface(font);
        return view;
    }

    private void _confirmQuestion() {
        dialogue_teacher_question_root_layout = (LinearLayout) rootView.findViewById(R.id.dialogue_teacher_question_root_layout);
        dialogue_teacher_question_root_layout.removeAllViews();
        currentChoiceView = _getQuestion(dialogue_teacher_question_root_layout, chosenItem, -1);
        dialogue_teacher_question_root_layout.addView(currentChoiceView, 0);
    }

    private void _addAnswerList() {
        dialogue_teacher_answer_root_layout = (LinearLayout) rootView.findViewById(R.id.dialogue_teacher_answer_root_layout);
        dialogue_teacher_answer_root_layout.removeAllViews();
        int index = 0;
        for(DialogueItem dialogueItem : currentItems) {
            currentChoiceView = _getAnswer(dialogue_teacher_answer_root_layout, dialogueItem, index);
            dialogue_teacher_answer_root_layout.addView(currentChoiceView, index);
            index++;
        }
        _setMsgText();
    }

    private View _getAnswer(LinearLayout linearLayout, DialogueItem dialogueItem, int index) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_answer, linearLayout, false);
        TextView answerNumberTextView = (TextView) view.findViewById(R.id.item_answer_textView_answer_number);
        TextView bodyTextView = (TextView) view.findViewById(R.id.item_answer_textView_body);
        view.setId(index);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "DialogueTeacherFragment Item click : " + v.getId());
                itemIndex = v.getId();
                _setMsgText();
            }
        });
        answerNumberTextView.setText("A" + (index + 1));
        answerNumberTextView.setTypeface(font);
        bodyTextView.setText(dialogueItem.getBody());
        bodyTextView.setTypeface(font);
        return view;
    }

    private void _setMsgText() {
       if(itemIndex == -1)
           msgTextView.setText("원하는 문장을 클릭해 주세요.");
       else
           msgTextView.setText((itemIndex+1) + "번째 문항이 선택되었습니다.");
    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {
        Log.d(TAG, "DialogueTeacherFragment : handleAjaxCallBack Object => " + object);
        try {
            if(object.getString("code").equals(Constants.CODE_ANSWER_LIST)) {
                JSONArray answer_list = object.getJSONArray("answer_list");
                if(answer_list.length() == 0) {
                    globalApplication.setFragment("Script", new ScriptFragment());
                    globalApplication.setDrawerType(R.array.Waiting_menu_array);
                    globalApplication.getSchoolActivity().initDrawer();
                    globalApplication.getSchoolActivity().initFragment();
                } else {
                    DialogueItem[] answerItems = new DialogueItem[answer_list.length()];
                    for(int index = 0; index < answer_list.length(); index++) {
                        JSONObject entryObject = answer_list.getJSONObject(index);
                        DialogueItem dialogueItem = new DialogueItem(entryObject.getString("type"), entryObject.getString("context"), entryObject.getString("id"));
                        answerItems[index] = dialogueItem;
                    }
                    currentItems = answerItems;
                    itemIndex = -1;
                    _setMsgText();
                    _confirmQuestion();
                    _addAnswerList();
                }
            } else {
                //Temporal Functionality
                JSONArray question_list = object.getJSONArray("question_list");
                if(question_list.length() == 0) {
                    globalApplication.setFragment("Script", new ScriptFragment());
                    globalApplication.setDrawerType(R.array.Waiting_menu_array);
                    globalApplication.getSchoolActivity().initDrawer();
                    globalApplication.getSchoolActivity().initFragment();
                } else {
                    //Only One question
                    for(int index = 0; index < question_list.length(); index++) {
                        JSONObject entryObject = question_list.getJSONObject(index);
                        DialogueItem dialogueItem = new DialogueItem(entryObject.getString("type"), entryObject.getString("context"), entryObject.getString("id"));
                        chosenItem = dialogueItem;
                    }
                    ajaxCallSender.select(chosenItem.getType(), chosenItem.getId());
                    globalApplication.addDialogue(chosenItem);
                }
            }
            isConfirmClicked = false;
            itemIndex = -1;
        } catch (JSONException e) { e.printStackTrace(); }
    }

    @Override
    public void handlePush(JSONObject object) {
        Log.d(TAG, "DialogueTeacherFragment handlePush : " + object);
        try {
            String code = object.getString("code");
            if(code.equals("PQA")) {

            }
        } catch (JSONException e) { e.printStackTrace(); }
    }
}
