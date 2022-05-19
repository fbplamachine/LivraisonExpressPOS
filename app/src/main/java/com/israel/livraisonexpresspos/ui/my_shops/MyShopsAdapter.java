package com.israel.livraisonexpresspos.ui.my_shops;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemMyShopBinding;
import com.israel.livraisonexpresspos.models.Shop;
import com.israel.livraisonexpresspos.ui.statistics.sale.SalesStatisticsActivity;
import com.israel.livraisonexpresspos.ui.statistics.shop.ShopStatisticActivity;

import java.util.ArrayList;
import java.util.List;

public class MyShopsAdapter extends RecyclerView.Adapter<MyShopsAdapter.MyShopsViewHolder> {
    public static final String SHOP_ID = "shopId";
    public static final String SHOP_NAME = "shopName";
    private List<Shop> mShops;
    private boolean mIsOrderStatistics;

    public MyShopsAdapter(boolean isOrderStatistics) {
        mShops = new ArrayList<>();
        mIsOrderStatistics = isOrderStatistics;
    }

    public void setShops(List<Shop> shops) {
        mShops = shops;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyShopsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMyShopBinding binding = ItemMyShopBinding.inflate(inflater, parent, false);
        return new MyShopsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyShopsViewHolder holder, int position) {
        Shop shop = mShops.get(position);
        holder.bind(shop);
    }

    @Override
    public int getItemCount() {
        return mShops.size();
    }

    public class MyShopsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemMyShopBinding mBinding;
        public MyShopsViewHolder(@NonNull ItemMyShopBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            itemView.setOnClickListener(this);
        }

        public void bind(Shop shop){
            mBinding.setShop(shop);
        }

        @Override
        public void onClick(View v) {
            if (mIsOrderStatistics){
                Intent intent = new Intent(itemView.getContext(), ShopStatisticActivity.class);
                intent.putExtra(SHOP_ID, mBinding.getShop().getId());
                intent.putExtra(SHOP_NAME, mBinding.getShop().getNom());
                itemView.getContext().startActivity(intent);
            }else {
                Intent intent = new Intent(itemView.getContext(), SalesStatisticsActivity.class);
                intent.putExtra(SHOP_ID, mBinding.getShop().getId());
                intent.putExtra(SHOP_NAME, mBinding.getShop().getNom());
                itemView.getContext().startActivity(intent);
            }
        }
    }
}
