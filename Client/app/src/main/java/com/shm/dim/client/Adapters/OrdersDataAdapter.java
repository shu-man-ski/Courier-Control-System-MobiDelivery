package com.shm.dim.client.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shm.dim.client.Models.Order;
import com.shm.dim.client.R;

import java.util.ArrayList;

public class OrdersDataAdapter extends RecyclerView.Adapter<OrdersDataAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Order> orders;
    private final OnItemClickListener listener;
    boolean[] selectItems;


    public OrdersDataAdapter(Context context, ArrayList<Order> orders, OnItemClickListener listener) {
        this.orders = orders;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
        selectItems = new boolean[getItemCount()];
    }


    @Override
    public OrdersDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrdersDataAdapter.ViewHolder holder, final int position) {
        Order order = orders.get(position);
        holder.mOrderCode.setText(order.getOrderCode());
        holder.mProductName.setText(order.getProductName());
        holder.mQuantity.setText(order.getQuantity());
        holder.mCost.setText(order.getCost());
        holder.mName.setText(order.getName());
        holder.mAddress.setText(order.getAddress());
        holder.mDate.setText(order.getDate());
        holder.mTime.setText(order.getTime());

        if(selectItems[position]) {
            holder.itemView.setBackgroundColor(Color.parseColor("#a8d1e0"));
        }
        else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        holder.bind(orders.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


    public interface OnItemClickListener {
        void onItemClick(Order order);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView mOrderCode, mProductName, mQuantity, mCost,
                mName, mAddress,
                mDate, mTime;

        ViewHolder(View view) {
            super(view);
            mOrderCode = view.findViewById(R.id.order_code);
            mProductName = view.findViewById(R.id.product_name);
            mQuantity = view.findViewById(R.id.quantity);
            mCost = view.findViewById(R.id.cost);
            mName = view.findViewById(R.id.customer_name);
            mAddress = view.findViewById(R.id.customer_address);
            mDate = view.findViewById(R.id.date);
            mTime = view.findViewById(R.id.time);
        }

        public void bind(final Order order, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int positin = getAdapterPosition();
                    // Выставляем false для всеч элементов массива
                    for (int i = 0; i < selectItems.length; i++) {
                        selectItems[i] = false;
                    }
                    // Выбранный элемент — true
                    selectItems[positin] = true;
                    // Передаем order в onItemClick адаптера
                    OrdersDataAdapter.this.listener.onItemClick(order);
                    // Вызов onBindViewHolder
                    notifyDataSetChanged();
                }
            });
        }
    }
}