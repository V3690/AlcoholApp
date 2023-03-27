package com.leopard4.alcoholrecipe.model;

import java.io.Serializable;

public class User implements Serializable {
    // 포스트맨 body 부분
//    {
//        "nickname": "TITI",
//         "email": "TTT@naver.com",
//         "password": "1234"
//    }
    private String email;
    private String password;

    private String nickname;

    private  int accountType;

    public User() {
    }

    public User(String email, String password, int accountType) {
        this.email = email;
        this.password = password;
        this.accountType = accountType;
    }

    public User( String email, String password, String nickname,int accountType) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.accountType = accountType;

    }

}
