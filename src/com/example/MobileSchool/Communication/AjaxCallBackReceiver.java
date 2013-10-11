package com.example.MobileSchool.Communication;

import android.util.Log;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.MobileSchool.Utils.Constants;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 11.
 * Time: 오후 4:45
 */
public class AjaxCallBackReceiver extends AjaxCallback<JSONObject> {
    private String TAG = Constants.TAG;

    @Override
    public void callback(String url, JSONObject object, AjaxStatus status) {
        Log.d(TAG, "AjaxCallBackReceiver : JSON -> " + object + ", Status -> " + status);
        _handleResult(object);
    }

    private void _handleResult(JSONObject object) {

    }
}
