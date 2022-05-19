package com.israel.livraisonexpresspos.ui.cart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.israel.livraisonexpresspos.data.Room.repository.CartRepository;
import com.israel.livraisonexpresspos.models.Cart;

import java.util.List;

public class CartViewModel extends AndroidViewModel {
    private final CartRepository mCartRepository;

    public CartViewModel(@NonNull Application application) {
        super(application);
        mCartRepository = new CartRepository(application);
    }

    public LiveData<List<Cart>> getItems(int orderId, int shopId){
        return mCartRepository.getAllLiveItems(orderId, shopId);
    }

    public void deleteCartItem(Cart cart){
        mCartRepository.delete(cart);
    }

    public LiveData<List<Long>> getAllTotals(int orderId){
        return mCartRepository.getAllTotalPrices(orderId);
    }

    public void updateCartItem(Cart cart){
        mCartRepository.update(cart);
    }
}
