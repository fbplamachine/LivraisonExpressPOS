package com.israel.livraisonexpresspos.ui.statistics.sale;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemProductStatisticsBinding;
import com.israel.livraisonexpresspos.models.ProductStatistic;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProductStatisticAdapter extends RecyclerView.Adapter<ProductStatisticAdapter.ProductStatisticViewHolder> {
    private final List<ProductStatistic> mProductStatistics = new ArrayList<>();

    public void setProductStatistics(List<ProductStatistic> productStatistics){
        mProductStatistics.clear();
        mProductStatistics.addAll(productStatistics);
    }

    @NonNull
    @NotNull
    @Override
    public ProductStatisticViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemProductStatisticsBinding binding = ItemProductStatisticsBinding.inflate(inflater, parent, false);
        return new ProductStatisticViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductStatisticViewHolder holder, int position) {
        ProductStatistic productStatistic = mProductStatistics.get(position);
        holder.bind(productStatistic);
    }

    @Override
    public int getItemCount() {
        return mProductStatistics.size();
    }

    public static class ProductStatisticViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductStatisticsBinding mBinding;

        public ProductStatisticViewHolder(@NonNull @NotNull ItemProductStatisticsBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(ProductStatistic productStatistic){
            mBinding.setProduct(productStatistic);
        }
    }
}
