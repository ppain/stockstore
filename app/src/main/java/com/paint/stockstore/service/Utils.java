package com.paint.stockstore.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.paint.stockstore.model.AccessToken;


public class Utils {

    private static SharedPreferences sharedPref;
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String TIMESTAMP = "timestamp";
    public static final String PATTERN_LOGIN = "^[a-z|A-Z|\\d|_]{3,100}$";

    private Utils(){}

    public static void initSharedPreferences(Context context){
        if(sharedPref == null) {
            sharedPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        }
    }

    public static String getStore(String key) {
        return sharedPref.getString(key, "");
    }

    public static void saveStore(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public static boolean isNetworkAvailable (Context context) {
        ConnectivityManager cm
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
       if(activeNetworkInfo != null && activeNetworkInfo.isConnected()){
           return true;
        } else {
           showMessage("Отсутсвует подключение к интернету", context);
           return false;
       }
    }


    public static void showMessage(@NonNull String text, Context context){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }


    public static Long getTime(){
        String timestamp = Utils.getStore(Utils.TIMESTAMP);
        return timestamp.isEmpty() ? 0L : Long.parseLong(timestamp);
    }

    public static void setTime(){
        saveStore(TIMESTAMP, String.valueOf(System.currentTimeMillis()));
    }


    public static String getToken(){
        return getStore(ACCESS_TOKEN);
    }

    public static String getRefreshToken(){
        return getStore(REFRESH_TOKEN);
    }

    public static void setToken(AccessToken token){
        saveStore(ACCESS_TOKEN, token.getAccessToken());
        saveStore(REFRESH_TOKEN, token.getRefreshToken());
    }


    public static String validateLogin(String login) {
        String setErrorLogin = "";

        if (!login.matches(Utils.PATTERN_LOGIN)) {
            setErrorLogin = "от 3 [a-z|A-Z|\\d]";
        }
        return setErrorLogin;
    }

    public static String validatePassword(String password) {
        String setErrorPassword = "";

        if (password.isEmpty() || password.trim().length() < 1 || password.length() < 8 || password.length() > 64) {
            setErrorPassword = "от 8 до 64 символов";
        }
        return setErrorPassword;
    }

}
