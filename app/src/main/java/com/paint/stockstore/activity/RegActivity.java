package com.paint.stockstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.paint.stockstore.R;


public class RegActivity extends AppCompatActivity {

    EditText textLogin, textPassword;
    Button buttonReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        init();
    }

    void init(){
        textLogin = (EditText) findViewById(R.id.textLogin);
        textPassword = (EditText) findViewById(R.id.textPassword);
        buttonReg = (Button) findViewById(R.id.buttonAuth);

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
            onLoginFailed();
            return;
        }

        buttonReg.setEnabled(false);

        String email = textLogin.getText().toString();
        String password = textPassword.getText().toString();

        //retrologic
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "signup failed", Toast.LENGTH_LONG).show();

        buttonReg.setEnabled(true);
    }


    public boolean validate() {
        boolean valid = true;

        String login = textLogin.getText().toString();
        String password = textPassword.getText().toString();

        if (login.isEmpty() || (login.trim().length() < 1)) {
            textLogin.setError("enter a valid login");
            valid = false;
        } else {
            textLogin.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            textPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            textPassword.setError(null);
        }

        return valid;
    }
}