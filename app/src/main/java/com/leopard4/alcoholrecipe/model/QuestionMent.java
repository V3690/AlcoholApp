package com.leopard4.alcoholrecipe.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class QuestionMent implements Serializable {


    // 유저가 입력한 건배사를 서버로 보내기위해 멘트를 작성합니다.

    private String question;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
