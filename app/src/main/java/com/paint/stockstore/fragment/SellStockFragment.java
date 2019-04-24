package com.paint.stockstore.fragment;

import android.os.Bundle;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellStockFragment extends BottomSheetDialogFragment {

    private final String PATTERN_INPUT = "\\d+";

    private EditText textAmount;
    int count;

    public static SellStockFragment newInstance() {
        return new SellStockFragment();
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
        count = bundle.getInt("count");
        tvDialog.setText(name);

        textAmount = (EditText) view.findViewById(R.id.textAmount);


        Button buttonBuy = (Button) view.findViewById(R.id.buttonDialog);
        buttonBuy.setText(R.string.sell);

        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = textAmount.getText().toString();

                if (validate(amount, count)){
                    Transaction transaction = new Transaction(stockId, amount);
                    requestSell(transaction, Utils.getToken());
                } else {
                    Utils.showMessage("Не может быть:\n- меньше 1\n- больше имеющихся", getActivity());
                }
            }
        });

        return view;
    }


    public boolean validate(String amount, int count) {
        if (amount.matches(PATTERN_INPUT)){
            int numAmount = Integer.parseInt(amount);
            return numAmount > 0 && !(numAmount > count);
        }
        return false;
    }

    private void requestSell(Transaction transaction, String token){
        RetrofitService.getInstance()
                .getApi()
                .sellStock(token, transaction)
                .enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                        int statusCode = response.code();
                        if(statusCode == 200) {
                            Utils.showMessage("Продано", getActivity());
                            ((BriefcaseActivity) getActivity()).forcedUpdate();
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