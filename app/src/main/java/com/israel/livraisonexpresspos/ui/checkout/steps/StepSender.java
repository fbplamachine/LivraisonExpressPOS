package com.israel.livraisonexpresspos.ui.checkout.steps;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import com.israel.livraisonexpresspos.databinding.LayoutCheckoutStep1Binding;
import com.israel.livraisonexpresspos.models.Address;
import com.israel.livraisonexpresspos.models.Contact;
import com.israel.livraisonexpresspos.models.Quarter;
import com.israel.livraisonexpresspos.ui.checkout.CheckoutActivity;
import com.israel.livraisonexpresspos.ui.checkout.SelectAddressDialog;
import com.israel.livraisonexpresspos.ui.select_contact.SelectContactActivity;
import com.israel.livraisonexpresspos.utils.Values;

import java.util.ArrayList;
import java.util.List;

public class StepSender implements
        CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    public static final int SENDER_REQUEST_CODE = 1;
    private final CheckoutActivity mActivity;
    private final LayoutCheckoutStep1Binding mBinding;
    private Address mSenderAddress;
    private double mLatitude, mLongitude;
    private List<Quarter> mQuarters = new ArrayList<>();

    public StepSender(CheckoutActivity activity, LayoutCheckoutStep1Binding binding) {
        mActivity = activity;
        mBinding = binding;
        init();
    }

    public LayoutCheckoutStep1Binding getBinding() {
        return mBinding;
    }

    private void init() {
        mBinding.cbSenderNewAddress.setOnCheckedChangeListener(this);
        mBinding.etSenderCity.setText(Values.city.toUpperCase());
        mBinding.tvImportSender.setOnClickListener(this);
        mBinding.tvImportSenderAddress.setOnClickListener(this);
        initSenderAddress();
        handleSenderImport(Values.sender);
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
        mBinding.etSenderQuarter.setAdapter(adapter);
    }

    private void handleSenderImport(Contact contact) {
        if (contact == null || contact.getAdresses() == null || contact.getAdresses().isEmpty()) {
            mBinding.tvImportSenderAddress.setVisibility(View.GONE);
            mBinding.cbSenderNewAddress.setVisibility(View.GONE);
        } else {
            mBinding.tvImportSenderAddress.setVisibility(View.VISIBLE);
            mBinding.cbSenderNewAddress.setVisibility(View.VISIBLE);
        }
    }

    private void initSenderAddress() {
        if (Values.sender == null || Values.sender.getAdresses() == null || Values.sender.getAdresses().isEmpty()) return;
        mSenderAddress = Values.sender.getAdresses().get(0);
        mBinding.setSenderAddress(mSenderAddress);
    }

    public Contact setSender() {
        Contact sender = mBinding.getSender();
        if (sender == null) sender = new Contact();
        sender.setFullname(mBinding.etSenderFullName.getText().toString());
        sender.setTelephone(mBinding.etSenderPhone1.getText().toString());
        sender.setTelephone_alt(mBinding.etSenderPhone2.getText().toString());
        sender.setEmail(mBinding.etSenderEmail.getText().toString());

        List<Address> addresses = new ArrayList<>();
        if (mSenderAddress == null)mSenderAddress = new Address();
        mSenderAddress.setQuartier(mBinding.etSenderQuarter.getText().toString().toUpperCase());
        for (Quarter q : mQuarters){
            if (q.getLibelle().equalsIgnoreCase(mSenderAddress.getQuartier())){
                mSenderAddress.setQuartier_id(q.getId());
            }
        }
        mSenderAddress.setDescription(mBinding.etSenderDescription.getText().toString());
        mSenderAddress.setClient_id(Integer.parseInt(sender.getId()));
        if (TextUtils.isEmpty(mSenderAddress.getVille()))mSenderAddress.setVille(Values.city);
        if (mSenderAddress.getVille_id() == 0)mSenderAddress.setVille_id(Values.getCityId(mActivity));
        if (mBinding.cbSenderNewAddress.isChecked()){
            mSenderAddress.setId(0);
        }
        addresses.add(mSenderAddress);
        sender.setAdresses(addresses);
        return sender;
    }

    public void getSelectedAddress(Address address) {
        mSenderAddress = address;
        mBinding.setSenderAddress(address);
    }

    public void getSelectedContact(Contact contact){
        Values.sender = contact;
        mBinding.setSenderAddress(null);
        if (Values.sender == null)return;
        if (Values.sender.getAdresses() != null && Values.sender.getAdresses().size() > 0){
            mBinding.setSenderAddress(Values.sender.getAdresses().get(0));
            mSenderAddress = Values.sender.getAdresses().get(0);
        }
        mBinding.setSender(Values.sender);
        handleSenderImport(contact);
    }

    public boolean validateStep() {
        boolean validator = true;
        if (TextUtils.isEmpty(mBinding.etSenderFullName.getText())) {
            validator = false;
            mBinding.etSenderFullName.setError("Veuillez entrer le nom.");
        } else {
            mBinding.etSenderFullName.setError(null);
        }
        if (TextUtils.isEmpty(mBinding.etSenderPhone1.getText())) {
            validator = false;
            mBinding.etSenderPhone1.setError("Veuillez entrer le téléphone");
        } else {
            mBinding.etSenderPhone1.setError(null);
        }
        if (!TextUtils.isEmpty(mBinding.etSenderEmail.getText())
                && !Patterns.EMAIL_ADDRESS.matcher(mBinding.etSenderEmail.getText()).matches()) {
            validator = false;
            mBinding.etSenderEmail.setError("Veuillez entrer un email valide.");
        } else {
            mBinding.etSenderEmail.setError(null);
        }
        if (TextUtils.isEmpty(mBinding.etSenderQuarter.getText())) {
            validator = false;
            mBinding.etSenderQuarter.setError("Veuillez entrer le quartier");
        } else {
            mBinding.etSenderQuarter.setError(null);
        }
        return validator;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mBinding.setSenderAddress(isChecked ? null : mSenderAddress);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBinding.tvImportSender.getId()) {
            Intent intent = new Intent(mActivity, SelectContactActivity.class);
            intent.putExtra(CheckoutActivity.FROM_CHECKOUT, true);
            mActivity.startActivityForResult(intent, SENDER_REQUEST_CODE);
        } else if (id == mBinding.tvImportSenderAddress.getId()) {
            SelectAddressDialog dialog = new SelectAddressDialog();
            Bundle args = new Bundle();
            args.putInt("step", 1);
            dialog.setArguments(args);
            dialog.show(mActivity.getSupportFragmentManager(), "SelectAddressDialog");
        }
    }
}