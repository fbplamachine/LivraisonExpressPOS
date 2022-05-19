package com.israel.livraisonexpresspos.ui.address;

import com.israel.livraisonexpresspos.models.Address;

public interface OnAddressAdded {
    void onAddressAdded(Address address);
    void onAddressAddedPersistence(Address address);
}
