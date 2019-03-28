package com.paint.stockstore.service;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenStore {
    private static Context context;

    public TokenStore(Context context){
        this.context = context;
    }

    public final static String PREFS_NAME = "stockstore";


    public static void setStore(String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStore(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        return prefs.getString(key,"");
    }
}
