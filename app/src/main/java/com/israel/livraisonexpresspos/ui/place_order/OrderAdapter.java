package com.israel.livraisonexpresspos.ui.place_order;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemOrderBinding;
import com.israel.livraisonexpresspos.models.OrderWithCarts;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {
    private List<OrderWithCarts> mOrders;
    private final PlaceOrderFragment mFragment;
    private final Application mApplication;

    public OrderAdapter(List<OrderWithCarts> orders, PlaceOrderFragment fragment, Application application) {
        mOrders = orders;
        mFragment = fragment;
        mApplication = application;
    }

    public void setOrders(List<OrderWithCarts> orders) {
        mOrders = orders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemOrderBinding binding = ItemOrderBinding.inflate(inflater, parent, false);
        return new OrderViewHolder(binding, mApplication);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderWithCarts order = mOrders.get(position);
        holder.bind(order, mFragment);
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }
}
