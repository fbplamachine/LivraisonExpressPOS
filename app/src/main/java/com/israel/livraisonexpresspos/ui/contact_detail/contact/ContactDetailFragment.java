package com.israel.livraisonexpresspos.ui.contact_detail.contact;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.FragmentContactDetailBinding;
import com.israel.livraisonexpresspos.models.Address;
import com.israel.livraisonexpresspos.models.Contact;
import com.israel.livraisonexpresspos.ui.address.AddressAdapter;
import com.israel.livraisonexpresspos.ui.address.AddressDialogFragment;
import com.israel.livraisonexpresspos.ui.address.OnAddressAdded;
import com.israel.livraisonexpresspos.ui.contact_detail.ContactDetailActivity;
import com.israel.livraisonexpresspos.uiComponents.Utilities;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ContactDetailFragment extends Fragment implements View.OnClickListener
        , OnAddressAdded {
    private FragmentContactDetailBinding mBinding;
    private Contact mContact;
    private ContactDetailActivity mActivity;
    private ContactDetailViewModel mViewModel;
    private ProgressDialog mDialog;
    private AddressAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_detail, container, false);
        mActivity = (ContactDetailActivity) requireActivity();
        mContact = mActivity.getContact();
        mViewModel = new ViewModelProvider(this).get(ContactDetailViewModel.class);
        initUI();
        stream();
        return mBinding.getRoot();
    }

    private void initUI() {
        mBinding.setContact(mContact);
        mDialog = Utilities.getProgressDialog(requireActivity());
        mBinding.ivAdd.setOnClickListener(this);
        List<String> services = new ArrayList<>();
        services.add("LIVRAISON");
        services.add("GAZ");
        services.add("RESTAURANT");
        services.add("SUPER MARCHé".toUpperCase());
        services.add("PHARMACIE");
        services.add("CADEAU");
        services.add("LIBRERIE");
        services.add("FLEURISTE");
        mBinding.etServices.setItems(services);
        initAddressList();
    }

    private void stream() {
        mViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading){
                mDialog.show();
            }else {
                mDialog.dismiss();
            }
        });

        mViewModel.getSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success){
                Toasty.success(getContext(), "Vos modifications ont été enregistrées.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initAddressList() {
        mAdapter = new AddressAdapter(mContact.getAdresses(), mActivity.getSupportFragmentManager());
        mBinding.rvAddress.setAdapter(mAdapter);
        mBinding.rvAddress.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBinding.ivAdd.getId()){
            addAddress();
        }
    }

    @Override
    public void onAddressAdded(Address address) {
        mAdapter.addNewAddress(address);
    }

    @Override
    public void onAddressAddedPersistence(Address address) {

    }

    public void addAddress() {
        AddressDialogFragment dialogFragment = new AddressDialogFragment();
        Bundle args = new Bundle();
        Address address = new Address();
        address.setClient_id(Integer.parseInt(mContact.getId()));
        address.setProvider_name(mContact.getProvider_name());
        args.putParcelable(AddressDialogFragment.ADDRESS_ARGUMENT, address);
        dialogFragment.setArguments(args);
        dialogFragment.show(mActivity.getSupportFragmentManager(), AddressDialogFragment.class.getSimpleName());
    }

    private boolean verify(){
        boolean validator = true;

        if (TextUtils.isEmpty(mBinding.etName.getText())){
            mBinding.etName.setError(getString(R.string.enter_name));
            validator = false;
        }else {
            mBinding.etName.setError(null);
        }

        if (!TextUtils.isEmpty(mBinding.etEmail.getText()) && !Patterns.EMAIL_ADDRESS.matcher(mBinding.etEmail.getText()).matches()){
            mBinding.etEmail.setError(getString(R.string.enter_valid_email));
            validator = false;
        }else {
            mBinding.etEmail.setError(null);
        }

        if (TextUtils.isEmpty(mBinding.etPhone1.getText())){
            mBinding.etPhone1.setError(getString(R.string.enter_phone_1));
            validator = false;
        }else {
            mBinding.etPhone1.setError(null);
        }
        return validator;
    }

    public void updateContact(){
        if (verify()){
            mContact.setFullname(mBinding.etName.getText().toString());
            mContact.setEmail(mBinding.etEmail.getText().toString());
            mContact.setTelephone(mBinding.etPhone1.getText().toString());
            mContact.setTelephone_alt(mBinding.etPhone2.getText().toString());
            mContact.setModules(mBinding.etServices.getText().toString());
            mViewModel.putContact(mContact);
        }
    }
}
