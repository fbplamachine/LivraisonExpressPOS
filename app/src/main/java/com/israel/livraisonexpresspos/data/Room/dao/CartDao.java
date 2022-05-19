package com.israel.livraisonexpresspos.data.Room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.israel.livraisonexpresspos.models.Cart;

import java.util.List;

@Dao
public interface CartDao {
    @Insert()
    void insert(Cart cart);

    @Delete()
    void delete(Cart cart);

    @Query("DELETE FROM cart_table WHERE orderId = :orderId")
    void deleteAll(Integer orderId);

    @Query("DELETE FROM cart_table WHERE orderId = :orderId AND magasin_id != :shopId")
    void deleteUnUsedItems(Integer orderId, Integer shopId);

    @Query("SELECT * FROM cart_table WHERE orderId = :orderId AND magasin_id = :shopId")
    LiveData<List<Cart>> getLiveItems(Integer orderId, Integer shopId);

    @Query("SELECT * FROM cart_table WHERE orderId = :orderId AND magasin_id = :shopId")
    List<Cart> getItems(Integer orderId, Integer shopId);

    @Query("SELECT (montant_total * quantite) as total FROM cart_table WHERE orderId = :orderId")
    LiveData<List<Long>> getAllTotalPrices(int orderId);

    @Update
    void update(Cart item);
}
