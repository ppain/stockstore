package com.paint.stockstore.model;

import com.google.gson.annotations.SerializedName;
import com.paint.stockstore.service.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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


    private String formatDate(String inputDate) {
        String outputDate;
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss.SSS", new Locale("ru"));
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = inputFormat.parse(inputDate);
            SimpleDateFormat outputFormat = new SimpleDateFormat(
                    "dd.MM.yyyy HH:mm:ss", new Locale("ru"));
            outputFormat.setTimeZone(TimeZone.getDefault());
            outputDate = String.valueOf(outputFormat.format(date));
        } catch (ParseException e) {
            outputDate = inputDate;
        }
        return outputDate;
    }
}