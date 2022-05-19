package com.israel.livraisonexpresspos.ui.inventory_management.search;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.StockFilterShopItemBinding;
import com.israel.livraisonexpresspos.models.inventory_management_models.FilterShop;
import com.israel.livraisonexpresspos.ui.inventory_management.StockInventoryActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StockFilterShopListAdapter extends RecyclerView.Adapter<StockFilterShopListViewHolder> {
    private List<FilterShop> shopList;
//    private List<FilterShop> filteredShopList;
    private StockInventoryActivity activity;

    public StockFilterShopListAdapter(@NonNull StockInventoryActivity activity){
        this.activity = activity;
        shopList = new ArrayList<>();
//        filteredShopList = new ArrayList<>();
    }

    @NonNull
    @Override
    public StockFilterShopListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        StockFilterShopItemBinding binding = StockFilterShopItemBinding.inflate(inflater);
        return new StockFilterShopListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StockFilterShopListViewHolder holder, int position) {
        holder.bindViewHolder(shopList.get(position),position);
    }

    @Override
    public int getItemCount() {
        return this.shopList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setShopList(List<FilterShop> shopList){
        this.shopList = new ArrayList<>(shopList);
//        this.filteredShopList = new ArrayList<>(shopList);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setFilteredShopList(List<FilterShop> shops){
//        this.filteredShopList = new ArrayList<>(shops);
        notifyDataSetChanged();
    }

    public void filterShops(String input) {
        List<FilterShop> result = new ArrayList<>();
        if (!input.isEmpty()){
            for (FilterShop shop : this.shopList) {
                if(shop.getNom().toUpperCase(Locale.ROOT).contains(input.toUpperCase())){
                    result.add(shop);
                }
            }
        }else {
            result.addAll(this.shopList);
        }
        this.setFilteredShopList(result);
    }
}
