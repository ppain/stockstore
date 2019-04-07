package com.paint.stockstore.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Stock {

    @SerializedName("nextItemId")
    private int nextItemId;

    @SerializedName("prevItemId")
    private int prevItemId;

    @SerializedName("items")
    private List<InfoStock> items;

    public Stock(int nextItemId, int prevItemId, List<InfoStock> items) {
        this.nextItemId = nextItemId;
        this.prevItemId = prevItemId;
        this.items = items;
    }

    public int getNextItemId() {
        return nextItemId;
    }

    public void setNextItemId(int nextItemId) {
        this.nextItemId = nextItemId;
    }

    public int getPrevItemId() {
        return prevItemId;
    }

    public void setPrevItemId(int prevItemId) {
        this.prevItemId = prevItemId;
    }

    public List<InfoStock> getItems() {
        return items;
    }

    public void setItems(List<InfoStock> items) {
        this.items = items;
    }


    public List<InfoStock> getStock() {
        return items;
    }

}