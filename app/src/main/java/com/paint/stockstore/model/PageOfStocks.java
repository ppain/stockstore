package com.paint.stockstore.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PageOfStocks {

    @SerializedName("nextItemId")
    private int nextItemId;

    @SerializedName("prevItemId")
    private int prevItemId;

    @SerializedName("items")
    private List<InfoStock> items;

    public PageOfStocks(int nextItemId, int prevItemId, List<InfoStock> items) {
        this.nextItemId = nextItemId;
        this.prevItemId = prevItemId;
        this.items = items;
    }

    public PageOfStocks(PageOfStocks pageOfStocks) {
        List<InfoStock> listInfoStock = new ArrayList<>();
        for (InfoStock stock : pageOfStocks.getItems()) {
            listInfoStock.add(new InfoStock(stock));
        }
        this.nextItemId = pageOfStocks.getNextItemId();
        this.prevItemId = pageOfStocks.getPrevItemId();
        this.items = listInfoStock;
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
}