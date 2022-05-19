package com.israel.livraisonexpresspos.ui.order_detail.content.feed;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemFeedBinding;
import com.israel.livraisonexpresspos.models.Feed;

public class OrderActivityFeedViewHolder extends RecyclerView.ViewHolder {

    private ItemFeedBinding mBinding;

    public OrderActivityFeedViewHolder(@NonNull ItemFeedBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    @SuppressLint("LongLogTag")
    public void bind(Feed orderActivityFeedItem) {
        // mettre la vue a jour et set les listeners et autre
        mBinding.setOrderActivityFeedItem(orderActivityFeedItem);
    }
}
