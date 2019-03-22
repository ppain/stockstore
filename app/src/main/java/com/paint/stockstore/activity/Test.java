package com.paint.stockstore.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//import com.jakewharton.rxbinding2.widget.RxTextView;
//import com.paint.stockstore.R;
//
//import io.reactivex.Observable;
//import io.reactivex.functions.BiFunction;
//import io.reactivex.functions.Function;
//import io.reactivex.observers.DisposableObserver;

public class Test extends AppCompatActivity {
//    EditText et_name, et_password;
//    TextView tv_status;
//    Button btn_login;
//
//    Observable<Boolean> observable;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
////        AppPrefs.init(this);
////        AppPrefs.setUserName("ayusch");
////        AppPrefs.setPassword("pass12");
//
//        init();
//
//    }
//
//    private void init() {
//        et_name = findViewById(R.id.et_name);
//        et_password = findViewById(R.id.et_password);
//        btn_login = findViewById(R.id.btn_login);
//        tv_status = findViewById(R.id.tv_status);
//
//        Observable<String> nameObservable = RxTextView.textChanges(et_name).skip(0).map(new Function<CharSequence, String>() {
//            @Override
//            public String apply(CharSequence charSequence) throws Exception {
//                return charSequence.toString();
//            }
//        });
//        Observable<String> passwordObservable = RxTextView.textChanges(et_password).skip(1).map(new Function<CharSequence, String>() {
//            @Override
//            public String apply(CharSequence charSequence) throws Exception {
//                return charSequence.toString();
//            }
//        });
//
//        observable = Observable.combineLatest(nameObservable, passwordObservable, new BiFunction<String, String, Boolean>() {
//            @Override
//            public Boolean apply(String s, String s2) throws Exception {
//                return isValidForm(s, s2);
//            }
//        });
//
//        observable.subscribe(new DisposableObserver<Boolean>() {
//            @Override
//            public void onNext(Boolean aBoolean) {
//                updateButton(aBoolean);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
//    }
//
//    public void updateButton(boolean valid) {
//        if (valid)
//            btn_login.setEnabled(true);
//    }
//
//    public boolean isValidForm(String name, String password) {
//        boolean validName = !name.isEmpty();
//
//        if (!validName) {
//            et_name.setError("Please enter valid name");
//        }
//
//        boolean validPass = !password.isEmpty() && password.equals("pass12");
//        if (!validPass) {
//            et_password.setError("Incorrect password");
//        }
//        return validName && validPass;
//    }


}

//    implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
//            implementation 'io.reactivex.rxjava2:rxjava:2.2.7'
//      implementation 'com.jakewharton.rxbinding2:rxbinding-appcompat-v7:2.1.1'