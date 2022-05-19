package com.israel.livraisonexpresspos.ui.checkout.steps;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.Observer;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.LayoutCheckoutStep3Binding;
import com.israel.livraisonexpresspos.models.Cart;
import com.israel.livraisonexpresspos.models.Contact;
import com.israel.livraisonexpresspos.models.Delivery;
import com.israel.livraisonexpresspos.models.Order;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.ui.checkout.CheckoutActivity;
import com.israel.livraisonexpresspos.ui.checkout.CheckoutViewModel;
import com.israel.livraisonexpresspos.ui.order_detail.content.delivery_men.DeliveryMenDialogFragment;
import com.israel.livraisonexpresspos.ui.order_detail.content.delivery_men.OnDeliveryMenSelectedListener;
import com.israel.livraisonexpresspos.utils.Values;

import java.util.ArrayList;
import java.util.List;

public class StepDelivery implements PopupMenu.OnMenuItemClickListener, View.OnClickListener
        , OnDeliveryMenSelectedListener {
    public static final String ERROR = "error";
    private final CheckoutActivity mActivity;
    private final LayoutCheckoutStep3Binding mBinding;
    private final CheckoutViewModel mViewModel;
    private ProgressDialog mDialog;
    private Delivery mDelivery;
    private List<Cart> mCarts = new ArrayList<>();
    private int paymentStatus = 0;
    private PopupMenu mPayModeMenu;
    private PopupMenu mPayStateMenu;
    private PopupMenu mPayCashierMenu;
    private PopupMenu mStatusMenu;
    private int mOrderStatus = 0;
    private int mDeliveryManId = 0;

    public StepDelivery(CheckoutActivity activity, LayoutCheckoutStep3Binding binding, CheckoutViewModel viewModel) {
        mActivity = activity;
        mBinding = binding;
        mViewModel = viewModel;
        init();
    }

    public LayoutCheckoutStep3Binding getBinding() {
        return mBinding;
    }

    private void init() {
        stream();
        mDialog = new ProgressDialog(mActivity);
        mDialog.setMessage(mActivity.getString(R.string.saving));
        mDialog.setCancelable(false);
        mDialog.create();
        mViewModel.handleShippingTime(mActivity);
        mViewModel.handleChipGroup(mBinding);
        mBinding.ibEditTime.setOnClickListener(this);
        mBinding.tvPayState.setOnClickListener(this);
        mBinding.tvPayMode.setOnClickListener(this);
        mBinding.tvCashierRole.setOnClickListener(this);
        mBinding.etDeliveryMan.setOnClickListener(this);
        mBinding.tvOrderState.setOnClickListener(this);

        initMenus();
    }

    private void initMenus() {
        mPayModeMenu = new PopupMenu(mActivity, mBinding.tvPayMode);
        mPayModeMenu.inflate(R.menu.menu_pay_mode);
        mPayStateMenu = new PopupMenu(mActivity, mBinding.tvPayState);
        mPayStateMenu.inflate(R.menu.menu_pay_state);
        mPayCashierMenu = new PopupMenu(mActivity, mBinding.tvCashierRole);
        mPayCashierMenu.inflate(R.menu.menu_cashier_role);
        mStatusMenu = new PopupMenu(mActivity, mBinding.tvOrderState);
        mStatusMenu.inflate(R.menu.menu_order_status);

        mPayModeMenu.setOnMenuItemClickListener(this);
        mPayStateMenu.setOnMenuItemClickListener(this);
        mPayCashierMenu.setOnMenuItemClickListener(this);
        mStatusMenu.setOnMenuItemClickListener(this);

    }

    public boolean validateStep(){
        boolean validator = true;
        if (mBinding.chipOther.isChecked()){
            if (!TextUtils.isEmpty(mBinding.etPrice.getText())){
                mViewModel.setShippingPrice(Integer.parseInt(mBinding.etPrice.getText().toString()));
            }
        }
        if (mViewModel.getShippingPrice().getValue() == null || mViewModel.getShippingPrice().getValue() <= 0) {
            validator = false;
            Toast.makeText(mActivity, "Veuillez choisir un prix de livraison", Toast.LENGTH_SHORT).show();
        }
        if (mOrderStatus == 1 && TextUtils.isEmpty(mBinding.etDeliveryMan.getText())){
            validator = false;
            mBinding.etDeliveryMan.setError(" ");
        }else {
            mBinding.etDeliveryMan.setError(null);
        }
        return validator;
    }

    public void placeOrder() {

        mDelivery = Values.stringToDelivery(Values.order.getStringDelivery());
        mDelivery.setInfos(mViewModel.setDeliveryInfo());
        mDelivery.getInfos().setStatut(mOrderStatus);
        mDelivery.getInfos().getCoursiers_ids().add(mDeliveryManId);
        mDelivery.setOrders(mViewModel.setDeliveryOrder(mCarts));
        if (paymentStatus == 0){
            mDelivery.setPaiement(mViewModel.setDeliveryPayment(false, "CASH", null, "unpaid", null));
        }else {
            mDelivery.setPaiement(mViewModel.setDeliveryPayment(true,
                    mBinding.tvPayMode.getText().toString(),
                    mBinding.tvCashierRole.getText().toString(),
                    paymentStatus == 1 ? "partially-paid" : "paid",
                    mBinding.etDescription.getText().toString()));
        }
        mDelivery.setClient(Contact.userToContact(User.getCurrentUser(mActivity)));
        mViewModel.placeOrder(Values.order, mDelivery);
    }

    private void stream(){
        mViewModel.getTime().observe(mActivity, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s == null) return;
                mBinding.tvShippingTime.setText(s);
            }
        });
        mViewModel.getLoad().observe(mActivity, new Observer<Boolean>() {
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
        if (Values.shop != null && Values.order != null){
            mViewModel.getCartItems(Values.order.getId(), Values.shop.getId()).observe(mActivity, new Observer<List<Cart>>() {
                @Override
                public void onChanged(List<Cart> carts) {
                    if (carts == null)return;
                    mCarts = carts;
                }
            });
        }
        mViewModel.getSuccess().observe(mActivity, new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                if (order == null)return;
                mViewModel.updateOrder(order);
                Values.home(mActivity);
            }
        });
        mViewModel.getError().observe(mActivity, new Observer<String>() {
            @Override
            public void onChanged(final String s) {
                if (s == null)return;
                Values.errorDialog(s);
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_not_paid){
            paymentStatus = 0;
            mBinding.layoutPayDetail.setVisibility(View.GONE);
            mBinding.tvPayState.setText(item.getTitle());
            return true;
        }else if (id == R.id.item_semi_paid ){
            mBinding.layoutPayDetail.setVisibility(View.VISIBLE);
            paymentStatus = 1;
            mBinding.tvPayState.setText(item.getTitle());
            return true;
        }else if (id == R.id.item_paid){
            mBinding.layoutPayDetail.setVisibility(View.VISIBLE);
            paymentStatus = 2;
            mBinding.tvPayState.setText(item.getTitle());
            return true;
        }else if (id == R.id.item_cash){
            mBinding.tvPayMode.setText(item.getTitle());
            return true;
        }else if (id == R.id.item_orange){
            mBinding.tvPayMode.setText(item.getTitle());
            return true;
        }else if (id == R.id.item_mtn){
            mBinding.tvPayMode.setText(item.getTitle());
            return true;
        }else if (id == R.id.item_paypal){
            mBinding.tvPayMode.setText(item.getTitle());
            return true;
        }else if (id == R.id.item_card){
            mBinding.tvPayMode.setText(item.getTitle());
            return true;
        }else if (id == R.id.item_manager){
            mBinding.tvCashierRole.setText(item.getTitle());
            return true;
        }else if (id == R.id.item_delivery_man){
            mBinding.tvCashierRole.setText(item.getTitle());
            return true;
        }else if (id == R.id.item_partner){
            mBinding.tvCashierRole.setText(item.getTitle());
            return true;
        }else if (id == R.id.item_un_assign){
            mBinding.tilDeliveryMan.setVisibility(View.GONE);
            mOrderStatus = 0;
            mBinding.tvOrderState.setText(item.getTitle());
            return true;
        }else if (id == R.id.item_assign){
            mOrderStatus = 1;
            mBinding.tilDeliveryMan.setVisibility(View.VISIBLE);
            showDeliveryMenDialog();
            mBinding.tvOrderState.setText(item.getTitle());
            return true;
        }else if (id == R.id.item_to_validate){
            mBinding.tilDeliveryMan.setVisibility(View.GONE);
            mOrderStatus = 7;
            mBinding.tvOrderState.setText(item.getTitle());
            return true;
        }
        return false;
    }

    private void showDeliveryMenDialog(){
        DeliveryMenDialogFragment dialogFragment = new DeliveryMenDialogFragment();
        dialogFragment.setListener(this);
        dialogFragment.show(mActivity.getSupportFragmentManager()
                , DeliveryMenDialogFragment.class.getSimpleName());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBinding.tvPayState.getId()){
            mPayStateMenu.show();
        }else if (id == mBinding.tvPayMode.getId()){
            mPayModeMenu.show();
        }else if (id == mBinding.tvCashierRole.getId()){
            mPayCashierMenu.show();
        }else if (id == mBinding.ibEditTime.getId()){
            mViewModel.showDatePicker();
        }else if (id == mBinding.etDeliveryMan.getId()){
            showDeliveryMenDialog();
        }else if (id == mBinding.tvOrderState.getId()){
            mStatusMenu.show();
        }
    }

    @Override
    public void onDeliveryMenSelected(User user) {
        if (user == null)return;
        mBinding.etDeliveryMan.setText(user.getFullname());
        mDeliveryManId = user.getId();
    }
}