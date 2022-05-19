package com.israel.livraisonexpresspos.ui.inventory_management.search;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemMovesByShopProductBinding;
import com.israel.livraisonexpresspos.models.inventory_management_models.MoveByShopProduct;

public class MoveByShopProductViewHolder extends RecyclerView.ViewHolder {
    private MoveByShopProduct mProduct;
    private int itemPosition;
    private ItemMovesByShopProductBinding mBinding;
    public MoveByShopProductViewHolder(@NonNull ItemMovesByShopProductBinding binding) {
        super(binding.getRoot());
        this.mBinding = binding;
    }

    public void bindViewHolder(MoveByShopProduct product, int position) {
        itemPosition = position;
        mProduct = product;
        mBinding.setMoveByShopProductItem(product);
    }
}
