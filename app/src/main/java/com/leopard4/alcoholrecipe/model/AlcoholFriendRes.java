package com.leopard4.alcoholrecipe.model;

import com.leopard4.alcoholrecipe.model.cheers.CheersMent;

import java.io.Serializable;

public class AlcoholFriendRes implements Serializable {
//
//    {
//        "result": "success",
//            "answer": "\n\n오늘은 금요일입니다."
//    }

    private String result;
    private String answer;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
