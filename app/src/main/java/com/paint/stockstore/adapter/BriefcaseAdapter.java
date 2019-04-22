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
import com.paint.stockstore.R;
import com.paint.stockstore.activity.BriefcaseActivity;
import com.paint.stockstore.model.InfoStock;

import java.util.ArrayList;
import java.util.List;


public class BriefcaseAdapter extends RecyclerView.Adapter<BriefcaseAdapter.BriefcaseViewHolder>{

    private List<InfoStock> data = new ArrayList<>();
    public SellBuyAdapterClickListener sellAdapterClickListener;
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

        String priceDelta;
        float percentDelta;

        String rub = context.getString(R.string.rub);
        String ruble = context.getString(R.string.ruble);
        String percent = context.getString(R.string.percent);
        String amount = context.getString(R.string.amount);
        float hundred = 100f;
        int colorRed = ContextCompat.getColor(context, R.color.colorRed);
        int colorGreen = ContextCompat.getColor(context, R.color.colorGreen);


        float fPriceDelta = Math.round(model.getPriceDelta() * hundred) / hundred;

        String price = String.valueOf(Math.round(model.getPrice() * hundred) / hundred) + rub;
        String count = String.valueOf(model.getCount()) + amount;


        if (fPriceDelta < 0) {
            holder.priceDeltaItem.setTextColor(colorRed);
            percentDelta = - fPriceDelta * hundred / (model.getPrice() - fPriceDelta);
            priceDelta = context.getString(R.string.arrow_down) + Math.abs(fPriceDelta) + ruble + Math.round(percentDelta * hundred) / hundred  + percent;
        } else {
            holder.priceDeltaItem.setTextColor(colorGreen);
            percentDelta = fPriceDelta * hundred / model.getPrice();
            priceDelta = context.getString(R.string.arrow_up) + fPriceDelta + ruble + Math.round(percentDelta * hundred) / hundred + percent;
        }

        Glide.with(context).load(model.getIconUrl()).into(holder.iconUrlItem);

        //TODO create color for icon on name_hash
        //holder.iconUrlItem.setImageIcon();
        holder.nameItem.setText(model.getName());
        holder.priceItem.setText(price);
        holder.priceDeltaItem.setText(priceDelta);
        holder.countItem.setText(count);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellAdapterClickListener.onItemClicked(String.valueOf(model.getId()),
                        model.getName(), model.getCount());
            }
        });
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