package com.paint.stockstore.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.paint.stockstore.model.InfoStock;
import com.paint.stockstore.model.AccountModel;

@Database(entities = {InfoStock.class, AccountModel.class}, version = 1, exportSchema = false)
public abstract class DatabaseHelper extends RoomDatabase {

    public abstract BriefcaseDAO briefcaseDao();
}