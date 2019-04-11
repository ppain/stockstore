package com.paint.stockstore.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.paint.stockstore.R;
import com.paint.stockstore.service.Utils;

import io.reactivex.Flowable;

public class PreloadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preload);

//        TokenStoreHelper.init(getApplicationContext());

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                testAndStart();
            }
        }, 1000);
    }

    void testAndStart(){
        String token = Utils.getToken();
        if(token.isEmpty()){
            startActivity(new Intent(PreloadActivity.this, LoginActivity.class));
        } else {
            startActivity(new Intent(PreloadActivity.this, BriefcaseActivity.class));
        }

//        startActivity(new Intent(PreloadActivity.this, StockActivity.class));
//        startActivity(new Intent(PreloadActivity.this, BriefcaseActivity.class));

        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}