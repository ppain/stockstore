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
import android.widget.TextView;
import android.widget.Toast;

import com.paint.stockstore.R;
import com.paint.stockstore.adapter.BriefcaseAdapter;
import com.paint.stockstore.data.BriefcaseDAO;
import com.paint.stockstore.model.AccessToken;
import com.paint.stockstore.model.AccountInfo;
import com.paint.stockstore.model.InfoStock;
import com.paint.stockstore.model.RefreshToken;
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
        adapterBriefcase = new BriefcaseAdapter(stock);
        rvBriefcase.setAdapter(adapterBriefcase);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Utils.isNetworkAvailable(getApplicationContext())){
                    requestInfo(Utils.getToken());
                } else {
                    showMessage("Отсутсвует подключение к интернету");
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        findViewById(R.id.button_history).setOnClickListener(v -> startActivity(new Intent(
                BriefcaseActivity.this, HistoryActivity.class
        )));

        findViewById(R.id.fab_stock).setOnClickListener(v -> startActivity(new Intent(
                BriefcaseActivity.this, StockActivity.class
        )));

        briefcaseDao = App.getInstance().getDatabase().briefcaseDao();

        getInfo();
    }


    private void setInfo(AccountInfo accountInfo){
        collapsingToolbar.setTitle(String.valueOf(accountInfo.getBalance()) + getString(R.string.rub));
        tvName.setText(accountInfo.getName());
    }


    private void setList(List<InfoStock> listInfoStock){
//        adapterBriefcase = new BriefcaseAdapter(accountInfo.getStocks());

        adapterBriefcase.swapList(listInfoStock);
        swipeRefreshLayout.setRefreshing(false);

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                adapterBriefcase.swapList(accountInfo.getStock());
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        }, 1000);
    }


    private void getInfo(){
//        accountInfo = AccountInfo.generateData();
        if(isRelevanceCache() && briefcaseDao.getData().size() > 0){
            setList(briefcaseDao.getData());

        } else if (Utils.isNetworkAvailable(getApplicationContext())) {
            requestInfo(Utils.getToken());

        } else {
            showMessage("Отсутсвует подключение к интернету");
            swipeRefreshLayout.setRefreshing(false);
        }
//        setInfo();
    }


    private void showMessage(@NonNull String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }


    private boolean isRelevanceCache(){
        Long minToLife = TimeUnit.MINUTES.toMillis(5L);
        Long diff = System.currentTimeMillis() - Utils.getTime();
        return (diff - minToLife < 0);
    }


    private void getNewToken(){
        requestToken(Utils.getToken(), new RefreshToken(Utils.getRefreshToken()));
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
                        if(statusCode == 200) {
                            Log.d("testing", "getAccountInfo/onResponse/response 200");

                            AccountInfo accountInfo = response.body();
                            setInfo(response.body());
                            setList(accountInfo.getStock());
                            briefcaseDao.clear();
                            briefcaseDao.insert(accountInfo.getStock());
                            Utils.setTime();
                        } else if (statusCode == 403) {
                            getNewToken();
                        } else {
                            Log.d("testing", "getAccountInfo/onResponse/something wrong");
                            showMessage(String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountInfo> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("testing", "getAccountInfo/onFailure/all wrong");
                        showMessage(t.toString());
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
                        if(statusCode == 200) {
                            Log.d("testing", "refreshAccessToken/onResponse/response 200");
//                            AccessToken token = response.body();
//                            Utils.setToken(token.getAccessToken(), token.getRefreshToken());
                            Utils.setToken(response.body());

                            getInfo();
                        } else if (statusCode == 401) {
                            startActivity(new Intent(BriefcaseActivity.this, LoginActivity.class));
                        } else {
                            Log.d("testing", "refreshAccessToken/onResponse/something wrong");
                            showMessage(String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        Log.d("testing", "refreshAccessToken/onFailure/all wrong");
                        showMessage(t.toString());
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this,"test",Toast.LENGTH_LONG).show();
    }
}
