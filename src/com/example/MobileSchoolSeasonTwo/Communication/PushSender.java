package com.example.MobileSchoolSeasonTwo.Communication;

import android.content.Context;
import com.example.MobileSchoolSeasonTwo.Utils.Constants;
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

        // Make Json
        JSONObject data = new JSONObject();
        try {
            data.put("action", Constants.PUSH_CUSTOM_INTENT);
            data.put("type", Constants.PUSH_TYPE_NOTIFICATION);
        } catch (JSONException e) { e.printStackTrace(); }

        // Push
        ParsePush push = new ParsePush();
        push.setQuery(parseQuery);
        push.setData(data);
        push.sendInBackground();
    }

}
