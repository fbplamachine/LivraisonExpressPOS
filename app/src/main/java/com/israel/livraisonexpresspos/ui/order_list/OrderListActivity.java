package com.israel.livraisonexpresspos.ui.order_list;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.databinding.ActivityOrderListBinding;
import com.israel.livraisonexpresspos.models.OrderWithCarts;
import com.israel.livraisonexpresspos.ui.place_order.OrderAdapter;

import java.util.List;

public class OrderListActivity extends AppCompatActivity {
    private ActivityOrderListBinding mBinding;
    private OrderViewModel mViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_list);
        initToolbar();
        initUI();
        stream();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.currentActivity = this;
    }

    private void initToolbar() {
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initUI() {
        mViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        mBinding.rvOrder.setLayoutManager(new LinearLayoutManager(this));
    }

    private void stream(){
        mViewModel.getOrders().observe(this, new Observer<List<OrderWithCarts>>() {
            @Override
            public void onChanged(List<OrderWithCarts> orders) {
                if (orders == null)return;
                Log.e("ORDER1", orders.toString());
//                for (OrderWithCarts o : orders) {
//                    o.setCarts(mViewModel.getItems(o.getId()));
//                }
                OrderAdapter adapter = null;
                mBinding.rvOrder.setAdapter(adapter);
            }
        });
    }
}