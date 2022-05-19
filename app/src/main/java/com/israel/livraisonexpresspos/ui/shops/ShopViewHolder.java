package com.israel.livraisonexpresspos.ui.shops;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemShopBinding;
import com.israel.livraisonexpresspos.models.Shop;
import com.israel.livraisonexpresspos.ui.new_course.NewCourseActivity;
import com.israel.livraisonexpresspos.ui.products.ProductActivity;
import com.israel.livraisonexpresspos.uiComponents.Utilities;
import com.israel.livraisonexpresspos.utils.Values;

public class ShopViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final ItemShopBinding mBinding;
    private Activity mActivity;

    public ShopViewHolder(@NonNull ItemShopBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public void bind(Shop shop, Activity activity){
        mActivity = activity;
        mBinding.getRoot().setOnClickListener(this);
        mBinding.setShop(shop);
        Utilities.displayImageFromUrl(mBinding.getRoot().getContext(), shop.getImage(),
                mBinding.ivShop);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(mActivity, ProductActivity.class);
        if (mBinding.getShop().getNom().contains("Coursier Express")){
            intent = new Intent(mActivity, NewCourseActivity.class);
        }
        Values.shop = mBinding.getShop();
        mActivity.startActivity(intent);
    }
}
