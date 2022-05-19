package com.israel.livraisonexpresspos.ui.new_course;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Room.repository.OrderRepository;
import com.israel.livraisonexpresspos.databinding.ActivityNewCourseBinding;
import com.israel.livraisonexpresspos.models.Cart;
import com.israel.livraisonexpresspos.models.Delivery;
import com.israel.livraisonexpresspos.models.Order;
import com.israel.livraisonexpresspos.models.OrderWithCarts;
import com.israel.livraisonexpresspos.ui.cart.CartActivity;
import com.israel.livraisonexpresspos.uiComponents.Utilities;
import com.israel.livraisonexpresspos.uiComponents.ViewAnimation;
import com.israel.livraisonexpresspos.utils.OrderStatus;
import com.israel.livraisonexpresspos.utils.PreferenceUtils;
import com.israel.livraisonexpresspos.utils.Values;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewCourseActivity extends AppCompatActivity {
    private ActivityNewCourseBinding mBinding;
    private NewCourseViewModel mViewModel;
    private List<Cart> mCarts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_course);
        initToolbar();
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.currentActivity = this;
        if (Values.order == null)return;
        stream();
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
        mViewModel = new ViewModelProvider(this).get(NewCourseViewModel.class);
    }

    private void stream() {
        if (mViewModel.getCartItems().hasObservers())return;
        mViewModel.getCartItems().observe(this, new Observer<List<Cart>>() {
            @Override
            public void onChanged(List<Cart> carts) {
                if (carts == null)return;
                mCarts = carts;
                if (carts.size() == 0){
                    mBinding.fabCart.setVisibility(View.GONE);
                }else {
                    ViewAnimation.fadeIn(mBinding.fabCart);
                }
                mBinding.fabCart.setCount(carts.size());
            }
        });
    }

    private boolean validateForm(){
        boolean validator = true;
        if (TextUtils.isEmpty(mBinding.etName.getText())){
            validator = false;
            mBinding.etName.setError("Veuillez entrer le nom du produit");
        }
        return validator;
    }

    private void addToCart(boolean internOrder){
        OrderRepository repository = new OrderRepository(getApplication());
        if (Values.order == null){
            Order order = new Order();
            order.setDateTime(Calendar.getInstance().getTimeInMillis());
            order.setStatus(OrderStatus.saved.toString());
            order.setShop(Values.shop.getNom());
            order.setShopId(Values.shop.getId());
            order.setShopObject(new Gson().toJson(Values.shop));
            order.setModuleObject(new Gson().toJson(Values.module));
            order.setCity(Values.city);
            order.setModuleSlug(Values.module.getSlug());
            Delivery delivery = new Delivery();
            delivery.setSender(Values.sender);
            delivery.setReceiver(Values.receiver);
            order.setStringDelivery(Values.deliveryToString(delivery));
            Values.order = order;
        }else if (mCarts.isEmpty()){
            Values.order.setShop(Values.shop.getNom());
            Values.order.setCity(Values.city);
            Values.order.setShopId(Values.shop.getId());
            Values.order.setShopObject(new Gson().toJson(Values.shop));
            Values.order.setModuleObject(new Gson().toJson(Values.module));
            Values.order.setModuleSlug(Values.module.getSlug());
            repository.update(Values.order);
        }

        Cart cart = new Cart();
        cart.setLibelle(internOrder ? "COURSE INTERNE" : mBinding.etName.getText().toString());
        cart.setImage(Values.module.getImage());
        cart.setMagasin_id(Values.shop.getId());
        cart.setProduct_id(PreferenceUtils.getInt(this, internOrder ? PreferenceUtils.INTERN_ORDER : PreferenceUtils.GENERIC_PRODUCT));
        int price, quantity;
        if (mBinding.etPrice.getText().toString().equals("")){
            price = 0;
        }else {
            price = Integer.parseInt(mBinding.etPrice.getText().toString());
        }
        String stringQuantity = mBinding.etQuantity.getText().toString();
        if (stringQuantity.equals("")
                || stringQuantity.equals("0")){
            quantity = 1;
        }else {
            quantity = Integer.parseInt(mBinding.etQuantity.getText().toString());
        }
        price = internOrder ? 1 : price;
        cart.setPrix_unitaire(price);
        cart.setMontant_total(String.valueOf(price));
        cart.setQuantite(internOrder ? 1 : quantity);
        Values.setSender(Values.shop);
        if (Values.order.getId() == 0){
            OrderWithCarts orderWithCarts = new OrderWithCarts();
            orderWithCarts.mOrder = Values.order;
            List<Cart> carts = new ArrayList<>();
            carts.add(cart);
            orderWithCarts.mCarts = carts;
            repository.insertOrderWithCarts(orderWithCarts);
        }else {
            cart.setOrderId(Values.order.getId());
            mViewModel.insertCart(cart);
        }
        clear();
    }

    public void onButtonClick(View view) {
        int id = view.getId();
        if (id ==  mBinding.buttonAdd.getId()){
            if (validateForm()){
                addToCart(false);
                stream();
            }
        }else if (id == mBinding.fabCart.getId()){
            startActivity(new Intent(this, CartActivity.class));
        }else if (id == mBinding.buttonInternOrder.getId()){
            addToCart(true);
            stream();
        }
    }

    private void clear() {
        mBinding.etName.requestFocus();
        mBinding.etName.setText("");
        mBinding.etPrice.setText("");
        mBinding.etQuantity.setText("");
        Utilities.hideKeyBoard(this);
    }
}