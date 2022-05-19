package com.israel.livraisonexpresspos.ui.checkout;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Api;
import com.israel.livraisonexpresspos.data.Room.repository.CartRepository;
import com.israel.livraisonexpresspos.data.Room.repository.OrderRepository;
import com.israel.livraisonexpresspos.databinding.LayoutCheckoutStep3Binding;
import com.israel.livraisonexpresspos.models.Cart;
import com.israel.livraisonexpresspos.models.Delivery;
import com.israel.livraisonexpresspos.models.DeliveryInfo;
import com.israel.livraisonexpresspos.models.DeliveryOrder;
import com.israel.livraisonexpresspos.models.DeliveryPayment;
import com.israel.livraisonexpresspos.models.Order;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.models.from_steed_app.Cashier;
import com.israel.livraisonexpresspos.utils.OrderStatus;
import com.israel.livraisonexpresspos.utils.Values;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutViewModel extends AndroidViewModel implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private final OrderRepository mRepository;
    private final CartRepository mCartRepository;
    private final MutableLiveData<String> mError;
    private final MutableLiveData<Boolean> mLoad;
    private final MutableLiveData<Order> mSuccess;
    private final MutableLiveData<String> mTime;
    private final MutableLiveData<Integer> mShippingPrice;
    private DatePickerDialog mDatePickerDialog;
    private TimePickerDialog mTimePickerDialog;
    private String mDate;
    private int mTotalCartAmount;
    private SimpleDateFormat mFormat;


    public CheckoutViewModel(@NonNull Application application) {
        super(application);
        mRepository = new OrderRepository(application);
        mCartRepository = new CartRepository(application);
        mError = new MutableLiveData<>();
        mLoad = new MutableLiveData<>();
        mSuccess = new MutableLiveData<>();
        mTime = new MutableLiveData<>();
        mShippingPrice = new MutableLiveData<>();
        mShippingPrice.setValue(500);
        mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
    }

    public void updateOrder(Order order){
        mRepository.update(order);
    }

    public MutableLiveData<String> getError() {
        return mError;
    }

    public MutableLiveData<Boolean> getLoad() {
        return mLoad;
    }

    public MutableLiveData<Order> getSuccess() {
        return mSuccess;
    }

    public MutableLiveData<String> getTime() {
        return mTime;
    }

    public MutableLiveData<Integer> getShippingPrice() {
        return mShippingPrice;
    }

    public void setShippingPrice(int value){
        mShippingPrice.setValue(value);
    }

    public LiveData<List<Cart>> getCartItems(int orderId, int shopId){
        return mCartRepository.getAllLiveItems(orderId, shopId);
    }

    public void placeOrder(final Order order, final Delivery delivery){
//        Log.e("ORDER", delivery.toString());
        if (Values.order != null && Values.shop != null){
            mCartRepository.deleteUnUsedItems(order.getId(), Values.shop.getId());
            Log.e("DELETE", "DELETE");
        }else {
            Log.e("DELETE", "NOT DELETED");
        }
        mLoad.setValue(true);
        Api.course().placeOrder("", User.getCurrentUser(getApplication()).getToken(), delivery)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        mLoad.setValue(false);
                        if (response.isSuccessful() && response.body() != null){
                            try {
                                String output = response.body().string();
                                JSONObject body = new JSONObject(output);
                                if (body.getBoolean("success")){
                                    String ref = body.getJSONObject("data").getJSONObject("infos")
                                            .getString("ref");
                                    order.setRef(ref);
                                    order.setStatus(OrderStatus.done.toString());
                                    order.setStringDelivery(body.getJSONObject("data").toString());
                                    mSuccess.setValue(order);
                                }else {
                                    mError.setValue(body.toString());
//                                    Toast.makeText(getApplication(), "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                                App.handleError(e);
                                Toast.makeText(getApplication(), "Erreur", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            if (response.code() == 401){
                                Values.unAuthorizedDialog();
                            }else{
                                Toast.makeText(getApplication(), "Une erreur est survenue", Toast.LENGTH_SHORT).show();
                                try {
                                    mError.setValue(response.errorBody() != null ? response.errorBody().string() : response.body() != null ? response.body().string() : "");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    App.handleError(e);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        order.setStringDelivery(new Gson().toJson(delivery));
                        order.setStatus(OrderStatus.pending.toString());
                        mSuccess.setValue(order);
                        mLoad.setValue(false);
                    }
                });
    }

    public void handleShippingTime(Context context){
        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(context, this,
                newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.getDatePicker().setMinDate(newCalendar.getTimeInMillis());
        mTimePickerDialog = new TimePickerDialog(context, this,
                newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = new Date();
        date.setTime(newCalendar.getTimeInMillis() + (30 * 60 * 1000));
        mTime.setValue(format.format(date));
    }

    public void showDatePicker(){
        mDatePickerDialog.show();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mDate = String.format("%02d-%02d", dayOfMonth, month + 1) + "-" + year;
        mTimePickerDialog.show();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mTime.setValue(mDate + " "+ String.format("%02d:%02d", hourOfDay, minute));
    }

    public DeliveryInfo setDeliveryInfo(){
        DeliveryInfo info = new DeliveryInfo();
        info.setOrigin("Livraison express POS");
        info.setPlatform("Mobile");

        info.setDate_livraison(swapDate(mTime.getValue()));
        info.setDate_chargement(mFormat.format(Calendar.getInstance().getTime()));
        info.setVille_livraison(Values.city);
        info.setDuration_text("");
        info.setDistance_text("");
        return info;
    }

    public DeliveryOrder setDeliveryOrder(List<Cart> carts){
        DeliveryOrder order = new DeliveryOrder();
        order.setModule(Values.order.getModuleSlug());
        order.setDescription(buildDescription(carts));
        order.setCommentaire("");
        order.setListe_articles(Cart.toArticles(carts));
        order.setMontant_total(mTotalCartAmount);
        order.setMagasin_id(Values.order.getShopId());
        if (mShippingPrice.getValue() != null) order.setMontant_livraison(mShippingPrice.getValue());
        return order;
    }

    public DeliveryPayment setDeliveryPayment(boolean paid, String mode, String cashier, String state, String description){
        DeliveryPayment payment = new DeliveryPayment();
        payment.setMessage("");
        payment.setMode_paiement(mode);
        payment.setStatut(state);
        Cashier c = new Cashier();
        c.setAgent(cashier);
        c.setComment(description);
        payment.setCashier(c);
        if (paid)payment.setDate_paiement(mFormat.format(Calendar.getInstance().getTime()));
        if (mShippingPrice.getValue() != null) payment.setMontant_total(mShippingPrice.getValue());
        payment.setStatut("");
        return payment;
    }

    private String swapDate(String string){
        if (string == null)return "";
        String date = string.split(" ")[0];
        String time = string.split(" ")[1];
        String[] dateArray = date.split("-");
        String newDate = dateArray[2]+"-"+dateArray[1]+"-"+dateArray[0];
        Log.e("SHIPPING DATE", newDate+" "+time+":00");
        return newDate+" "+time+":00";
    }

    public void handleChipGroup(final LayoutCheckoutStep3Binding binding) {
        binding.groupShippingPrice.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                mShippingPrice.setValue(0);
                binding.tilPrice.setVisibility(View.GONE);
                if (checkedId == binding.chip500.getId() && binding.chip500.isChecked()) {
                    mShippingPrice.setValue(500);
                } else if (checkedId == binding.chip1000.getId() && binding.chip1000.isChecked()) {
                    mShippingPrice.setValue(1000);
                } else if (checkedId == binding.chip1500.getId() && binding.chip1500.isChecked()) {
                    mShippingPrice.setValue(1500);
                } else if (checkedId == binding.chip2000.getId() && binding.chip2000.isChecked()) {
                    mShippingPrice.setValue(2000);
                } else if (checkedId == binding.chipOther.getId() && binding.chipOther.isChecked()) {
                    binding.tilPrice.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public String buildDescription(List<Cart> carts){
        StringBuilder description = new StringBuilder();
        int totalPrice = 0;
        for (Cart c : carts){
            totalPrice += c.getQuantite() * c.getMontantTotal();
            description.append("\u2022")
                    .append(c.getLibelle()).append(" : ")
                    .append(c.getMontant_total())
                    .append(" x ".concat(String.valueOf(c.getQuantite())))
                    .append(" = ".concat(String.valueOf(c.getQuantite() * c.getMontantTotal())))
                    .append(" FCFA").append("\n");
        }

        mTotalCartAmount = totalPrice;
        return description.toString();
    }
}