package com.paint.stockstore.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.paint.stockstore.R;
import com.paint.stockstore.model.InfoStock;

import java.util.ArrayList;
import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {

    private List<InfoStock> data = new ArrayList<>();
    private Context context;
    public BuyAdapterClickListener buyAdapterClickListener;

    public StockAdapter(List<InfoStock> data, Context context, BuyAdapterClickListener buyAdapterClickListener) {
        this.data.addAll(data);
        this.context = context;
        this.buyAdapterClickListener = buyAdapterClickListener;
    }


    public class StockViewHolder extends RecyclerView.ViewHolder {

        private ImageView iconUrlItem;
        private TextView nameItem;
        private TextView priceItem;
        private TextView priceDeltaItem;
        private TextView codeItem;

        LinearLayout parentLayout;

        private StockViewHolder(View itemView) {
            super(itemView);

            iconUrlItem = (ImageView) itemView.findViewById(R.id.im_iconUrl_item);
            nameItem = (TextView) itemView.findViewById(R.id.tv_name_item);
            priceItem = (TextView) itemView.findViewById(R.id.tv_price_item);
            priceDeltaItem = (TextView) itemView.findViewById(R.id.tv_priceDelta_date_item);
            codeItem = (TextView) itemView.findViewById(R.id.tv_count_code_item);

            parentLayout = itemView.findViewById(R.id.item_stock);
        }
    }

    @NonNull
    @Override
    public StockAdapter.StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock, parent, false);
        return new StockAdapter.StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockAdapter.StockViewHolder holder, int position) {
        InfoStock model = data.get(position);

        String priceDelta;
        float percentDelta;

        String rub = context.getString(R.string.rub);
        String ruble = context.getString(R.string.ruble);
        String percent = context.getString(R.string.percent);
        float hundred = 100f;
        int colorRed = ContextCompat.getColor(context, R.color.colorRed);
        int colorGreen = ContextCompat.getColor(context, R.color.colorGreen);

        float fPriceDelta = Math.round(model.getPriceDelta() * hundred) / hundred;

        String price = String.valueOf(Math.round(model.getPrice() * hundred) / hundred) + rub;

        if (fPriceDelta < 0) {
            holder.priceDeltaItem.setTextColor(colorRed);
            percentDelta = -fPriceDelta * hundred / (model.getPrice() - fPriceDelta);
            priceDelta = context.getString(R.string.arrow_down) + Math.abs(fPriceDelta) + ruble + Math.round(percentDelta * hundred) / hundred + percent;
        } else {
            holder.priceDeltaItem.setTextColor(colorGreen);
            percentDelta = fPriceDelta * hundred / model.getPrice();
            priceDelta = context.getString(R.string.arrow_up) + fPriceDelta + ruble + Math.round(percentDelta * hundred) / hundred + percent;
        }

        Glide.with(holder.itemView.getContext())
                .load(model.getIconUrl())
                .thumbnail(0.5f)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.baseline_add_shopping_cart_black_48dp)
                .into(holder.iconUrlItem);

        holder.nameItem.setText(model.getName());
        holder.priceItem.setText(price);
        holder.priceDeltaItem.setText(priceDelta);
        holder.codeItem.setText(String.valueOf(model.getCode()));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyAdapterClickListener.onItemClicked(String.valueOf(model.getId()), model.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void updateList(List<InfoStock> list) {
        data.addAll(list);

        if (data != null) {
            notifyDataSetChanged();
        }
    }


    public void clearList() {
        if (data != null) {
            data.clear();
        }
    }
}
