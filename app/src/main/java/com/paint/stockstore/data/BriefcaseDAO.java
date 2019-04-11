package com.paint.stockstore.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import io.reactivex.Flowable;
import io.reactivex.Single;

import com.paint.stockstore.model.InfoStock;

import java.util.List;

@Dao
public interface BriefcaseDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<InfoStock> infoStock);

    @Update
    void update(List<InfoStock> infoStock);

    @Delete
    void delete(List<InfoStock> infoStock);

    @Query("DELETE FROM InfoStock")
    void clear();

    @Query("SELECT * FROM InfoStock")
    List<InfoStock> getData();

    @Query("SELECT * FROM InfoStock")
    Single<List<InfoStock>> getRxData();

//    @Query("SELECT * FROM InfoStock")
//    Flowable<List<InfoStock>> getAllData();
//
//    //пример запроса с выборкой
//    @Query("SELECT * FROM InfoStock WHERE name LIKE :name")
//    Flowable<List<InfoStock>> getByName(String name);

}