package com.israel.livraisonexpresspos.ui.cart;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemCartBinding;
import com.israel.livraisonexpresspos.models.Cart;
import com.israel.livraisonexpresspos.uiComponents.Utilities;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public static final String CART_ITEM = "cartItem";
    private final ItemCartBinding mBinding;
    private final OnCartItemChangeListener mItemChangeListener;
    private int quantity;
    private final FragmentManager mManager;

    public CartViewHolder(@NonNull ItemCartBinding binding, FragmentManager manager) {
        super(binding.getRoot());
        mBinding = binding;
        mManager = manager;
        mItemChangeListener = (CartActivity) itemView.getContext();
    }

    public void bind(Cart cart){
        mBinding.setCart(cart);
        quantity = cart.getQuantite();
        Utilities.displayImageFromUrlWithError(itemView.getContext(), cart.getImage(), mBinding.ivCart);
        mBinding.tvDetail.setVisibility(cart.getAttribute() == null ? View.GONE : View.VISIBLE);
        mBinding.ivDelete.setOnClickListener(this);
        mBinding.ivAdd.setOnClickListener(this);
        mBinding.ivRemove.setOnClickListener(this);
        mBinding.ivEditPrice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBinding.ivDelete.getId()){
            mItemChangeListener.onDelete(mBinding.getCart());
        }else if (id == mBinding.ivAdd.getId()){
            quantity++;
            Cart cart = mBinding.getCart();
            cart.setQuantite(quantity);
            mBinding.setCart(cart);
            mItemChangeListener.onChange(cart);
        }else if (id == mBinding.ivRemove.getId()){
            if (quantity <= 1)return;
            quantity--;
            Cart cart = mBinding.getCart();
            cart.setQuantite(quantity);
            mBinding.setCart(cart);
            mItemChangeListener.onChange(cart);
        }else if (id == mBinding.ivEditPrice.getId()){
            EditCartItemPriceDialog dialog = new EditCartItemPriceDialog();
            Bundle args = new Bundle();
            args.putParcelable(CART_ITEM, mBinding.getCart());
            dialog.setArguments(args);
            dialog.show(mManager, "CartDialog");
        }
    }
}
