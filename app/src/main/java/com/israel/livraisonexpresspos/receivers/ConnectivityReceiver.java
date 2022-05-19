package com.israel.livraisonexpresspos.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.MainActivity;
import com.israel.livraisonexpresspos.data.Room.repository.OrderRepository;
import com.israel.livraisonexpresspos.models.Delivery;
import com.israel.livraisonexpresspos.models.Order;
import com.israel.livraisonexpresspos.ui.checkout.CheckoutViewModel;
import com.israel.livraisonexpresspos.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class ConnectivityReceiver extends BroadcastReceiver {
    private final LiveData<List<Order>> mOrders;
    private final MainActivity mActivity;
    private final CheckoutViewModel mViewModel;
    private final List<Integer> orderIds = new ArrayList<>();
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private ConnectivityManager.NetworkCallback mNetworkCallback;

    public ConnectivityReceiver(MainActivity activity){
        mActivity = activity;
        OrderRepository repository = new OrderRepository(mActivity.getApplication());
        mOrders = repository.getUnSyncedOrders();
        mViewModel = new ViewModelProvider(activity).get(CheckoutViewModel.class);
        handleCallback();
    }

    public void unregister(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            NetworkUtils.getConnectivityManager().unregisterNetworkCallback(mNetworkCallback);
        }else {
            mActivity.unregisterReceiver(this);
        }
    }

    private void handleCallback() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
//            mNetworkCallback = new ConnectivityManager.NetworkCallback(){
//                @Override
//                public void onAvailable(@NonNull Network network) {
//                    stream();
//                    super.onAvailable(network);
//                }
//
//                @Override
//                public void onLost(@NonNull Network network) {
//                    orderIds.clear();
//                    mOrders.removeObservers(mActivity);
//                    mViewModel.getSuccess().removeObservers(mActivity);
//                    super.onLost(network);
//                }
//            };
//            NetworkUtils.getConnectivityManager().registerDefaultNetworkCallback(mNetworkCallback);
//        }else {
//        }
        mActivity.registerReceiver(this, new IntentFilter(CONNECTIVITY_ACTION));
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        String intentAction = intent.getAction();
        if (intentAction != null){
            if (CONNECTIVITY_ACTION.equals(intentAction)) {
                if (NetworkUtils.isConnected()) {
                    stream();
                } else {
                    orderIds.clear();
                    mOrders.removeObservers(mActivity);
                    mViewModel.getSuccess().removeObservers(mActivity);
                }
            }
        }
    }

    private void stream() {
        mOrders.observe(mActivity, new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                if (orders == null) return;
                for (Order order : orders) {
                    boolean save = true;
                    for (int i : orderIds) {
                        if (i == order.getId()) {
                            save = false;
                            break;
                        }
                    }
                    if (!save) continue;
                    orderIds.add(order.getId());
                    Delivery delivery = new Gson().fromJson(order.getStringDelivery(), new TypeToken<Delivery>() {
                    }.getType());
                    Log.e("======DELIVERY", delivery.toString());
                    mViewModel.placeOrder(order, delivery);
                }
            }
        });

        mViewModel.getSuccess().observe(mActivity, new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                if (order == null) return;
                mViewModel.updateOrder(order);
            }
        });
    }
}
