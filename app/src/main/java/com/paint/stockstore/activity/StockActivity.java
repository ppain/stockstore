package com.paint.stockstore.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.paint.stockstore.R;
import com.paint.stockstore.adapter.StockAdapter;
import com.paint.stockstore.model.InfoStock;
import com.paint.stockstore.model.Stock;
import com.paint.stockstore.service.RetrofitService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StockActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private List<InfoStock> stock = new ArrayList<>();
    private StockAdapter adapterStock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stocks, menu);
        getSupportActionBar().setTitle("Акции");
        return true;
    }

    void init() {

        Toolbar toolbar = findViewById(R.id.toolbar_stock);
        setSupportActionBar(toolbar);

        RecyclerView rvStocks = (RecyclerView) findViewById(R.id.list_stock);
        rvStocks.setLayoutManager(new LinearLayoutManager(this));
        rvStocks.setHasFixedSize(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_stock);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestStock();
            }
        });

        adapterStock = new StockAdapter(stock);
        rvStocks.setAdapter(adapterStock);

        requestStock("Sber", String.valueOf(10), String.valueOf(1));
    }


    private void setList(Stock stock){
        adapterStock.swapList(stock.getStock());
        swipeRefreshLayout.setRefreshing(false);
    }


    private void requestStock(String search, String count, String itemId){

        RetrofitService.getInstance()
                .getApi()
                .getAccountInfoStockParam(search, count, itemId)
                .enqueue(new Callback<Stock>() {
                    @Override
                    public void onResponse(Call<Stock> call, Response<Stock> response) {
                        Log.d("testing", "getAccountInfo/onResponse");
                        swipeRefreshLayout.setRefreshing(false);
                        int statusCode = response.code();
                        if(statusCode == 200) {
                            Log.d("testing", "getAccountInfo/onResponse/response 200");
                            setList(response.body());
                        } else {
                            Log.d("testing", "getAccountInfo/onResponse/something wrong");
                        }
                    }

                    @Override
                    public void onFailure(Call<Stock> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("testing", "getAccountInfo/onFailure/all wrong");
                    }
                });
    }


    private void requestStock(){

        RetrofitService.getInstance()
                .getApi()
                .getAccountInfoStock()
                .enqueue(new Callback<Stock>() {
                    @Override
                    public void onResponse(Call<Stock> call, Response<Stock> response) {
                        Log.d("testing", "getAccountInfo/onResponse");
                        swipeRefreshLayout.setRefreshing(false);
                        int statusCode = response.code();
                        if(statusCode == 200) {
                            Log.d("testing", "getAccountInfo/onResponse/response 200");
                            setList(response.body());

                        } else {
                            Log.d("testing", "getAccountInfo/onResponse/something wrong");
                        }
                    }

                    @Override
                    public void onFailure(Call<Stock> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("testing", "getAccountInfo/onFailure/all wrong");
                    }
                });
    }

}