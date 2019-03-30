package com.paint.stockstore.service;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class TokenStoreHelper {

    private static SharedPreferences sharedPref;
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";

    private TokenStoreHelper(){}

    public static void init(Context context){
        if(sharedPref == null) {
            sharedPref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        }
    }

    public static String getStore(String key) {
        return sharedPref.getString(key, "");
    }

    public static void setStore(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }
}