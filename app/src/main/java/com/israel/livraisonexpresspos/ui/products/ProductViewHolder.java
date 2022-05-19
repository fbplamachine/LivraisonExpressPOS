package com.israel.livraisonexpresspos.ui.products;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemProductBinding;
import com.israel.livraisonexpresspos.models.Product;
import com.israel.livraisonexpresspos.uiComponents.Utilities;
import com.israel.livraisonexpresspos.utils.Values;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final ItemProductBinding mBinding;
    private OnProductClickListener mListener;

    public ProductViewHolder(@NonNull ItemProductBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
        itemView.setOnClickListener(this);
    }

    public void bind(Product product, Activity activity){
        mBinding.setProduct(product);
        mListener = (ProductActivity) activity;
        mBinding.tvPrice.setTextColor(Values.getColorDark());
        Utilities.displayImageFromUrlWithError(activity, product.getImage(), mBinding.ivProduct);
    }

    @Override
    public void onClick(View view) {
        mListener.onProductClick(mBinding.getProduct());
    }
}
