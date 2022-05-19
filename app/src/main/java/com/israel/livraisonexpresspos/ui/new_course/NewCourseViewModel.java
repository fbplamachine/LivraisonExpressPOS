package com.israel.livraisonexpresspos.ui.new_course;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.israel.livraisonexpresspos.data.Room.repository.CartRepository;
import com.israel.livraisonexpresspos.models.Cart;
import com.israel.livraisonexpresspos.utils.Values;

import java.util.List;

public class NewCourseViewModel extends AndroidViewModel {
    private final CartRepository mCartRepository;

    public NewCourseViewModel(@NonNull Application application) {
        super(application);
        mCartRepository = new CartRepository(application);
    }

    public void insertCart(Cart cart){
        mCartRepository.insert(cart);
    }

    public LiveData<List<Cart>> getCartItems() {
        if (Values.order == null || Values.shop == null){
            return null;
        }
        return mCartRepository.getAllLiveItems(Values.order.getId(), Values.shop.getId());
    }
}
