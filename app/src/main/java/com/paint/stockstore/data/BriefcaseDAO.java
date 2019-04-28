package com.paint.stockstore.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import io.reactivex.Maybe;
import io.reactivex.Single;

import com.paint.stockstore.model.InfoStock;
import com.paint.stockstore.model.AccountModel;

import java.util.List;

@Dao
public abstract class BriefcaseDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertStock(List<InfoStock> infoStock);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertAccount(AccountModel accountModel);

    @Query("DELETE FROM InfoStock")
    abstract void clearStock();

    @Query("DELETE FROM AccountModel")
    abstract void clearAccount();

    @Transaction
    public void updateStock(List<InfoStock> infoStock, AccountModel accountModel) {
        clearStock();
        clearAccount();
        insertStock(infoStock);
        insertAccount(accountModel);
    }

    @Query("SELECT * FROM InfoStock")
    public abstract Maybe<List<InfoStock>> getRxStock();

    @Query("SELECT * FROM AccountModel")
    public abstract Single<AccountModel> getRxAccount();
}