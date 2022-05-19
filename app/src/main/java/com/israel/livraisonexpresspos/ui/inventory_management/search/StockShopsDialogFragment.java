package com.israel.livraisonexpresspos.ui.inventory_management.search;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.FragmentStockShopsBinding;
import com.israel.livraisonexpresspos.ui.inventory_management.StockInventoryActivity;

public class StockShopsDialogFragment extends AppCompatDialogFragment implements View.OnClickListener, TextWatcher {

    private FragmentStockShopsBinding mBinding;
    private StockInventoryActivity activity;
    private MovesFilterViewModel mViewModel;
    private StockFilterShopListAdapter mAdapter;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        mBinding = FragmentStockShopsBinding.inflate(inflater);
        builder.setView(mBinding.getRoot());
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initViewModel();
        initUi();
        stream();
        return mBinding.getRoot();
    }

    private void initViewModel(){
        mViewModel = new ViewModelProvider(requireActivity()).get(MovesFilterViewModel.class);
    }

    /*todo : le flow a adopter : le user demande le dialog, tu lance la requête et interprête les données recues et puis tu permet la selection des user donc tu ecoute le clic sur une item*/

    private void initUi(){
        activity = (StockInventoryActivity) requireActivity();
        mBinding.imgBtnClose.setOnClickListener(this);
        mBinding.etSearch.addTextChangedListener(this);
        mAdapter = new StockFilterShopListAdapter(activity);
        mBinding.rvFilterShops.setAdapter(mAdapter);

        mViewModel.setLoadingShops(true);
        mViewModel.loadShops();
    }

    private void stream(){
        mViewModel.isLoadingShops().observe(getViewLifecycleOwner(), isLoading->{
            if(isLoading){
                mBinding.progressBar.setVisibility(View.VISIBLE);
                mBinding.layoutList.setVisibility(View.GONE);
            }else {
                mBinding.progressBar.setVisibility(View.GONE);
                mBinding.layoutList.setVisibility(View.VISIBLE);
            }
        });

        mViewModel.getShopsList().observe(getViewLifecycleOwner(), filterShops -> {
            mAdapter.setShopList(filterShops);
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.img_btn_close){
            dismiss();
        }
    }


    /*this is for the et search, we listen text change and filter the given shop lis accordingly */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String input = mBinding.etSearch.getText().toString();
        mAdapter.filterShops(input);
    }
}

