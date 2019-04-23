package com.paint.stockstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.paint.stockstore.R;
import com.paint.stockstore.adapter.BriefcaseAdapter;
import com.paint.stockstore.adapter.BuyAdapterClickListener;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BriefcaseActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private BriefcaseAdapter adapterBriefcase;
    private CollapsingToolbarLayout collapsingToolbar;
    private List<InfoStock> stock = new ArrayList<>();
    private TextView tvName;

//    private DatabaseHelper database;
    private BriefcaseDAO briefcaseDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_briefcase);

////        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        getSupportActionBar().setTitle(" UserName");
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setIcon(R.drawable.ic_default_18dp);

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
        //TODO replace unicode        https://unicode-table.com/ru/20BD/
        collapsingToolbar.setTitle(getString(R.string.default_balance) + getString(R.string.rub));

        tvName = (TextView) findViewById(R.id.tv_name_item);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_briefcase);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        RecyclerView rvBriefcase = (RecyclerView) findViewById(R.id.list_briefcase);
        rvBriefcase.setLayoutManager(new LinearLayoutManager(this));
        adapterBriefcase = new BriefcaseAdapter(stock, BriefcaseActivity.this, new SellBuyAdapterClickListener() {
            @Override
            public void onItemClicked(String stockId, String name, int count) {
                if(Utils.isNetworkAvailable(getApplicationContext())) {
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
            if(Utils.isNetworkAvailable(getApplicationContext())) {
                startActivity(new Intent(BriefcaseActivity.this, HistoryActivity.class));
            }
        });


        findViewById(R.id.button_account).setOnClickListener((v) -> {
            if(Utils.isNetworkAvailable(getApplicationContext())) {
                startActivity(new Intent(BriefcaseActivity.this, LoginActivity.class));
            }
        });


        findViewById(R.id.fab_stock).setOnClickListener((v) -> {
            if(Utils.isNetworkAvailable(getApplicationContext())) {
                startActivity(new Intent(BriefcaseActivity.this, StockActivity.class));
            }
        });

        briefcaseDao = App.getInstance().getDatabase().briefcaseDao();

        getInfo();
    }

    public void forcedUpdate(){
        if(Utils.isNetworkAvailable(getApplicationContext())) {
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

        sellStockFragment.show(getSupportFragmentManager(),"sellStockFragment");
    }


    private void setInfo(String name, float balance){
        collapsingToolbar.setTitle(String.valueOf(Math.round(balance * 100f) / 100f) + getString(R.string.rub));
        tvName.setText(name);
    }


    private void setList(List<InfoStock> listInfoStock){
//        adapterBriefcase = new BriefcaseAdapter(accountInfo.getStocks());

        adapterBriefcase.swapList(listInfoStock);
        swipeRefreshLayout.setRefreshing(false);
    }


//TODO test cash
    private void getInfo(){
//        accountInfo = AccountInfo.generateData();
        if(isRelevanceCache() && !briefcaseDao.getStock().isEmpty()){
            setInfo(briefcaseDao.getAccount().getName(), briefcaseDao.getAccount().getBalance());
            setList(briefcaseDao.getStock());

        } else if (Utils.isNetworkAvailable(getApplicationContext())) {
            requestInfo(Utils.getToken());

        } else if (!briefcaseDao.getStock().isEmpty()){
            setInfo(briefcaseDao.getAccount().getName(), briefcaseDao.getAccount().getBalance());
            setList(briefcaseDao.getStock());
        } else {
            Utils.showMessage("No data", getApplicationContext());
        }
//        setInfo();
    }


//    private void showMessage(@NonNull String text){
//        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
//    }


    private boolean isRelevanceCache(){
        Long minToLife = TimeUnit.MINUTES.toMillis(5L);
        Long diff = System.currentTimeMillis() - Utils.getTime();
        return (diff - minToLife < 0);
    }


    private void getNewToken(){
        requestToken(Utils.getToken(), new RefreshToken(Utils.getRefreshToken()));
    }

    private void setData(AccountInfo accountInfo){
        setInfo(accountInfo.getName(), accountInfo.getBalance());
        setList(accountInfo.getStock());
        briefcaseDao.clearStock();
        briefcaseDao.insertStock(accountInfo.getStock());
        briefcaseDao.clearAccount();
        briefcaseDao.insertAccount(new AccountModel(accountInfo.getName(), accountInfo.getBalance()));
        Utils.setTime();
    }


    private void requestInfo(String token){

        RetrofitService.getInstance()
                .getApi()
                .getAccountInfo(token)
                .enqueue(new Callback<AccountInfo>() {
                    @Override
                    public void onResponse(Call<AccountInfo> call, Response<AccountInfo> response) {
                        Log.d("testing", "getAccountInfo/onResponse");
                        swipeRefreshLayout.setRefreshing(false);
                        int statusCode = response.code();
                        if(statusCode == 200 && response.body() != null) {
                            Log.d("testing", "getAccountInfo/onResponse/response 200");

                            setData(response.body());
                        } else if (statusCode == 403) {
                            getNewToken();
                        } else {
                            Log.d("testing", "getAccountInfo/onResponse/something wrong");
//                            showMessage(String.valueOf(statusCode));
                            Utils.showMessage(String.valueOf(statusCode), getApplicationContext());
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountInfo> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("testing", "getAccountInfo/onFailure/all wrong");
//                        showMessage(t.toString());
                        Utils.showMessage(t.toString(), getApplicationContext());
                    }
                });
    }


    private void requestToken(String token, RefreshToken refreshToken){

        RetrofitService.getInstance()
                .getApi()
                .refreshAccessToken(token, refreshToken)
                .enqueue(new Callback<AccessToken>() {

                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        Log.d("testing", "refreshAccessToken/onResponse");
                        int statusCode = response.code();
                        if(statusCode == 200 && response.body() != null) {
                            Log.d("testing", "refreshAccessToken/onResponse/response 200");
                            Utils.setToken(response.body());

                            getInfo();
                        } else if (statusCode == 401) {
                            startActivity(new Intent(BriefcaseActivity.this, LoginActivity.class));
                        } else {
                            Log.d("testing", "refreshAccessToken/onResponse/something wrong");
                            Utils.showMessage(String.valueOf(statusCode), getApplicationContext());
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        Log.d("testing", "refreshAccessToken/onFailure/all wrong");
                        Utils.showMessage(t.toString(), getApplicationContext());
                    }
                });
    }
}
