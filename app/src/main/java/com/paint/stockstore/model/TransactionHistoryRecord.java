package com.paint.stockstore.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionHistoryRecord {
    @SerializedName("transactionId")
    private int transactionId;

    @SerializedName("stock")
    private InfoStock stock;

    @SerializedName("amount")
    private int amount;

    @SerializedName("totalPrice")
    private float totalPrice;

    @SerializedName("date")
    private String date;

    @SerializedName("type")
    private String type;

    public TransactionHistoryRecord(int transactionId, InfoStock stock, int amount, float totalPrice,
                                    String date, String type) {
        this.transactionId = transactionId;
        this.stock = stock;
        this.amount = amount;
        this.totalPrice = totalPrice;
        this.date = date;
        this.type = type;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public InfoStock getStock() {
        return stock;
    }

    public void setStock(InfoStock stock) {
        this.stock = stock;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}