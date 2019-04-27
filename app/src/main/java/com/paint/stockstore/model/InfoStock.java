package com.paint.stockstore.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.paint.stockstore.service.Utils;

@Entity
public class InfoStock {

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    private Long id;

    @SerializedName("code")
    private String code;

    @NonNull
    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;

    @ColumnInfo(name = "iconUrl")
    @SerializedName("iconUrl")
    private String iconUrl;

    @ColumnInfo(name = "price")
    @SerializedName("price")
    private float price;

    @ColumnInfo(name = "priceDelta")
    @SerializedName("priceDelta")
    private float priceDelta;

    @ColumnInfo(name = "count")
    @SerializedName("count")
    private int count;

    public InfoStock(@NonNull Long id, String code, @NonNull String name, String iconUrl, float price, float priceDelta, int count) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.iconUrl = iconUrl;
        this.price = price;
        this.priceDelta = priceDelta;
        this.count = count;
    }

    public InfoStock(InfoStock infoStock) {
        this.id = infoStock.getId();
        this.code = infoStock.getCode();
        this.name = infoStock.getName();
        this.iconUrl = infoStock.getIconUrl();
        this.price = Utils.roundToFloat(infoStock.getPrice());
        this.priceDelta = Utils.roundToFloat(infoStock.getPriceDelta());
        this.count = infoStock.getCount();
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
