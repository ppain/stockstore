package com.paint.stockstore.service;

import com.paint.stockstore.model.AccessToken;
import com.paint.stockstore.model.AccountInfo;
import com.paint.stockstore.model.PageOfStocks;
import com.paint.stockstore.model.PageOfTransactions;
import com.paint.stockstore.model.RefreshToken;
import com.paint.stockstore.model.Transaction;
import com.paint.stockstore.model.User;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface StockApi {

    @Headers("Content-Type: application/json")
    @POST("auth/refresh")
    Call<AccessToken> refreshAccessToken(
            @Header("Authorization") String token,
            @Body RefreshToken refreshToken);

    @Headers("Content-Type: application/json")
    @POST("auth/signup")
    Call<AccessToken> postReg(
            @Body User user);

    @Headers("Content-Type: application/json")
    @POST("auth/signin")
    Call<AccessToken> postLogin(
            @Body User user);

    @GET("account/info")
    Call<AccountInfo> getAccountInfo(
            @Header("Authorization") String token);

    @GET("stocks")
    Call<PageOfStocks> getStock(
            @Query("search") String search,
            @Query("count") int count,
            @Query("itemId") int itemId);

    @Headers("Content-Type: application/json")
    @POST("transaction/buy")
    Call<JSONObject> buyStock(
            @Header("Authorization") String token,
            @Body Transaction transaction);

    @POST("transaction/sell")
    Call<JSONObject> sellStock(
            @Header("Authorization") String token,
            @Body Transaction transaction);

    @Headers("Content-Type: application/json")
    @GET("transaction/history")
    Call<PageOfTransactions> getHistory(
            @Header("Authorization") String token,
            @Query("search") String search,
            @Query("count") int count,
            @Query("itemId") int itemId);
}