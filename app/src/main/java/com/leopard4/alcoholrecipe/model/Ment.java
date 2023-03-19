package com.leopard4.alcoholrecipe.model;

import java.util.ArrayList;
import java.util.List;

public class Ment {

    // 위의 단어들은 자주쓰는 단어들이다



    // 유저가 입력한 건배사를 서버로 보내기위해 멘트를 작성합니다.
    private String ment ;

    public Ment() {
    }

    public Ment(String ment) {

        this.ment = ment;
    }

    public String getMent() {
        return ment;
    }

    public void setMent(String ment) {
        this.ment = ment;
    }
}
