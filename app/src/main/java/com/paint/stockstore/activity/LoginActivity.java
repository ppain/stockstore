package com.paint.stockstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paint.stockstore.R;

public class LoginActivity extends AppCompatActivity {

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
        textPassword = (EditText) findViewById(R.id.textPassword);
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

        buttonLogin.setEnabled(false);

        String email = textLogin.getText().toString();
        String password = textPassword.getText().toString();

        // retrologic
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        buttonLogin.setEnabled(true);
    }


    public boolean validate() {
        boolean valid = true;

        String login = textLogin.getText().toString();
        String password = textPassword.getText().toString();

        if (login.isEmpty() || (login.trim().length() < 3)) {
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