package com.israel.livraisonexpresspos.ui.select_contact;

import com.israel.livraisonexpresspos.models.Contact;

public interface OnContactSelectedListener {
    void onContactSelected(Contact contact, boolean newOrder);
}
