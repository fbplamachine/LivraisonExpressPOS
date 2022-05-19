package com.israel.livraisonexpresspos.ui.inventory_management.move_details;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemMoveDetailsShopBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MoveDetailsShopListAdapter extends RecyclerView.Adapter<MoveDetailsShopViewHolder> {
    private List<HashMap<String, Object>> shops;
    private Activity activity;

    public MoveDetailsShopListAdapter(Activity activity){
        this.activity = activity;
    }

    @NonNull
    @Override
    public MoveDetailsShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMoveDetailsShopBinding binding = ItemMoveDetailsShopBinding.inflate(inflater, parent, false);
        return new MoveDetailsShopViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MoveDetailsShopViewHolder holder, int position) {
        holder.bindViewHolder(shops.get(position),position);
    }

    @Override
    public int getItemCount() {
        return this.shops.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setProductsList(List<HashMap<String, Object>> product) {
        this.shops = new ArrayList<>(product);
        this.notifyDataSetChanged();
    }

//    @SuppressLint("NotifyDataSetChanged")
//    public void addMove(Product product){
//        this.products.add(0, product);
//        notifyDataSetChanged();
//    }

    @SuppressLint("NotifyDataSetChanged")
    public void removeProduct(int position){
        this.shops.remove(position);
        notifyDataSetChanged();
    }
}
