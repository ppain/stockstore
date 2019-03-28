package com.paint.stockstore.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AccountInfo {

    @SerializedName("name")
    private String name;

    @SerializedName("balance")
    private int balance;

    @SerializedName("stocks")
    private List<AccountInfoStock> stocks;

    public AccountInfo(String name, int balance, List<AccountInfoStock> stocks) {
        this.name = name;
        this.balance = balance;
        this.stocks = stocks;
    }

}