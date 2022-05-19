package com.israel.livraisonexpresspos.ui.statistics.sale;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemCancellationStatisticsBinding;
import com.israel.livraisonexpresspos.models.CancellationStatistics;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CancellationStatisticAdapter extends RecyclerView.Adapter<CancellationStatisticAdapter.CancellationStatisticViewHolder> {
    private final List<CancellationStatistics> mCancellationStatistics = new ArrayList<>();

    public void setCancellationStatistics(List<CancellationStatistics> cancellationStatistics){
        mCancellationStatistics.clear();
        mCancellationStatistics.addAll(cancellationStatistics);
    }

    @NonNull
    @NotNull
    @Override
    public CancellationStatisticViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCancellationStatisticsBinding binding = ItemCancellationStatisticsBinding.inflate(inflater, parent, false);
        return new CancellationStatisticViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CancellationStatisticViewHolder holder, int position) {
        CancellationStatistics cancellationStatistics = mCancellationStatistics.get(position);
        holder.bind(cancellationStatistics);
    }

    @Override
    public int getItemCount() {
        return mCancellationStatistics.size();
    }

    public static class CancellationStatisticViewHolder extends RecyclerView.ViewHolder {
        private final ItemCancellationStatisticsBinding mBinding;

        public CancellationStatisticViewHolder(@NonNull @NotNull ItemCancellationStatisticsBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(CancellationStatistics cancellationStatistics){
            mBinding.setCancellation(cancellationStatistics);
        }
    }
}
