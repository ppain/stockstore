package com.paint.stockstore.data;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;

import com.paint.stockstore.model.InfoStock;

@Database(entities = { InfoStock.class }, version = 1, exportSchema = false)
public abstract class DatabaseHelper extends RoomDatabase {

    public abstract BriefcaseDAO briefcaseDao();

//    @Override
//    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
//        return null;
//    }
//
//    @Override
//    protected InvalidationTracker createInvalidationTracker() {
//        return null;
//    }
}