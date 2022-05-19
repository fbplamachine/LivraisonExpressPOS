package com.israel.livraisonexpresspos.ui.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.israel.livraisonexpresspos.MainActivity;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.databinding.ActivityCheckoutBinding;
import com.israel.livraisonexpresspos.models.Address;
import com.israel.livraisonexpresspos.models.Contact;
import com.israel.livraisonexpresspos.models.Delivery;
import com.israel.livraisonexpresspos.models.Order;
import com.israel.livraisonexpresspos.ui.checkout.steps.StepDelivery;
import com.israel.livraisonexpresspos.ui.checkout.steps.StepReceiver;
import com.israel.livraisonexpresspos.ui.checkout.steps.StepSender;
import com.israel.livraisonexpresspos.ui.select_contact.SelectContactActivity;
import com.israel.livraisonexpresspos.uiComponents.Utilities;
import com.israel.livraisonexpresspos.uiComponents.ViewAnimation;
import com.israel.livraisonexpresspos.utils.Values;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity implements OnAddressSelectedListener{
    public static final String FROM_CHECKOUT = "from_checkout";
    private ActivityCheckoutBinding mBinding;
    private final List<View> mViewList = new ArrayList<>();
    private final List<RelativeLayout> mStepViewList = new ArrayList<>();
    private int mCurrentStep = 0;
    private int mSuccessStep = 0;
    private CheckoutViewModel mViewModel;
    private StepSender mStepSender;
    private StepReceiver mStepReceiver;
    private StepDelivery mStepDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_checkout);
        mViewModel = new ViewModelProvider(this).get(CheckoutViewModel.class);
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
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initUI() {
        mStepSender = new StepSender(this, mBinding.includeSender);
        mStepReceiver = new StepReceiver(this, mBinding.includeReceiver);
        mStepDelivery = new StepDelivery(this, mBinding.includeDelivery, mViewModel);

        mViewList.add(mStepSender.getBinding().layoutSender);
        mViewList.add(mStepReceiver.getBinding().layoutReceiver);
        mViewList.add(mStepDelivery.getBinding().layoutShipping);

        mStepViewList.add(mStepSender.getBinding().stepSender);
        mStepViewList.add(mStepReceiver.getBinding().stepReceiver);
        mStepViewList.add(mStepDelivery.getBinding().stepShipping);

        for (View v : mViewList) v.setVisibility(View.GONE);
        mViewList.get(0).setVisibility(View.VISIBLE);

        if (Values.sender != null) mStepSender.getSelectedContact(Values.sender);
        if (Values.receiver != null) mStepReceiver.getSelectedContact(Values.receiver);
    }

    public void onButtonClick(View view) {
        int id = view.getId();
        if(id == R.id.buttonSenderNext){
            if (mStepSender.validateStep()){
                collapseAndContinue(0);
                Delivery delivery = Values.stringToDelivery(Values.order.getStringDelivery());
                delivery.setSender(mStepSender.setSender());
                Values.order.setStringDelivery(Values.deliveryToString(delivery));
                updateOrder(Values.order);
            }
        }else if (id == R.id.buttonReceiverNext) {
            if (mStepReceiver.validateStep()){
                collapseAndContinue(1);
                Delivery delivery = Values.stringToDelivery(Values.order.getStringDelivery());
                delivery.setReceiver(mStepReceiver.setReceiver());
                Values.order.setStringDelivery(Values.deliveryToString(delivery));
                updateOrder(Values.order);
            }
        }else if (id == R.id.buttonOrder) {
            if(mStepDelivery.validateStep()){
                mStepDelivery.placeOrder();
            }
        }
    }

    public void collapseAndContinue(int index) {
        ViewAnimation.collapse(mViewList.get(index));
        Utilities.setCheckedStep(this, mStepViewList.get(index));
        index++;
        mCurrentStep = index;
        mSuccessStep = Math.max(index, mSuccessStep);
        ViewAnimation.expand(mViewList.get(index));
    }

    public void onTitleClick(View view) {
        int id = view.getId();
        int step = 0;
        if (id == R.id.tvReceiverTitle) {
            step = 1;
        }else if (id == R.id.tvShippingTitle) {
            step = 2;
        }
        if (mSuccessStep >= step && mCurrentStep > step) {
            mCurrentStep = step;
            ViewAnimation.collapseAll(mViewList);
            ViewAnimation.expand(mViewList.get(step));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Contact contact = data != null ?
                (Contact) data.getParcelableExtra(SelectContactActivity.SELECTED_CONTACT) : null;
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case StepSender.SENDER_REQUEST_CODE :
                    mStepSender.getSelectedContact(contact);
                    break;
                case StepReceiver.RECEIVER_REQUEST_CODE :
                    mStepReceiver.getSelectedContact(contact);
                    break;
            }
        }
    }

    @Override
    public void addressSelected(Address address) {
        if (mCurrentStep == 0){
            mStepSender.getSelectedAddress(address);
        }else if (mCurrentStep == 1){
            mStepReceiver.getSelectedAddress(address);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_home){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(FROM_CHECKOUT, true);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateOrder(Order order){
        mViewModel.updateOrder(order);
    }
}