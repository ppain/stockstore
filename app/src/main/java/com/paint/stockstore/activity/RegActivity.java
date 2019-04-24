package com.paint.stockstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.paint.stockstore.R;
import com.paint.stockstore.model.AccessToken;
import com.paint.stockstore.model.User;
import com.paint.stockstore.service.Utils;
import com.paint.stockstore.service.RetrofitService;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegActivity extends AppCompatActivity {

    EditText textLogin, textPassword;
    Button buttonReg;
    private ProgressBar progressBar;

    Observable<Boolean> observable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        init();
    }

    void init() {
        textLogin = (EditText) findViewById(R.id.textLogin);
        textPassword = (EditText) findViewById(R.id.textPassword);

        buttonReg = (Button) findViewById(R.id.buttonAuth);
        progressBar = findViewById(R.id.progressBar);

        showProgress(false);

        buttonReg.setText(R.string.signup);
        buttonReg.setOnClickListener((v) -> {
            if (Utils.isNetworkAvailable(getApplicationContext())) {
                signup();
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
                Utils.showMessage(e.toString(), getApplicationContext());
            }

            @Override
            public void onComplete() {
            }
        });
    }


    private void setEnableButtonLogin(boolean enable) {
        buttonReg.setEnabled(enable);
    }


    public void signup() {

        showProgress(true);

        User user = new User(textLogin.getText().toString(), textPassword.getText().toString());

        RetrofitService.getInstance()
                .getApi()
//                .postReg(login, password)
                .postReg(user)
                .enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        int statusCode = response.code();
                        if (statusCode == 200 && response.body() != null) {
                            Utils.setToken(response.body());

                            showProgress(false);
                            onSuccessfulAuth();
                        } else {
                            showProgress(false);
                            Utils.showMessage(response.errorBody().toString(), getApplicationContext());
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        showProgress(false);
                        Utils.showMessage(t.toString(), getApplicationContext());
                    }
                });
    }


    private boolean validate(String login, String password) {
        boolean valid = true;

        String setErrorLogin = Utils.validateLogin(login);
        String setErrorPassword = Utils.validatePassword(password);
        if (setErrorLogin.equals("")) {
            textLogin.setError(null);
        } else {
            textLogin.setError(setErrorLogin);
            valid = false;
        }

        if (setErrorPassword.equals("")) {
            textPassword.setError(null);
        } else {
            textPassword.setError(setErrorPassword);
            valid = false;
        }

        return valid;
    }


    private void onSuccessfulAuth() {
        startActivity(new Intent(RegActivity.this, BriefcaseActivity.class));
    }


    private void showProgress(boolean visible) {
        progressBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }
}