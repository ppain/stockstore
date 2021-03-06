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
import com.paint.stockstore.service.Utils;

import java.util.ArrayList;
import java.util.List;


public class BriefcaseAdapter extends RecyclerView.Adapter<BriefcaseAdapter.BriefcaseViewHolder> {

    private List<InfoStock> data = new ArrayList<>();
    private SellBuyAdapterClickListener sellAdapterClickListener;
    private Context context;

    public BriefcaseAdapter(List<InfoStock> data, Context context, SellBuyAdapterClickListener sellAdapterClickListener) {
        this.data.addAll(data);
        this.context = context;
        this.sellAdapterClickListener = sellAdapterClickListener;
    }


    public class BriefcaseViewHolder extends RecyclerView.ViewHolder {

        private ImageView iconUrlItem;
        private TextView nameItem;
        private TextView priceItem;
        private TextView priceDeltaItem;
        private TextView countItem;

        LinearLayout parentLayout;

        private BriefcaseViewHolder(View itemView) {
            super(itemView);

            iconUrlItem = (ImageView) itemView.findViewById(R.id.im_iconUrl_item);
            nameItem = (TextView) itemView.findViewById(R.id.tv_name_item);
            priceItem = (TextView) itemView.findViewById(R.id.tv_price_item);
            priceDeltaItem = (TextView) itemView.findViewById(R.id.tv_priceDelta_date_item);
            countItem = (TextView) itemView.findViewById(R.id.tv_count_code_item);

            parentLayout = itemView.findViewById(R.id.item_stock);
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

        String priceDelta;
        float percentDelta;

        String rub = context.getString(R.string.rub);
        String ruble = context.getString(R.string.ruble);
        String percent = context.getString(R.string.percent);
        String amount = context.getString(R.string.amount);
        String space_bar = context.getString(R.string.space_bar);
        int colorRed = ContextCompat.getColor(context, R.color.colorRed);
        int colorGreen = ContextCompat.getColor(context, R.color.colorGreen);

        float PriceDelta = model.getPriceDelta();

        String price = model.getPrice() + space_bar + rub;
        String count = model.getCount() + amount;

//        Calculate interest
        if (PriceDelta < 0) {
            holder.priceDeltaItem.setTextColor(colorRed);
            percentDelta = -PriceDelta * Utils.hundred / (model.getPrice() - PriceDelta);
            priceDelta = context.getString(R.string.arrow_down) + Math.abs(PriceDelta) + ruble + Utils.roundToString(percentDelta) + percent;
        } else {
            holder.priceDeltaItem.setTextColor(colorGreen);
            percentDelta = PriceDelta * Utils.hundred / model.getPrice();
            priceDelta = context.getString(R.string.arrow_up) + PriceDelta + ruble + Utils.roundToString(percentDelta) + percent;
        }

        Glide.with(holder.itemView.getContext())
                .load(model.getIconUrl())
                .thumbnail(0.5f)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.baseline_business_center_black_48dp)
                .into(holder.iconUrlItem);

        holder.nameItem.setText(model.getName());
        holder.priceItem.setText(price);
        holder.priceDeltaItem.setText(priceDelta);
        holder.countItem.setText(count);

        holder.parentLayout.setOnClickListener(v -> sellAdapterClickListener.onItemClicked(String.valueOf(model.getId()),
                model.getName(), model.getCount()));
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