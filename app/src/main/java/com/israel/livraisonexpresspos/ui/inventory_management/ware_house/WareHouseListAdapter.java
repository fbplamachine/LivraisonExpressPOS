package com.israel.livraisonexpresspos.ui.inventory_management.ware_house;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemWareHouseBinding;
import com.israel.livraisonexpresspos.models.inventory_management_models.Site;
import com.israel.livraisonexpresspos.ui.inventory_management.StockInventoryActivity;

import java.util.ArrayList;
import java.util.List;

public class WareHouseListAdapter extends RecyclerView.Adapter<WareHouseListViewHolder> {

    private final StockInventoryActivity activity;
    private List<Site> mSiteList;

    public WareHouseListAdapter(StockInventoryActivity activity) {
        this.activity = activity;
        mSiteList = new ArrayList<>();
    }

    @NonNull
    @Override
    public WareHouseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemWareHouseBinding binding = ItemWareHouseBinding.inflate(inflater, parent, false);
        return new WareHouseListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WareHouseListViewHolder holder, int position) {
        holder.bindViewHolder(mSiteList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mSiteList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSites(List<Site> sites) {
        this.mSiteList = sites;
        notifyDataSetChanged();
    }
}
