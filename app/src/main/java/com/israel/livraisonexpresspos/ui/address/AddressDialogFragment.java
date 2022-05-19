package com.israel.livraisonexpresspos.ui.address;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.DialogAddressBinding;
import com.israel.livraisonexpresspos.models.Address;
import com.israel.livraisonexpresspos.models.Quarter;
import com.israel.livraisonexpresspos.ui.map.MapActivity;
import com.israel.livraisonexpresspos.ui.order_detail.OrderDetailActivity;
import com.israel.livraisonexpresspos.utils.Values;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;

public class AddressDialogFragment extends AppCompatDialogFragment implements View.OnClickListener,
        PopupMenu.OnMenuItemClickListener, RadioGroup.OnCheckedChangeListener {
    public static final String ADDRESS_NAME = "geolocationAddress";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String ADDRESS_ARGUMENT = "addressArgument";
    public static final int REQUEST_CODE = 123;
    private DialogAddressBinding mBinding;
    private AddressViewModel mViewModel;
    private Address mAddress;
    private PopupMenu mPopupMenu;
    private ProgressDialog mDialog;
    private int mCityId = 1;
    private List<Quarter> mQuarters;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mBinding = DialogAddressBinding.inflate(inflater);
        builder.setView(mBinding.getRoot());
        initUI();
        stream();
        return builder.create();
    }

    private void initUI() {
        mViewModel = new ViewModelProvider(requireActivity()).get(AddressViewModel.class);
        mViewModel.setActivity(requireActivity());
        mDialog = new ProgressDialog(requireContext());
        mDialog.setMessage(getString(R.string.loading));
        mDialog.create();
        initAddress();

        mBinding.ibClose.setOnClickListener(this);
        mBinding.etAddressTitle.setOnClickListener(this);
        mBinding.buttonGeoLocate.setOnClickListener(this);
        mBinding.buttonSave.setOnClickListener(this);
        mBinding.radioBtnContainer.setOnCheckedChangeListener(this);

        handleTitlePopupMenu();
        populateQuarters();
    }

    private void initAddress() {
        Bundle args = getArguments();
        if (args == null)return;
        if (args.getParcelable(ADDRESS_ARGUMENT) != null){
            mAddress = args.getParcelable(ADDRESS_ARGUMENT);
            if (mAddress.getVille_id() != null)mCityId = mAddress.getVille_id();
            mBinding.setAddress(mAddress);
        }else {
            mAddress = new Address();
        }
        if (mAddress.getId() != null && !mAddress.getQuartier().toLowerCase().contains("inconnu")){
            mBinding.etQuarter.setEnabled(false);
            mBinding.tilQuarter.setEnabled(false);
        }
    }

    private void populateQuarters() {
        mQuarters = Values.city.equalsIgnoreCase("douala") ? Quarter.DOUALA : Quarter.YAOUNDE;
        List<String> strings = new ArrayList<>();
        for (Quarter q : mQuarters) {
            strings.add(q.getLibelle());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1
                , strings);
        mBinding.etQuarter.setAdapter(adapter);
    }

    private String getCity(){
        if (mBinding.radioDouala.isChecked()){
            return "douala";
        }else {
            return "yaoundé";
        }
    }

    private void stream() {
        mViewModel.getLoad().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == null)return;
                if (aBoolean){
                    mDialog.show();
                }else {
                    mDialog.dismiss();
                }
            }
        });

        mViewModel.getSuccess().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                try {
                    if (aBoolean == null)return;
                    if (aBoolean){
                        Toasty.success(requireContext(), "Adresse enregistrée avec succès").show();
                        dismiss();
                        Log.e("je suis la ", "oui");
                    }else {
                        Toasty.success(requireContext(), "Une erreur est survenue").show();
                    }
                    handleReload();
                }catch (Exception e){
                    e.printStackTrace();
                    dismiss();
                }
            }
        });
    }

    @Override
    public void dismiss() {
        mViewModel.onCleared();
        super.dismiss();
    }

    private void handleReload() {
        if (requireActivity() instanceof OrderDetailActivity){
            OrderDetailActivity activity = (OrderDetailActivity) requireActivity();
            int currentItem = activity.getBinding().viewPager.getCurrentItem();
            if (currentItem == 1){
                activity.setReloadSenderAddresses(true);
            }else if (currentItem == 2){
                activity.setReloadReceiverAddresses(true);
            }
        }
    }

    private void handleTitlePopupMenu() {
        mPopupMenu = new PopupMenu(requireContext(), mBinding.etAddressTitle);
        mPopupMenu.inflate(R.menu.edit_addres_title);
        mPopupMenu.setOnMenuItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBinding.ibClose.getId()){
            dismiss();
        }else if (id == mBinding.buttonSave.getId()){
            if (mViewModel.validate(mBinding)){
                mAddress.setVille_id(mCityId);
                mAddress.setQuartier(mBinding.etQuarter.getText().toString());
                for (Quarter q : mQuarters){
                    if (q.getLibelle().equalsIgnoreCase(mAddress.getQuartier())){
                        mAddress.setQuartier_id(q.getId());
                    }
                }
                if (mAddress.getId() == null){
                    mViewModel.addAddress(mViewModel.getAddress(mBinding, mAddress));
                }else {
                    mViewModel.updateAddress(mViewModel.getAddress(mBinding, mAddress), getArguments().getLong("address_list_position"));
                }
            }
        }else if (id == mBinding.buttonGeoLocate.getId()){
            Intent intent = new Intent(requireContext(), MapActivity.class);
            if (mAddress.getLat() != 0.0 && mAddress.getLon() != 0.0){
                intent.putExtra(LATITUDE, mAddress.getLatitude());
                intent.putExtra(LONGITUDE, mAddress.getLongitude());
            }
            startActivityForResult(intent, REQUEST_CODE);
        }else if (id == mBinding.etAddressTitle.getId()){
            mPopupMenu.show();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        mBinding.etAddressTitle.setText(item.getTitle());
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            if (resultCode == RESULT_OK && data != null){
                mAddress.setNom(data.getStringExtra(ADDRESS_NAME));
                mBinding.etLatitude.setText(String.valueOf(data.getDoubleExtra(LATITUDE, 0.0)));
                mBinding.etLongitude.setText(String.valueOf(data.getDoubleExtra(LONGITUDE, 0.0)));
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        populateQuarters();
        if (mBinding.radioDouala.getId() == checkedId){
            mCityId = 1;
        }else {
            mCityId = 2;
        }
    }
}
