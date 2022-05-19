package com.israel.livraisonexpresspos.ui.cart;

import com.israel.livraisonexpresspos.models.Cart;

public interface OnCartItemChangeListener {
    void onDelete(Cart cart);

    void onChange(Cart cart);
}
