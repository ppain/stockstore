package com.paint.stockstore.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.paint.stockstore.R;
import com.paint.stockstore.model.AccessToken;
import com.paint.stockstore.model.AccountInfo;
import com.paint.stockstore.service.RetrofitService;
import com.paint.stockstore.service.TokenStoreHelper;

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

//        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(" UserName");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_default_18dp);

        init();
    }

    void init() {
        textLogin = (EditText) findViewById(R.id.textLogin);
        textPassword = (EditText) findViewById(R.id.textPassword);
        buttonLogin = (Button) findViewById(R.id.buttonAuth);

        getInfo();
    }

    private void getInfo(){
        String token = TokenStoreHelper.getStore(TokenStoreHelper.ACCESS_TOKEN);
        reqestInfo(token);
    }

    private void getNewToken(){
        String token = TokenStoreHelper.getStore(TokenStoreHelper.ACCESS_TOKEN);
        String refreshToken = TokenStoreHelper.getStore(TokenStoreHelper.REFRESH_TOKEN);
        reqestToken(token, refreshToken);
    }


    private void reqestToken(String token, String refreshToken){

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


    private void reqestInfo(String token){

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

                        } else if (statusCode == 404) {
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
