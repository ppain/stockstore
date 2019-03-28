package com.paint.stockstore.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.paint.stockstore.model.AccessToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginHelper {

    public void getNewToken(String refreshToken){
        RetrofitService.getInstance()
                .getApi()
                .refreshAccessToken(refreshToken)
                .enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        Log.d("testing", "AccessToken/onResponse");
                        int statusCode = response.code();
                        if(statusCode == 200) {
                            Log.d("testing", "AccessToken/onResponse/response 200");
                            AccessToken token = response.body();
                            //preferences.edit().putBoolean("oauth.loggedIn", true).apply();
                            preferences.edit().putString("accessToken", token.getAccessToken()).apply();
                            preferences.edit().putString("refreshToken", token.getRefreshToken()).apply();

                            onSuccessfulAuth();
                        } else {
                            Log.d("testing", "AccessToken/onResponse/something wrong");
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        Log.d("testing", "AccessToken/onFailure/all wrong");
                    }
                });
    }



//    public boolean validate() {
//        boolean valid = true;
//
//        String login = textLogin.getText().toString();
//        String password = textPassword.getText().toString();
//
//        if (login.matches(PATTERN_LOGIN)) {
//            textLogin.setError(null);
//        } else {
//            textLogin.setError("Корректный логин - " + PATTERN_LOGIN);
//            valid = false;
//        }
//
//        if (password.isEmpty() || password.trim().length() < 1 || password.length() < 8 || password.length() > 64) {
//            textPassword.setError("от 8 до 64 символов");
//            valid = false;
//        } else {
//            textPassword.setError(null);
//        }
//
//        return valid;
//    }
}
