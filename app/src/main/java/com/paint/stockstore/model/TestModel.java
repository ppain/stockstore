package com.paint.stockstore.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestModel {
    private String name;
    private int cost;

    public TestModel(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public static List<TestModel> generateData() {
        List<TestModel> data = new ArrayList<TestModel>();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 1; i < 30; i++) {
            data.add(new TestModel("Item_" + i, random.nextInt(1000)));
        }
        return data;
    }
}
