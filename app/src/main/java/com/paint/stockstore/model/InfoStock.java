package com.paint.stockstore.model;

import com.google.gson.annotations.SerializedName;

public class InfoStock {

    @SerializedName("id")
    private Long id;

    @SerializedName("code")
    private String code;

    @SerializedName("name")
    private String name;

    @SerializedName("iconUrl")
    private String iconUrl;

    @SerializedName("price")
    private float price;

    @SerializedName("priceDelta")
    private float priceDelta;

    @SerializedName("count")
    private int count;

    public InfoStock(Long id, String code, String name, String iconUrl, float price, float priceDelta, int count) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.iconUrl = iconUrl;
        this.price = price;
        this.priceDelta = priceDelta;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPriceDelta() {
        return priceDelta;
    }

    public void setPriceDelta(float priceDelta) {
        this.priceDelta = priceDelta;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
