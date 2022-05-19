package com.israel.livraisonexpresspos.ui.my_shops;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.FragmentMyShopsBinding;
import com.israel.livraisonexpresspos.models.Shop;
import com.israel.livraisonexpresspos.ui.orders.OrderViewModel;

import java.util.List;

public class MyShopsFragment extends Fragment {
    private MyShopsViewModel mViewModel;
    private OrderViewModel mOrderViewModel;
    private FragmentMyShopsBinding mBinding;
    private MyShopsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_shops, container, false);
        initUi();
        stream();
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private void initUi() {
        mViewModel = new ViewModelProvider(this).get(MyShopsViewModel.class);
        mOrderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        mAdapter = new MyShopsAdapter(mOrderViewModel.isOrderStatistics());
        mBinding.rvMyShops.setAdapter(mAdapter);
        mBinding.rvMyShops.setLayoutManager(new LinearLayoutManager(requireContext()));
        mViewModel.getUserShops();
    }

    private void stream() {
        mViewModel.getShops().observe(getViewLifecycleOwner(), new Observer<List<Shop>>() {
            @Override
            public void onChanged(List<Shop> shops) {
                if (shops == null)return;
                mAdapter.setShops(shops);
            }
        });

        mViewModel.getLoad().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == null)return;
                mBinding.progressBar.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
            }
        });
    }
}
