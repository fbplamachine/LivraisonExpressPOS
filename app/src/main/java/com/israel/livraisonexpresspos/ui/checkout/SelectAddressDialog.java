package com.israel.livraisonexpresspos.ui.checkout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.israel.livraisonexpresspos.databinding.DialogSelectAddressBinding;
import com.israel.livraisonexpresspos.ui.address.AddressAdapter;
import com.israel.livraisonexpresspos.utils.Values;

public class SelectAddressDialog extends AppCompatDialogFragment implements View.OnClickListener {
    private DialogSelectAddressBinding mBinding;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mBinding = DialogSelectAddressBinding.inflate(inflater);
        builder.setView(mBinding.getRoot());
        initUI();
        return builder.create();
    }

    private void initUI() {
        mBinding.rvAddress.setLayoutManager(new LinearLayoutManager(requireContext()));
        int step = getArguments().getInt("step");
        mBinding.tvCancel.setOnClickListener(this);
        if (step == 0)return;
        AddressAdapter adapter;
        if (step == 1){
            adapter = new AddressAdapter(Values.sender.getAdresses(), this);
        }else {
            adapter = new AddressAdapter(Values.receiver.getAdresses(), this);
        }
        mBinding.rvAddress.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBinding.tvCancel.getId()){
            dismiss();
        }
    }
}
