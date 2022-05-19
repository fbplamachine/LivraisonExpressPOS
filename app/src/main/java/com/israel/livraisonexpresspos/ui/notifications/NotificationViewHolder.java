package com.israel.livraisonexpresspos.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.databinding.ItemNotificationBinding;
import com.israel.livraisonexpresspos.models.Notification;
import com.israel.livraisonexpresspos.ui.order_detail.OrderDetailActivity;
import com.israel.livraisonexpresspos.utils.Values;

public class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final ItemNotificationBinding mBinding;

    public NotificationViewHolder(@NonNull ItemNotificationBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
        itemView.setOnClickListener(this);
    }

    public void bind(final Notification notification) {
        mBinding.setNotification(notification);
    }

    @Override
    public void onClick(View v) {
        try {
            Context context = mBinding.getRoot().getContext();
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra(OrderDetailActivity.ORDER_ID, Integer.parseInt(mBinding.getNotification().getId_notification_for()));
            context.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
            App.handleError(e);
            Values.alertDialog("Impossible d'ouvrir cette notification. Veuillez réessayer plustard.");
        }
    }
}
