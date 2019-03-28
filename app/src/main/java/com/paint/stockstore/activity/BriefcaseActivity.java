package com.paint.stockstore.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.paint.stockstore.BuildConfig;
import com.paint.stockstore.R;
import com.paint.stockstore.model.AccountInfo;
import com.paint.stockstore.service.RetrofitService;
import com.paint.stockstore.service.TokenStore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import io.reactivex.Observable;

public class BriefcaseActivity extends AppCompatActivity {

    EditText textLogin;
    EditText textPassword;
    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_briefcase);

        init();
    }

    void init() {
        textLogin = (EditText) findViewById(R.id.textLogin);
        textPassword = (EditText) findViewById(R.id.textPassword);
        buttonLogin = (Button) findViewById(R.id.buttonAuth);

        TokenStore tokenStore = new TokenStore(this);
        String token = tokenStore.getStore("accessToken");
    }


    private void getInfo(String token){
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

                        } else if (statusCode == 403) {

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

    ///api/account/info





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
