package com.israel.livraisonexpresspos.ui.shops;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemShopBinding;
import com.israel.livraisonexpresspos.models.Shop;

import java.util.ArrayList;
import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopViewHolder> {
    private final List<Shop> mShops;
    private final List<Shop> mShopList;
    private final Activity mActivity;

    public ShopAdapter(List<Shop> shops, Activity activity) {
        mShops = shops;
        mShopList = new ArrayList<>(shops);
        mActivity = activity;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemShopBinding binding = ItemShopBinding.inflate(inflater, parent, false);
        return new ShopViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        Shop shop = mShops.get(position);
        holder.bind(shop, mActivity);
    }

    @Override
    public int getItemCount() {
        return mShops.size();
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
