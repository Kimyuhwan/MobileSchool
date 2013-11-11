package com.example.MobileSchool.Utils;

/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 11.
 * Time: 오후 4:46
 */
public class Constants {
    public static String TAG = "OpenSchool";

    // Parse
    public static String PARSE_APPLICATION_ID = "SfY8SbpgXz11WGUa32OyxJp7P2vqwGbtB1zz73sn";
    public static String PARSE_CLIENT_KEY = "9dJXjReap5vMCnyShPlUb7WU6bNKkg1KnOkpYHPG";

    // Push
    public static Integer PUSH_NOTIFICATION_UNIQUE_ID = 5474;
    public static String PUSH_CUSTOM_INTENT = "com.example.MobileSchool.CUSTOM_INTENT";
    public static String PUSH_CUSTOM_NOTIFICATION_CONFIRM_EVENT = "com.example.MobileSchool.ACTION_CONFIRM";
    public static String PUSH_CUSTOM_NOTIFICATION_CANCEL_EVENT = "com.example.MobileSchool.ACTION_CANCEL";

    public static String PUSH_TYPE_NOTIFICATION = "noti";
    public static String PUSH_TYPE_MESSAGE = "msg";

    public static String PUSH_KEY_RESULT = "result";
    public static String PUSH_KEY_CODE = "code";
    public static String PUSH_KEY_TEACHER_INFO = "teacher_info";
    public static String PUSH_KEY_STUDENT_INFO = "student_info";
    public static String PUSH_KEY_WAITING = "waiting";
    public static String PUSH_KEY_ENTRY_LIST = "entry_list";

    public static String PUSH_VALUE_TEACHER_ID = "tid";
    public static String PUSH_VALUE_STUDENT_ID = "sid";

    public static String CODE_TEACHER_CONFIRM = "TC";
    public static String CODE_STUDENT_START = "SS";
    public static String CODE_PUSH_TEACHER_INFO = "PTI";
    public static String CODE_PUSH_STUDENT_ANSWER = "PSA";
    public static String CODE_PUSH_ROOT_QUESTION = "PRQ";
    public static String CODE_PUSH_TEACHER = "PT";
    public static String CODE_TEACHER_FINISH = "TF";
    public static String CODE_STUDENT_FINISH = "SF";
    public static String CODE_NO_STUDENT = "NS";
    public static String CODE_DAILY_DIALOGUE = "DD";
    public static String CODE_ANSWER_LIST = "AL";
    public static String CODE_QUESTION_LIST = "QL";
    public static String CODE_PUSH_QUESTION_ANSWER = "PQA";


    // SharedPreferences
    public static String SHAREDPREFERENCES_MY_INFO = TAG + "_MY_INFO";
    public static String SHAREDPREFERENCES_USER_ID = TAG + "_USER_ID";
    public static String SHAREDPREFERENCES_USER_SID = TAG + "_USER_SID";
    public static String SHAREDPREFERENCES_QUEUE_ID = TAG + "_QUEUE_ID";
    public static String SHAREDPREFERENCES_PHONE_NUMBER = TAG + "_PHONE_NUMBER";

    // Account
    public static String SHAREDPREFERENCES_EMPTY = "EMPTY";

    // Fragment
    public static String FRAGMENT_TITLE_HOME = "Home";
    public static String FRAGMENT_TITLE_SETTING = "Setting";

    // Old Call Back Response
    public static String RESPONSE_LOGIN_SUCCESS = "success_account_login";
    public static String RESPONSE_LOGIN_FAIL_WRONG_ID = "failed_wrong_id";
    public static String RESPONSE_LOGIN_FAIL_WRONG_PASSWORD = "failed_wrong_password";
    public static String RESPONSE_REGISTRATION_TEACHER_SUCCESS = "success_teacher_register";
    public static String RESPONSE_REGISTRATION_STUDENT_SUCCESS = "success_student_register";
    public static String RESPONSE_REGISTRATION_FAIL = "failed_id_already_exists";

    // Registration
    public static String REGISTRATION_GENDER_MALE = "male";
    public static String REGISTRATION_GENDER_FEMALE = "female";
    public static String REGISTRATION_TYPE_STUDENT = "student";
    public static String REGISTRATION_TYPE_TEACHER = "teacher";

    // Interactive Dialogue
    public static int DIALOGUE_CHOICE_0 = 0;
    public static int DIALOGUE_CHOICE_1 = 1;
    public static int DIALOGUE_CHOICE_2 = 2;

    // Contents
    public static String CONTENTS = "contents";

    // Recorder
    public static String SD_CARD_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String RECODER_PATH = SD_CARD_PATH + "/talentDonation/";


}
