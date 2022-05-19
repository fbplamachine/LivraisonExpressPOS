package com.israel.livraisonexpresspos.ui.order_detail.content.edit_cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemCartListBinding;
import com.israel.livraisonexpresspos.models.Cart;
import com.israel.livraisonexpresspos.ui.cart.OnCartItemChangeListener;
import com.israel.livraisonexpresspos.ui.order_detail.content.ContentFragment;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {
    private final List<Cart> mCarts;
    private final ContentFragment mFragment;

    public CartItemAdapter(List<Cart> carts, ContentFragment fragment) {
        mCarts = carts;
        mFragment = fragment;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCartListBinding binding = ItemCartListBinding.inflate(inflater, parent, false);
        return new CartItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        Cart cart = mCarts.get(position);
        holder.bind(cart);
    }

    @Override
    public int getItemCount() {
        return mCarts.size();
    }

    public class CartItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemCartListBinding mBinding;
        private final OnCartItemChangeListener mChangeListener = mFragment;

        public CartItemViewHolder(@NonNull ItemCartListBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Cart cart){
            mBinding.setItem(cart);
            mBinding.ivDelete.setOnClickListener(this);
            mBinding.ivEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == mBinding.ivEdit.getId()){
                mChangeListener.onChange(mBinding.getItem());
            }else if (id == mBinding.ivDelete.getId()){
                mChangeListener.onDelete(mBinding.getItem());
            }
        }
    }
}
