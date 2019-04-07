package com.paint.stockstore.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("login")
    private String login;

    @SerializedName("password")
    private String password;

    public User(String login, String password ) {
        this.login = login;
        this.password = password;
    }
}
