package com.paint.stockstore.model;

import com.google.gson.annotations.SerializedName;

public class RefreshToken {
    @SerializedName("refreshToken")
    private String refreshToken;

    public RefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}