package com.paint.stockstore.activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.paint.stockstore.R;
import com.paint.stockstore.adapter.BuyAdapterClickListener;
import com.paint.stockstore.adapter.StockAdapter;
import com.paint.stockstore.fragment.BuyStockFragment;
import com.paint.stockstore.model.InfoStock;
import com.paint.stockstore.model.PageOfStocks;
import com.paint.stockstore.service.RetrofitService;
import com.paint.stockstore.service.RxSearchObservable;
import com.paint.stockstore.service.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StockActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private StockAdapter adapterStock;
    private RecyclerView rvStocks;
    private LinearLayoutManager llManager;
    private Boolean isLoaded = false;
    private final int COUNT_DEFAULT = 10;
    private final int ITEM_ID_DEFAULT = 1;
    private final int HIDE_ITEM = 5;
    private int nextItemId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        init();
    }


    void init() {

        Toolbar toolbar = findViewById(R.id.toolbar_stock);
        setSupportActionBar(toolbar);

        rvStocks = (RecyclerView) findViewById(R.id.list_stock);
        llManager = new LinearLayoutManager(this);
        rvStocks.setLayoutManager(llManager);
        rvStocks.setHasFixedSize(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_stock);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initClearRequest("");
            }
        });

        initAdapter();

        initClearRequest("");
    }


    @SuppressLint("CheckResult")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stock, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MenuItem menuItemSearch = menu.findItem(R.id.action_search);
        menuItemSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                initClearRequest("");
                return true;
            }
        });

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getString(R.string.dots));

        RxSearchObservable.fromView(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String text){
                        return !(text.length() < 2);
                    }
                })
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> initClearRequest(result),
                        throwable -> Utils.showMessage(throwable.toString(), getApplicationContext())
                );

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    void initClearRequest(String query) {
        swipeRefreshLayout.setRefreshing(true);
        nextItemId = ITEM_ID_DEFAULT;
        adapterStock.clearList();
        requestStock(query, COUNT_DEFAULT, nextItemId);
    }


    void initAdapter() {
        adapterStock = new StockAdapter(new ArrayList<>(), StockActivity.this, new BuyAdapterClickListener() {
            @Override
            public void onItemClicked(String stockId, String name) {
                showBuyStockFragment(stockId, name);
            }
        });

        rvStocks.setAdapter(adapterStock);

        rvStocks.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int sizeList = adapterStock.getItemCount();
                    int lastVisibleItemPosition = llManager.findLastVisibleItemPosition();
                    Log.d("testing", "lastVisibleItemPosition: " + lastVisibleItemPosition
                            + ", adapterStock.getItemCount(): " + sizeList);
                    if ((lastVisibleItemPosition > sizeList - HIDE_ITEM) && !isLoaded) {
                        isLoaded = true;
                        swipeRefreshLayout.setRefreshing(true);
                        requestStock("", COUNT_DEFAULT, nextItemId);
                    }
                }
            }
        });
    }


    private void setList(List<InfoStock> list){
        adapterStock.updateList(list);
        swipeRefreshLayout.setRefreshing(false);
    }


    public void showBuyStockFragment(String stockId, String name) {

        BuyStockFragment buyStockFragment = BuyStockFragment.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("stockId", stockId);
        bundle.putString("name", name);
        buyStockFragment.setArguments(bundle);

        buyStockFragment.show(getSupportFragmentManager(),"buyStockFragment");
    }


    private void requestStock(String search, int count, int itemId){
        RetrofitService.getInstance()
                .getApi()
                .getStock(search, count, itemId)
                .enqueue(new Callback<PageOfStocks>() {
                    @Override
                    public void onResponse(Call<PageOfStocks> call, Response<PageOfStocks> response) {
                        int statusCode = response.code();
                        PageOfStocks body = response.body();
                        if(statusCode == 200 && body != null) {
                            nextItemId = body.getNextItemId();
                            setList(body.getItems());
                            isLoaded = false;
                        } else {
                            Utils.showMessage(String.valueOf(statusCode), getApplicationContext());
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<PageOfStocks> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        Utils.showMessage(t.toString(), getApplicationContext());
                    }
                });
    }
}