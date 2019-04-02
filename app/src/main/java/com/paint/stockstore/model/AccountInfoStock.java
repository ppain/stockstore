package com.paint.stockstore.model;

import com.google.gson.annotations.SerializedName;

public class AccountInfoStock {

    @SerializedName("id")
    private Long id;

    @SerializedName("code")
    private String code;

    @SerializedName("name")
    private String name;

    @SerializedName("iconUrl")
    private String iconUrl;

    @SerializedName("price")
    private int price;

    @SerializedName("priceDelta")
    private float priceDelta;

    @SerializedName("count")
    private int count;

    public AccountInfoStock(Long id, String code, String name, String iconUrl, int price, float priceDelta, int count) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.iconUrl = iconUrl;
        this.price = price;
        this.priceDelta = priceDelta;
        this.count = count;
    }
}
