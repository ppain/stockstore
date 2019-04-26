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
import com.paint.stockstore.activity.BriefcaseActivity;
import com.paint.stockstore.model.Transaction;
import com.paint.stockstore.service.RetrofitService;
import com.paint.stockstore.service.Utils;

import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellStockFragment extends BottomSheetDialogFragment {

    private EditText textAmount;
    int count;

    public static SellStockFragment newInstance() {
        return new SellStockFragment();
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
        count = bundle.getInt(getString(R.string.txt_count));
        tvDialog.setText(name);

        textAmount = (EditText) view.findViewById(R.id.textAmount);

        buttonBuy.setText(R.string.sell);
        buttonBuy.setOnClickListener(v -> {
            String amount = textAmount.getText().toString();

            if (validate(amount, count)) {
                isLoading(true);
                Transaction transaction = new Transaction(stockId, amount);
                requestSell(transaction, Utils.getToken());
            } else {
                Utils.showMessage(getResources().getString(R.string.error_sale), getActivity());
            }
        });

        return view;
    }


    public boolean validate(String amount, int count) {
        int numAmount = Integer.parseInt(amount);
        return numAmount > 0 && !(numAmount > count);
    }


    private void requestSell(Transaction transaction, String token) {
        RetrofitService.getInstance()
                .getApi()
                .sellStock(token, transaction)
                .enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull Call<JSONObject> call, @NonNull Response<JSONObject> response) {
                        int statusCode = response.code();
                        isLoading(false);
                        if (statusCode == 200) {
                            ((BriefcaseActivity) Objects.requireNonNull(getActivity())).forcedUpdate();
                            Utils.showMessage(getResources().getString(R.string.sale), getActivity());
                            onDestroyView();
                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                                Utils.showMessage(jObjError.getString("message"), getActivity());
                            } catch (Exception e) {
                                Utils.showMessage(e.toString(), getActivity());
                            }
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
        ((BriefcaseActivity) Objects.requireNonNull(getActivity())).isLoading(state);
    }
}