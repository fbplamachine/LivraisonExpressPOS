package com.israel.livraisonexpresspos.ui.inventory_management.move_details;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.FragmentMoveDetailsBinding;
import com.israel.livraisonexpresspos.ui.inventory_management.MovesViewModel;
import com.israel.livraisonexpresspos.ui.inventory_management.StockInventoryActivity;


public class MoveDetailsFragment extends Fragment {

    private MovesViewModel mViewModel;
    private FragmentMoveDetailsBinding mBinding;
    private MoveDetailsShopListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_move_details, container, false);
        // Inflate the layout for this fragment
        initViewModel();
        initUi();
        stream();
        return mBinding.getRoot();
    }

    private void initViewModel(){
        mViewModel = new ViewModelProvider(requireActivity()).get(MovesViewModel.class);
    }

    private void initUi(){
        /*todo : do your thing here */
        mAdapter = new MoveDetailsShopListAdapter(getActivity());
        mBinding.rvProducts.setAdapter(mAdapter);
        mBinding.toolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_close){
                StockInventoryActivity activity = (StockInventoryActivity) getActivity();
                assert activity != null;
                activity.onCloseFullScreenFragment();
            }
            return true;
        });
    }

    private void stream(){
        mViewModel.getCurrentViewedMove().observe(getViewLifecycleOwner(), move -> {
            mBinding.setMove(move);
        });

        mViewModel.getCurrentViewedMoveProducts().observe(getViewLifecycleOwner(),products -> {
            mAdapter.setProductsList(products);
        });
    }
}