package com.israel.livraisonexpresspos.ui.inventory_management.search;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemMovesByShopBinding;
import com.israel.livraisonexpresspos.models.inventory_management_models.MoveByShop;
import com.israel.livraisonexpresspos.ui.inventory_management.StockInventoryActivity;

public class MoveByShopListViewHolder extends RecyclerView.ViewHolder {
    private ItemMovesByShopBinding mBinding;
    private int itemPosition;
    private MoveByShop moveByShopItem;
    private MoveByShopProductListAdapter mAdapter;
    public MoveByShopListViewHolder(@NonNull ItemMovesByShopBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public void bindViewHolder(MoveByShop moveByShop, int position) {
        this.moveByShopItem = moveByShop;
        this.itemPosition = position;
        mBinding.setMoveByShopItem(this.moveByShopItem);
        mAdapter = new MoveByShopProductListAdapter((StockInventoryActivity) itemView.getContext());
        mBinding.rvMoves.setAdapter(mAdapter);
        mAdapter.setProductList(moveByShopItem.getProduits());
    }
}
