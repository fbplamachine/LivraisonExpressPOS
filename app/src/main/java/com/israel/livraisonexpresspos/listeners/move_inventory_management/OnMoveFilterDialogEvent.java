package com.israel.livraisonexpresspos.listeners.move_inventory_management;

import com.israel.livraisonexpresspos.models.inventory_management_models.FilterShop;

public interface OnMoveFilterDialogEvent {
    void onFilterShopSelected(FilterShop filterShop);
}
