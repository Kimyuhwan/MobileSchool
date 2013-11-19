package com.example.MobileSchoolDev.Model;

/**
 * User: yuhwan
 * Date: 13. 10. 31
 * Time: 오후 4:36
 */
public class DialogueItem {
    private String type;
    private String id;
    private String body;
    private String successor;

    public DialogueItem(String type, String body, String id, String successor) {
        this.type = type;
        this.body = body;
        this.id = id;
        this.successor = successor;
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

    public String getSuccessor() {
        return successor;
    }

}
