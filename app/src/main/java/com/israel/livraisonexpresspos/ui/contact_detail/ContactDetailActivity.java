package com.israel.livraisonexpresspos.ui.contact_detail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.ActivityContactDetailBinding;
import com.israel.livraisonexpresspos.models.Address;
import com.israel.livraisonexpresspos.models.Contact;
import com.israel.livraisonexpresspos.ui.address.OnAddressAdded;
import com.israel.livraisonexpresspos.ui.address.OnAddressUpdate;
import com.israel.livraisonexpresspos.ui.contact_detail.contact.ContactDetailFragment;
import com.israel.livraisonexpresspos.ui.contact_detail.contact.ContactDetailViewModel;
import com.israel.livraisonexpresspos.ui.contact_detail.orders.ContactOrdersFragment;
import com.israel.livraisonexpresspos.ui.contacts.ContactViewHolder;
import com.israel.livraisonexpresspos.uiComponents.Utilities;
import com.israel.livraisonexpresspos.uiComponents.ViewPagerAdapter;

public class ContactDetailActivity extends AppCompatActivity implements OnAddressUpdate, OnAddressAdded, ViewPager.OnPageChangeListener {
    private ActivityContactDetailBinding mBinding;
    private ViewPagerAdapter mAdapter;
    private Contact mContact;
    private ContactDetailViewModel mViewModel;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_contact_detail);
        mViewModel = new ViewModelProvider(this).get(ContactDetailViewModel.class);
        initToolbar();
        initUI();
        stream();
    }

    private void stream() {
        mViewModel.getLoading().observe(this, aBoolean -> {
            if (aBoolean == null)return;
            if (aBoolean){
                mProgressDialog.show();
            } else {
                mProgressDialog.dismiss();
            }
        });

        mViewModel.getSuccess().observe(this, aBoolean -> {
            if (aBoolean == null)return;
            if (aBoolean){
                Toast.makeText(ContactDetailActivity.this, "Mise à jour réussi.", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(ContactDetailActivity.this, "Erreur.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initToolbar() {
        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mBinding.viewPager.getCurrentItem() == 0){
            getMenuInflater().inflate(R.menu.menu_done, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done){
            Utilities.hideKeyBoard(this);
            ContactDetailFragment fragment = (ContactDetailFragment) mAdapter.getItem(mBinding.viewPager.getCurrentItem());
            fragment.updateContact();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initUI(){
        Intent intent = getIntent();
        if (!intent.hasExtra(ContactViewHolder.CONTACT)){
            return;
        }
        mContact = intent.getParcelableExtra(ContactViewHolder.CONTACT);
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new ContactDetailFragment(), getString(R.string.contact));
        mAdapter.addFragment(new ContactOrdersFragment(), getString(R.string.contact_orders_title));

        mBinding.viewPager.addOnPageChangeListener(this);
        mBinding.viewPager.setAdapter(mAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        mProgressDialog = Utilities.getProgressDialog(this);
    }

    public Contact getContact() {
        return mContact;
    }

    @Override
    public void onAddressAdded(Address address) {
        OnAddressAdded onAddressAdded = (OnAddressAdded) mAdapter.getItem(mBinding.viewPager.getCurrentItem());
        onAddressAdded.onAddressAdded(address);
    }

    @Override
    public void onAddressAddedPersistence(Address address) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        invalidateOptionsMenu();
        Utilities.hideKeyBoard(this);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onAddressUpdated(Address address, long addressPosition) {

    }

    @Override
    public void onAddressUpdatesPersistence(Address address, long position) {

    }
}