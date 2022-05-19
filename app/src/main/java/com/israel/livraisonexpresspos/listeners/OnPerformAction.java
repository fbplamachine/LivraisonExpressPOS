package com.israel.livraisonexpresspos.listeners;

import androidx.annotation.Nullable;

public interface OnPerformAction {
    void performAction(String role, @Nullable String motif, @Nullable String date, @Nullable String time);
}
