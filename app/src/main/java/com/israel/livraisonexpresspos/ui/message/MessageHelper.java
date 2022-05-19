package com.israel.livraisonexpresspos.ui.message;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

public class MessageHelper {

    public void displayDialogMessageSelector(String phoneNumber, FragmentActivity activity, String userType) {
        MessageSelectorDialogFragment messageSelectorDialogFragment = new MessageSelectorDialogFragment();
        Bundle messageDialogArgument = new Bundle();
        messageDialogArgument.putString("phone_number", phoneNumber);
        messageDialogArgument.putString("user_type", userType);
        messageSelectorDialogFragment.setArguments(messageDialogArgument);
        messageSelectorDialogFragment.show(activity.getSupportFragmentManager(), messageSelectorDialogFragment.getTag());
    }

}
