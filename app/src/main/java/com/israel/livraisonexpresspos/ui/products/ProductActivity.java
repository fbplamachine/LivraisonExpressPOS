package com.israel.livraisonexpresspos.ui.products;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.databinding.ActivityProductBinding;
import com.israel.livraisonexpresspos.models.Cart;
import com.israel.livraisonexpresspos.models.Product;
import com.israel.livraisonexpresspos.ui.cart.CartActivity;
import com.israel.livraisonexpresspos.ui.new_course.NewCourseActivity;
import com.israel.livraisonexpresspos.uiComponents.ViewAnimation;
import com.israel.livraisonexpresspos.utils.Values;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity
        implements OnProductClickListener, TextWatcher {
    ActivityProductBinding mBinding;
    private ProductViewModel mViewModel;
    private ProductAdapter mAdapter;
    private List<Cart> mCarts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_product);
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
        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mBinding.toolbar.setTitle(Values.shop.getNom());
        mBinding.toolbar.setBackgroundColor(Values.getColor());
        getWindow().setStatusBarColor(Values.getColor());
    }

    private void initUI() {
        mViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        mBinding.rvProducts.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ProductAdapter(new ArrayList<Product>(), this);
        mBinding.rvProducts.setAdapter(mAdapter);
        mBinding.etSearch.addTextChangedListener(this);
        mViewModel.fetchProducts(this);
    }

    private void stream() {
        mViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s == null)return;
            }
        });

        mViewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == null)return;
//                mBinding.layoutProgress.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
                mBinding.rvProducts.setVisibility(aBoolean ? View.GONE : View.VISIBLE);
            }
        });

        mViewModel.getProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                if (products == null)return;
                mAdapter.setProducts(products);
            }
        });
        if (Values.order == null)return;
        observeCart();
    }

    private void observeCart(){
        if (Values.order == null || Values.shop == null)return;
        if (mViewModel.getCartItems(Values.order.getId(), Values.shop.getId()).hasObservers())return;
        mViewModel.getCartItems(Values.order.getId(), Values.shop.getId()).observe(this, new Observer<List<Cart>>() {
            @Override
            public void onChanged(List<Cart> carts) {
                if (carts == null)return;
                mCarts = carts;
                if (carts.size() == 0){
                    mBinding.fabCart.setVisibility(View.GONE);
                }else {
                    ViewAnimation.fadeIn(mBinding.fabCart);
                }
                int total = 0;
                for(Cart c : carts){
                    total += c.getQuantite();
                }
                mBinding.fabCart.setCount(total);
            }
        });
    }

    @Override
    public void onProductClick(final Product product) {
        Snackbar.make(mBinding.getRoot(), "Voulez ajouter " + product.getLibelle() + " au panier?", 7000)
                .setActionTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setAction("AJOUTER", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewModel.addProductToCart(product, mCarts.size(), Values.order == null ? 0 : Values.order.getId(), Values.shop.getId());
                        observeCart();
                    }
                }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add){
            Intent intent = new Intent(this, NewCourseActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void onButtonClick(View view) {
        startActivity(new Intent(this, CartActivity.class));
    }

}