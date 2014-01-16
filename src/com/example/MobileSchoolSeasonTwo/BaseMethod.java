package com.example.MobileSchoolSeasonTwo;

import org.json.JSONObject;

/**
 * User: yuhwan
 * Date: 13. 10. 17
 * Time: 오후 4:24
 */
public interface BaseMethod {

    public void handleSocketMessage(String message);
    public void handleAjaxCallBack(JSONObject object);
    public void handlePush(JSONObject object);
}
