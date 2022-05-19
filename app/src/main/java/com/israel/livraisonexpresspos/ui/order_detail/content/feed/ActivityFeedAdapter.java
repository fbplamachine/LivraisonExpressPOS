package com.israel.livraisonexpresspos.ui.order_detail.content.feed;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemFeedBinding;
import com.israel.livraisonexpresspos.models.Feed;

import java.util.List;

public class ActivityFeedAdapter extends RecyclerView.Adapter<OrderActivityFeedViewHolder> {
    private List<Feed> orderActivityFeedItems;

    @SuppressLint("LongLogTag")
    public ActivityFeedAdapter(List<Feed> orderActivityFeedItems) {
        this.orderActivityFeedItems = orderActivityFeedItems;
    }

    @NonNull
    @Override
    public OrderActivityFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemFeedBinding binding = ItemFeedBinding.inflate(inflater, parent, false);
        return new OrderActivityFeedViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderActivityFeedViewHolder holder, int position) {
        holder.bind(orderActivityFeedItems.get(position));
    }

    @Override
    public int getItemCount() {
        return orderActivityFeedItems.size();
    }

    public void setFeedItems(List<Feed> feedItems) {
        orderActivityFeedItems.addAll(feedItems);
        notifyDataSetChanged();
    }

    public List<Feed> getOrderActivityFeedItems(){
        return orderActivityFeedItems;
    }
}
