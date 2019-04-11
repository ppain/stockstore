package com.paint.stockstore.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paint.stockstore.R;
import com.paint.stockstore.model.InfoStock;

import java.util.ArrayList;
import java.util.List;


public class BriefcaseAdapter extends RecyclerView.Adapter<BriefcaseAdapter.BriefcaseViewHolder>{

    private List<InfoStock> data = new ArrayList<>();

    public BriefcaseAdapter(List<InfoStock> data) {
        this.data.addAll(data);
    }


    public class BriefcaseViewHolder extends RecyclerView.ViewHolder {

        public ImageView iconUrlItem;
        public TextView nameItem;
        public TextView priceItem;
        public TextView priceDeltaItem;
        public TextView countItem;

        public BriefcaseViewHolder(View itemView) {
            super(itemView);

            iconUrlItem = (ImageView) itemView.findViewById(R.id.im_iconUrl_item);
            nameItem = (TextView) itemView.findViewById(R.id.tv_name_item);
            priceItem = (TextView) itemView.findViewById(R.id.tv_price_item);
            priceDeltaItem = (TextView) itemView.findViewById(R.id.tv_priceDelta_item);
            countItem = (TextView) itemView.findViewById(R.id.tv_count_code_item);

            iconUrlItem.setImageResource(R.drawable.baseline_business_center_black_48dp);
        }
    }

    @NonNull
    @Override
    public BriefcaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock, parent, false);
        return new BriefcaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BriefcaseViewHolder holder, int position) {
        InfoStock model = data.get(position);

        //String price = String.valueOf(model.getPrice()) + getString(R.string.part_price);
        String price = String.valueOf(model.getPrice()) + "р";
        //String count = String.valueOf(model.getCount()) + R.string.part_count;
        String count = String.valueOf(model.getCount()) + "шт";

        //TODO create color for icon on name_hash
        //holder.iconUrlItem.setImageIcon();
        holder.nameItem.setText(model.getName());

        holder.priceItem.setText(price);
        holder.priceDeltaItem.setText(String.valueOf(model.getPriceDelta()));
        holder.countItem.setText(count);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void swapList(List<InfoStock> list) {
        if (data != null) {
            data.clear();
        }
        data = list;

        if (data != null) {
            notifyDataSetChanged();
        }
    }

}