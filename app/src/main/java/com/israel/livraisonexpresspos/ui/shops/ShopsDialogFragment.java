package com.israel.livraisonexpresspos.ui.shops;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.israel.livraisonexpresspos.databinding.DialogShopBinding;

import org.jetbrains.annotations.NotNull;

public class ShopsDialogFragment extends AppCompatDialogFragment implements View.OnClickListener {
    private DialogShopBinding mBinding;
    private ShopDialogViewModel mViewModel;
    private final ShopDialogAdapter mAdapter = new ShopDialogAdapter();

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mBinding = DialogShopBinding.inflate(inflater);
        builder.setView(mBinding.getRoot());
        initUI();
        stream();
        return builder.create();
    }

    public void setListener(OnShopSelectedListener listener) {
        mAdapter.setListener(listener);
    }

    private void initUI() {
        mViewModel = new ViewModelProvider(requireActivity()).get(ShopDialogViewModel.class);
        mBinding.rvShops.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.rvShops.setAdapter(mAdapter);
        mViewModel.fetchShops();
        mBinding.buttonCancel.setOnClickListener(this);
        mBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void stream() {
        mViewModel.getLoading().observe(requireActivity(), load -> {
            if (load == null)return;
            if (load){
                mBinding.progressBar.setVisibility(View.VISIBLE);
            }else {
                mBinding.progressBar.setVisibility(View.GONE);
            }
        });

        mViewModel.getShops().observe(requireActivity(), shops -> {
            if (shops == null)return;
            mBinding.layoutList.setVisibility(View.VISIBLE);
            mAdapter.setShops(shops);
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
