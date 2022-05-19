package com.israel.livraisonexpresspos.ui.select_contact;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.databinding.ActivitySelectContactBinding;
import com.israel.livraisonexpresspos.models.Contact;
import com.israel.livraisonexpresspos.ui.checkout.CheckoutActivity;
import com.israel.livraisonexpresspos.ui.contacts.ContactListAdapter;
import com.israel.livraisonexpresspos.ui.contacts.ContactViewModel;
import com.israel.livraisonexpresspos.ui.new_contact.NewContactActivity;
import com.israel.livraisonexpresspos.utils.Values;

import java.util.ArrayList;
import java.util.List;

public class SelectContactActivity extends AppCompatActivity implements
        OnContactSelectedListener, View.OnClickListener {
    public static final String SELECTED_CONTACT = "selectedContact";
    public static final String FOR_CHECKOUT = "forCheckout";
    private ActivitySelectContactBinding mBinding;
    private ContactViewModel mViewModel;
    private ContactListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_contact);
        initToolbar();
        initUI();
        stream();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.currentActivity = this;
    }

    private void initToolbar() {
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initUI() {
        mAdapter = new ContactListAdapter(new ArrayList<Contact>(), true);
        mBinding.rvContacts.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvContacts.setAdapter(mAdapter);
        mViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        mBinding.fab.setOnClickListener(this);
        mBinding.ibSearch.setOnClickListener(this);
    }

    private void stream(){
        mViewModel.getContactList().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contact) {
                if (contact == null)return;
                mAdapter.setContacts(contact);
            }
        });

        mViewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == null)return;
                mBinding.progressBar.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
                mBinding.rvContacts.setVisibility(aBoolean ? View.GONE : View.VISIBLE);
            }
        });

        mViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s == null)return;
                Toast.makeText(SelectContactActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onContactSelected(Contact contact, boolean newOrder) {
        if (getIntent().getBooleanExtra(CheckoutActivity.FROM_CHECKOUT, false)){
            Intent intent = new Intent();
            intent.putExtra(SELECTED_CONTACT, contact);
            setResult(RESULT_OK, intent);
        }else {
            Values.receiver = contact;
            startActivity(new Intent(this, CheckoutActivity.class));
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBinding.fab.getId()){
            Intent intent = new Intent(this, NewContactActivity.class);
            intent.putExtra(FOR_CHECKOUT, true);
            startActivity(intent);
        }else if (id == mBinding.ibSearch.getId()){
            mBinding.etSearch.clearFocus();
            if (!TextUtils.isEmpty(mBinding.etSearch.getText())){
                mViewModel.fetchContacts(mBinding.etSearch.getText().toString());
            }else {
                Toast.makeText(this, getString(R.string.enter_search_pattern), Toast.LENGTH_SHORT).show();
            }
        }
    }
}