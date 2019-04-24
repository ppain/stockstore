package com.paint.stockstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.paint.stockstore.R;
import com.paint.stockstore.model.AccessToken;
import com.paint.stockstore.model.User;
import com.paint.stockstore.service.Utils;
import com.paint.stockstore.service.RetrofitService;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

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

    void init() {
        textLogin = (EditText) findViewById(R.id.textLogin);
//        TODO clear
        textLogin.setText("r8d8");
        textPassword = (EditText) findViewById(R.id.textPassword);
        textPassword.setText("12345678");
        buttonLogin = (Button) findViewById(R.id.buttonAuth);
        progressBar = findViewById(R.id.progressBar);

        showProgress(false);

        buttonLogin.setText(R.string.sigin);

        buttonLogin.setOnClickListener((v) -> {
            if (Utils.isNetworkAvailable(getApplicationContext())) {
                signin();
            }
        });

        TextView register = (TextView) findViewById(R.id.linkRegister);
        register.setMovementMethod(LinkMovementMethod.getInstance());
        register.setOnClickListener((v) -> {
            startActivity(new Intent(LoginActivity.this, RegActivity.class));
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
        buttonLogin.setEnabled(enable);
    }


    public void signin() {

        showProgress(true);

        User user = new User(textLogin.getText().toString(), textPassword.getText().toString());

        RetrofitService.getInstance()
                .getApi()
                .postLogin(user)
                .enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        showProgress(false);
                        int statusCode = response.code();
                        if (statusCode == 200 && response.body() != null) {
                            Utils.setToken(response.body());

                            onSuccessfulAuth();
                        } else if (statusCode == 401) {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Utils.showMessage(jObjError.getString("message"), getApplicationContext());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Utils.showMessage(response.errorBody().source().toString(), getApplicationContext());
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
        startActivity(new Intent(LoginActivity.this, BriefcaseActivity.class));
    }

    private void showProgress(boolean visible) {
        progressBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }
}