package com.israel.livraisonexpresspos.data.Room.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.israel.livraisonexpresspos.data.Room.LivrexRoomDatabase;
import com.israel.livraisonexpresspos.data.Room.dao.CartDao;
import com.israel.livraisonexpresspos.models.Cart;

import java.util.List;

public class CartRepository {
    private final CartDao mCartDao;

    public CartRepository(Application application){
        LivrexRoomDatabase db = LivrexRoomDatabase.getDatabase(application);
        mCartDao = db.mCartDao();
    }

    public CartDao getCartDao() {
        return mCartDao;
    }

    public LiveData<List<Cart>> getAllLiveItems(int orderId, int shopId){
        return mCartDao.getLiveItems(orderId, shopId);
    }

    public void insert(Cart cart){
        insertAsync(cart);
    }

    public LiveData<List<Long>> getAllTotalPrices(int orderId){
        return mCartDao.getAllTotalPrices(orderId);
    }

    public void delete(Cart cart){
        deleteAsync(cart);
    }

    public void update(Cart cart){
        updateAsync(cart);
    }

    public void deleteUnUsedItems(int orderId, int shopId) {
        new Thread(() -> mCartDao.deleteUnUsedItems(orderId, shopId)).start();
    }

    private void insertAsync(Cart cart){
        new Thread(() -> mCartDao.insert(cart)).start();
    }

    private void deleteAsync(Cart cart){
        new Thread(() -> mCartDao.delete(cart)).start();
    }

    private void updateAsync(Cart cart){
        new Thread(() -> mCartDao.update(cart)).start();
    }
}
