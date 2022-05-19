package com.israel.livraisonexpresspos.ui.modules;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.databinding.ActivityModuleBinding;
import com.israel.livraisonexpresspos.models.Cart;
import com.israel.livraisonexpresspos.models.City;
import com.israel.livraisonexpresspos.models.Module;
import com.israel.livraisonexpresspos.ui.cart.CartActivity;
import com.israel.livraisonexpresspos.uiComponents.SpacingItemDecoration;
import com.israel.livraisonexpresspos.uiComponents.Tools;
import com.israel.livraisonexpresspos.utils.PreferenceUtils;
import com.israel.livraisonexpresspos.utils.Values;

import java.util.ArrayList;
import java.util.List;

public class ModuleActivity extends AppCompatActivity {
    private ModuleViewModel mViewModel;
    private ActivityModuleBinding mBinding;
    private String selectedCity;
    private PopupMenu mPopupMenu;
    private ModuleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_module);
        mViewModel = new ViewModelProvider(this).get(ModuleViewModel.class);
        selectedCity = PreferenceUtils.getString(this, PreferenceUtils.SELECTED_CITY)
                .equals("") ? "DOUALA" : PreferenceUtils.getString(this,
                PreferenceUtils.SELECTED_CITY);
        Values.city = selectedCity;
        initToolbar();
        initUI();
        stream();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.currentActivity = this;
        if (Values.order == null)return;
        observeCart();
    }

    private void initToolbar() {
        mBinding.toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initUI() {
        mViewModel = new ViewModelProvider(this).get(ModuleViewModel.class);
        mBinding.tvCity.setText(selectedCity);
        mBinding.rvModules.setLayoutManager(new GridLayoutManager(this, 2));
        mBinding.rvModules.addItemDecoration(new SpacingItemDecoration(2,
                Tools.dpToPx(this, 0), false));
        mAdapter = new ModuleAdapter(new ArrayList<Module>(), this);
        mBinding.rvModules.setAdapter(mAdapter);
    }

    private void stream() {
        mViewModel.getConfigs(this, selectedCity.toLowerCase());
        mViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if(s == null) return;
            }
        });

        mViewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == null) return;
                mBinding.tvCity.setEnabled(!aBoolean);
                mBinding.rvModules.setVisibility(aBoolean ? View.GONE : View.VISIBLE);
//                mBinding.layoutProgress.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
            }
        });

        mViewModel.getModules().observe(this, new Observer<List<Module>>() {
            @Override
            public void onChanged(List<Module> modules) {
                if (modules == null) return;
                mAdapter.setModules(modules);
            }
        });

        mViewModel.getCities().observe(this, new Observer<List<City>>() {
            @Override
            public void onChanged(List<City> cities) {
                if(cities == null) return;
                setCitiesList(cities);
            }
        });
        if (Values.order == null)return;
        observeCart();
    }

    private void observeCart(){
        if (mViewModel.getCartItems().hasObservers())return;
        mViewModel.getCartItems().observe(this, new Observer<List<Cart>>() {
            @Override
            public void onChanged(List<Cart> carts) {
                if (carts == null)return;
                if (carts.size() == 0){
                    mBinding.fabCart.setVisibility(View.GONE);
                }else {
//                    ViewAnimation.fadeIn(mBinding.fabCart);
                }
                mBinding.fabCart.setCount(carts.size());
            }
        });
    }

    private void setCitiesList(List<City> cities){
        mPopupMenu = new PopupMenu(this, mBinding.tvCity);
        for(int i = 0; i < cities.size(); i++){
            City c = cities.get(i);
            mPopupMenu.getMenu().add(Menu.NONE, i, Menu.NONE,  c.getName().toUpperCase());
        }
        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                selectedCity = item.getTitle().toString();
                mViewModel.getConfigs(getApplicationContext(), selectedCity.toLowerCase());
                mBinding.tvCity.setText(selectedCity);
                Values.city = selectedCity;
                return true;
            }
        });
    }

    public void showCities(View view) {
        mPopupMenu.show();
    }

    public void openCart(View view) {
        startActivity(new Intent(this, CartActivity.class));
    }
}