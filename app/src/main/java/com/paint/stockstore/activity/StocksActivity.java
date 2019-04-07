package com.paint.stockstore.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.paint.stockstore.R;


public class StocksActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);

        init();
    }

    void init() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


}