package com.israel.livraisonexpresspos.ui.new_contact;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Room.repository.ContactRemoteRepository;
import com.israel.livraisonexpresspos.databinding.ActivityNewContactBinding;
import com.israel.livraisonexpresspos.models.Address;
import com.israel.livraisonexpresspos.models.Contact;
import com.israel.livraisonexpresspos.models.Quarter;
import com.israel.livraisonexpresspos.ui.checkout.CheckoutActivity;
import com.israel.livraisonexpresspos.ui.map.MapPickerActivity;
import com.israel.livraisonexpresspos.ui.select_contact.SelectContactActivity;
import com.israel.livraisonexpresspos.uiComponents.AutoCompleteFocusListener;
import com.israel.livraisonexpresspos.uiComponents.AutoCompleteValidator;
import com.israel.livraisonexpresspos.uiComponents.Utilities;
import com.israel.livraisonexpresspos.utils.QuarterUtils;
import com.israel.livraisonexpresspos.utils.Values;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.israel.livraisonexpresspos.ui.address.AddressDialogFragment.ADDRESS_NAME;
import static com.israel.livraisonexpresspos.ui.address.AddressDialogFragment.LATITUDE;
import static com.israel.livraisonexpresspos.ui.address.AddressDialogFragment.LONGITUDE;

public class NewContactActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int REQUEST_CODE = 202;
    private ActivityNewContactBinding mBinding;
    private ContactRemoteRepository mRemoteRepository;
    private ProgressDialog mDialog;
    private boolean mForCheckout;
    private Address mAddress;
    private List<Quarter> mQuarters = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_contact);
        mAddress = new Address();
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
        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void initUI() {
        mRemoteRepository = new ContactRemoteRepository(getApplication());
        mDialog = Utilities.getProgressDialog(this);
        mBinding.etGeolocation.setOnClickListener(this);
        handleAutocomplete();
        Intent intent = getIntent();
        if (intent.hasExtra(SelectContactActivity.FOR_CHECKOUT)){
            mForCheckout = intent.getBooleanExtra(SelectContactActivity.FOR_CHECKOUT, false);
        }
    }

    private void handleAutocomplete() {
        ArrayAdapter<String> titleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                new QuarterUtils().getTITLES());
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                new QuarterUtils().getCITIES());
        mQuarters = Values.city.equalsIgnoreCase("douala") ? Quarter.DOUALA : Quarter.YAOUNDE;
        List<String> strings = new ArrayList<>();
        for (Quarter q : mQuarters) {
            strings.add(q.getLibelle());
        }
        ArrayAdapter<String> quarterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                strings);

        List<String> services = new ArrayList<>();
        services.add("LIVRAISON");
        services.add("GAZ");
        services.add("RESTAURANT");
        services.add("SUPER MARCHé".toUpperCase());
        services.add("PHARMACIE");
        services.add("CADEAU");
        services.add("LIBRERIE");
        services.add("FLEURISTE");

        mBinding.etAddressTitle.setAdapter(titleAdapter);
        mBinding.etAddressTitle.setValidator(new AutoCompleteValidator(new QuarterUtils().getTITLES()));
        mBinding.etAddressTitle.setOnFocusChangeListener(new AutoCompleteFocusListener());
        mBinding.etCity.setAdapter(cityAdapter);
        mBinding.etServices.setItems(services);
        mBinding.etCity.setValidator(new AutoCompleteValidator(new QuarterUtils().getCITIES()));
        mBinding.etCity.setOnFocusChangeListener(new AutoCompleteFocusListener());
        mBinding.etQuarter.setAdapter(quarterAdapter);
    }

    private void stream(){
        mRemoteRepository.getLoading().observe(this, aBoolean -> {
            if (aBoolean == null)return;
            if (aBoolean){
                mDialog.show();
            }else {
                mDialog.dismiss();
            }
        });

        mRemoteRepository.getSuccess().observe(this, aBoolean -> {
            if(aBoolean == null)return;
            if (aBoolean){
                Toasty.success(NewContactActivity.this, "Contact enregistré avec succes", Toast.LENGTH_SHORT).show();
                if (!mForCheckout){
                    onBackPressed();
                }
            }else {
                Toast.makeText(NewContactActivity.this, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
            }
        });

        mRemoteRepository.getReceiver().observe(this, new Observer<Contact>() {
            @Override
            public void onChanged(Contact contact) {
                if (contact == null)return;
                if (mForCheckout){
                    Values.receiver = contact;
                    startActivity(new Intent(NewContactActivity.this, CheckoutActivity.class));
                    finish();
                }
            }
        });
    }

    private void saveContact() {
        Contact contact = new Contact();
        contact.setFullname(mBinding.etName.getText().toString());
        contact.setEmail(mBinding.etEmail.getText().toString());
        contact.setTelephone(mBinding.etPhone1.getText().toString());
        contact.setTelephone_alt(mBinding.etPhone2.getText().toString());
        contact.setModules(mBinding.etServices.getText().toString());

        List<Address> addresses = new ArrayList<>();

        contact.setProvider_name(Values.PROVIDER_NAME);

        mAddress.setDescription(mBinding.etDescription.getText().toString());
        mAddress.setQuartier(mBinding.etQuarter.getText().toString());
        mAddress.setSurnom(mBinding.etAddressTitle.getText().toString());
        mAddress.setTitre(mBinding.etAddressTitle.getText().toString());
        mAddress.setProvider_name(Values.PROVIDER_NAME);
        mAddress.setVille_id(mBinding.etCity.getText().toString().toLowerCase().equals("douala")
                ? 1 : 2);
        for (Quarter q : mQuarters){
            if (q.getLibelle().equalsIgnoreCase(mAddress.getQuartier())){
                mAddress.setQuartier_id(q.getId());
            }
        }

        addresses.add(mAddress);
        contact.setAdresses(addresses);

        mRemoteRepository.postContact(contact);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done){
            if (verify()){
                saveContact();
            }
        }
        return super.onOptionsItemSelected(item);
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

        if (TextUtils.isEmpty(mBinding.etCity.getText())){
            mBinding.etCity.setError("Veuillez entrer la ville");
            validator = false;
        }else {
            mBinding.etCity.setError(null);
        }

        if (TextUtils.isEmpty(mBinding.etQuarter.getText())){
            mBinding.etQuarter.setError("Veuillez entrer le quartier");
            validator = false;
        }else {
            mBinding.etQuarter.setError(null);
        }

        return validator;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBinding.etGeolocation.getId()){
            Intent intent = new Intent(this, MapPickerActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            if (resultCode == RESULT_OK && data != null){
                mAddress.setNom(data.getStringExtra(ADDRESS_NAME));
                mAddress.setLatitude(String.valueOf(data.getDoubleExtra(LATITUDE, 0.0)));
                mAddress.setLongitude(String.valueOf(data.getDoubleExtra(LONGITUDE, 0.0)));
                mBinding.etGeolocation.setText(mAddress.getNom());
            }
        }
    }
}