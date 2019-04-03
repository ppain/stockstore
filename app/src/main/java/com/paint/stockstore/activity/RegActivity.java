package com.paint.stockstore.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.paint.stockstore.BuildConfig;
import com.paint.stockstore.R;
import com.paint.stockstore.model.AccessToken;
import com.paint.stockstore.model.User;
import com.paint.stockstore.service.RetrofitService;
import com.paint.stockstore.service.TokenStoreHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegActivity extends AppCompatActivity {

    private static final String PATTERN_LOGIN = "^[a-z|A-Z|\\d|_]{3,100}$";

    EditText textLogin, textPassword;
    Button buttonReg;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        init();
    }

    void init(){
        textLogin = (EditText) findViewById(R.id.textLogin);
        textLogin.setText("r2d2");
        textPassword = (EditText) findViewById(R.id.textPassword);
        textPassword.setText("12345678");

        buttonReg = (Button) findViewById(R.id.buttonAuth);
        progressBar = findViewById(R.id.progressBar);

        showProgress(false);

        buttonReg.setText(R.string.signup);
        buttonReg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    public void signup() {

        if (!validate()) {
            onRegFailed();
            return;
        }

//        buttonReg.setEnabled(false);

        User user = new User(textLogin.getText().toString(), textPassword.getText().toString());

//        String login = textLogin.getText().toString();
//        String password = textPassword.getText().toString();

        RetrofitService.getInstance()
                .getApi()
//                .postReg(login, password)
                .postReg(user)
                .enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        Log.d("testing", "AccessToken/onResponse");
                        int statusCode = response.code();
                        if(statusCode == 200) {
                            Log.d("testing", "AccessToken/onResponse/response 200");
                            AccessToken token = response.body();
                            TokenStoreHelper.setStore(TokenStoreHelper.ACCESS_TOKEN, token.getAccessToken());
                            TokenStoreHelper.setStore(TokenStoreHelper.REFRESH_TOKEN, token.getRefreshToken());

                            onSuccessfulAuth();
                        } else {
                            Log.d("testing", "AccessToken/onResponse/something wrong");
                            showError(response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        showError(t.toString());
                    }
                });
    }

    public void onRegFailed() {
        Toast.makeText(getBaseContext(), "Ошибка регистрации", Toast.LENGTH_LONG).show();

//        buttonReg.setEnabled(true);
    }


    //TODO create new class
    public boolean validate() {
        boolean valid = true;

        String login = textLogin.getText().toString();
        String password = textPassword.getText().toString();

        if (login.matches(PATTERN_LOGIN)) {
            textLogin.setError(null);
        } else {
            textLogin.setError("Корректный логин - " + PATTERN_LOGIN);
            valid = false;
        }

        if (password.isEmpty() || password.trim().length() < 1 || password.length() < 8 || password.length() > 64) {
            textPassword.setError("от 8 до 64 символов");
            valid = false;
        } else {
            textPassword.setError(null);
        }

        return valid;
    }

    private void showError(@NonNull String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    private void onSuccessfulAuth() {
        Intent intent = new Intent(RegActivity.this, BriefcaseActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void showProgress(boolean visible) {
        progressBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }
}