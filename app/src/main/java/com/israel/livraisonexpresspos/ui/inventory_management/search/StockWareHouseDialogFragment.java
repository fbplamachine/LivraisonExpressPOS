package com.israel.livraisonexpresspos.ui.inventory_management.search;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.FragmentStockWareHouseBinding;
import com.israel.livraisonexpresspos.ui.inventory_management.StockInventoryActivity;

public class StockWareHouseDialogFragment extends AppCompatDialogFragment implements View.OnClickListener, TextWatcher {
    private FragmentStockWareHouseBinding mBinding;
    private StockInventoryActivity activity;
    private MovesFilterViewModel mViewModel;
    private FilterStockSiteListAdapter mAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mBinding = FragmentStockWareHouseBinding.inflate(inflater);
        builder.setView(mBinding.getRoot());
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initViewModel();
        initUi();
        stream();
        return mBinding.getRoot();
    }

    private void stream() {
        mViewModel.getSiteList().observe(getViewLifecycleOwner(), sites -> {
            mAdapter.setSites(sites);
        });
    }

    private void initUi() {
        activity = (StockInventoryActivity) requireActivity();
        mBinding.imgBtnClose.setOnClickListener(this);
        mBinding.etSearch.addTextChangedListener(this);
        mAdapter = new FilterStockSiteListAdapter(activity);
        mBinding.rvSites.setAdapter(mAdapter);

        mViewModel.loadSites();
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(requireActivity()).get(MovesFilterViewModel.class);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.img_btn_close){
            this.dismiss();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //todo : perferm site filtering here
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
