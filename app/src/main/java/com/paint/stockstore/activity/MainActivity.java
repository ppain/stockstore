package com.paint.stockstore.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.paint.stockstore.R;
import com.paint.stockstore.service.TokenStoreHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TokenStoreHelper.init(getApplicationContext());

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                testAndStart();
            }
        }, 500);
    }

    void testAndStart(){

        String token = TokenStoreHelper.getStore(TokenStoreHelper.ACCESS_TOKEN);
        if(token.isEmpty()){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else {
            startActivity(new Intent(MainActivity.this, BriefcaseActivity.class));
        }

//        SharedPreferences sharedPrefs = getSharedPreferences("com.paint.stockstore", MODE_PRIVATE);
//        Intent intent;
//        if(sharedPrefs.contains("accessToken")){
//            intent = new Intent(MainActivity.this, BriefcaseActivity.class);
//        } else {
//            intent = new Intent(MainActivity.this, LoginActivity.class);
//        }

        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}