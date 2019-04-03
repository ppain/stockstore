package com.paint.stockstore.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.paint.stockstore.R;
import com.paint.stockstore.adapter.BriefcaseAdapter;
import com.paint.stockstore.model.AccessToken;
import com.paint.stockstore.model.AccountInfo;
import com.paint.stockstore.model.TestModel;
import com.paint.stockstore.service.RetrofitService;
import com.paint.stockstore.service.TokenStoreHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import io.reactivex.Observable;

public class BriefcaseActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private BriefcaseAdapter adapterBriefcase;
    private List<TestModel> data;

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

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeMain);
        //swipeRefreshLayout.setColorSchemeResources( R.color.colorAccent, R.color.colorPrimaryDark,R.color.colorPrimary);
        swipeRefreshLayout.setColorSchemeResources( R.color.colorAccent);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapterBriefcase.clear();
                getInfo();
            }
        });


        RecyclerView rvBriefcase = (RecyclerView) findViewById(R.id.listMain);

//        data = TestModel.generateData();
        adapterBriefcase = new BriefcaseAdapter(TestModel.generateData());

        rvBriefcase.setAdapter(adapterBriefcase);
        rvBriefcase.setLayoutManager(new LinearLayoutManager(this));


        getInfo();
    }

    private void getInfo(){
        String token = TokenStoreHelper.getStore(TokenStoreHelper.ACCESS_TOKEN);
        requestInfo(token);
        adapterBriefcase.addAll(TestModel.generateData());
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getNewToken(){
        String token = TokenStoreHelper.getStore(TokenStoreHelper.ACCESS_TOKEN);
        String refreshToken = TokenStoreHelper.getStore(TokenStoreHelper.REFRESH_TOKEN);
        requestToken(token, refreshToken);
    }


    private void requestToken(String token, String refreshToken){

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
                            AccessToken token = response.body();
                            TokenStoreHelper.setStore(TokenStoreHelper.ACCESS_TOKEN, token.getAccessToken());
                            TokenStoreHelper.setStore(TokenStoreHelper.REFRESH_TOKEN, token.getRefreshToken());

                            getInfo();
                        } else {
                            Log.d("testing", "refreshAccessToken/onResponse/something wrong");
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        Log.d("testing", "refreshAccessToken/onFailure/all wrong");
                    }
                });
    }


    private void requestInfo(String token){

        RetrofitService.getInstance()
                .getApi()
                .getAccountInfo(token)
                .enqueue(new Callback<AccountInfo>() {
                    @Override
                    public void onResponse(Call<AccountInfo> call, Response<AccountInfo> response) {
                        Log.d("testing", "getAccountInfo/onResponse");
                        int statusCode = response.code();
                        if(statusCode == 200) {
                            Log.d("testing", "getAccountInfo/onResponse/response 200");
                            AccountInfo accountInfo = response.body();

                            swipeRefreshLayout.setRefreshing(false);

                        } else if (statusCode == 403) {
                            getNewToken();
                        }
                        else {
                            Log.d("testing", "getAccountInfo/onResponse/something wrong");
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountInfo> call, Throwable t) {
                        Log.d("testing", "getAccountInfo/onFailure/all wrong");
                    }
                });
    }




/*
    public void trimAndFilterText(){
        RxTextView
                .textChanges(searchEditText)
                .map(text -> text.toString().trim())
                .filter(text -> text.length() != 0);
    }

    public void loginAndPassword(){
        Observable
                .combineLatest(
                        RxTextView.textChanges(loginEditText),
                        RxTextView.textChanges(passwordEditText),
                        (login, password) -> login.length() > 0 && password.length() > 0)
                .subscribe(loginButton::setEnabled);
    }
    */

}
