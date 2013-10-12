package com.example.MobileSchool.Communication;

import android.content.Context;
import com.example.MobileSchool.Utils.Constants;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 11.
 * Time: 오후 4:45
 */
public class PushSender {

    private String TAG = Constants.TAG;

    public PushSender(Context context) {}

    public void pushToDevice(String myId, String targetId, String message) {
        // Set Query
        ParseQuery parseQuery = ParseInstallation.getQuery();
        parseQuery.whereEqualTo("channels",targetId);
        parseQuery.whereEqualTo("deviceType", "android");

        // Push
        ParsePush push = new ParsePush();
        push.setQuery(parseQuery);
        push.setMessage("From " + myId + " : " + message);
        push.sendInBackground();
    }


}
