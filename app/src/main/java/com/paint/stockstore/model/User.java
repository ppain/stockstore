package com.paint.stockstore.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("login")
    String login;

    @SerializedName("password")
    String password;

    public User(String login, String password ) {
        this.login = login;
        this.password = password;
    }
}
