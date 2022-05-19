package com.israel.livraisonexpresspos.ui.address;

import com.israel.livraisonexpresspos.models.Address;

public interface OnAddressUpdate {
     void onAddressUpdated(Address address, long addressPosition);
     void onAddressUpdatesPersistence(Address address, long position);
}
