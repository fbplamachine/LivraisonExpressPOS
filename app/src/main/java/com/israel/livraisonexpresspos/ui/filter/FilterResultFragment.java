package com.israel.livraisonexpresspos.ui.filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.FragmentFilterFormBinding;
import com.israel.livraisonexpresspos.models.from_steed_app.OrderSteed;
import com.israel.livraisonexpresspos.ui.orders.OrderListAdapter;

import java.util.ArrayList;
import java.util.List;

public class FilterResultFragment extends Fragment implements View.OnClickListener {
    private FragmentFilterFormBinding mBinding;
    private FilterViewModel mViewModel;
    private OrderListAdapter adapter;

    public FilterResultFragment() {
        // Required empty public constructor
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getArguments().getString("filterConstraint");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter_form, container, false);

        initUi();
        stream();
        return mBinding.getRoot();
    }

    private void stream() {

        if (!mViewModel.getOrderSteeds().hasActiveObservers()) {
            mViewModel.getOrderSteeds().observe(getViewLifecycleOwner(), new Observer<List<OrderSteed>>() {
                @Override
                public void onChanged(List<OrderSteed> orderSteeds) {
                    if (orderSteeds != null) {
                        if (orderSteeds.size() > 0) {
                            mBinding.tvIssues.setVisibility(View.GONE);
                            adapter.setOrderSteeds(orderSteeds);
                        } else {
                            mBinding.tvIssues.setVisibility(View.VISIBLE);
                            mBinding.tvIssues.setText(R.string.no_order_found);
                            adapter.setOrderSteeds(new ArrayList<OrderSteed>());
                        }
                    } else {
                        mBinding.tvIssues.setVisibility(View.VISIBLE);
                        mBinding.tvIssues.setText(R.string.no_order_found);
                        adapter.setOrderSteeds(new ArrayList<OrderSteed>());
                    }

                }
            });
        }

        if (!mViewModel.getIsLoading().hasActiveObservers()) {
            mViewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (aBoolean) {
                        mBinding.progressBar.setVisibility(View.VISIBLE);
                    } else {
                        mBinding.progressBar.setVisibility(View.GONE);
                    }

                    if (mViewModel.getIssueType() > 0) {
                        mBinding.tvIssues.setVisibility(View.VISIBLE);
                        mBinding.tvIssues.setText(R.string.something_went_wrong);
                    }
                }
            });
        }

        if (!mViewModel.getConstraint().hasActiveObservers()) {
            mViewModel.getConstraint().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    if (s.isEmpty()){
                        mBinding.fabFilter.setCount(0);
                        adapter.setOrderSteeds(new ArrayList<OrderSteed>());
                    }else {
                        mBinding.fabFilter.setCount(1);
                    }
                }
            });
        }
    }

    private void initUi() {
        mViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        adapter = new OrderListAdapter(requireActivity());
        mBinding.rvOrder.setLayoutManager(layoutManager);
        mBinding.rvOrder.setAdapter(adapter);
        mBinding.progressBar.setVisibility(View.GONE);

        mBinding.fabFilter.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == mBinding.fabFilter.getId()) {
            FilterDialogFragment filterDialog = new FilterDialogFragment();
            Bundle args = new Bundle();
            args.putString("constraint", mViewModel.getConstraint().getValue());
            filterDialog.setArguments(args);
            filterDialog.show(getChildFragmentManager(), FilterDialogFragment.class.getSimpleName());
        }
    }

}
