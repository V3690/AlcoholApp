package com.leopard4.alcoholrecipe.model;

import java.io.Serializable;

public class DiceRes implements Serializable {

//                            "result": "success",
//                                    "subject": "내가",
//                                    "action": "가운데로 나와서 춤을 춘다"


    private String result;

    private String subject;

    private String action;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
