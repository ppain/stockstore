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

import com.paint.stockstore.R;
import com.paint.stockstore.model.InfoStock;
import com.paint.stockstore.model.TransactionHistoryRecord;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{

    private List<TransactionHistoryRecord> data = new ArrayList<>();
    private Context context;

    public HistoryAdapter(List<TransactionHistoryRecord> data, Context context) {
        this.data.addAll(data);
        this.context = context;
    }


    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        public ImageView iconUrlItem;
        public TextView nameItem;
        public TextView priceItem;
        public TextView dateItem;
        public TextView codeCountItem;
        public View indicatorItem;

        public HistoryViewHolder(View itemView) {
            super(itemView);

            iconUrlItem = (ImageView) itemView.findViewById(R.id.im_iconUrl_item);
            nameItem = (TextView) itemView.findViewById(R.id.tv_name_item);
            priceItem = (TextView) itemView.findViewById(R.id.tv_price_item);
            dateItem = (TextView) itemView.findViewById(R.id.tv_priceDelta_date_item);
            codeCountItem = (TextView) itemView.findViewById(R.id.tv_count_code_item);

            indicatorItem = (View) itemView.findViewById(R.id.indicator);

            iconUrlItem.setImageResource(R.drawable.baseline_add_shopping_cart_black_48dp);
        }
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryAdapter.HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {
        TransactionHistoryRecord model = data.get(position);

        String totalPrice = String.valueOf(model.getTotalPrice()) + "р";
        String codeAmount = model.getStock().getCode() + "/" + String.valueOf(model.getAmount());

        if(model.getType() == "sell") {
            holder.indicatorItem.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRed));
        } else {
            holder.indicatorItem.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen));
        }
        //TODO create color for icon on name_hash
        //holder.iconUrlItem.setImageIcon();
        holder.nameItem.setText(model.getStock().getName());
        holder.priceItem.setText(totalPrice);
        holder.codeCountItem.setText(codeAmount);
        holder.dateItem.setText(model.getDate());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void updateList(List<TransactionHistoryRecord> list){
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