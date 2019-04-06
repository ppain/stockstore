package com.paint.stockstore.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AccountInfo {

    @SerializedName("name")
    private String name;

    @SerializedName("balance")
    private float balance;

    @SerializedName("stocks")
    private List<AccountInfoStock> stocks;

    public AccountInfo(String name, float balance, List<AccountInfoStock> stocks) {
        this.name = name;
        this.balance = balance;
        this.stocks = stocks;
    }

    public static AccountInfo generateData() {

        List<AccountInfoStock> stocks = new ArrayList<>();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 1; i < 10; i++) {
            stocks.add(new AccountInfoStock(Long.valueOf(i),"s" + i,"Stock_" + i, "ic_history_black_18dp", random.nextFloat() * 100, random.nextFloat(), i));
        }
        return new AccountInfo("UserName", random.nextFloat() * 1000, stocks);
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

    public List<AccountInfoStock> getStocks() {
        return stocks;
    }

    public void setStocks(List<AccountInfoStock> stocks) {
        this.stocks = stocks;
    }

}