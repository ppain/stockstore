package com.paint.stockstore.activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.paint.stockstore.R;
import com.paint.stockstore.adapter.StockAdapter;
import com.paint.stockstore.fragment.BuyStockFragment;
import com.paint.stockstore.model.AccessToken;
import com.paint.stockstore.model.InfoStock;
import com.paint.stockstore.model.PageOfStocks;
import com.paint.stockstore.model.RefreshToken;
import com.paint.stockstore.service.RetrofitService;
import com.paint.stockstore.service.RxSearchObservable;
import com.paint.stockstore.service.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
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
    private int nextItemId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        initUI();
        initAdapter();
        initClearRequest("");
    }


    void initUI() {
        Toolbar toolbar = findViewById(R.id.toolbar_stock);
        setSupportActionBar(toolbar);

        rvStocks = (RecyclerView) findViewById(R.id.list_stock);
        llManager = new LinearLayoutManager(this);
        rvStocks.setLayoutManager(llManager);
        rvStocks.setHasFixedSize(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_stock);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            isLoading(true);
            initClearRequest("");
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stock, menu);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

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
                .filter(text -> !(text.length() < 2))
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::initClearRequest,
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
        if (Utils.isNetworkAvailable(getApplicationContext())) {
            isLoading(true);
            nextItemId = Utils.ITEM_ID_DEFAULT;
            adapterStock.clearList();
            requestStock(query, nextItemId);
        } else {
            isLoading(false);
        }
    }


    void initAdapter() {
        adapterStock = new StockAdapter(new ArrayList<>(), StockActivity.this, this::showBuyStockFragment);

        rvStocks.setAdapter(adapterStock);

        rvStocks.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int sizeList = adapterStock.getItemCount();
                    int lastVisibleItemPosition = llManager.findLastVisibleItemPosition();
                    Log.d("testing", "lastVisibleItemPosition: " + lastVisibleItemPosition
                            + ", adapterStock.getItemCount(): " + sizeList);
                    if (!isLoaded && (lastVisibleItemPosition > sizeList - Utils.HIDE_ITEM
                            && Utils.isNetworkAvailable(getApplicationContext()))) {
                        isLoaded = true;
                        isLoading(true);
                        requestStock("", nextItemId);
                    }
                }
            }
        });
    }


    private void setList(List<InfoStock> list) {
        adapterStock.updateList(list);
        isLoading(false);
    }


    public void showBuyStockFragment(String stockId, String name) {
        BuyStockFragment buyStockFragment = BuyStockFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.txt_stock_id), stockId);
        bundle.putString(getString(R.string.txt_name), name);
        buyStockFragment.setArguments(bundle);
        buyStockFragment.show(getSupportFragmentManager(), getResources().getString(R.string.tag_buy_fragment));
    }


    private void checkOnEndList(PageOfStocks pageOfStocks) {
        nextItemId = pageOfStocks.getNextItemId();
        if (nextItemId != 0) {
            isLoaded = false;
        }
        setList(pageOfStocks.getItems());
    }


    private void requestStock(String search, int itemId) {
        RetrofitService.getInstance()
                .getApi()
                .getStock(search, Utils.COUNT_DEFAULT, itemId)
                .enqueue(new Callback<PageOfStocks>() {
                    @Override
                    public void onResponse(@NonNull Call<PageOfStocks> call, @NonNull Response<PageOfStocks> response) {
                        int statusCode = response.code();
                        isLoading(false);
                        PageOfStocks body = response.body();
                        if (statusCode == 200 && body != null) {
                            checkOnEndList(new PageOfStocks(body));
                        } else if (statusCode == 403) {
                            requestToken(Utils.getToken(), new RefreshToken(Utils.getRefreshToken()));
                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                                Utils.showMessage(jObjError.getString("message"), getApplicationContext());
                            } catch (Exception e) {
                                Utils.showMessage(e.toString(), getApplicationContext());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PageOfStocks> call, @NonNull Throwable t) {
                        isLoading(false);
                        Utils.showMessage(t.toString(), getApplicationContext());
                    }
                });
    }

    private void requestToken(String token, RefreshToken refreshToken) {
        RetrofitService.getInstance()
                .getApi()
                .refreshAccessToken(token, refreshToken)
                .enqueue(new Callback<AccessToken>() {

                    @Override
                    public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {
                        int statusCode = response.code();
                        if (statusCode == 200 && response.body() != null) {
                            Utils.setToken(response.body());
                            initClearRequest("");
                        } else if (statusCode == 401) {
                            renewRefreshToken();
                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                                Utils.showMessage(jObjError.getString("message"), getApplicationContext());
                            } catch (Exception e) {
                                Utils.showMessage(e.toString(), getApplicationContext());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {
                        Utils.showMessage(t.toString(), getApplicationContext());
                    }
                });
    }

    private void renewRefreshToken() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void isLoading(boolean state) {
        swipeRefreshLayout.setRefreshing(state);
        if (state) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}