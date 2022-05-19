package com.israel.livraisonexpresspos.ui.cart;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemCartBinding;
import com.israel.livraisonexpresspos.models.Cart;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {
    private List<Cart> mCarts;
    private FragmentManager mManager;

    public CartAdapter(@NonNull List<Cart> carts, FragmentManager manager) {
        mCarts = carts;
        mManager = manager;
    }

    public void setCarts(List<Cart> carts) {
        mCarts = carts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCartBinding binding = ItemCartBinding.inflate(inflater, parent, false);
        return new CartViewHolder(binding, mManager);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = mCarts.get(position);
        holder.bind(cart);
    }

    @Override
    public int getItemCount() {
        return mCarts.size();
    }
}
