package com.paint.stockstore.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.paint.stockstore.R;
import com.paint.stockstore.adapter.BriefcaseAdapter;
import com.paint.stockstore.data.BriefcaseDAO;
import com.paint.stockstore.fragment.SellStockFragment;
import com.paint.stockstore.model.AccessToken;
import com.paint.stockstore.model.AccountInfo;
import com.paint.stockstore.model.InfoStock;
import com.paint.stockstore.model.RefreshToken;
import com.paint.stockstore.model.AccountModel;
import com.paint.stockstore.service.App;
import com.paint.stockstore.service.RetrofitService;
import com.paint.stockstore.service.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.Maybe;
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
    private TextView tvName;
    private TextView tvEmptyList;
    private BriefcaseDAO briefcaseDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_briefcase);
        init();
    }

    protected void onResume() {
        super.onResume();
        if (Utils.isFlagUpdate()) {
            Utils.setFlagUpdate(false);
            forcedUpdate();
        }
    }


    void init() {
        ImageView buttonHistory = findViewById(R.id.button_history);
        ImageView buttonAccount = findViewById(R.id.button_account);
        tvEmptyList = (TextView) findViewById(R.id.tv_empty_list);
        tvName = (TextView) findViewById(R.id.tv_name_item);
        FloatingActionButton buttonStock = findViewById(R.id.fab_stock);
        buttonHistory.setOnClickListener((v) -> {
            if (Utils.isNetworkAvailable(getApplicationContext())) {
                startActivity(new Intent(this, HistoryActivity.class));
            }
        });
        buttonAccount.setOnClickListener((v) -> {
            if (Utils.isNetworkAvailable(getApplicationContext())) {
                startActivity(new Intent(this, LoginActivity.class));
            }
        });
        buttonStock.setOnClickListener((v) -> {
            if (Utils.isNetworkAvailable(getApplicationContext())) {
                startActivity(new Intent(this, StockActivity.class));
            }
        });

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_briefcase));
        collapsingToolbar = findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbar.setTitle(getString(R.string.default_balance) + getString(R.string.space_bar) + getString(R.string.rub));

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_briefcase);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this::forcedUpdate);

        RecyclerView rvBriefcase = (RecyclerView) findViewById(R.id.list_briefcase);
        rvBriefcase.setLayoutManager(new LinearLayoutManager(this));
        adapterBriefcase = new BriefcaseAdapter(new ArrayList<>(), BriefcaseActivity.this, (stockId, name, count) -> {
            if (Utils.isNetworkAvailable(getApplicationContext())) {
                showSellStockFragment(stockId, name, count);
            }
        });
        rvBriefcase.setAdapter(adapterBriefcase);

        briefcaseDao = App.getInstance().getDatabase().briefcaseDao();

        getInfo();
    }

    public void forcedUpdate() {
        if (Utils.isNetworkAvailable(getApplicationContext())) {
            isLoading(true);
            requestInfo(Utils.getToken());
        } else {
            isLoading(false);
        }
    }


    public void showSellStockFragment(String stockId, String name, int count) {
        SellStockFragment sellStockFragment = SellStockFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.txt_stock_id), stockId);
        bundle.putString(getString(R.string.txt_name), name);
        bundle.putInt(getString(R.string.txt_count), count);
        sellStockFragment.setArguments(bundle);
        sellStockFragment.show(getSupportFragmentManager(), getResources().getString(R.string.tag_sell_fragment));
    }


    private void setInfo(String name, float balance) {
        collapsingToolbar.setTitle(balance + getString(R.string.space_bar) + getString(R.string.rub));
        tvName.setText(name);
    }


    private void setList(List<InfoStock> listInfoStock) {
        hideEmptyList(listInfoStock.isEmpty());
        adapterBriefcase.swapList(listInfoStock);
        isLoading(false);
    }


    private void hideEmptyList(boolean empty) {
        if (empty) {
            swipeRefreshLayout.setVisibility(View.INVISIBLE);
            tvEmptyList.setVisibility(View.VISIBLE);
        } else {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            tvEmptyList.setVisibility(View.INVISIBLE);
        }
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
        Maybe<List<InfoStock>> stock = briefcaseDao.getRxStock();
        stock.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                            if (item.isEmpty() && Utils.isNetworkAvailable(getApplicationContext())) {
                                getFromNetwork();
                            } else {
                                setList(item);
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
                .subscribe(Utils::setTime,
                        throwable -> Utils.showMessage(throwable.toString(), getApplicationContext())
                );
    }


    private void requestInfo(String token) {
        RetrofitService.getInstance()
                .getApi()
                .getAccountInfo(token)
                .enqueue(new Callback<AccountInfo>() {
                    @Override
                    public void onResponse(@NonNull Call<AccountInfo> call, @NonNull Response<AccountInfo> response) {
                        isLoading(false);
                        int statusCode = response.code();
                        AccountInfo body = response.body();
                        if (statusCode == 200 && body != null) {
                            setData(new AccountInfo(body));
                        } else if (statusCode == 403) {
                            getNewToken();
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
                    public void onFailure(@NonNull Call<AccountInfo> call, @NonNull Throwable t) {
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
                            getInfo();
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