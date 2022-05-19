package com.israel.livraisonexpresspos.ui.checkout.steps;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import com.israel.livraisonexpresspos.databinding.LayoutCheckoutStep2Binding;
import com.israel.livraisonexpresspos.models.Address;
import com.israel.livraisonexpresspos.models.Contact;
import com.israel.livraisonexpresspos.models.Quarter;
import com.israel.livraisonexpresspos.ui.checkout.CheckoutActivity;
import com.israel.livraisonexpresspos.ui.checkout.SelectAddressDialog;
import com.israel.livraisonexpresspos.ui.select_contact.SelectContactActivity;
import com.israel.livraisonexpresspos.utils.Values;

import java.util.ArrayList;
import java.util.List;

public class StepReceiver implements
        CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    public static final int RECEIVER_REQUEST_CODE = 2;
    private final CheckoutActivity mActivity;
    private final LayoutCheckoutStep2Binding mBinding;
    private Address mReceiverAddress;
    private double mLatitude, mLongitude;
    private List<Quarter> mQuarters = new ArrayList<>();

    public StepReceiver(CheckoutActivity activity, LayoutCheckoutStep2Binding binding) {
        mActivity = activity;
        mBinding = binding;
        init();
    }

    public LayoutCheckoutStep2Binding getBinding() {
        return mBinding;
    }

    private void init() {
        mBinding.cbNewAddress.setOnCheckedChangeListener(this);
        mBinding.etCity.setText(Values.city.toUpperCase());
        mBinding.tvImportReceiver.setOnClickListener(this);
        mBinding.tvImportReceiverAddress.setOnClickListener(this);
        handleReceiverImport(Values.receiver);
        handleAutocomplete();
    }

    private void handleAutocomplete() {
        mQuarters = Values.city.equalsIgnoreCase("douala") ? Quarter.DOUALA : Quarter.YAOUNDE;
        List<String> strings = new ArrayList<>();
        for (Quarter q : mQuarters) {
            strings.add(q.getLibelle());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_list_item_1
                , strings);
        mBinding.etQuarter.setAdapter(adapter);
    }

    private void handleReceiverImport(Contact contact){
        if (contact == null || contact.getAdresses() == null || contact.getAdresses().isEmpty()){
            mBinding.tvImportReceiverAddress.setVisibility(View.GONE);
            mBinding.cbNewAddress.setVisibility(View.GONE);
        }else {
            mBinding.tvImportReceiverAddress.setVisibility(View.VISIBLE);
            mBinding.cbNewAddress.setVisibility(View.VISIBLE);
        }
    }

    public Contact setReceiver(){
        Contact receiver = mBinding.getReceiver();
        receiver.setFullname(mBinding.etFullName.getText().toString());
        receiver.setTelephone(mBinding.etPhone1.getText().toString());
        receiver.setTelephone_alt(mBinding.etPhone2.getText().toString());
        receiver.setEmail(mBinding.etEmail.getText().toString());

        List<Address> addresses = new ArrayList<>();
        if (mReceiverAddress == null || mBinding.cbNewAddress.isChecked())mReceiverAddress = new Address();
        mReceiverAddress.setQuartier(mBinding.etQuarter.getText().toString().toUpperCase());
        for (Quarter q : mQuarters){
            if (q.getLibelle().equalsIgnoreCase(mReceiverAddress.getQuartier())){
                mReceiverAddress.setQuartier_id(q.getId());
            }
        }
        mReceiverAddress.setDescription(mBinding.etDescription.getText().toString());
        mReceiverAddress.setClient_id(Integer.parseInt(receiver.getId()));
        if (TextUtils.isEmpty(mReceiverAddress.getVille()))mReceiverAddress.setVille(Values.city);
        if (mReceiverAddress.getVille_id() == null)mReceiverAddress.setVille_id(Values.getCityId(mActivity));
        addresses.add(mReceiverAddress);
        receiver.setAdresses(addresses);
        return receiver;
    }

    public void getSelectedAddress(Address address){
        mReceiverAddress = address;
        mBinding.setReceiverAddress(address);
    }

    public void getSelectedContact(Contact contact){
        Values.receiver = contact;
        mBinding.setReceiverAddress(null);
        if (Values.receiver == null)return;
        mActivity.updateOrder(Values.order);
        if (Values.receiver.getAdresses() != null && Values.receiver.getAdresses().size() > 0){
            mBinding.setReceiverAddress(Values.receiver.getAdresses().get(0));
            mReceiverAddress = Values.receiver.getAdresses().get(0);
        }
        mBinding.setReceiver(Values.receiver);
        handleReceiverImport(contact);
    }

    public boolean validateStep() {
        boolean validator = true;
        if (TextUtils.isEmpty(mBinding.etFullName.getText())) {
            validator = false;
            mBinding.etFullName.setError("Veuillez entrer le nom.");
        } else {
            mBinding.etFullName.setError(null);
        }
        if (TextUtils.isEmpty(mBinding.etPhone1.getText())) {
            validator = false;
            mBinding.etPhone1.setError("Veuillez entrer le téléphone");
        } else {
            mBinding.etPhone1.setError(null);
        }
        if (!TextUtils.isEmpty(mBinding.etEmail.getText())
                && !Patterns.EMAIL_ADDRESS.matcher(mBinding.etEmail.getText()).matches()) {
            validator = false;
            mBinding.etEmail.setError("Veuillez entrer un email valide.");
        } else {
            mBinding.etEmail.setError(null);
        }
        if (TextUtils.isEmpty(mBinding.etQuarter.getText())) {
            validator = false;
            mBinding.etQuarter.setError("Veuillez entrer le quartier");
        } else {
            mBinding.etQuarter.setError(null);
        }
        return validator;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mBinding.setReceiverAddress(isChecked ? null : mReceiverAddress);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBinding.tvImportReceiver.getId()){
            Intent intent = new Intent(mActivity, SelectContactActivity.class);
            intent.putExtra(CheckoutActivity.FROM_CHECKOUT, true);
            mActivity.startActivityForResult(intent, RECEIVER_REQUEST_CODE);
        }else if (id == mBinding.tvImportReceiverAddress.getId()){
            SelectAddressDialog dialog = new SelectAddressDialog();
            Bundle args = new Bundle();
            args.putInt("step", 2);
            dialog.setArguments(args);
            dialog.show(mActivity.getSupportFragmentManager(), "SelectAddressDialog");
        }
    }
}