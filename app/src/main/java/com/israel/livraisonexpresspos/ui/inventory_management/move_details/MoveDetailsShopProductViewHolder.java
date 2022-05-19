package com.israel.livraisonexpresspos.ui.inventory_management.move_details;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.MoveDetailsItemShopProductBinding;
import com.israel.livraisonexpresspos.models.inventory_management_models.MoveDetailsShopProduct;

public class MoveDetailsShopProductViewHolder extends RecyclerView.ViewHolder {
    private MoveDetailsItemShopProductBinding mBinding;
    private int itemPosition;
    private MoveDetailsShopProduct product;

    public MoveDetailsShopProductViewHolder(@NonNull MoveDetailsItemShopProductBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public void bindViewHolder(MoveDetailsShopProduct product, int position) {
        itemPosition = position;
        this.product = product;
        mBinding.setProduct(product);
    }
}
