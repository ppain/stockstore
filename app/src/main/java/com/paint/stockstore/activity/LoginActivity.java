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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.paint.stockstore.BuildConfig;
import com.paint.stockstore.R;
import com.paint.stockstore.model.AccessToken;
import com.paint.stockstore.model.User;
import com.paint.stockstore.service.Utils;
import com.paint.stockstore.service.RetrofitService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

//    private final String PATTERN_LOGIN = getResources().getString(R.string.pattern);
    private final String PATTERN_LOGIN = "^[a-z|A-Z|\\d|_]{3,100}$";

    private EditText textLogin, textPassword;
    private Button buttonLogin;
    private ProgressBar progressBar;

    Observable<Boolean> observable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    void init(){
        textLogin = (EditText) findViewById(R.id.textLogin);
        textLogin.setText("r5d5");
        textPassword = (EditText) findViewById(R.id.textPassword);
        textPassword.setText("12345678");
        buttonLogin = (Button) findViewById(R.id.buttonAuth);
        progressBar = findViewById(R.id.progressBar);

        showProgress(false);

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


        Observable<String> nameObservable = RxTextView.textChanges(textLogin).skip(1).map(new Function<CharSequence, String>() {
            @Override
            public String apply(CharSequence charSequence) throws Exception {
                return charSequence.toString();
            }
        });
        Observable<String> passwordObservable = RxTextView.textChanges(textPassword).skip(1).map(new Function<CharSequence, String>() {
            @Override
            public String apply(CharSequence charSequence) throws Exception {
                return charSequence.toString();
            }
        });

        observable = Observable.combineLatest(nameObservable, passwordObservable, new BiFunction<String, String, Boolean>() {
            @Override
            public Boolean apply(String login, String password) throws Exception {
                return validate(login, password);
            }
        });

        observable.subscribe(new DisposableObserver<Boolean>() {
            @Override
            public void onNext(Boolean enable) {
                setEnableButtonLogin(enable);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }


    private void setEnableButtonLogin(boolean enable) {
        buttonLogin.setEnabled(enable);
    }


    public void signin() {

        showProgress(true);

        User user = new User(textLogin.getText().toString(), textPassword.getText().toString());

        //TODO cut methot in single file for reg
        RetrofitService.getInstance()
                .getApi()
                .postLogin(user)
                .enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        Log.d("testing", "AccessToken/onResponse");
                        showProgress(false);
                        int statusCode = response.code();
                        if(statusCode == 200) {
                            Log.d("testing", "AccessToken/onResponse/response 200");
                            Utils.setToken(response.body());

                            onSuccessfulAuth();
                        } else if (statusCode == 401) {
//                            showMessage(response.raw().message());

                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                showMessage(jObjError.getString("message"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.d("testing", "401");
                        } else {
                            Log.d("testing", "AccessToken/onResponse/something wrong");
                            showMessage(response.errorBody().source().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        showProgress(false);
                        Log.d("testing", "AccessToken/onFailure/all wrong");
                        showMessage(t.toString());
                    }
                });
    }

    private void showMessage(@NonNull String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }


    //TODO replace this code
    public boolean validate(String login, String password) {
        boolean valid = true;

        if (login.matches(PATTERN_LOGIN)) {
            textLogin.setError(null);
        } else {
            textLogin.setError("от 3 [a-z|A-Z|\\d]");
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


    private void onSuccessfulAuth() {
        Intent intent = new Intent(LoginActivity.this, BriefcaseActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void showProgress(boolean visible) {
        progressBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }


//TODO replace this
//    public void trimAndFilterText(){
//        RxTextView
//                .textChanges(searchEditText)
//                .map(text -> text.toString().trim())
//                .filter(text -> text.length() != 0);
//    }
//
//    public void loginAndPassword(){
//        Observable
//                .combineLatest(
//                        RxTextView.textChanges(loginEditText),
//                        RxTextView.textChanges(passwordEditText),
//                        (login, password) -> login.length() > 0 && password.length() > 0)
//                .subscribe(loginButton::setEnabled);
//    }

}