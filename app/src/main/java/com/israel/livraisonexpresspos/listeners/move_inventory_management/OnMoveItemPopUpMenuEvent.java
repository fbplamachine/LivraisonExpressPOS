package com.israel.livraisonexpresspos.listeners.move_inventory_management;

public interface OnMoveItemPopUpMenuEvent {
    void onItemDelete(int itemId);

    void onItemDetailsIntent(int itemId);

    void onCloseFullScreenFragment();
}
