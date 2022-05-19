package com.israel.livraisonexpresspos.uiComponents;

import android.view.View;
import android.widget.AutoCompleteTextView;

public class AutoCompleteFocusListener implements View.OnFocusChangeListener {
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus && v instanceof AutoCompleteTextView){
            ((AutoCompleteTextView) v).performValidation();
        }
    }
}
