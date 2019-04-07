package com.paint.stockstore.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.paint.stockstore.R;
import com.paint.stockstore.service.TokenStoreHelper;

public class PreloadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preload);

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

//        startActivity(new Intent(PreloadActivity.this, LoginActivity.class));

//        String token = TokenStoreHelper.getStore(TokenStoreHelper.ACCESS_TOKEN);
//        if(token.isEmpty()){
//            startActivity(new Intent(PreloadActivity.this, LoginActivity.class));
//        } else {
//            startActivity(new Intent(PreloadActivity.this, BriefcaseActivity.class));
//        }

//        SharedPreferences sharedPrefs = getSharedPreferences("com.paint.stockstore", MODE_PRIVATE);
//        Intent intent;
//        if(sharedPrefs.contains("accessToken")){
//            intent = new Intent(PreloadActivity.this, BriefcaseActivity.class);
//        } else {
//            intent = new Intent(PreloadActivity.this, LoginActivity.class);
//        }

        startActivity(new Intent(PreloadActivity.this, StocksActivity.class));
//        startActivity(new Intent(PreloadActivity.this, BriefcaseActivity.class));

        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}