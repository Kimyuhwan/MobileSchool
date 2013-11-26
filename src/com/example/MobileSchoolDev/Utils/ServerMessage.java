package com.example.MobileSchoolDev.Utils;

/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 11.
 * Time: 오후 4:46
 */
public class ServerMessage {

    public static String SERVER_IP = "143.248.91.216";
    public static String SERVER_ADDRESS = "http://" + SERVER_IP + "/alpha/";
    public static Integer SOCKET_PORT = 8001;
    public static String URL_SCREEN_MONITORING = SERVER_ADDRESS + "monitoring/screen/"; // It will be changed to monitoring/device/
    public static String URL_APP_MONITORING = SERVER_ADDRESS + "monitoring/app/";

    // New Message
    public static String URL_START = SERVER_ADDRESS + "start/";
    public static String URL_CONFIRM = SERVER_ADDRESS + "confirm/";
    public static String URL_ANSWER = SERVER_ADDRESS + "answer/";
    public static String URL_DIALOGUE_TEACHER = SERVER_ADDRESS + "get/dialogue/for/teacher/";
    public static String URL_DIALOGUE_STUDENT = SERVER_ADDRESS + "get/dialogue/for/student/";
    public static String URL_SELECT_SELECT = SERVER_ADDRESS + "select/id/";
    public static String URL_READY = SERVER_ADDRESS + "ready/";

    // Old Message
    public static String URL_LOGIN = SERVER_ADDRESS + "account/login/";
    public static String URL_TEACHER_REGISTER = SERVER_ADDRESS + "teacher/register/";
    public static String URL_STUDENT_REGISTER = SERVER_ADDRESS + "student/register/";

}
