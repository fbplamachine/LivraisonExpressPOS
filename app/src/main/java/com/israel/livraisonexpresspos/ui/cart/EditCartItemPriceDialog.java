package com.israel.livraisonexpresspos.ui.cart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.israel.livraisonexpresspos.databinding.FragmentEditCartItemPriceBinding;
import com.israel.livraisonexpresspos.models.Cart;


public class EditCartItemPriceDialog extends AppCompatDialogFragment implements View.OnClickListener {
    private FragmentEditCartItemPriceBinding mBinding;
    private OnCartItemChangeListener mChangeListener;
    private Cart mCart;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mBinding = FragmentEditCartItemPriceBinding.inflate(inflater);
        mChangeListener = (CartActivity) requireActivity();
        builder.setView(mBinding.getRoot());
        initUI();
        return builder.create();
    }

    private void initUI() {
        Bundle args = getArguments();
        mBinding.buttonSave.setOnClickListener(this);
        mBinding.buttonCancel.setOnClickListener(this);
        if (args == null)return;
        mCart = args.getParcelable(CartViewHolder.CART_ITEM);
        mBinding.etPrice.setText(String.valueOf(mCart.getMontant_total()));
        mBinding.tvTitle.setText(mCart.getLibelle());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBinding.buttonCancel.getId()){
            dismiss();
        }else if (id == mBinding.buttonSave.getId()){
            if (TextUtils.isEmpty(mBinding.etPrice.getText())){
                mBinding.tilPrice.setError("Veuillez entrer le prix du produit.");
            }else {
                int price = Integer.parseInt(mBinding.etPrice.getText().toString());
                if (mCart == null)return;
                mCart.setPrix_unitaire(price);
                mCart.setMontant_total(String.valueOf(price));
                mChangeListener.onChange(mCart);
                dismiss();
            }
        }
    }
}