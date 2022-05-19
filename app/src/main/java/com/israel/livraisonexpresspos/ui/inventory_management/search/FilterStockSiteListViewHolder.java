package com.israel.livraisonexpresspos.ui.inventory_management.search;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.StockFilterSiteItemBinding;
import com.israel.livraisonexpresspos.models.inventory_management_models.Site;
import com.israel.livraisonexpresspos.ui.inventory_management.StockInventoryActivity;

public class FilterStockSiteListViewHolder extends RecyclerView.ViewHolder {
    private final StockFilterSiteItemBinding mBinding;
    private StockInventoryActivity activity;
    private int itemPosition;
    private Site mSite;
    public FilterStockSiteListViewHolder(@NonNull StockFilterSiteItemBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }


    public void bindViewHolder(Site site, int position) {
        this.mSite = site;
        this.itemPosition = position;
        this.activity = (StockInventoryActivity) itemView.getContext();
        mBinding.setSite(this.mSite);
        itemView.setOnClickListener(view -> {
            //todo : handle the callback logic to perform the site selection .

        });
    }
}
