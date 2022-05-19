package com.israel.livraisonexpresspos.ui.inventory_management.search;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.FragmentStockSearchBinding;
import com.israel.livraisonexpresspos.ui.filter.FilterDialogFragment;
import com.israel.livraisonexpresspos.ui.inventory_management.MovesViewModel;
import com.israel.livraisonexpresspos.ui.inventory_management.StockInventoryActivity;

public class StockSearchResultsFragment extends Fragment {
    private FragmentStockSearchBinding mBinding;
    private MovesViewModel mViewModel;
    private MovesFilterViewModel mFilterViewModel;
    private MoveByShopListAdapter mAdapter;
    private StockInventoryActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_stock_search, container, false);
        initViewModel();
        initUi();
        stream();
        return mBinding.getRoot();
    }

    public void stream() {
        /*todo : listen to the shop list changes */
        mFilterViewModel.getMovesByShopsList().observe(getViewLifecycleOwner(), moveByShops -> {
            mAdapter.setMoveByShopList(moveByShops);
        });
    }


    private void initUi() {
        initToolbar();
        activity = (StockInventoryActivity) requireActivity();
        mAdapter = new MoveByShopListAdapter(activity);
        mBinding.rvMovesByShop.setAdapter(mAdapter);

        mFilterViewModel.loadMovesByShops();
    }

    private void initToolbar() {
        mBinding.toolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_add_filter) {
                MoveFilterDialogFragment filterDialog = new MoveFilterDialogFragment();
                filterDialog.show(getChildFragmentManager(), FilterDialogFragment.class.getSimpleName());
            }
            return true;
        });
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(requireActivity()).get(MovesViewModel.class);
        mFilterViewModel = new ViewModelProvider(requireActivity()).get(MovesFilterViewModel.class);
    }
}