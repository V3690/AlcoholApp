package com.leopard4.alcoholrecipe.model;

import java.io.Serializable;

public class User implements Serializable {
    // 포스트맨 body 부분
//    {
//        "username": "TITI",
//            "email": "TTT@naver.com",
//            "password": "1234"
//    }
    private String email;
    private String password;

    private String nickname;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User( String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;

    }

}
