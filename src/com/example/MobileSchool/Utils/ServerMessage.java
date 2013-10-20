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

    // New Message
    public static String URL_START = SERVER_ADDRESS + "start/";
    public static String URL_CONFIRM = SERVER_ADDRESS + "confirm/";
    public static String URL_ANSWER = SERVER_ADDRESS + "answer/";

    // Old Message
    public static String URL_LOGIN = SERVER_ADDRESS + "account/login/";

}
