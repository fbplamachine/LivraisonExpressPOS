package com.israel.livraisonexpresspos.ui.statistics.sale;

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

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.ActivitySaleStatisticsBinding;
import com.israel.livraisonexpresspos.models.MonthStatistic;
import com.israel.livraisonexpresspos.models.ShopStatistic;

import java.util.ArrayList;
import java.util.List;

public class SaleStatisticsFragment extends Fragment {
    private ActivitySaleStatisticsBinding mBinding;
    private ProgressDialog mDialog;
    private SaleStatisticsViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.activity_sale_statistics, container, false);
        initUI();
        stream();
        return mBinding.getRoot();
    }

    private void initUI() {
        initDialog();
        mViewModel = new ViewModelProvider(this).get(SaleStatisticsViewModel.class);
        mViewModel.getSaleStatistics(0);
    }

    private void stream() {
        mViewModel.getSales().observe(getViewLifecycleOwner(), new Observer<List<ShopStatistic>>() {
            @Override
            public void onChanged(List<ShopStatistic> shopStatistics) {
                if (shopStatistics == null)return;
                LineData lineData = new LineData(lineChartDataSet(shopStatistics));
                mBinding.lineChart.invalidate();
            }
        });

        mViewModel.getLoad().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == null)return;
                if (aBoolean){
                    mDialog.show();
                }else {
                    mDialog.dismiss();
                }
            }
        });
    }

    private void initDialog() {
        mDialog = new ProgressDialog(requireContext());
        mDialog.setMessage(getString(R.string.loading));
        mDialog.setCancelable(false);
        mDialog.create();
    }

    private ArrayList<ILineDataSet> lineChartDataSet(List<ShopStatistic> statistics){
        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
        for (ShopStatistic statistic : statistics) {
            ArrayList<Entry> dataSet = new ArrayList<>();
            int i = 0;
            for (MonthStatistic monthStatistic: statistic.getStats()) {
                int total = 0;
                if (monthStatistic.getStats() != null && monthStatistic.getStats().getAchat() != null){
                    total = (int) Double.parseDouble(monthStatistic.getStats().getAchat());
                }
                dataSet.add(new Entry(i, total));
                i++;
            }
            LineDataSet lineDataSet = new LineDataSet(dataSet, statistic.getMagasin());
            iLineDataSets.add(lineDataSet);
        }
        return iLineDataSets;
    }
}
