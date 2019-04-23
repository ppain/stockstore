package com.paint.stockstore.service;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.paint.stockstore.model.AccountInfo;
import com.paint.stockstore.model.InfoStock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
