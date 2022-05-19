package com.israel.livraisonexpresspos.uiComponents;

import android.widget.AutoCompleteTextView;

import java.util.List;

public class AutoCompleteValidator implements AutoCompleteTextView.Validator {
    private final List<String> validItems;

    public AutoCompleteValidator(List<String> validItems) {
        this.validItems = validItems;
    }

    @Override
    public boolean isValid(CharSequence text) {
        for (String s : validItems){
            if (s.equals(text.toString())){
                return true;
            }
        }
        return false;
    }

    @Override
    public CharSequence fixText(CharSequence invalidText) {
        return "";
    }
}
