package com.paint.stockstore.service;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity
public class AccountModel {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "balance")
    private float balance;

    public AccountModel(String name, float balance) {
        this.name = name;
        this.balance = balance;
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

}
