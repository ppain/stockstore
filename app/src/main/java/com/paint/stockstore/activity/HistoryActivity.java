package com.paint.stockstore.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.paint.stockstore.R;
import com.paint.stockstore.adapter.HistoryAdapter;
import com.paint.stockstore.model.PageOfTransactions;
import com.paint.stockstore.service.RetrofitService;
import com.paint.stockstore.service.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private HistoryAdapter adapterHistory;
    private RecyclerView rvHistory;
    private LinearLayoutManager llManager;
    private Boolean isLoaded = false;
    private final int COUNT_DEFAULT = 10;
    private final int ITEM_ID_DEFAULT = 1;
    private final int HIDE_ITEM = 5;
    private int nextItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        init();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_history);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progressBar);

        showProgress(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rvHistory = (RecyclerView) findViewById(R.id.list_history);
        llManager = new LinearLayoutManager(this);
        rvHistory.setLayoutManager(llManager);
        rvHistory.setHasFixedSize(true);

        initAdapter();

        requestHistory(Utils.getToken(), "", COUNT_DEFAULT, ITEM_ID_DEFAULT);
    }


    void initAdapter() {
        adapterHistory = new HistoryAdapter(new ArrayList<>(), HistoryActivity.this);

        rvHistory.setAdapter(adapterHistory);

        rvHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int sizeList = adapterHistory.getItemCount();
                    int lastVisibleItemPosition = llManager.findLastVisibleItemPosition();
                    Log.d("testing", "lastVisibleItemPosition: " + lastVisibleItemPosition
                            + ", adapterStock.getItemCount(): " + sizeList);
                    if ((lastVisibleItemPosition > sizeList - HIDE_ITEM) && !isLoaded) {
                        isLoaded = true;
                        showProgress(true);
                        requestHistory(Utils.getToken(), "", COUNT_DEFAULT, nextItemId);
                    }
                }
            }
        });
    }


    private void setList(PageOfTransactions pageOfTransactions) {
        adapterHistory.updateList(pageOfTransactions.getItems());
        showProgress(false);
    }


    private void requestHistory(String token, String search, int count, int itemId) {
        RetrofitService.getInstance()
                .getApi()
                .getHistory(token, search, count, itemId)
                .enqueue(new Callback<PageOfTransactions>() {
                    @Override
                    public void onResponse(Call<PageOfTransactions> call, Response<PageOfTransactions> response) {
                        int statusCode = response.code();
                        PageOfTransactions body = response.body();
                        if (statusCode == 200 && body != null) {
                            nextItemId = body.getNextItemId();
                            setList(body);
                            isLoaded = false;
                        } else {
                            Utils.showMessage(String.valueOf(statusCode), getApplicationContext());
                            showProgress(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<PageOfTransactions> call, Throwable t) {
                        Utils.showMessage(t.toString(), getApplicationContext());
                        showProgress(false);
                    }
                });
    }

    private void showProgress(boolean visible) {
        progressBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }
}