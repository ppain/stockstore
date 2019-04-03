package com.paint.stockstore.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paint.stockstore.R;
import com.paint.stockstore.model.TestModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class BriefcaseAdapter extends RecyclerView.Adapter<BriefcaseAdapter.BriefcaseViewHolder>{

    private List<TestModel> data;

    public BriefcaseAdapter(List<TestModel> data) {
        this.data = data;
    }


    public class BriefcaseViewHolder extends RecyclerView.ViewHolder {

        public TextView nameItem;
        public TextView costItem;

        public BriefcaseViewHolder(View itemView) {
            super(itemView);

            nameItem = (TextView) itemView.findViewById(R.id.tv_name_item);
            costItem = (TextView) itemView.findViewById(R.id.tv_cost_item);
        }
    }

    @NonNull
    @Override
    public BriefcaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_briefcase, parent, false);
        return new BriefcaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BriefcaseViewHolder holder, int position) {
        TestModel model = data.get(position);

        holder.nameItem.setText(model.getName());
        holder.costItem.setText(String.valueOf(model.getCost()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    // Clean all elements of the recycler
    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<TestModel> list) {
        data.addAll(list);
        notifyDataSetChanged();
    }

}