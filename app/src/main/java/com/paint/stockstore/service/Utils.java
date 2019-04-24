package com.paint.stockstore.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.paint.stockstore.R;
import com.paint.stockstore.model.AccessToken;

import java.util.concurrent.TimeUnit;


public class Utils {

    private static SharedPreferences sharedPref;
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";
    private static final String TIMESTAMP = "timestamp";
    private static final String PATTERN_LOGIN = "^[a-z|A-Z|\\d|_]{3,100}$";

    private static boolean FLAG_UPDATE = false;

    public static final int COUNT_DEFAULT = 10;
    public static final int ITEM_ID_DEFAULT = 1;
    public static final int HIDE_ITEM = 5;

    private Utils() {
    }

    public static boolean isFlagUpdate() {
        return FLAG_UPDATE;
    }

    public static void setFlagUpdate(boolean flagUpdate) {
        FLAG_UPDATE = flagUpdate;
    }


    public static void initSharedPreferences(Context context) {
        if (sharedPref == null) {
            sharedPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        }
    }

    private static String getStore(String key) {
        return sharedPref.getString(key, "");
    }

    private static void saveStore(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return true;
        } else {
            showMessage(context.getString(R.string.connection_error), context);
            return false;
        }
    }


    public static void showMessage(@NonNull String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }


    private static Long getTime() {
        String timestamp = Utils.getStore(Utils.TIMESTAMP);
        return timestamp.isEmpty() ? 0L : Long.parseLong(timestamp);
    }

    public static void setTime() {
        saveStore(TIMESTAMP, String.valueOf(System.currentTimeMillis()));
    }

    public static boolean isRelevanceCache() {
        Long minToLife = TimeUnit.MINUTES.toMillis(5L);
        Long diff = System.currentTimeMillis() - getTime();
        return (diff - minToLife < 0);
    }


    public static String getToken() {
        return getStore(ACCESS_TOKEN);
    }

    public static String getRefreshToken() {
        return getStore(REFRESH_TOKEN);
    }

    public static void setToken(AccessToken token) {
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
