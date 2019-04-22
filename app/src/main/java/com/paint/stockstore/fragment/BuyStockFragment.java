package com.paint.stockstore.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paint.stockstore.R;
import com.paint.stockstore.model.Transaction;
import com.paint.stockstore.service.RetrofitService;
import com.paint.stockstore.service.Utils;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyStockFragment extends BottomSheetDialogFragment {

    private final String PATTERN_INPUT = "\\d+";

    private EditText textAmount;

    public static BuyStockFragment newInstance() {
        return new BuyStockFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bs_dialog, container, false);

        Bundle bundle = getArguments();
        TextView tvDialog = (TextView) view.findViewById(R.id.tv_dialog);
        String stockId = bundle.getString("stockId");
        String name = bundle.getString("name");
        tvDialog.setText(name);

        textAmount = (EditText) view.findViewById(R.id.textAmount);

//        RxTextView.textChanges(textCount)
//                .map(new Function<CharSequence, String>() {
//                    @Override
//                    public String apply(CharSequence charSequence) throws Exception {
//                        return charSequence.toString();
//                    }
//                })
//                .filter(value -> value.matches(PATTERN_INPUT))
//                .map(new Function<String, Integer>() {
//                    @Override
//                    public Integer apply(String value) throws Exception {
//                        return Integer.parseInt(value);
//                    }
//                })
//                .filter(value -> value > 0)
//                .subscribe(value -> {
//                    setClickable(true);
//                    showMessage(String.valueOf(value));
//                });


//                        setClickable(true));

        Button buttonBuy = (Button) view.findViewById(R.id.buttonDialog);
        buttonBuy.setText(R.string.buy);

        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = textAmount.getText().toString();

                if (validate(amount)){
                    Transaction transaction = new Transaction(stockId, amount);
                    requestBuy(transaction, Utils.getToken());
                } else {
                    showMessage("Минимальная покупка 1");
                }
            }
        });

        return view;
    }


    public boolean validate(String amount) {
        return amount.matches(PATTERN_INPUT) && Integer.parseInt(amount) > 0;
    }


    private void requestBuy(Transaction transaction, String token){
        RetrofitService.getInstance()
                .getApi()
                .buyStock(token, transaction)
                .enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                        Log.d("testing", "buyStock/onResponse");
                        int statusCode = response.code();
                        if(statusCode == 200) {
                            showMessage("Оплачено");
                            onDestroyView();
                            Log.d("testing", "buyStock/onResponse/response 200");
                        } else {
                            showMessage(String.valueOf(statusCode));
                            Log.d("testing", "buyStock/onResponse/something wrong");
                        }
                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {
                        Log.d("testing", "buyStock/onFailure/all wrong");
                        showMessage(t.toString());
                    }
                });
    }

    private void showMessage(@NonNull String text){
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

}