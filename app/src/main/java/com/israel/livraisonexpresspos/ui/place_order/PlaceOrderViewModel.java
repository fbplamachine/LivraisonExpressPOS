package com.israel.livraisonexpresspos.ui.place_order;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.israel.livraisonexpresspos.data.Room.repository.OrderRepository;
import com.israel.livraisonexpresspos.models.Cart;
import com.israel.livraisonexpresspos.models.Delivery;
import com.israel.livraisonexpresspos.models.Order;
import com.israel.livraisonexpresspos.models.OrderWithCarts;
import com.israel.livraisonexpresspos.utils.OrderStatus;
import com.israel.livraisonexpresspos.utils.Values;

import java.util.Calendar;
import java.util.List;

public class PlaceOrderViewModel extends AndroidViewModel {
    private final LiveData<List<OrderWithCarts>> mOrders;
    private final OrderRepository mOrderRepository;

    public PlaceOrderViewModel(@NonNull Application application) {
        super(application);
        mOrderRepository = new OrderRepository(application);
        mOrders = mOrderRepository.getAllOrders();
    }

    public LiveData<List<OrderWithCarts>> getOrders() {
        return mOrders;
    }

    public void deleteOrder(Order order){
        mOrderRepository.deleteOrder(order);
    }

    public void duplicateOrder(OrderWithCarts orderWithCarts){
        OrderRepository repository = new OrderRepository(getApplication());
        Order oldOrder = orderWithCarts.mOrder;
        Order newOrder = new Order();
        newOrder.setCity(oldOrder.getCity());
        newOrder.setShop(oldOrder.getShop());
        newOrder.setShopId(oldOrder.getShopId());
        newOrder.setShopObject(oldOrder.getShopObject());
        newOrder.setModuleObject(oldOrder.getModuleObject());
        newOrder.setModuleSlug(oldOrder.getModuleSlug());
        Delivery oldDelivery = Values.stringToDelivery(oldOrder.getStringDelivery());
        Delivery newDelivery = new Delivery();
        newDelivery.setSender(oldDelivery.getSender());
        newOrder.setStringDelivery(Values.deliveryToString(newDelivery));
        newOrder.setDateTime(Calendar.getInstance().getTimeInMillis());
        newOrder.setStatus(OrderStatus.saved.toString());
        orderWithCarts.mOrder = newOrder;
        for(Cart c : orderWithCarts.mCarts){
            c.setId(0);
        }
        repository.insertOrderWithCarts(orderWithCarts);
        if (mOrders.getValue() == null)return;
    }
}