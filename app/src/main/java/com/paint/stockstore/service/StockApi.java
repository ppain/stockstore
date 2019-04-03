package com.paint.stockstore.service;

import com.paint.stockstore.model.AccessToken;
import com.paint.stockstore.model.AccountInfo;
import com.paint.stockstore.model.AccountInfoStock;
import com.paint.stockstore.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StockApi {

    @FormUrlEncoded
    @POST("auth/refresh")
    Call<AccessToken> refreshAccessToken(
            @Header("Authorization") String token,
            @Field("refreshToken") String refreshToken);

    @Headers("Content-Type: application/json")
    @POST("auth/signup")
    Call<AccessToken> postReg(
            @Body User user);


    @Headers("Content-Type: application/json")
    @POST("auth/signin")
    Call<AccessToken> postLogin(
            @Body User user);

    @GET("account/info?id={id}")
    Call<AccountInfoStock> geTest(
            @Path("id") String id,
            @Header("Authorization") String token);

    @GET("account/info")
    Call<AccountInfo> getAccountInfo(
            @Header("Authorization") String token);

    @GET("/api/stocks")
    Call<AccessToken> getUser();


    //    @FormUrlEncoded
//    @Headers("Content-Type: application/x-www-form-urlencoded")
//    @POST("part_auth/signup")
//    Call<AccessToken> postReg(
//            @Field("login") String login,
//            @Field("password") String password);
}