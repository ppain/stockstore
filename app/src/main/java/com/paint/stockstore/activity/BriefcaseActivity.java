package com.paint.stockstore.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.paint.stockstore.R;
import com.paint.stockstore.adapter.BriefcaseAdapter;
import com.paint.stockstore.adapter.SellBuyAdapterClickListener;
import com.paint.stockstore.data.BriefcaseDAO;
import com.paint.stockstore.fragment.SellStockFragment;
import com.paint.stockstore.model.AccessToken;
import com.paint.stockstore.model.AccountInfo;
import com.paint.stockstore.model.InfoStock;
import com.paint.stockstore.model.RefreshToken;
import com.paint.stockstore.service.AccountModel;
import com.paint.stockstore.service.App;
import com.paint.stockstore.service.RetrofitService;
import com.paint.stockstore.service.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BriefcaseActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private BriefcaseAdapter adapterBriefcase;
    private CollapsingToolbarLayout collapsingToolbar;
    private List<InfoStock> stock = new ArrayList<>();
    private TextView tvName;
    private BriefcaseDAO briefcaseDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_briefcase);

        init();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        forcedUpdate();
    }


    void init() {

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_briefcase));
        collapsingToolbar = findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbar.setTitle(getString(R.string.default_balance) + getString(R.string.rub));

        tvName = (TextView) findViewById(R.id.tv_name_item);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_briefcase);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        RecyclerView rvBriefcase = (RecyclerView) findViewById(R.id.list_briefcase);
        rvBriefcase.setLayoutManager(new LinearLayoutManager(this));
        adapterBriefcase = new BriefcaseAdapter(stock, BriefcaseActivity.this, new SellBuyAdapterClickListener() {
            @Override
            public void onItemClicked(String stockId, String name, int count) {
                if (Utils.isNetworkAvailable(getApplicationContext())) {
                    showSellStockFragment(stockId, name, count);
                }
            }
        });
        rvBriefcase.setAdapter(adapterBriefcase);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                forcedUpdate();
            }
        });

        findViewById(R.id.button_history).setOnClickListener((v) -> {
            if (Utils.isNetworkAvailable(getApplicationContext())) {
                startActivity(new Intent(BriefcaseActivity.this, HistoryActivity.class));
            }
        });


        findViewById(R.id.button_account).setOnClickListener((v) -> {
            if (Utils.isNetworkAvailable(getApplicationContext())) {
                startActivity(new Intent(BriefcaseActivity.this, LoginActivity.class));
            }
        });


        findViewById(R.id.fab_stock).setOnClickListener((v) -> {
            if (Utils.isNetworkAvailable(getApplicationContext())) {
                startActivity(new Intent(BriefcaseActivity.this, StockActivity.class));
            }
        });

        briefcaseDao = App.getInstance().getDatabase().briefcaseDao();

        getInfo();
    }

    public void forcedUpdate() {
        if (Utils.isNetworkAvailable(getApplicationContext())) {
            requestInfo(Utils.getToken());
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    public void showSellStockFragment(String stockId, String name, int count) {

        SellStockFragment sellStockFragment = SellStockFragment.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("stockId", stockId);
        bundle.putString("name", name);
        bundle.putInt("count", count);
        sellStockFragment.setArguments(bundle);

        sellStockFragment.show(getSupportFragmentManager(), "sellStockFragment");
    }


    private void setInfo(String name, float balance) {
        collapsingToolbar.setTitle(String.valueOf(Math.round(balance * 100f) / 100f) + getString(R.string.rub));
        tvName.setText(name);
    }


    private void setList(List<InfoStock> listInfoStock) {
        adapterBriefcase.swapList(listInfoStock);
        swipeRefreshLayout.setRefreshing(false);
    }


    private void getInfo() {
        if (Utils.isRelevanceCache() || !Utils.isNetworkAvailable(getApplicationContext())) {
            getFromCache();
        } else {
            getFromNetwork();
        }
    }


    private void getFromNetwork() {
        if (Utils.isNetworkAvailable(getApplicationContext())) {
            requestInfo(Utils.getToken());
        }
    }


    @SuppressLint("CheckResult")
    private void getFromCache() {
        Single<List<InfoStock>> stock = briefcaseDao.getRxStock();
        stock.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                            if (!item.isEmpty()) {
                                setList(item);
                            } else {
                                getFromNetwork();
                            }
                        },
                        throwable -> Utils.showMessage(throwable.toString(), getApplicationContext())
                );

        Single<AccountModel> account = briefcaseDao.getRxAccount();
        account.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                            if (item.getName() != null) {
                                setInfo(item.getName(), item.getBalance());
                            } else {
                                getFromNetwork();
                            }
                        },
                        throwable -> Utils.showMessage(throwable.toString(), getApplicationContext())
                );
    }


    private void getNewToken() {
        requestToken(Utils.getToken(), new RefreshToken(Utils.getRefreshToken()));
    }

    @SuppressLint("CheckResult")
    private void setData(AccountInfo accountInfo) {
        setInfo(accountInfo.getName(), accountInfo.getBalance());
        setList(accountInfo.getStock());

        Completable.fromAction(() -> briefcaseDao.updateStock(accountInfo.getStock()
                , new AccountModel(accountInfo.getName(), accountInfo.getBalance())))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Utils.setTime(),
                        throwable -> Utils.showMessage(throwable.toString(), getApplicationContext())
                );
    }


    private void requestInfo(String token) {

        RetrofitService.getInstance()
                .getApi()
                .getAccountInfo(token)
                .enqueue(new Callback<AccountInfo>() {
                    @Override
                    public void onResponse(Call<AccountInfo> call, Response<AccountInfo> response) {
                        swipeRefreshLayout.setRefreshing(false);
                        int statusCode = response.code();
                        if (statusCode == 200 && response.body() != null) {

                            setData(response.body());
                        } else if (statusCode == 403) {
                            getNewToken();
                        } else {
                            Utils.showMessage(String.valueOf(statusCode), getApplicationContext());
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountInfo> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
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
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        int statusCode = response.code();
                        if (statusCode == 200 && response.body() != null) {
                            Utils.setToken(response.body());

                            getInfo();
                        } else if (statusCode == 401) {
                            startActivity(new Intent(BriefcaseActivity.this, LoginActivity.class));
                        } else {
                            Utils.showMessage(String.valueOf(statusCode), getApplicationContext());
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        Utils.showMessage(t.toString(), getApplicationContext());
                    }
                });
    }
}
