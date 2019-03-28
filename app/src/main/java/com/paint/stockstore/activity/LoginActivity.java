package com.paint.stockstore.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paint.stockstore.BuildConfig;
import com.paint.stockstore.R;
import com.paint.stockstore.model.AccessToken;
import com.paint.stockstore.model.User;
import com.paint.stockstore.service.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String PATTERN_LOGIN = "^[a-z|A-Z|\\d|_]{3,100}$";

    EditText textLogin, textPassword;
    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    void init(){
        textLogin = (EditText) findViewById(R.id.textLogin);
        textLogin.setText("r2d2");
        textPassword = (EditText) findViewById(R.id.textPassword);
        textPassword.setText("12345678");
        buttonLogin = (Button) findViewById(R.id.buttonAuth);

        buttonLogin.setText(R.string.sigin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signin();
            }
        });

        TextView register = (TextView)findViewById(R.id.linkRegister);
        register.setMovementMethod(LinkMovementMethod.getInstance());
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signin() {

        if (!validate()) {
            onLoginFailed();
            return;
        }

//        buttonLogin.setEnabled(false);

        User user = new User(textLogin.getText().toString(), textPassword.getText().toString());

        String login = textLogin.getText().toString();
        String password = textPassword.getText().toString();

        final SharedPreferences preferences = this.getSharedPreferences(
                BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);

        RetrofitService.getInstance()
                .getApi()
//                .postLogin(login, password)
                .postLogin(user)
                .enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        Log.d("testing", "AccessToken/onResponse");
                        int statusCode = response.code();
                        if(statusCode == 200) {
                            Log.d("testing", "AccessToken/onResponse/response 200");
                            AccessToken token = response.body();
                            //preferences.edit().putBoolean("oauth.loggedIn", true).apply();
                            preferences.edit().putString("accessToken", token.getAccessToken()).apply();
                            preferences.edit().putString("refreshToken", token.getRefreshToken()).apply();

                            onSuccessfulAuth();
                        } else {
                            Log.d("testing", "AccessToken/onResponse/something wrong");
                            showError(response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        Log.d("testing", "AccessToken/onFailure/all wrong");
                        showError(t.toString());
                    }
                });
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Ошибка регистрации", Toast.LENGTH_LONG).show();

//        buttonLogin.setEnabled(true);
    }


    //TODO replace this code
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
        Intent intent = new Intent(LoginActivity.this, BriefcaseActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}