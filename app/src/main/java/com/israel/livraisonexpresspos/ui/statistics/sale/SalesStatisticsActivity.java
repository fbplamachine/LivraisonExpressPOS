package com.israel.livraisonexpresspos.ui.statistics.sale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.databinding.ActivitySaleStatisticsBinding;
import com.israel.livraisonexpresspos.models.MonthStatistic;
import com.israel.livraisonexpresspos.models.ShopStatistic;
import com.israel.livraisonexpresspos.models.from_steed_app.Badge;
import com.israel.livraisonexpresspos.ui.my_shops.MyShopsAdapter;
import com.israel.livraisonexpresspos.ui.statistics.shop.StatisticViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SalesStatisticsActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    private ActivitySaleStatisticsBinding mBinding;
    private SaleStatisticsViewModel mViewModel;
    private StatisticViewModel mStatisticViewModel;
    private ProgressDialog mDialog;
    private Intent mIntent;
    private ArrayList<BarEntry> mBarEntries = new ArrayList<>();
    private ArrayList<String> mLabel = new ArrayList<>();
    private int mShopId;
    private int mSelectedMonth;
    private ShopStatistic mShopStatistic;
    private int mItemSelected;
    private ProductStatisticAdapter mProductStatisticAdapter;
    private CancellationStatisticAdapter mCancellationStatisticAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sale_statistics);
        App.currentActivity = this;
        mIntent = getIntent();
        if (mIntent == null) return;
        initUi();
        stream();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.currentActivity = this;
    }

    private void initToolbar() {
        mBinding.toolbar2.setTitle(mIntent.getStringExtra(MyShopsAdapter.SHOP_NAME));
        setSupportActionBar(mBinding.toolbar2);
        mBinding.toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initUi() {
        initToolbar();
        initDialog();
        mShopId = mIntent.getIntExtra(MyShopsAdapter.SHOP_ID, 0);
        mViewModel = new ViewModelProvider(this).get(SaleStatisticsViewModel.class);
        mStatisticViewModel = new ViewModelProvider(this).get(StatisticViewModel.class);
        mProductStatisticAdapter = new ProductStatisticAdapter();
        mCancellationStatisticAdapter = new CancellationStatisticAdapter();
        mBinding.rvProducts.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvCancellations.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvProducts.setAdapter(mProductStatisticAdapter);
        mBinding.rvCancellations.setAdapter(mCancellationStatisticAdapter);
        mViewModel.getSaleStatistics(mShopId);
        mBinding.lineChart.setOnChartValueSelectedListener(this);
        mBinding.pieChart.setDrawEntryLabels(false);
        mBinding.pieChart.setUsePercentValues(true);
        mBinding.pieChart.setDrawHoleEnabled(false);
        mBinding.pieChart.getDescription().setEnabled(false);
        mBinding.pieChart.setDrawCenterText(false);
    }

    private void initDialog() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage(getString(R.string.loading));
        mDialog.setCancelable(false);
        mDialog.create();
    }

    private void stream() {
        mViewModel.getSales().observe(this, new Observer<List<ShopStatistic>>() {
            @Override
            public void onChanged(List<ShopStatistic> shopStatistics) {
                if (shopStatistics == null || shopStatistics.isEmpty())return;
                mShopStatistic = shopStatistics.get(0);
                for (int i = 0; i < mShopStatistic.getStats().size(); i++){
                    MonthStatistic statistic = mShopStatistic.getStats().get(i);
                    String monthString = statistic.getNom();
                    mLabel.add(monthString.substring(0, Math.min(monthString.length(), 3)));
                    int sale = 0;
                    if (statistic.getStats() != null && statistic.getStats().getAchat() != null){
                        sale = Double.valueOf(statistic.getStats().getAchat()).intValue();
                    }
                    mBarEntries.add(new BarEntry(i, sale));
                }

                BarDataSet barDataSet = new BarDataSet(mBarEntries, "Mois");
                barDataSet.setColor(getResources().getColor(R.color.colorAccent));
                Description description = new Description();
                description.setText("Ventes Annuelle");
                mBinding.lineChart.setDescription(description);
                BarData barData = new BarData(barDataSet);
                mBinding.lineChart.setData(barData);

                XAxis xAxis = mBinding.lineChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(mLabel));

                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(false);
                xAxis.setDrawGridLinesBehindData(false);
                xAxis.setDrawGridLinesBehindData(false);
                xAxis.setGranularity(1f);
                xAxis.setLabelCount(mLabel.size());
                xAxis.setLabelRotationAngle(270);
                mBinding.lineChart.animateY(2000);
                mBinding.lineChart.invalidate();

            }
        });

        mStatisticViewModel.getBadge().observe(this, new Observer<Badge>() {
            @Override
            public void onChanged(Badge badge) {
                if (badge == null) return;
                float unassign = (float) badge.getUnassigned() / badge.getTotal();
                float assign = (float) badge.getAssigned() / badge.getTotal();
                float running = (float) badge.getRunning() / badge.getTotal();
                float delivered = (float) badge.getDelivered() / badge.getTotal();
                float cancel = (float) badge.getCancelled() / badge.getTotal();

                ArrayList<PieEntry> entries = new ArrayList<>();
                entries.add(new PieEntry(unassign, "Non assignée"));
                entries.add(new PieEntry(assign, "En attente"));
                entries.add(new PieEntry(running, "En cours"));
                entries.add(new PieEntry(delivered, "Terminée"));
                entries.add(new PieEntry(cancel, "Annulée"));

                ArrayList<Integer> colors = new ArrayList<>();
                colors.add(rgb("#999999"));
                colors.add(rgb("#ffb74d"));
                colors.add(rgb("#81c784"));
                colors.add(rgb("#90caf9"));
                colors.add(rgb("#EB4242"));


                PieDataSet dataSet = new PieDataSet(entries, "Statuts");
                dataSet.setColors(colors);

                PieData data = new PieData(dataSet);
                data.setDrawValues(true);
                data.setValueFormatter(new PercentFormatter(mBinding.pieChart));
                data.setValueTextSize(12f);
                data.setValueTextColor(Color.BLACK);

                mBinding.pieChart.setData(data);
                mBinding.pieChart.invalidate();
                mBinding.setMonth(getMonthById(mSelectedMonth));
                mBinding.layoutPie.setVisibility(View.VISIBLE);
                mBinding.tvSaleCost.setText("0.00 FCFA");
                mBinding.tvShippingCost.setText("0.00 FCFA");
                if (mShopStatistic != null && mShopStatistic.getStats() != null && !mShopStatistic.getStats().isEmpty()){
                   MonthStatistic statistic =  mShopStatistic.getStats().get(mItemSelected);
                   if (statistic != null && statistic.getStats() != null){
                       mBinding.tvSaleCost.setText(statistic.getStats().getAchat() + " FCFA");
                       mBinding.tvShippingCost.setText(statistic.getStats().getLivraison() + " FCFA");
                   }
                }

            }
        });
        mStatisticViewModel.getLoading().observe(this, new Observer<Boolean>() {
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
        mViewModel.getLoad().observe(this, new Observer<Boolean>() {
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
        mViewModel.getProductLoad().observe(this, load -> {
            if  (load == null)return;
            if(load){
                mBinding.progressProducts.setVisibility(View.VISIBLE);
                mBinding.rvProducts.setVisibility(View.GONE);
            }else {
                mBinding.progressProducts.setVisibility(View.GONE);
                mBinding.rvProducts.setVisibility(View.VISIBLE);
            }
        });

        mViewModel.getCancellationLoad().observe(this, load -> {
            if  (load == null)return;
            if(load){
                mBinding.progressCancellation.setVisibility(View.VISIBLE);
                mBinding.rvCancellations.setVisibility(View.GONE);
            }else {
                mBinding.progressCancellation.setVisibility(View.GONE);
                mBinding.rvCancellations.setVisibility(View.VISIBLE);
            }
        });

        mViewModel.getProductStatistics().observe(this, products -> {
            if (products == null)return;
            mProductStatisticAdapter.setProductStatistics(products);
        });

        mViewModel.getCancellationStatistics().observe(this, cancellations -> {
            if (cancellations == null)return;
            mCancellationStatisticAdapter.setCancellationStatistics(cancellations);
        });

    }

    private int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color) & 0xFF;
        return Color.rgb(r, g, b);
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

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        mBinding.layoutProducts.setVisibility(View.VISIBLE);
        mBinding.layoutCancellations.setVisibility(View.VISIBLE);
        try {
            mItemSelected = (int)e.getX();
            int month =  ((int)e.getX()) + 1;
            if (month != mSelectedMonth){
                mSelectedMonth = month;
                int year = Calendar.getInstance().get(Calendar.YEAR);
                String startDate = String.format(Locale.FRANCE, "%02d/%02d", 1, month) + "/" + year;
                String startDateProduct = year + "-" + String.format(Locale.FRANCE,  "%02d-%02d", month, 1) + " 00:00:00";
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
                Date date = format.parse(startDate);
                if (date == null)return;
                calendar.setTime(date);
                int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                String endDate = String.format(Locale.FRANCE, "%02d/%02d", lastDayOfMonth, month) + "/" + year;
                String endDateProduct = year + "-" + String.format(Locale.FRANCE,  "%02d-%02d", month, lastDayOfMonth) + " 23:59:59";
                mStatisticViewModel.getShopStatistics(mShopId, startDate, endDate);
                mViewModel.getMvpProducts(startDateProduct, endDateProduct, mShopId);
                mViewModel.getCancellationStatistics(startDateProduct, endDateProduct, mShopId);
            }else {
                mBinding.layoutPie.setVisibility(View.VISIBLE);
            }

        } catch (ParseException parseException) {
            parseException.printStackTrace();
            App.handleError(parseException);
        }
    }

    @Override
    public void onNothingSelected() {
        mBinding.layoutPie.setVisibility(View.GONE);
        mBinding.layoutProducts.setVisibility(View.GONE);
        mBinding.layoutCancellations.setVisibility(View.GONE);

    }

    private String getMonthById(int id){
        switch (id){
            case 1:
                return "Janvier";
            case 2:
                return "Février";
            case 3:
                return "Mars";
            case 4:
                return "Avril";
            case 5:
                return "Mai";
            case 6:
                return "Juin";
            case 7:
                return "Juillet";
            case 8:
                return "Aout";
            case 9:
                return "Septembre";
            case 10:
                return "Octobre";
            case 11:
                return "Novembre";
            case 12:
                return "Décembre";
            default:
                return null;
        }
    }
}
