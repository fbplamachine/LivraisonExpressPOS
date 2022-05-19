package com.israel.livraisonexpresspos.ui.inventory_management.search;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.StockFilterShopItemBinding;
import com.israel.livraisonexpresspos.models.inventory_management_models.FilterShop;
import com.israel.livraisonexpresspos.ui.inventory_management.StockInventoryActivity;

public class StockFilterShopListViewHolder extends RecyclerView.ViewHolder {
    private final StockInventoryActivity activity;
    private final StockFilterShopItemBinding mBinding;
    private FilterShop mShop;
    private int itemPosition;

    public StockFilterShopListViewHolder(@NonNull StockFilterShopItemBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
        activity = (StockInventoryActivity) itemView.getContext();
    }

    public void bindViewHolder(FilterShop shop, int itemPosition){
        this.itemPosition = itemPosition;
        this.mShop = shop;
        Log.e("TAG", "bindViewHolder: "+this.mShop.getNom());
        mBinding.setShop(this.mShop);
        itemView.setOnClickListener(view -> {
            /*todo : update the filter constrain with the selected user */
            activity.onFilterShopSelected(mShop);
        });
    }
}
