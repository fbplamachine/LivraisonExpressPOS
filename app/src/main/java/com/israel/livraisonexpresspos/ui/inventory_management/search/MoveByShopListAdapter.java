package com.israel.livraisonexpresspos.ui.inventory_management.search;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemMovesByShopBinding;
import com.israel.livraisonexpresspos.models.inventory_management_models.MoveByShop;
import com.israel.livraisonexpresspos.ui.inventory_management.StockInventoryActivity;

import java.util.ArrayList;
import java.util.List;

public class MoveByShopListAdapter extends RecyclerView.Adapter<MoveByShopListViewHolder> {
    private List<MoveByShop> moveByShopList;
    private StockInventoryActivity activity;

    public MoveByShopListAdapter(@NonNull StockInventoryActivity activity){
        this.activity = activity;
        moveByShopList = new ArrayList<>();
    }
    @NonNull
    @Override
    public MoveByShopListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMovesByShopBinding binding = ItemMovesByShopBinding.inflate(inflater, parent, false);
        return new MoveByShopListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MoveByShopListViewHolder holder, int position) {
        holder.bindViewHolder(moveByShopList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return this.moveByShopList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMoveByShopList(List<MoveByShop> moveByShopList) {
        this.moveByShopList = moveByShopList;
        notifyDataSetChanged();
    }
}
