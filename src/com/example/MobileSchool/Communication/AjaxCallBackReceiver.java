package com.example.MobileSchool.Communication;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.MobileSchool.BaseMethod;
import com.example.MobileSchool.Utils.AccountManager;
import com.example.MobileSchool.Utils.Constants;
import com.example.MobileSchool.Utils.GlobalApplication;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: yuhwan
 * Date: 13. 10. 11.
 * Time: 오후 4:45
 */
public class AjaxCallBackReceiver extends AjaxCallback<JSONObject> {
    private String TAG = Constants.TAG;

    private Context context;
    private Fragment fragment;
    private Activity activity;

    public AjaxCallBackReceiver(Context context) {
        this.context = context;
    }

    public AjaxCallBackReceiver(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    public AjaxCallBackReceiver(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    public void callback(String url, JSONObject object, AjaxStatus status) {
        if(activity != null)
            ((BaseMethod) activity).handleAjaxCallBack(object);
        else if(fragment != null)
            ((BaseMethod) fragment).handleAjaxCallBack(object);
        else
            _handleResult(object);
    }

    private void _handleResult(JSONObject object) {
        Log.d(TAG, "AjaxCallBackReceiver without Fragment : JSON -> " + object);
    }
}
