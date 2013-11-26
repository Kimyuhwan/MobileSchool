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
import com.example.MobileSchoolDev.Communication.SocketCommunication;
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
    private TextView msgTextView;


    private View currentChoiceView = null;

    private DialogueItem t_sentence;
    private DialogueItem[] s_sentences;
    private int r_sentecne_index = -1;
    private DialogueItem[] r_sentences;
    private int s_sentence_index = -1;

    private boolean isConfirmClicked = false;
    private Typeface font;

    private SocketCommunication socketCommunication;

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

        r_sentences = globalApplication.getEntryItems();
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
        dialogue_teacher_answer_root_layout = (LinearLayout) rootView.findViewById(R.id.dialogue_teacher_answer_root_layout);
        msgTextView = (TextView) rootView.findViewById(R.id.dialogue_teacher_textView_msg);
        Button confirmButton = (Button) rootView.findViewById(R.id.dialogue_teacher_button_confirm);
        globalApplication.initDialogueList();

        int index = 0;
        for(DialogueItem dialogueItem :  r_sentences) {
            currentChoiceView = _getRoot(dialogue_teacher_question_root_layout, dialogueItem, index);
            dialogue_teacher_question_root_layout.addView(currentChoiceView, index);
            index++;
        }
        _setMsgText("학생이 원하는 수업 주제를 아래에서 클릭하세요.");

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(r_sentecne_index == -1 && s_sentence_index == -1) {
                        msgTextView.setText("아무것도 선택되지 않았습니다.");
                    } else {
                        Log.d(TAG, "DialogueTeacherFragment Confirm : " + s_sentence_index);
                        if(isConfirmClicked)
                            Log.d(TAG, "DialogueTeacherFragment Confirm : 여러번 클릭하였습니다. 처리가 될때까지 기다려주세요.");
                        else if(!socketCommunication._checkReady())
                            Log.d(TAG, "Socket is not ready");
                        else {  // check
                            DialogueItem chosenItem = null;
                            if(s_sentences == null)
                                chosenItem = r_sentences[r_sentecne_index];
                            else
                                chosenItem = s_sentences[s_sentence_index];
                            ajaxCallSender.select(chosenItem.getType(), chosenItem.getId());
                            globalApplication.addDialogue(chosenItem);
                            socketCommunication.sendMsg(chosenItem.getId());
                            isConfirmClicked = true;
                        }
                    }
                }
//            }
        });
    }

    private View _getRoot(LinearLayout linearLayout, DialogueItem dialogueItem, int index) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_question, linearLayout, false);
        TextView questionNumberTextView = (TextView) view.findViewById(R.id.item_question_textView_question_number);
        TextView bodyTextView = (TextView) view.findViewById(R.id.item_question_textView_body);

        view.setId(index);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "DialogueTeacherFragment Item click : " + v.getId());
                r_sentecne_index = v.getId();
                _setMsgText((v.getId() + 1) + "번째 문항이 선택되었습니다.");
            }
        });

        questionNumberTextView.setText(dialogueItem.getType() + (index+1));
        questionNumberTextView.setTypeface(font);
        bodyTextView.setText(dialogueItem.getBody());
        bodyTextView.setTypeface(font);
        return view;
    }

    private void _changeDialogues() {
        // Change questions
        _changeQuestions();
        // Change answers
        _changeAnswers();
        // Set text messages
        _setMsgText("학생에게 질문을 하고 파란색 문항중 원하는 것을 선택하도록 해보세요.");
        isConfirmClicked = false;
    }

    private void _changeQuestions() {
        dialogue_teacher_question_root_layout.removeAllViews();
        currentChoiceView = _getQuestion(dialogue_teacher_question_root_layout, t_sentence);
        dialogue_teacher_question_root_layout.addView(currentChoiceView, 0);
    }

    private View _getQuestion(LinearLayout linearLayout, DialogueItem dialogueItem) {
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

    private void _changeAnswers() {
        dialogue_teacher_answer_root_layout.removeAllViews();
        int index = 0;
        for(DialogueItem dialogueItem : s_sentences) {
            currentChoiceView = _getAnswer(dialogue_teacher_answer_root_layout, dialogueItem, index);
            dialogue_teacher_answer_root_layout.addView(currentChoiceView, index);
            index++;
        }
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
                s_sentence_index = v.getId();
                _setMsgText((v.getId() + 1) + "번째 문항이 선택되었습니다.");
            }
        });

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
    public void onStart() {
        // Socket
        socketCommunication = new SocketCommunication(getActivity(), this);
        socketCommunication.socketInit();
        super.onStart();
    }

    @Override
    public void onStop() {
        socketCommunication.socketFinish();
        super.onStop();
    }

    @Override
    public void handleSocketMessage(String message) {
        Log.d(TAG, "DialogueTeacherFragment message : " + message);
    }

    @Override
    public void handleAjaxCallBack(JSONObject object) {
        Log.d(TAG, "DialogueTeacherFragment : handleAjaxCallBack Object => " + object);
        try {
            if(object.getString("code").equals(Constants.CODE_CHILD_SENTENCE)) {
                JSONArray student_sentence_list = object.getJSONArray("s_sentence_list");
                if(student_sentence_list.length() == 0) {
                    socketCommunication.socketFinish();
                    globalApplication.setFragment("Script", new ScriptFragment());
                    globalApplication.setDrawerType(R.array.Waiting_menu_array);
                    globalApplication.getSchoolActivity().initDrawer();
                    globalApplication.getSchoolActivity().initFragment();
                } else {
                    //Student sentences
                    s_sentences = new DialogueItem[student_sentence_list.length()];
                    for(int index = 0; index < student_sentence_list.length(); index++) {
                        JSONObject entryObject = student_sentence_list.getJSONObject(index);
                        DialogueItem dialogueItem = new DialogueItem(entryObject.getString("type"), entryObject.getString("context"), entryObject.getString("id"), entryObject.getString("successor"));
                        s_sentences[index] = dialogueItem;
                    }

                    //Teacher sentences
                    JSONObject teacher_sentence = object.getJSONObject("t_sentence");
                    t_sentence = new DialogueItem(teacher_sentence.getString("type"), teacher_sentence.getString("context"), teacher_sentence.getString("id"), teacher_sentence.getString("successor"));
                    _changeDialogues();
                    globalApplication.addDialogue(t_sentence);
                    s_sentence_index = -1;
                    r_sentecne_index = -1;
                }
            }
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
