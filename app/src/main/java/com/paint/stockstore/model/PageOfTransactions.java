package com.paint.stockstore.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PageOfTransactions {
    @SerializedName("nextItemId")
    private int nextItemId;

    @SerializedName("prevItemId")
    private int prevItemId;

    @SerializedName("items")
    private List<TransactionHistoryRecord> items;

    public PageOfTransactions(int nextItemId, int prevItemId, List<TransactionHistoryRecord> items) {
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

    public List<TransactionHistoryRecord> getItems() {
        return items;
    }

    public void setItems(List<TransactionHistoryRecord> items) {
        this.items = items;
    }
}
