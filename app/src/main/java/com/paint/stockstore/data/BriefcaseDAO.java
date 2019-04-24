package com.paint.stockstore.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;
import io.reactivex.Flowable;
import io.reactivex.Single;

import com.paint.stockstore.model.InfoStock;
import com.paint.stockstore.model.AccountInfo;
import com.paint.stockstore.service.AccountModel;

import java.util.List;

@Dao
public interface BriefcaseDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStock(List<InfoStock> infoStock);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAccount(AccountModel accountModel);

    @Query("DELETE FROM InfoStock")
    void clearStock();

    @Query("DELETE FROM AccountModel")
    void clearAccount();

//    TODO add transaction
//    @Transaction
//    void updateStock(List<InfoStock> infoStock) {
//        clearStock();
//        insertStock(infoStock);
//    }

    @Query("SELECT * FROM InfoStock")
    List<InfoStock> getStock();

    @Query("SELECT * FROM AccountModel")
    AccountModel getAccount();

    @Query("SELECT * FROM InfoStock")
    Single<List<InfoStock>> getRxStock();

    @Query("SELECT * FROM AccountModel")
    Single<AccountModel> getRxAccount();

//    @Query("SELECT * FROM InfoStock")
//    Flowable<List<InfoStock>> getAllData();
//
//    //пример запроса с выборкой
//    @Query("SELECT * FROM InfoStock WHERE name LIKE :name")
//    Flowable<List<InfoStock>> getByName(String name);

}