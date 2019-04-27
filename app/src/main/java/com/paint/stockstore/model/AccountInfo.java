package com.paint.stockstore.model;

import com.google.gson.annotations.SerializedName;
import com.paint.stockstore.service.Utils;

import java.util.ArrayList;
import java.util.List;

public class AccountInfo {

    @SerializedName("name")
    private String name;

    @SerializedName("balance")
    private float balance;

    @SerializedName("stocks")
    private List<InfoStock> stocks;

    public AccountInfo(String name, float balance, List<InfoStock> stocks) {
        this.name = name;
        this.balance = balance;
        this.stocks = stocks;
    }

    public AccountInfo(AccountInfo accountInfo) {
        List<InfoStock> listInfoStock = new ArrayList<>();
        for (InfoStock stock : accountInfo.getStock()) {
            listInfoStock.add(new InfoStock(stock));
        }
        this.name = accountInfo.getName();
        this.balance = Utils.roundToFloat(accountInfo.getBalance());
        this.stocks = listInfoStock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public List<InfoStock> getStock() {
        return stocks;
    }

    public void setStock(List<InfoStock> stocks) {
        this.stocks = stocks;
    }

}