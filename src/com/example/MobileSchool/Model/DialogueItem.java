package com.example.MobileSchool.Model;

/**
 * User: yuhwan
 * Date: 13. 10. 31
 * Time: 오후 4:36
 */
public class DialogueItem {
    private String type;
    private String id;
    private String body;

    public DialogueItem(String type, String body, String id) {
        this.type = type;
        this.body = body;
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

}
