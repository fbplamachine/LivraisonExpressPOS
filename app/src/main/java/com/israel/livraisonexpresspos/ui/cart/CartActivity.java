package com.israel.livraisonexpresspos.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.databinding.ActivityCartBinding;
import com.israel.livraisonexpresspos.models.Cart;
import com.israel.livraisonexpresspos.ui.checkout.CheckoutActivity;
import com.israel.livraisonexpresspos.ui.select_contact.SelectContactActivity;
import com.israel.livraisonexpresspos.utils.Values;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements OnCartItemChangeListener {
    private ActivityCartBinding mBinding;
    private CartViewModel mViewModel;
    private CartAdapter mAdapter;
    private int mTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
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
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initUI() {
        mViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        mBinding.rvCart.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CartAdapter(new ArrayList<Cart>(), getSupportFragmentManager());
        mBinding.rvCart.setAdapter(mAdapter);
    }

    private void stream() {
        if (Values.order == null || Values.shop == null)return;
        mViewModel.getItems(Values.order.getId(), Values.shop.getId()).observe(this, new Observer<List<Cart>>() {
            @Override
            public void onChanged(List<Cart> carts) {
                if (carts == null)return;
                mBinding.cvButton.setVisibility(carts.isEmpty() ? View.GONE : View.VISIBLE);
                mAdapter.setCarts(carts);
                mTotalPrice = calculateTotalAmount(carts);
                mBinding.tvPrice.setText(String.valueOf(mTotalPrice).concat(" FCFA"));
            }
        });
    }

    private int calculateTotalAmount(List<Cart> carts){
        int total = 0;
        for (Cart c : carts){
            total += (c.getMontantTotal() * c.getQuantite());
        }
        return total;
    }

    public void checkout(View view) {
        if (mTotalPrice != 0){
            startActivity(new Intent(this, Values.receiver == null
                    ? SelectContactActivity.class
                    : CheckoutActivity.class));
        }else {
            Values.alertDialog("Le montant total du panier dois être supérieur à 0");
        }
    }

    @Override
    public void onDelete(final Cart cart) {
        Snackbar.make(mBinding.getRoot(), "Voulez vous supprimer " + cart.getLibelle() + " du panier?", 5000)
                .setActionTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setAction(getString(R.string.delete), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewModel.deleteCartItem(cart);
                    }
                }).show();
    }

    @Override
    public void onChange(Cart cart) {
        mViewModel.updateCartItem(cart);
    }
}