package com.example.MobileSchoolDev.Fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.MobileSchoolDev.BaseMethod;
import com.example.MobileSchoolDev.Communication.AjaxCallSender;
import com.example.MobileSchoolDev.Communication.PushSender;
import com.example.MobileSchoolDev.Model.DialogueItem;
import com.example.MobileSchoolDev.R;
import com.example.MobileSchoolDev.Utils.AccountManager;
import com.example.MobileSchoolDev.Utils.Constants;
import com.example.MobileSchoolDev.Utils.GlobalApplication;
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

    private DialogueItem[] rootItems;
    private DialogueItem t_sentence;
    private DialogueItem[] s_sentences;

    private Typeface font;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "DialogueStudentFragment : onCreateView");
        rootView = inflater.inflate(R.layout.disp_dialogue_student, container, false);
        getActivity().setTitle(title);

        globalApplication = (GlobalApplication) getActivity().getApplication();
        accountManager = globalApplication.getAccountManager();
        pushSender = new PushSender(getActivity().getApplicationContext());
        ajaxCallSender = new AjaxCallSender(getActivity().getApplicationContext(), this);
        font = Typeface.createFromAsset(getActivity().getAssets(), "Applemint.ttf");

        _initUI();
        _initFont(rootView);

        // Answer
        ajaxCallSender.answer();
        return rootView;
    }

    private void _initFont(View rootView) {
        ViewGroup container = (LinearLayout) rootView.findViewById(R.id.dialogue_student_layout_root);
        globalApplication.setAppFont(container);
    }

    private void _initUI() {
        msgTextView = (TextView) rootView.findViewById(R.id.dialogue_student_textView_msg);
        dialogue_student_question_root_layout = (LinearLayout) rootView.findViewById(R.id.dialogue_student_question_root_layout);
        dialogue_student_answer_root_layout = (LinearLayout) rootView.findViewById(R.id.dialogue_student_answer_root_layout);
    }

    private void _addRoot() {
        int index = 0;
        for(DialogueItem dialogueItem : rootItems) {
            View currentChoiceView = _getRoot(dialogue_student_question_root_layout, dialogueItem, index);
            dialogue_student_question_root_layout.addView(currentChoiceView, index);
            index++;
        }
        _setMsgText("오늘 수업 주제를 선택해보세요.");
        globalApplication.initDialogueList();
    }

    private View _getRoot(LinearLayout linearLayout, DialogueItem dialogueItem, int index) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_question, linearLayout, false);
        TextView questionNumberTextView = (TextView) view.findViewById(R.id.item_question_textView_question_number);
        TextView bodyTextView = (TextView) view.findViewById(R.id.item_question_textView_body);

        questionNumberTextView.setText(dialogueItem.getType() + (index + 1));
        questionNumberTextView.setTypeface(font);
        bodyTextView.setText(dialogueItem.getBody());
        bodyTextView.setTypeface(font);
        return view;
    }

    private void _changeDialogues() {
        // Change Teacher sentence
        _changeTeacherSentence();
        // Change Student sentence
        _changeStudentSentence();
        // Set Text Message
        _setMsgText("질문을 보고 파란색 항목중 원하는 것을 골라 선생님께 말해보세요.");
    }

    private void _changeTeacherSentence() {
        dialogue_student_question_root_layout.removeAllViews();
        dialogue_student_question_root_layout.addView(_getTeacherSentence(dialogue_student_question_root_layout, t_sentence));
    }

    private View _getTeacherSentence(LinearLayout linearLayout, DialogueItem dialogueItem) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_question, linearLayout, false);
        TextView questionNumberTextView = (TextView) view.findViewById(R.id.item_question_textView_question_number);
        TextView bodyTextView = (TextView) view.findViewById(R.id.item_question_textView_body);

        questionNumberTextView.setText(dialogueItem.getType());
        questionNumberTextView.setTypeface(font);
        bodyTextView.setText(dialogueItem.getBody());
        bodyTextView.setTypeface(font);
        return view;
    }

    private void _changeStudentSentence() {
        dialogue_student_answer_root_layout.removeAllViews();
        int index = 0;
        for(DialogueItem dialogueItem : s_sentences) {
            dialogue_student_answer_root_layout.addView(_getAnswer(dialogue_student_answer_root_layout, dialogueItem, index));
            index++;
        }
    }

    private View _getAnswer(LinearLayout linearLayout, DialogueItem dialogueItem, int index) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_answer, linearLayout, false);
        TextView answerNumberTextView = (TextView) view.findViewById(R.id.item_answer_textView_answer_number);
        TextView bodyTextView = (TextView) view.findViewById(R.id.item_answer_textView_body);

        answerNumberTextView.setText(dialogueItem.getType() + (index + 1));
        answerNumberTextView.setTypeface(font);
        bodyTextView.setText(dialogueItem.getBody());
        bodyTextView.setTypeface(font);
        return view;
    }

    private void _setMsgText(String msg) {
        msgTextView.setText(msg);
    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {
        Log.d(TAG, "DialogueStudentFragment : handleAjaxCallBack Object => " + object);
        try {
            String code = object.getString("code");
            if(code.equals(Constants.CODE_STUDENT_ANSWER)) {
                JSONArray topics = object.getJSONArray(Constants.PUSH_TYPE_TOPICS);
                rootItems = new DialogueItem[topics.length()];
                for(int index = 0; index < topics.length(); index++) {
                    JSONObject topic = topics.getJSONObject(index);
                    DialogueItem dialogueItem = new DialogueItem(topic.getString("type"), topic.getString("context"), topic.getString("id"), topic.getString("successor"));
                    rootItems[index] = dialogueItem;
                }
                _addRoot();
            }
        } catch (JSONException e) { e.printStackTrace(); }

    }

    @Override
    public void handlePush(JSONObject object) {
        try {
            String code = object.getString("code");
            if(code.equals(Constants.CODE_PUSH_QUESTION_ANSWER)) {
                JSONObject msg = object.getJSONObject("msg");
                JSONArray student_sentence_list = msg.getJSONArray("s_sentence_list");
                JSONObject teacher_sentence = msg.getJSONObject("t_sentence");
                JSONObject selected = msg.getJSONObject("selected");
                if(student_sentence_list.length() == 0) {
                    globalApplication.addDialogue(new DialogueItem(selected.getString("type"),selected.getString("context"),selected.getString("id"),selected.getString("successor")));
                    globalApplication.setFragment("Script", new ScriptFragment());
                    globalApplication.setDrawerType(R.array.Waiting_menu_array);
                    globalApplication.getSchoolActivity().initDrawer();
                    globalApplication.getSchoolActivity().initFragment();
                } else {
                    // Student sentences
                    s_sentences = new DialogueItem[student_sentence_list.length()];
                    for(int index = 0; index < student_sentence_list.length(); index++) {
                        JSONObject s_sentence = student_sentence_list.getJSONObject(index);
                        DialogueItem dialogueItem = new DialogueItem(s_sentence.getString("type"), s_sentence.getString("context"), s_sentence.getString("id"),s_sentence.getString("successor"));
                        s_sentences[index] = dialogueItem;
                    }

                    // Teacher sentences
                    t_sentence = new DialogueItem(teacher_sentence.getString("type"), teacher_sentence.getString("context"), teacher_sentence.getString("id"),teacher_sentence.getString("successor"));
                    _changeDialogues();

                    globalApplication.addDialogue(new DialogueItem(selected.getString("type"),selected.getString("context"),selected.getString("id"),selected.getString("successor")));
                    globalApplication.addDialogue(t_sentence);
                }
            }
        } catch (JSONException e) { e.printStackTrace(); }
    }
}
