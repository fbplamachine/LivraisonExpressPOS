package com.israel.livraisonexpresspos.ui.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.FragmentOrderBinding;
import com.israel.livraisonexpresspos.models.from_steed_app.OrderSteed;
import com.israel.livraisonexpresspos.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

public class CancelledFragment extends Fragment {
    private FragmentOrderBinding mBinding;
    private OrderViewModel mOrderViewModel;
    private OrderListAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false);
        initUI();
        stream();
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private void initUI() {
        mOrderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        mAdapter = new OrderListAdapter(requireActivity());
        mBinding.rvOrder.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.rvOrder.setAdapter(mAdapter);
    }

    private void stream() {
        mOrderViewModel.getCancelledList().observe(getViewLifecycleOwner(), new Observer<List<OrderSteed>>() {
            @Override
            public void onChanged(List<OrderSteed> orderSteeds) {
                if (orderSteeds == null)return;
                List<OrderSteed> steeds = new ArrayList<>(orderSteeds);
                if (mOrderViewModel.getToBeTreatedPage() == 1){
                    mAdapter.setOrderSteeds(steeds);
                    mAdapter.getFilter().filter(PreferenceUtils.getString(requireContext(), PreferenceUtils.SEARCH_PATTERN_ + mOrderViewModel.getState()));
                }else {
                    mAdapter.addOrders(orderSteeds);
                }
            }
        });

        mOrderViewModel.getLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == null)return;
                mBinding.progressBar.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
            }
        });
    }
}
