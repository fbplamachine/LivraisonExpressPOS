package com.israel.livraisonexpresspos.ui.inventory_management.ware_house;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.FragmentWareHouseBinding;
import com.israel.livraisonexpresspos.ui.inventory_management.StockInventoryActivity;


public class WareHouseFragment extends Fragment {
    private FragmentWareHouseBinding mBinding;
    private WareHouseViewModel mViewModel;
    private WareHouseListAdapter mAdapter;
    private StockInventoryActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_ware_house, container, false);
        initViewModel();
        initUi();
        stream();
        return mBinding.getRoot();
    }

    private void stream() {
        /*todo : set observers on site loading state  */
        mViewModel.getSiteList().observe(getViewLifecycleOwner(), sites -> {
            mAdapter.setSites(sites);
        });
    }

    private void initUi() {
        initToolbar();
        activity = (StockInventoryActivity) requireActivity();
        mAdapter = new WareHouseListAdapter(activity);
        mBinding.rvWareHouse.setAdapter(mAdapter);

        mViewModel.loadSites();
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(requireActivity()).get(WareHouseViewModel.class);
    }

    private void initToolbar() {
        mBinding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_add) {
                /*todo : add interface for ware houses events */
                activity.onAddSiteIntent();
                /*todo : at the end of this process  take the new added move and add it to the vm list and then to rv lis the refresh data set change */
            }
            return true;
        });
    }


}