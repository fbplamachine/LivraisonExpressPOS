package com.israel.livraisonexpresspos.data.Room.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.israel.livraisonexpresspos.data.Room.LivrexRoomDatabase;
import com.israel.livraisonexpresspos.data.Room.dao.OrderDao;
import com.israel.livraisonexpresspos.models.Cart;
import com.israel.livraisonexpresspos.models.Order;
import com.israel.livraisonexpresspos.models.OrderWithCarts;
import com.israel.livraisonexpresspos.utils.OrderStatus;
import com.israel.livraisonexpresspos.utils.Values;

import java.util.Calendar;
import java.util.List;

public class OrderRepository {
    private final OrderDao mOrderDao;
    private final LiveData<List<OrderWithCarts>> mAllOrders;
    private final LiveData<List<Order>> mUnSyncedOrders;
    private final CartRepository mCartRepository;

    public OrderRepository(Application application){
        LivrexRoomDatabase db = LivrexRoomDatabase.getDatabase(application);
        mOrderDao = db.mOrderDao();
        long currentTimeMillis = Calendar.getInstance().getTimeInMillis();
        mAllOrders = mOrderDao.getOrdersWithChild(currentTimeMillis);
        deleteOldOrdersAsync(currentTimeMillis);
        mUnSyncedOrders = mOrderDao.getUnSyncedOrders(OrderStatus.pending.toString());
        mCartRepository = new CartRepository(application);
    }

    public LiveData<List<OrderWithCarts>> getAllOrders(){
        return mAllOrders;
    }

    public LiveData<List<Order>> getUnSyncedOrders() {
        return mUnSyncedOrders;
    }

    public void insertOrderWithCarts(OrderWithCarts order){
        insertOrderAndCartAsync(order);
    }

    public void update(Order order){
        updateAsync(order);
    }

    public void deleteOrder(Order order){
        deleteAsync(order);
    }

    private void insertOrderAndCartAsync(OrderWithCarts order){
        new Thread(() -> {
            long orderId = mOrderDao.insert(order.mOrder);
            for (Cart c : order.mCarts){
                c.setOrderId((int) orderId);
                mCartRepository.insert(c);
            }
            if (Values.order == null)return;
            if (Values.order.getId() == 0) Values.order.setId((int) orderId);
        }).start();
    }

    private void updateAsync(Order order){
        new Thread(() -> mOrderDao.updateOrder(order)).start();
    }

    private void deleteAsync(Order order){
        new Thread(() -> mOrderDao.deleteOrder(order)).start();
    }

    private void deleteOldOrdersAsync(long currentTime){
        new Thread(() -> mOrderDao.deleteOldOrders(currentTime));
    }
}
