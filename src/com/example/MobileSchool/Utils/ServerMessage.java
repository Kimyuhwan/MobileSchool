package com.example.MobileSchool.Utils;

/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 11.
 * Time: 오후 4:46
 */
public class ServerMessage {

    public static String SERVER_ADDRESS = "http://143.248.91.216/dev/";
    public static String URL_SCREEN_MONITORING = SERVER_ADDRESS + "monitoring/screen/"; // It will be changed to monitoring/device/
    public static String URL_APP_MONITORING = SERVER_ADDRESS + "monitoring/app/";

    public static String URL_MATCH_MAKE = SERVER_ADDRESS + "teacher/match/make/";
    public static String URL_MATCH_FIND = SERVER_ADDRESS + "student/match/find/";
    public static String URL_MATCH_JOIN = SERVER_ADDRESS + "teacher/match/join/";

    // Communication between devices
    public static String COMMUNICATION_MSG_FIND = "COMMUNICATION_MSG_FIND";

    // Receive from Server
    public static String RESPONSE_MATCH_MAKE = "success_teacher_match_make";
    public static String RESPONSE_MATCH_JOIN = "success_teacher_match_join";

}
