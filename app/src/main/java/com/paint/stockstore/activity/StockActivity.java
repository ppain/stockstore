package com.paint.stockstore.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;
import com.paint.stockstore.R;
import com.paint.stockstore.adapter.StockAdapter;
import com.paint.stockstore.model.InfoStock;
import com.paint.stockstore.model.Stock;
import com.paint.stockstore.service.RetrofitService;
import com.paint.stockstore.service.RxSearchObservable;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StockActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private List<InfoStock> stock = new ArrayList<>();
    private StockAdapter adapterStock;
    String s;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stock, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.stock));

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("...");


//        RxSearchView.queryTextChanges(searchView)
//                .debounce(300, TimeUnit.MILLISECONDS)
//                .map(CharSequence::toString)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
//
//                    }
//
//                    @Override
//                    public void onNext(String result) {
//                        s = result;
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }


//        RxSearchView.queryTextChanges(searchView)
//                .debounce(300, TimeUnit.MILLISECONDS)
////                .filter((Predicate<String>) text -> {
////                    if (text.isEmpty()) {
////                        return false;
////                    } else {
////                        return true;
////                    }
////                })
//                .distinctUntilChanged()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
////                .subscribe(result -> requestStock((String) result, 10, 1));
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String result) throws Exception {
//                        s = result;
//                    }
//                });



        return true;
//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    void init() {

        Toolbar toolbar = findViewById(R.id.toolbar_stock);
        //toolbar.setNavigationIcon(android.R.id.home);
        setSupportActionBar(toolbar);

        Toolbar searchView = findViewById(R.id.action_search);

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

        requestStock("Rai", 10, 1);


//        RxSearchObservable.fromView(searchView)
//                .debounce(300, TimeUnit.MILLISECONDS)
//                .filter(new Predicate<String>() {
//                    @Override
//                    public boolean test(String text) throws Exception {
//                        if (text.isEmpty()) {
//                            return false;
//                        } else {
//                            return true;
//                        }
//                    }
//                })
//                .distinctUntilChanged()
//                .switchMap(new Function<String, ObservableSource<String>>() {
//                    @Override
//                    public ObservableSource<String> apply(String query) throws Exception {
//                        return dataFromNetwork(query);
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String result) throws Exception {
//                        textViewResult.setText(result);
//                    }
//                });
    }


    private void setList(Stock stock){
        adapterStock.swapList(stock.getStock());
        swipeRefreshLayout.setRefreshing(false);
    }


    private void requestStock(String search, int count, int itemId){
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