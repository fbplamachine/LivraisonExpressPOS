package com.israel.livraisonexpresspos.ui.inventory_management.move_details;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.MoveDetailsItemShopProductBinding;
import com.israel.livraisonexpresspos.models.inventory_management_models.MoveDetailsShopProduct;
import com.israel.livraisonexpresspos.ui.inventory_management.StockInventoryActivity;

import java.util.ArrayList;
import java.util.List;

public class MoveDetailsShopProductListAdapter extends RecyclerView.Adapter<MoveDetailsShopProductViewHolder> {
    private StockInventoryActivity activity;
    private List<MoveDetailsShopProduct> products;
    public MoveDetailsShopProductListAdapter(Activity activity){

    }

    @NonNull
    @Override
    public MoveDetailsShopProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MoveDetailsItemShopProductBinding binding = MoveDetailsItemShopProductBinding.inflate(inflater, parent, false);
        return new MoveDetailsShopProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MoveDetailsShopProductViewHolder holder, int position) {
        holder.bindViewHolder(products.get(position),position);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setProduct(List<MoveDetailsShopProduct> products) {
        this.products = new ArrayList<>(products);
        notifyDataSetChanged();
    }
}
