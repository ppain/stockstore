package com.paint.stockstore.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.paint.stockstore.R;
import com.paint.stockstore.service.Utils;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                testAndStart();
            }
        }, 1000);
    }

    void testAndStart(){
        if(!Utils.getToken().isEmpty()){
            startActivity(new Intent(LaunchActivity.this, BriefcaseActivity.class));
        } else {
            startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
        }

        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        finish();
    }
}