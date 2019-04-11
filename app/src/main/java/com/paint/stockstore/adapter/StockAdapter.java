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

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder>{

        private List<InfoStock> data = new ArrayList<>();

        public StockAdapter(List<InfoStock> data) {
            this.data.addAll(data);
        }


        public class StockViewHolder extends RecyclerView.ViewHolder {

            public ImageView iconUrlItem;
            public TextView nameItem;
            public TextView priceItem;
            public TextView priceDeltaItem;
            public TextView codeItem;

            public StockViewHolder(View itemView) {
                super(itemView);

                iconUrlItem = (ImageView) itemView.findViewById(R.id.im_iconUrl_item);
                nameItem = (TextView) itemView.findViewById(R.id.tv_name_item);
                priceItem = (TextView) itemView.findViewById(R.id.tv_price_item);
                priceDeltaItem = (TextView) itemView.findViewById(R.id.tv_priceDelta_item);
                codeItem = (TextView) itemView.findViewById(R.id.tv_count_code_item);

                iconUrlItem.setImageResource(R.drawable.baseline_add_shopping_cart_black_48dp);
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

            //String price = String.valueOf(model.getPrice()) + getString(R.string.part_price);
            String price = String.valueOf(model.getPrice()) + "р";

            //TODO create color for icon on name_hash
            //holder.iconUrlItem.setImageIcon();
            holder.nameItem.setText(model.getName());
            holder.priceItem.setText(price);
            holder.priceDeltaItem.setText(String.valueOf(model.getPriceDelta()));
            holder.codeItem.setText(String.valueOf(model.getCode()));
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
