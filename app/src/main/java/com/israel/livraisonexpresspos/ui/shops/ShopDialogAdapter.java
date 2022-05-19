package com.israel.livraisonexpresspos.ui.shops;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemShopDialogBinding;
import com.israel.livraisonexpresspos.models.Shop;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShopDialogAdapter extends RecyclerView.Adapter<ShopDialogAdapter.ShopDialogViewHolder> {
    private List<Shop> mShopList;
    private List<Shop> mShops;
    private OnShopSelectedListener mListener;

    public void setShops(List<Shop> shops){
        mShops = shops;
        mShopList = new ArrayList<>(shops);
        notifyDataSetChanged();
    }

    public void setListener(OnShopSelectedListener listener) {
        mListener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public ShopDialogViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemShopDialogBinding binding = ItemShopDialogBinding.inflate(inflater, parent, false);
        return new ShopDialogViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ShopDialogViewHolder holder, int position) {
        holder.bind(mShops.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mShops.size();
    }

    public static class ShopDialogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemShopDialogBinding mBinding;
        private OnShopSelectedListener mListener;

        public ShopDialogViewHolder(@NonNull @NotNull ItemShopDialogBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Shop shop, OnShopSelectedListener listener){
            itemView.setOnClickListener(this);
            mListener = listener;
            mBinding.setShop(shop);
        }

        @Override
        public void onClick(View v) {
            mListener.onShopSelected(mBinding.getShop().getNom(), mBinding.getShop().getId());
        }
    }

    public Filter getFilter(){
        return mShopsFilter;
    }

    private final Filter mShopsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Shop> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(mShopList);
            }else {
                String pattern = constraint.toString().toLowerCase().trim();
                for (Shop s : mShopList){
                    if (s.getNom().toLowerCase().trim().contains(pattern)){
                        filteredList.add(s);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mShops.clear();
            if (results.values == null)return;
            mShops.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}
