package com.israel.livraisonexpresspos.ui.notifications;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemNotificationBinding;
import com.israel.livraisonexpresspos.models.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationViewHolder> {
    private final List<Notification> mNotifications = new ArrayList<>();

    public void addNotifications(List<Notification> notificationList){
        mNotifications.addAll(notificationList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemNotificationBinding binding = ItemNotificationBinding.inflate(inflater, parent,false);
        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.bind(mNotifications.get(position));
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }
}
