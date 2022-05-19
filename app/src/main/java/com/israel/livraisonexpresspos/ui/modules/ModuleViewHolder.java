package com.israel.livraisonexpresspos.ui.modules;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.databinding.ItemModuleBinding;
import com.israel.livraisonexpresspos.models.Delivery;
import com.israel.livraisonexpresspos.models.Module;
import com.israel.livraisonexpresspos.ui.new_course.NewCourseActivity;
import com.israel.livraisonexpresspos.ui.products.ProductActivity;
import com.israel.livraisonexpresspos.ui.shops.ShopActivity;
import com.israel.livraisonexpresspos.uiComponents.Utilities;
import com.israel.livraisonexpresspos.utils.Values;

public class ModuleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final ItemModuleBinding mBinding;
    private Activity mActivity;

    public ModuleViewHolder(@NonNull ItemModuleBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public void bind(Module module, Activity activity){
        mActivity = activity;
        mBinding.getRoot().setOnClickListener(this);
        mBinding.setModule(module);
        Utilities.displayImageFromUrl(itemView.getContext(), module.getImage(),
                mBinding.ivModule);
        Utilities.setBW(!module.isIs_active_in_city(), mBinding.ivModule);

        if (module.getSlug().equals("delivery")){
            if (module.getShops() == null || module.getShops().isEmpty())return;
            Values.setSender(module.getShops().get(0));
        }

        if (Values.order != null){
            Delivery delivery = new Gson().fromJson(Values.order.getStringDelivery(), new TypeToken<Delivery>(){}.getType());
            if (delivery != null && delivery.getSender() != null){
                Values.sender = delivery.getSender();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(!mBinding.getModule().isIs_active_in_city())return;
        Values.module = mBinding.getModule();
        Intent intent = new Intent(mActivity, ShopActivity.class);
        if (mBinding.getModule().getShops().size() == 1){
            Values.shop = mBinding.getModule().getShops().get(0);
            if(mBinding.getModule().getSlug().equals("delivery")){
                intent = new Intent(mActivity, NewCourseActivity.class);
            }else {
                intent = new Intent(mActivity, ProductActivity.class);
            }
        }
        mActivity.startActivity(intent);
    }
}
