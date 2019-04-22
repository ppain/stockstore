package com.paint.stockstore.model;

import com.google.gson.annotations.SerializedName;

public class Transaction {
    @SerializedName("stockId")
    private String stockId;

    @SerializedName("amount")
    private String amount;

    public Transaction(String stockId, String amount) {
        this.stockId = stockId;
        this.amount = amount;
    }
}
