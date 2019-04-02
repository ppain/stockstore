package com.paint.stockstore.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.paint.stockstore.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StocksActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);

        final ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(createAdapter());
        final SwipeRefreshLayout refreshLayout = ((SwipeRefreshLayout) findViewById(R.id.swipe_container));
        //refreshLayout.setColor(R.color.progress_red, R.color.progress_green, R.color.progress_blue, R.color.progress_orange);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(createAdapter());
                        refreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topPosition = (listView == null || listView.getChildCount() == 0) ? 0 : listView.getChildAt(0).getTop();
                refreshLayout.setEnabled(topPosition >= 0);
            }
        });
    }

    private BaseAdapter createAdapter() {
        return new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, generateData());
    }

    private List<String> generateData() {
        List<String> data = new ArrayList<String>();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 20; i++) {
            data.add(String.valueOf(random.nextInt(1000)));
        }
        return data;
    }

}