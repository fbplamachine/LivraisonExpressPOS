package com.israel.livraisonexpresspos.ui.order_detail.content.delivery_men;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.israel.livraisonexpresspos.databinding.FragmentDeliveryMenBinding;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.ui.order_detail.content.ContentViewModel;

import java.util.List;

public class DeliveryMenDialogFragment extends AppCompatDialogFragment implements View.OnClickListener {
    private FragmentDeliveryMenBinding mBinding;
    private ContentViewModel mViewModel;
    private OnDeliveryMenSelectedListener mListener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mBinding = FragmentDeliveryMenBinding.inflate(inflater);
        builder.setView(mBinding.getRoot());
        initUI();
        stream();
        return builder.create();
    }

    public void setListener(OnDeliveryMenSelectedListener listener) {
        mListener = listener;
    }

    private void initUI() {
        mViewModel = new ViewModelProvider(requireActivity()).get(ContentViewModel.class);
        mBinding.rvDeliveryMen.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.progressBar.setVisibility(View.VISIBLE);
        mViewModel.getDeliveryMen();
        mBinding.buttonCancel.setOnClickListener(this);
    }

    private void stream() {
        mViewModel.getUsers().observe(requireActivity(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (users == null)return;
                mBinding.progressBar.setVisibility(View.GONE);
                if (users.isEmpty()){
                    mBinding.tvNoDeliveryMan.setVisibility(View.VISIBLE);
                    return;
                }
                mBinding.tvNoDeliveryMan.setVisibility(View.GONE);
                DeliveryMenAdapter adapter;
                if (mListener == null){
                    adapter = new DeliveryMenAdapter(users, DeliveryMenDialogFragment.this);
                }else {
                    adapter = new DeliveryMenAdapter(users, DeliveryMenDialogFragment.this, mListener);
                }
                mBinding.rvDeliveryMen.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBinding.buttonCancel.getId()){
            dismiss();
        }
    }
}
