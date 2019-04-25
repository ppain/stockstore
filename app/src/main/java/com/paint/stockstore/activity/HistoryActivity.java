package com.paint.stockstore.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.paint.stockstore.R;
import com.paint.stockstore.adapter.HistoryAdapter;
import com.paint.stockstore.model.PageOfTransactions;
import com.paint.stockstore.model.TransactionHistoryRecord;
import com.paint.stockstore.service.RetrofitService;
import com.paint.stockstore.service.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private HistoryAdapter adapterHistory;
    private RecyclerView rvHistory;
    private LinearLayoutManager llManager;
    private Boolean isLoaded = false;
    private int nextItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initUI();
        initAdapter();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_history);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progressBar);

        isLoading(true);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rvHistory = (RecyclerView) findViewById(R.id.list_history);
        llManager = new LinearLayoutManager(this);
        rvHistory.setLayoutManager(llManager);
        rvHistory.setHasFixedSize(true);
    }


    void initAdapter() {
        adapterHistory = new HistoryAdapter(new ArrayList<>(), HistoryActivity.this);

        rvHistory.setAdapter(adapterHistory);

        rvHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int sizeList = adapterHistory.getItemCount();
                    int lastVisibleItemPosition = llManager.findLastVisibleItemPosition();
                    if (!isLoaded && (lastVisibleItemPosition > sizeList - Utils.HIDE_ITEM)) {
                        isLoaded = true;
                        isLoading(true);
                        requestHistory(Utils.getToken(), nextItemId);
                    }
                }
            }
        });
        requestHistory(Utils.getToken(), Utils.ITEM_ID_DEFAULT);
    }


    private void setList(List<TransactionHistoryRecord> items) {
        adapterHistory.updateList(items);
    }


    private void checkOnEndList(PageOfTransactions pageOfTransactions) {
        nextItemId = pageOfTransactions.getNextItemId();
        if (nextItemId != 0) {
            isLoaded = false;
        }
        setList(pageOfTransactions.getItems());
    }


    private void requestHistory(String token, int itemId) {
        RetrofitService.getInstance()
                .getApi()
                .getHistory(token, "", Utils.COUNT_DEFAULT, itemId)
                .enqueue(new Callback<PageOfTransactions>() {
                    @Override
                    public void onResponse(@NonNull Call<PageOfTransactions> call, @NonNull Response<PageOfTransactions> response) {
                        isLoading(false);
                        int statusCode = response.code();
                        PageOfTransactions body = response.body();
                        if (statusCode == 200 && body != null) {
                            checkOnEndList(body);
                        } else {
                            Utils.showMessage(Objects.requireNonNull(response.errorBody()).source().toString(), getApplicationContext());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PageOfTransactions> call, @NonNull Throwable t) {
                        Utils.showMessage(t.toString(), getApplicationContext());
                        isLoading(false);
                    }
                });
    }

    private void isLoading(boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}