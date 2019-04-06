package com.paint.stockstore.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestModel {
    private String name;
    private int price;

    public TestModel(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public static List<TestModel> generateData() {
        List<TestModel> data = new ArrayList<>();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 1; i < 30; i++) {
            data.add(new TestModel("Item_" + i, random.nextInt(1000)));
        }
        return data;
    }
}
