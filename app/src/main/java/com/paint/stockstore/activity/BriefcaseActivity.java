package com.paint.stockstore.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.paint.stockstore.R;

import io.reactivex.Observable;

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
