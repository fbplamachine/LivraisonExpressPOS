package com.israel.livraisonexpresspos.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;

import java.util.ArrayList;
import java.util.List;

public class MultiSelectionEditText extends AppCompatEditText implements
        DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private List<String> items  = new ArrayList<>();
    private boolean[] selected;

    public MultiSelectionEditText(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public MultiSelectionEditText(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    public MultiSelectionEditText(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (isChecked)
            selected[which] = true;
        else
            selected[which] = false;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner
        StringBuffer spinnerBuffer = new StringBuffer();
        boolean someSelected = false;
        for (int i = 0; i < items.size(); i++) {
            if (selected[i]) {
                spinnerBuffer.append(items.get(i));
                spinnerBuffer.append(", ");
                someSelected = true;
            }
        }
        String spinnerText = "";
        if (someSelected) {
            spinnerText = spinnerBuffer.toString();
            if (spinnerText.length() > 2)
                spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        }
        setText(spinnerText);
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(
                items.toArray(new CharSequence[items.size()]), selected, this);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.setOnCancelListener(this);
        builder.show();
        return true;
    }

    public void setItems(List<String> items) {
        this.items = items;
        selected = new boolean[items.size()];
    }

    public void setServices(String services){
        String[] serviceList = services.toUpperCase().split(",");
        for (int i = 0; i < items.size(); i++){
            for (String s : serviceList){
                if (s.equals(items.get(i))) {
                    selected[i] = true;
                    break;
                }
            }
        }
    }
}
