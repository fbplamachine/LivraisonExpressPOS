package com.israel.livraisonexpresspos.ui.order_detail.content.delivery_men;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemDeliveryMenBinding;
import com.israel.livraisonexpresspos.models.User;

import java.util.List;

public class DeliveryMenAdapter extends RecyclerView.Adapter<DeliveryMenViewHolder> {
    private final List<User> mUsers;
    private final DeliveryMenDialogFragment mFragment;
    private OnDeliveryMenSelectedListener mListener;

    public DeliveryMenAdapter(List<User> users, DeliveryMenDialogFragment fragment) {
        mUsers = users;
        mFragment = fragment;
    }

    public DeliveryMenAdapter(List<User> users, DeliveryMenDialogFragment fragment, OnDeliveryMenSelectedListener listener) {
        mUsers = users;
        mFragment = fragment;
        mListener = listener;
    }

    @NonNull
    @Override
    public DeliveryMenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemDeliveryMenBinding binding = ItemDeliveryMenBinding.inflate(inflater, parent, false);
        return new DeliveryMenViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryMenViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.bind(user, mFragment);
        if (mListener != null){
            holder.setListener(mListener);
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
