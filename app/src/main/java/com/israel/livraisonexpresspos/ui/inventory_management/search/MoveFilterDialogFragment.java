package com.israel.livraisonexpresspos.ui.inventory_management.search;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.ChipGroup;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.FragmentMoveFilterBinding;
import com.israel.livraisonexpresspos.ui.inventory_management.StockInventoryActivity;

public class MoveFilterDialogFragment extends AppCompatDialogFragment implements ChipGroup.OnCheckedChangeListener, View.OnClickListener {
    private FragmentMoveFilterBinding mBinding;
    private StockInventoryActivity activity;
    private MovesFilterViewModel mViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mBinding = FragmentMoveFilterBinding.inflate(inflater);
        builder.setView(mBinding.getRoot());
        initViewModel();
        initUi();
        return builder.create();
    }

    private void initUi() {
        activity = (StockInventoryActivity) getActivity();
        mBinding.groupMoveType.setOnCheckedChangeListener(this);
        mBinding.groupDate.setOnCheckedChangeListener(this);
        mBinding.tvShopName.setOnClickListener(this);
        mBinding.tvDepartureSite.setOnClickListener(this);
        mBinding.tvArrivalSite.setOnClickListener(this);
        mBinding.tvResetFilter.setOnClickListener(this);

    }

    private void initViewModel(){
        mViewModel = new ViewModelProvider(requireActivity()).get(MovesFilterViewModel.class);
    }

    private void setLastConstraint() {

    }

    @Override
    public void onCheckedChanged(ChipGroup group, int checkedId) {
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_shop_name) {
            /*todo : fire up the load shop request*/
            StockShopsDialogFragment fragment = new StockShopsDialogFragment();
            fragment.show(getChildFragmentManager(), StockShopsDialogFragment.class.getSimpleName());
        }

        if (viewId == R.id.tv_departure_site) {
            /*todo : site selection process*/
            StockWareHouseDialogFragment fragment = new StockWareHouseDialogFragment();
            fragment.show(getChildFragmentManager(), StockWareHouseDialogFragment.class.getSimpleName());
        }

        if (viewId == R.id.tv_arrival_site) {
            /* todo : site selection process*/
        }

        if (viewId == R.id.tv_reset_filter) {
            /*todo : reset filter process*/
        }

        if (viewId == R.id.img_btn_close) {
            /*todo : close the filter*/
            this.dismiss();
        }

        if (viewId == R.id.img_btn_filter) {
            /*todo : filter process*/
            /**/
        }
    }
}
