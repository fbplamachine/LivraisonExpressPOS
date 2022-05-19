package com.israel.livraisonexpresspos.ui.inventory_management.search;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.StockFilterSiteItemBinding;
import com.israel.livraisonexpresspos.models.inventory_management_models.Site;
import com.israel.livraisonexpresspos.ui.inventory_management.StockInventoryActivity;

import java.util.ArrayList;
import java.util.List;

public class FilterStockSiteListAdapter extends RecyclerView.Adapter<FilterStockSiteListViewHolder> {
    private List<Site> siteList;
    private StockInventoryActivity activity;

    public FilterStockSiteListAdapter(StockInventoryActivity activity) {
        this.activity = activity;
        siteList = new ArrayList<>();
    }

    @NonNull
    @Override
    public FilterStockSiteListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        StockFilterSiteItemBinding binding = StockFilterSiteItemBinding.inflate(inflater);
        return new FilterStockSiteListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterStockSiteListViewHolder holder, int position) {
        holder.bindViewHolder(siteList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return this.siteList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSites(List<Site> sites) {
        this.siteList = sites;
        notifyDataSetChanged();
    }
}
