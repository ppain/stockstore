package com.paint.stockstore.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.paint.stockstore.model.AccessToken;


public class Utils {

    private static SharedPreferences sharedPref;
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String TIMESTAMP = "timestamp";

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
        return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
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



//    public static String dateToString(Date date, String pattern)
//            throws Exception {
//        return new SimpleDateFormat(pattern).format(date);
//    }
//
//    public static Date stringToDate(String dateStr, String pattern)
//            throws Exception {
//        return new SimpleDateFormat(pattern).parse(dateStr);
//    }
}
