package com.israel.livraisonexpresspos.ui.shops;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.databinding.ActivityShopBinding;
import com.israel.livraisonexpresspos.utils.Values;

public class ShopActivity extends AppCompatActivity {
    private ActivityShopBinding mBinding;
    private ShopAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_shop);

        initToolbar();
        initUI();

    }

    @Override
    protected void onResume() {
        super.onResume();
        App.currentActivity = this;
    }

    private void initToolbar() {
        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if (Values.module != null){
            mBinding.toolbar.setTitle(Values.module.getLibelle());
        }
        mBinding.toolbar.setBackgroundColor(Values.getColor());
        getWindow().setStatusBarColor(Values.getColorDark());
    }

    private void initUI() {
        mBinding.rvShops.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ShopAdapter(Values.module.getShops(), this);
        mBinding.rvShops.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }


}