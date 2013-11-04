package com.example.MobileSchool;

import org.json.JSONObject;

/**
 * User: yuhwan
 * Date: 13. 10. 17
 * Time: 오후 4:24
 */
public interface BaseMethod {

    public void handleAjaxCallBack(JSONObject object);
    public void handlePush(JSONObject object);
}
