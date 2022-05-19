package com.israel.livraisonexpresspos.data.Room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.israel.livraisonexpresspos.models.Order;
import com.israel.livraisonexpresspos.models.OrderWithCarts;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert()
    long insert(Order order);

    @Delete
    void deleteOrder(Order order);

    @Update
    void updateOrder(Order order);

    @Query("SELECT * FROM order_table")
    LiveData<List<Order>> getOrders();

    @Query("SELECT * FROM order_table WHERE status = :status")
    LiveData<List<Order>> getUnSyncedOrders(String status);

    @Query("DELETE FROM order_table WHERE (:currentTime - dateTime) >= 86400000")
    void deleteOldOrders(long currentTime);

    @Transaction
    @Query("SELECT * FROM order_table WHERE ((:currentTime - dateTime) < 86400000)")
    LiveData<List<OrderWithCarts>> getOrdersWithChild(long currentTime);
}
