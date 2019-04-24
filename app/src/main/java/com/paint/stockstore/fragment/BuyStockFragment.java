package com.paint.stockstore.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

        Button buttonBuy = (Button) view.findViewById(R.id.buttonDialog);
        buttonBuy.setText(R.string.buy);


        buttonBuy.setOnClickListener((v) -> {
            String amount = textAmount.getText().toString();

            if (validate(amount)){
                Transaction transaction = new Transaction(stockId, amount);
                requestBuy(transaction, Utils.getToken());
            } else {
                Utils.showMessage("Минимальная покупка 1", getActivity());
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
                        int statusCode = response.code();
                        if(statusCode == 200) {
                            Utils.showMessage("Оплачено", getActivity());
                            onDestroyView();
                        } else {
                            Utils.showMessage(String.valueOf(statusCode), getActivity());
                        }
                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {
                        Utils.showMessage(t.toString(), getActivity());
                    }
                });
    }
}