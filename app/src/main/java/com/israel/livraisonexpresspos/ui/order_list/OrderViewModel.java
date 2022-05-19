package com.israel.livraisonexpresspos.ui.order_list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.israel.livraisonexpresspos.data.Room.repository.OrderRepository;
import com.israel.livraisonexpresspos.models.OrderWithCarts;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {
    private final LiveData<List<OrderWithCarts>> mOrders;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        OrderRepository repository = new OrderRepository(application);
        mOrders = repository.getAllOrders();
    }

    public LiveData<List<OrderWithCarts>> getOrders() {
        return mOrders;
    }

}
