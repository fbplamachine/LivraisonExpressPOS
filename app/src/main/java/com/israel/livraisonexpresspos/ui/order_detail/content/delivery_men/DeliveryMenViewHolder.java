package com.israel.livraisonexpresspos.ui.order_detail.content.delivery_men;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemDeliveryMenBinding;
import com.israel.livraisonexpresspos.models.User;

public class DeliveryMenViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final ItemDeliveryMenBinding mBinding;
    private DeliveryMenDialogFragment mFragment;
    private OnDeliveryMenSelectedListener mListener;

    public DeliveryMenViewHolder(@NonNull ItemDeliveryMenBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public void bind(User user, DeliveryMenDialogFragment fragment){
        mFragment = fragment;
        mBinding.setUser(user);
        itemView.setOnClickListener(this);
    }

    public void setListener(OnDeliveryMenSelectedListener listener){
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mListener == null){
            mListener = (OnDeliveryMenSelectedListener) itemView.getContext();
        }
        mListener.onDeliveryMenSelected(mBinding.getUser());
        mFragment.dismiss();
    }
}
