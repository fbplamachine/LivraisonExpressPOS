package com.israel.livraisonexpresspos.ui.statistics.delivery_men;

import android.app.ProgressDialog;
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

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.ActivityShopStatisticBinding;
import com.israel.livraisonexpresspos.models.from_steed_app.Badge;
import com.israel.livraisonexpresspos.models.from_steed_app.OrderSteed;
import com.israel.livraisonexpresspos.ui.filter.FilterViewModel;
import com.israel.livraisonexpresspos.ui.orders.OrderListAdapter;
import com.israel.livraisonexpresspos.ui.statistics.shop.StatisticViewModel;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DeliveryMenStatisticsFragment extends Fragment {
    private ActivityShopStatisticBinding mBinding;
    private OrderListAdapter mAdapter;
    private StatisticViewModel mViewModel;
    private ProgressDialog mDialog;
    private FilterViewModel mFilterViewModel;
    private Calendar mCalendar;
    private String mToday;
    private int option = 1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.activity_shop_statistic, container, false);
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
        initDialog();
        mBinding.toolbar2.setVisibility(View.GONE);
        mViewModel = new ViewModelProvider(this).get(StatisticViewModel.class);
        mFilterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);
        mCalendar = Calendar.getInstance();
        mToday = mCalendar.get(Calendar.YEAR) + "-" + (mCalendar.get(Calendar.MONTH) + 1) + "-" + mCalendar.get(Calendar.DAY_OF_MONTH) + " 00:00:00";
        mViewModel.getDeliveryMenStatistics(mToday);
    }

    private void initDialog() {
        mDialog = new ProgressDialog(requireContext());
        mDialog.setMessage(getString(R.string.loading));
        mDialog.setCancelable(false);
        mDialog.create();
    }

    private void stream() {
        mViewModel.getBadge().observe(getViewLifecycleOwner(), new Observer<Badge>() {
            @Override
            public void onChanged(Badge badge) {
                if (badge == null) return;
                initStatistics();
                mBinding.setBadge(badge);
                float unassign = (float) badge.getUnassigned() / badge.getTotal() * 100;
                float assign = (float) badge.getAssigned() / badge.getTotal() * 100;
                float running = (float) badge.getRunning() / badge.getTotal() * 100;
                float delivered = (float) badge.getDelivered() / badge.getTotal() * 100;
                float cancel = (float) badge.getCancelled() / badge.getTotal() * 100;
                mBinding.tvUnnassigned.setText(String.format(Locale.FRANCE, "%.1f", unassign).concat("%"));
                mBinding.tvAssigned.setText(String.format(Locale.FRANCE, "%.1f", assign).concat("%"));
                mBinding.tvRunning.setText(String.format(Locale.FRANCE, "%.1f", running).concat("%"));
                mBinding.tvDelivered.setText(String.format(Locale.FRANCE, "%.1f", delivered).concat("%"));
                mBinding.tvCancel.setText(String.format(Locale.FRANCE, "%.1f", cancel).concat("%"));
                switch (option){
                    case 1:
                        mBinding.tvTopic.setText("Aujourd'hui");
                        mBinding.tvDates.setText("Statistiques pour la journée du: " + mToday);
                        break;
                    case 2:
                        mBinding.tvTopic.setText("Hier");
                        mBinding.tvDates.setText("Statistiques pour la journée du: " + mViewModel.getStartDate());
                        break;
                    case 3:
                        mBinding.tvTopic.setText("7 Jours");
                        mBinding.tvDates.setText("Statistiques pour la période du: " + mViewModel.getStartDate() + " au " + mViewModel.getEndDate());
                        break;
                    case 4:
                        mBinding.tvTopic.setText("30 Jours");
                        mBinding.tvDates.setText("Statistiques pour la période du: " + mViewModel.getStartDate() + " au " + mViewModel.getEndDate());
                        break;
                    case 5:
                        mBinding.tvTopic.setText("Période personnalisé");
                        mBinding.tvDates.setText("Statistiques pour la période du: " + mViewModel.getStartDate() + " au " + mViewModel.getEndDate());
                        break;
                }
            }
        });
        mViewModel.getLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == null) return;
                if (aBoolean) {
                    mDialog.show();
                } else {
                    mDialog.dismiss();
                }
            }
        });
        mFilterViewModel.getOrderSteeds().observe(getViewLifecycleOwner(), new Observer<List<OrderSteed>>() {
            @Override
            public void onChanged(List<OrderSteed> orderSteeds) {
                if (orderSteeds == null) return;
                mAdapter.setOrderSteeds(orderSteeds);
                mBinding.rvCourse.setVisibility(View.VISIBLE);
            }
        });
        mFilterViewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == null) return;
                if (aBoolean) {
                    mDialog.show();
                } else {
                    mDialog.dismiss();
                }
            }
        });
    }

    private void initStatistics() {
        mBinding.rvCourse.setVisibility(View.GONE);
    }
}
