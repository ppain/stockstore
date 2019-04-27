package com.paint.stockstore.model;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;
import com.paint.stockstore.service.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    TransactionHistoryRecord(TransactionHistoryRecord transactionHistoryRecord) {
        this.transactionId = transactionHistoryRecord.getTransactionId();
        this.stock = transactionHistoryRecord.getStock();
        this.amount = transactionHistoryRecord.getAmount();
        this.totalPrice = Utils.roundToFloat(transactionHistoryRecord.getTotalPrice());
        this.date = formatDate(transactionHistoryRecord.getDate());
        this.type = transactionHistoryRecord.getType();
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

    private String formatDate(String oldDate) {
        String newDate;
        try {
            @SuppressLint("SimpleDateFormat") Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(oldDate);
            newDate = String.valueOf(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", new Locale("ru")).format(date));

        } catch (ParseException e) {
            newDate = oldDate.replace("-", ".").replace("T", " ")
                    .substring(0, 19);
        }
        return newDate;
    }

}