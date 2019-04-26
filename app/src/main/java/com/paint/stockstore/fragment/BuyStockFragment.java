package com.paint.stockstore.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.paint.stockstore.R;
import com.paint.stockstore.activity.StockActivity;
import com.paint.stockstore.model.Transaction;
import com.paint.stockstore.service.RetrofitService;
import com.paint.stockstore.service.Utils;

import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyStockFragment extends BottomSheetDialogFragment {

    private EditText textAmount;

    public static BuyStockFragment newInstance() {
        return new BuyStockFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bs_dialog, container, false);
        Button buttonBuy = (Button) view.findViewById(R.id.buttonDialog);

        Bundle bundle = getArguments();
        TextView tvDialog = (TextView) view.findViewById(R.id.tv_dialog);
        String stockId = Objects.requireNonNull(bundle).getString(getString(R.string.txt_stock_id));
        String name = bundle.getString(getString(R.string.txt_name));
        tvDialog.setText(name);

        textAmount = (EditText) view.findViewById(R.id.textAmount);

        buttonBuy.setText(R.string.buy);
        buttonBuy.setOnClickListener((v) -> {
            String amount = textAmount.getText().toString();

            if (validate(amount)) {
                isLoading(true);
                Transaction transaction = new Transaction(stockId, amount);
                requestBuy(transaction, Utils.getToken());
            } else {
                Utils.showMessage(getResources().getString(R.string.error_buy), getActivity());
            }
        });

        return view;
    }


    public boolean validate(String amount) {
        return Integer.parseInt(amount) > 0;
    }


    private void requestBuy(Transaction transaction, String token) {
        RetrofitService.getInstance()
                .getApi()
                .buyStock(token, transaction)
                .enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull Call<JSONObject> call, @NonNull Response<JSONObject> response) {
                        int statusCode = response.code();
                        isLoading(false);
                        if (statusCode == 200) {
                            Utils.setFlagUpdate(true);
                            Utils.showMessage(getResources().getString(R.string.paid), getActivity());
                            onDestroyView();
                        } else {
                            Utils.showMessage(Objects.requireNonNull(response.errorBody()).source().toString(), getActivity());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<JSONObject> call, @NonNull Throwable t) {
                        Utils.showMessage(t.toString(), getActivity());
                        isLoading(false);
                    }
                });
    }

    private void isLoading(boolean state) {
        ((StockActivity) Objects.requireNonNull(getActivity())).isLoading(state);
    }
}