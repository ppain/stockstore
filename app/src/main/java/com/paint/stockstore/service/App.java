package com.paint.stockstore.service;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.paint.stockstore.data.DatabaseHelper;


public class App extends Application {

    private static App instance;
    private DatabaseHelper database;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(getApplicationContext(), DatabaseHelper.class, "briefcase")
//                .allowMainThreadQueries()
                .build();

        Utils.initSharedPreferences(getApplicationContext());
    }

    public static App getInstance() {
        return instance;
    }

    public DatabaseHelper getDatabase() {
        return database;
    }
}