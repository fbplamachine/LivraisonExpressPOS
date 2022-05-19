package com.israel.livraisonexpresspos.ui.contact_detail.orders;

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
import com.israel.livraisonexpresspos.databinding.FragmentContactOrdersBinding;
import com.israel.livraisonexpresspos.models.from_steed_app.OrderSteed;
import com.israel.livraisonexpresspos.ui.contact_detail.ContactDetailActivity;
import com.israel.livraisonexpresspos.ui.orders.OrderListAdapter;

import java.util.List;

public class ContactOrdersFragment extends Fragment {
    private FragmentContactOrdersBinding mBinding;
    private ContactOrderViewModel mViewModel;
    private ContactDetailActivity mActivity;
    private OrderListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_orders, container, false);
        mViewModel = new ViewModelProvider(this).get(ContactOrderViewModel.class);
        mActivity = (ContactDetailActivity) requireActivity();
        initUI();
        stream();
        return mBinding.getRoot();
    }

    private void initUI() {
        mViewModel.getContactOrder(Integer.parseInt(mActivity.getContact().getId()));
        mAdapter = new OrderListAdapter(requireActivity());
        mBinding.rvOrder.setAdapter(mAdapter);
        mBinding.rvOrder.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void stream() {
        mViewModel.getLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == null)return;
                mBinding.progressBar.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
            }
        });

        mViewModel.getOrders().observe(getViewLifecycleOwner(), new Observer<List<OrderSteed>>() {
            @Override
            public void onChanged(List<OrderSteed> orderSteeds) {
                if (orderSteeds == null)return;
                mAdapter.setOrderSteeds(orderSteeds);
            }
        });
    }
}
