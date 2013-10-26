package com.example.MobileSchool.Model;

/**
 * User: yuhwan
 * Date: 13. 10. 26
 * Time: 오전 12:26
 */
public class Content {
    private String expression;
    private String script;
    private String tip;

    public Content(String expression, String script, String tip) {
        this.expression = expression;
        this.script = script;
        this.tip = tip;
    }

    public String getExpression() {
        return expression;
    }

    public String getScript() {
        return script;
    }

    public String getTip() {
        return tip;
    }
}
