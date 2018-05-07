package com.shm.dim.client;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class OrdersDataAdapter extends RecyclerView.Adapter<OrdersDataAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Order> orders;
    private final OnItemClickListener listener;
    private static int position;


    OrdersDataAdapter(Context context, ArrayList<Order> orders, OnItemClickListener listener) {
        this.orders = orders;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }


    @Override
    public OrdersDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrdersDataAdapter.ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.mOrderCode.setText(order.getOrderCode());
        holder.mProductName.setText(order.getProductName());
        holder.mQuantity.setText(order.getQuantity());
        holder.mCost.setText(order.getCost());
        holder.mName.setText(order.getName());
        holder.mAddress.setText(order.getAddress());
        holder.mDate.setText(order.getDate());
        holder.mTime.setText(order.getTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPosition(holder.getAdapterPosition());
            }
        });
        holder.bind(orders.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


    public interface OnItemClickListener {
        void onItemClick(Order order);
    }

    public static int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

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
                    OrdersDataAdapter.this.listener.onItemClick(order);
                }
            });
        }
    }
}