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
public class DialogueStudentFragment extends Fragment implements BaseMethod {

    private String TAG = Constants.TAG;
    private String title = "Dialogue";

    private GlobalApplication globalApplication;
    private AccountManager accountManager;
    private PushSender pushSender;
    private AjaxCallSender ajaxCallSender;

    private View rootView;
    private LinearLayout dialogue_student_question_root_layout;
    private LinearLayout dialogue_student_answer_root_layout;

    private TextView msgTextView;

    private DialogueItem[] answerItems;
    private Typeface font;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "DialogueTeacherFragment : onCreateView");
        rootView = inflater.inflate(R.layout.disp_dialogue_student, container, false);
        getActivity().setTitle(title);

        globalApplication = (GlobalApplication) getActivity().getApplication();
        accountManager = globalApplication.getAccountManager();
        pushSender = new PushSender(getActivity().getApplicationContext());
        ajaxCallSender = new AjaxCallSender(getActivity().getApplicationContext(), this);
        font = Typeface.createFromAsset(getActivity().getAssets(), "Applemint.ttf");

        _initUI();
        _initFont(rootView);
        return rootView;
    }

    private void _initFont(View rootView) {
        ViewGroup container = (LinearLayout) rootView.findViewById(R.id.dialogue_student_layout_root);
        globalApplication.setAppFont(container);
    }

    private void _initUI() {
        msgTextView = (TextView) rootView.findViewById(R.id.dialogue_student_textView_msg);
        msgTextView.setText("새로운 질문을 선택중입니다.");
    }

    private void _addQuestion(DialogueItem dialogueItem) {
        dialogue_student_question_root_layout = (LinearLayout) rootView.findViewById(R.id.dialogue_student_question_root_layout);

        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_question, dialogue_student_question_root_layout, false);
        TextView questionNumberTextView = (TextView) view.findViewById(R.id.item_question_textView_question_number);
        TextView bodyTextView = (TextView) view.findViewById(R.id.item_question_textView_body);

        questionNumberTextView.setText("Q");
        questionNumberTextView.setTypeface(font);
        bodyTextView.setText(dialogueItem.getBody());
        bodyTextView.setTypeface(font);
        dialogue_student_question_root_layout.removeAllViews();
        dialogue_student_question_root_layout.addView(view);
        globalApplication.addDialogue(new DialogueItem(dialogueItem.getType(),dialogueItem.getBody(),dialogueItem.getId()));
    }

    private void _addAnswer() {
        dialogue_student_answer_root_layout = (LinearLayout) rootView.findViewById(R.id.dialogue_student_answer_root_layout);
        dialogue_student_answer_root_layout.removeAllViews();
        int index = 0;
        for(DialogueItem dialogueItem : answerItems) {
            dialogue_student_answer_root_layout.addView(_getAnswer(dialogueItem, index));
            index++;
        }
    }

    private View _getAnswer(DialogueItem dialogueItem, int index) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_answer, dialogue_student_answer_root_layout, false);
        TextView answerNumberTextView = (TextView) view.findViewById(R.id.item_answer_textView_answer_number);
        TextView bodyTextView = (TextView) view.findViewById(R.id.item_answer_textView_body);

        answerNumberTextView.setText("A" + (index + 1));
        answerNumberTextView.setTypeface(font);
        bodyTextView.setText(dialogueItem.getBody());
        bodyTextView.setTypeface(font);
        return view;
    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {
        Log.d(TAG, "DialogueTeacherFragment : handleAjaxCallBack Object => " + object);
    }

    @Override
    public void handlePush(JSONObject object) {
        try {
            String code = object.getString("code");
            if(code.equals(Constants.CODE_PUSH_QUESTION_ANSWER)) {
                JSONObject msg = object.getJSONObject("msg");
                JSONArray answer_list = msg.getJSONArray("answer_list");
                if(answer_list.length() == 0) {
                    JSONObject question = msg.getJSONObject("question");
                    globalApplication.addDialogue(new DialogueItem(question.getString("type"),question.getString("context"),question.getString("id")));
                    globalApplication.setFragment("Script", new ScriptFragment());
                    globalApplication.setDrawerType(R.array.Waiting_menu_array);
                    globalApplication.getSchoolActivity().initDrawer();
                    globalApplication.getSchoolActivity().initFragment();
                } else {
                    JSONObject question = msg.getJSONObject("question");
                    DialogueItem questionItem = new DialogueItem(question.getString("type"), question.getString("context"), question.getString("id"));
                    _addQuestion(questionItem);

                    answerItems = new DialogueItem[answer_list.length()];
                    for(int index = 0; index < answer_list.length(); index++) {
                        JSONObject entryObject = answer_list.getJSONObject(index);
                        DialogueItem dialogueItem = new DialogueItem(entryObject.getString("type"), entryObject.getString("context"), entryObject.getString("id"));
                        answerItems[index] = dialogueItem;
                    }
                    _addAnswer();
                    msgTextView.setText("");
                }


            } else if(code.equals("PA")) {
                JSONObject msg = object.getJSONObject("msg");
                JSONObject answer = msg.getJSONObject("answer");
                globalApplication.addDialogue(new DialogueItem(answer.getString("type"),answer.getString("context"), answer.getString("id")));
            }
        } catch (JSONException e) { e.printStackTrace(); }
    }
}
