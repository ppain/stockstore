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
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameItem;
        public TextView costItem;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public BriefcaseViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
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
        // Get the data model based on position
        TestModel model = data.get(position);

        holder.nameItem.setText(model.getName());
        holder.nameItem.setText(model.getCost());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}