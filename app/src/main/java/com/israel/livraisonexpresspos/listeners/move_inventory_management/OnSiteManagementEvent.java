package com.israel.livraisonexpresspos.listeners.move_inventory_management;

public interface OnSiteManagementEvent {
    void onAddSiteIntent();
    void onSiteItemDetailsIntent(long itemPosition);
    void onSiteItemEditIntent(long itemPosition);
}
