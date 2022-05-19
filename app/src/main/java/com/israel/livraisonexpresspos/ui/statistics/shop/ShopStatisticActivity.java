package com.israel.livraisonexpresspos.ui.statistics.shop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.databinding.ActivityShopStatisticBinding;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.models.from_steed_app.Badge;
import com.israel.livraisonexpresspos.models.from_steed_app.OrderSteed;
import com.israel.livraisonexpresspos.ui.filter.FilterViewModel;
import com.israel.livraisonexpresspos.ui.my_shops.MyShopsAdapter;
import com.israel.livraisonexpresspos.ui.orders.OrderListAdapter;
import com.israel.livraisonexpresspos.utils.CourseStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ShopStatisticActivity extends AppCompatActivity implements DateRangeListener {
    private ActivityShopStatisticBinding mBinding;
    private StatisticViewModel mViewModel;
    private FilterViewModel mFilterViewModel;
    private ProgressDialog mDialog;
    private Intent mIntent;
    private int mId;
    private OrderListAdapter mAdapter;
    private Calendar mCalendar;
    private String mToday;
    private int option = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_shop_statistic);
        mIntent = getIntent();
        if (mIntent == null) return;
        initToolbar();
        initUi();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        String startDate = "";
        String endDate = mToday;
        mCalendar = Calendar.getInstance();
        if (id == R.id.item_today){
            option = 1;
            startDate = mCalendar.get(Calendar.DAY_OF_MONTH) + "/" + (mCalendar.get(Calendar.MONTH) + 1) + "/" + mCalendar.get(Calendar.YEAR);
            mViewModel.getShopStatistics(mId, startDate, endDate);
        }else if (id == R.id.item_yesterday){
            option = 2;
            mCalendar.add(Calendar.DATE, -1);
            endDate = mCalendar.get(Calendar.DAY_OF_MONTH) + "/" + (mCalendar.get(Calendar.MONTH) + 1) + "/" + mCalendar.get(Calendar.YEAR);
            startDate = mCalendar.get(Calendar.DAY_OF_MONTH) + "/" + (mCalendar.get(Calendar.MONTH) + 1) + "/" + mCalendar.get(Calendar.YEAR);
            mViewModel.getShopStatistics(mId, startDate, endDate);
        }else if (id == R.id.item_week){
            option = 3;
            mCalendar.add(Calendar.DATE, -7);
            startDate = mCalendar.get(Calendar.DAY_OF_MONTH) + "/" + (mCalendar.get(Calendar.MONTH) + 1) + "/" + mCalendar.get(Calendar.YEAR);
            mViewModel.getShopStatistics(mId, startDate, endDate);
        }else if (id == R.id.item_month){
            option = 4;
            mCalendar.add(Calendar.DATE, -30);
            startDate = mCalendar.get(Calendar.DAY_OF_MONTH) + "/" + (mCalendar.get(Calendar.MONTH) + 1) + "/" + mCalendar.get(Calendar.YEAR);
            mViewModel.getShopStatistics(mId, startDate, endDate);
        }else if (id == R.id.item_other_date_range){
            option = 5;
            DateRangeDialog rangeDialog = new DateRangeDialog();
            rangeDialog.show(getSupportFragmentManager(), rangeDialog.getTag());
        }

        return super.onOptionsItemSelected(item);
    }

    private void initStatistics(){
        shadeSelectedStatus(mBinding.toolbar2);
        mBinding.rvCourse.setVisibility(View.GONE);
    }

    private void initUi() {
        initDialog();
        mId = mIntent.getIntExtra(MyShopsAdapter.SHOP_ID, 0);
        mCalendar = Calendar.getInstance();
        mToday = mCalendar.get(Calendar.DAY_OF_MONTH) + "/" + (mCalendar.get(Calendar.MONTH) + 1) + "/" + mCalendar.get(Calendar.YEAR);
        mAdapter = new OrderListAdapter(ShopStatisticActivity.this);
        mBinding.rvCourse.setAdapter(mAdapter);
        mBinding.rvCourse.setLayoutManager(new LinearLayoutManager(this));
        mViewModel = new ViewModelProvider(this).get(StatisticViewModel.class);
        mFilterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);
        mViewModel.getShopStatistics(mId, mToday, mToday);
        mViewModel.getBadge().observe(this, new Observer<Badge>() {
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
        mViewModel.getLoading().observe(this, new Observer<Boolean>() {
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
        mFilterViewModel.getOrderSteeds().observe(this, new Observer<List<OrderSteed>>() {
            @Override
            public void onChanged(List<OrderSteed> orderSteeds) {
                if (orderSteeds == null) return;
                mAdapter.setOrderSteeds(orderSteeds);
                mBinding.rvCourse.setVisibility(View.VISIBLE);
            }
        });
        mFilterViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
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
//        mBinding.fabDateFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DateRangeDialog rangeDialog = new DateRangeDialog();
//                rangeDialog.show(getSupportFragmentManager(), rangeDialog.getTag());
//            }
//        });
    }

    private void initDialog() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage(getString(R.string.loading));
        mDialog.setCancelable(false);
        mDialog.create();
    }

    @Override
    public void onDateRangeSet(String startDate, String endDate) {
        mViewModel.getShopStatistics(mId, startDate, endDate);
    }

    private void buildFilter(Integer status) {
        try {
            JSONObject constraint = new JSONObject();
            constraint.put("search_customer", "");
            constraint.put("ville", "");
            constraint.put("quartier_type", "0");
            constraint.put("quartier", "");
            constraint.put("tri_date", "desc");
            constraint.put("is_manager", User.isManager() || User.isPartner());
            if(status != null)constraint.put("status", String.valueOf(status));
            constraint.put("from_date", mViewModel.getStartDate());
            constraint.put("to_date", mViewModel.getEndDate());
            constraint.put("magasins", String.valueOf(mId));
            constraint.put("per_page", 200);
            mFilterViewModel.getOrders(constraint.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            App.handleError(e);
        }
    }

    public void getOrders(View view) {
        int id = view.getId();
        if (id == mBinding.layoutUnassigned.getId()) {
            buildFilter(CourseStatus.CODE_UNASSIGNED);
            shadeSelectedStatus(mBinding.layoutUnassigned);
        } else if (id == mBinding.layoutAssigned.getId()) {
            buildFilter(CourseStatus.CODE_ASSIGNED);
            shadeSelectedStatus(mBinding.layoutAssigned);
        } else if (id == mBinding.layoutRunning.getId()) {
            buildFilter(CourseStatus.CODE_INPROGRESS);
            shadeSelectedStatus(mBinding.layoutRunning);
        } else if (id == mBinding.layoutDelivered.getId()) {
            buildFilter(CourseStatus.CODE_DELIVERED);
            shadeSelectedStatus(mBinding.layoutDelivered);
        } else if (id == mBinding.layoutCancel.getId()) {
            buildFilter(CourseStatus.CODE_CANCELED);
            shadeSelectedStatus(mBinding.layoutCancel);
        } else if (id == mBinding.layoutTotal.getId()) {
            buildFilter(null);
            shadeSelectedStatus(mBinding.layoutTotal);
        }
    }

    private void shadeSelectedStatus(View selectedStatus){
        mBinding.layoutUnassigned.setBackgroundColor(ContextCompat.getColor(this, selectedStatus == mBinding.layoutUnassigned ? R.color.overlay_dark_10 : R.color.transparent));
        mBinding.layoutAssigned.setBackgroundColor(ContextCompat.getColor(this, selectedStatus == mBinding.layoutAssigned ? R.color.overlay_dark_10 : R.color.transparent));
        mBinding.layoutRunning.setBackgroundColor(ContextCompat.getColor(this, selectedStatus == mBinding.layoutRunning ? R.color.overlay_dark_10 : R.color.transparent));
        mBinding.layoutDelivered.setBackgroundColor(ContextCompat.getColor(this, selectedStatus == mBinding.layoutDelivered ? R.color.overlay_dark_10 : R.color.transparent));
        mBinding.layoutCancel.setBackgroundColor(ContextCompat.getColor(this, selectedStatus == mBinding.layoutCancel ? R.color.overlay_dark_10 : R.color.transparent));
        mBinding.layoutTotal.setBackgroundColor(ContextCompat.getColor(this, selectedStatus == mBinding.layoutTotal ? R.color.overlay_dark_10 : R.color.transparent));
    }
}