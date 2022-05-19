package com.israel.livraisonexpresspos.ui.order_detail.content;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Api;
import com.israel.livraisonexpresspos.data.Room.repository.UnSyncedRepository;
import com.israel.livraisonexpresspos.models.Cart;
import com.israel.livraisonexpresspos.models.Feed;
import com.israel.livraisonexpresspos.models.UnSynced;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.models.from_steed_app.OrderSteed;
import com.israel.livraisonexpresspos.utils.CourseStatus;
import com.israel.livraisonexpresspos.utils.Values;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContentViewModel extends AndroidViewModel implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private final MutableLiveData<String> mStatusHuman;
    private final MutableLiveData<String> mDeliveryDate;
    private final MutableLiveData<Boolean> mLoad;
    private final MutableLiveData<Integer> mStatus;
    private final MutableLiveData<List<User>> mUsers;
    private final MutableLiveData<Cart> mCart;
    private final MutableLiveData<Integer> mDeliveryCost;
    private Call<ResponseBody> mCall;
    private final UnSyncedRepository mUnSyncedRepository;
    private final MutableLiveData<String> mTime;
    private String mDate;
    private DatePickerDialog mDatePickerDialog;
    private TimePickerDialog mTimePickerDialog;
    private final User mUser;

    public ContentViewModel(@NonNull Application application) {
        super(application);
        mUser = User.getCurrentUser(application);
        mUnSyncedRepository = new UnSyncedRepository(application);
        mStatus = new MutableLiveData<>();
        mStatusHuman = new MutableLiveData<>();
        mDeliveryDate = new MutableLiveData<>();
        mUsers = new MutableLiveData<>();
        mLoad = new MutableLiveData<>();
        mTime = new MutableLiveData<>();
        mCart = new MutableLiveData<>();
        mDeliveryCost = new MutableLiveData<>();
    }

    public MutableLiveData<Integer> getStatus() {
        return mStatus;
    }

    public MutableLiveData<String> getStatusHuman() {
        return mStatusHuman;
    }

    public MutableLiveData<List<User>> getUsers() {
        return mUsers;
    }

    public MutableLiveData<String> getTime() {
        return mTime;
    }

    public MutableLiveData<Cart> getCart() {
        return mCart;
    }

    public MutableLiveData<Integer> getDeliveryCost() {
        return mDeliveryCost;
    }

    public MutableLiveData<String> getDeliveryDate() {
        return mDeliveryDate;
    }

    public void updateStatus(final OrderSteed orderSteed, final String message, final String requestState, final int newStatus, final String newStatusHuman){
        if (App.isConnected){
            mLoad.setValue(true);
            mCall = Api.order().updateOrderStatus(
                    mUser.getToken(),
                    orderSteed.getInfos().getId(),
                    requestState,
                    orderSteed.getPaiement().getMontant_total()
            );
            mCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    mLoad.setValue(false);
                    if (response.isSuccessful() && response.body() != null){
                        try {
                            String output = response.body().string();
                            JSONObject outputObject = new JSONObject(output);
                            if (outputObject.getBoolean("success")){
                                mStatus.setValue(newStatus);
                                mStatusHuman.setValue(newStatusHuman);
                                Toasty.success(getApplication(), message, Toast.LENGTH_LONG).show();
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            App.handleError(e);
                        }
                    }else {
                        if (response.code() == 401){
                            Values.unAuthorizedDialog();
                        }
                        Toasty.info(getApplication(), "Une erreur est survenue").show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    mLoad.setValue(false);
                    Toasty.info(getApplication(), "Erreur de connection").show();
                }
            });
        } else {
            if (orderSteed.getKey() == null || orderSteed.getKey().isEmpty()){
                Toast.makeText(App.currentActivity, "Veuillez sortir puis recommencer le processus de changement de statut", Toast.LENGTH_LONG).show();
            }
            orderSteed.getInfos().setStatut(newStatus);
            orderSteed.getInfos().setStatut_human(newStatusHuman);
            orderSteed.getInfos().addDeliveryMan(mUser.getId());
            mStatus.setValue(newStatus);
            mStatusHuman.setValue(newStatusHuman);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                    .child("courses")
                    .child(orderSteed.getKey());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Toasty.success(getApplication(), message, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject object = new JSONObject();
                        object.put("order_id", orderSteed.getInfos().getId());
                        object.put("status", requestState);
                        object.put("total_price", orderSteed.getPaiement().getMontant_total());
                        UnSynced unSynced = new UnSynced();
                        unSynced.setDateTime(Calendar.getInstance().getTimeInMillis());
                        unSynced.setObject(object.toString());
                        unSynced.setType(UnSynced.STATUS);
                        mUnSyncedRepository.insert(unSynced);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        App.handleError(e);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            reference.setValue(orderSteed);
        }
    }

    public void managerStatusChange(int id, final String status, Map<String, Object> body){
        mLoad.setValue(true);
        mCall = Api.order().managerOrderStatusChange(
                mUser.getToken(),
                id,
                status,
                body
        );
        mCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                mLoad.setValue(false);
                if (response.isSuccessful() && response.body() != null){
                    try {
                        String output = response.body().string();
                        JSONObject outputObject = new JSONObject(output);
                        if (outputObject.getBoolean("success")){
                            switch (status) {
                                case "delivered":
                                    mStatus.setValue(CourseStatus.CODE_DELIVERED);
                                    mStatusHuman.setValue("Terminée");
                                    Toasty.success(getApplication(), "Course terminée avec succes", Toast.LENGTH_LONG).show();
                                    break;
                                case "to_validate":
                                    mStatus.setValue(CourseStatus.CODE_TO_VALIDATE);
                                    mStatusHuman.setValue("A Valider");
                                    Toasty.success(getApplication(), "Le statut de la course a été changé avec success", Toast.LENGTH_LONG).show();
                                    break;
                                case "reset":
                                    mStatus.setValue(CourseStatus.CODE_UNASSIGNED);
                                    mStatusHuman.setValue("Non assignée");
                                    Toasty.success(getApplication(), "Course désassignée avec succes", Toast.LENGTH_LONG).show();
                                    break;
                                case "canceled":
                                    mStatus.setValue(CourseStatus.CODE_CANCELED);
                                    mStatusHuman.setValue("Annulée");
                                    Toasty.success(getApplication(), "Course annulée avec succes", Toast.LENGTH_LONG).show();
                                    break;
                                case "relaunch":
                                    mStatus.setValue(CourseStatus.CODE_RELAUNCH);
                                    mStatusHuman.setValue("Relance");
                                    mDeliveryDate.setValue(mTime.getValue());
                                    Toasty.success(getApplication(), "Course relancé avec succes", Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        App.handleError(e);
                    }
                }else {
                    if (response.code() == 401){
                        Values.unAuthorizedDialog();
                    }
                    Toasty.info(getApplication(), "Une erreur est survenue").show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                mLoad.setValue(false);
                t.printStackTrace();
                Toasty.info(getApplication(), "Erreur de connection").show();
            }
        });
    }

    public void rejectOrder(String motif, int id, final Activity context){
        mLoad.setValue(true);
        mCall = Api.order().rejectOrder(mUser.getToken(), id, motif);
        mCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                mLoad.setValue(false);
                if (response.isSuccessful() && response.body() != null){
                    try {
                        String output = response.body().string();
                        JSONObject outputObject = new JSONObject(output);
                        if (outputObject.getBoolean("success")){
                            Toasty.success(context, "Course rejetée avec succès").show();
                            context.finish();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        App.handleError(e);
                    }
                } else {

                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                mLoad.setValue(false);
            }
        });
    }

    public MutableLiveData<Boolean> getLoad() {
        return mLoad;
    }

    public void relaunchOrder(int id, Map<String, Object> map, final Activity context) {
        mLoad.setValue(true);
        mCall = Api.order().rescheduleOrder(mUser.getToken(), id, map);
        mCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mLoad.setValue(false);
                    try {
                        String output = response.body().string();
                        JSONObject outputObject = new JSONObject(output);
                        if (outputObject.getBoolean("success")) {
                            Toasty.success(context, "Course relancée avec succès").show();
                            context.finish();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        App.handleError(e);
                    }
                }else {
                    if (response.code() == 401){
                        Values.unAuthorizedDialog();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

            }
        });
    }

    public void initTime(){
        Calendar newCalendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        date.setTime(newCalendar.getTimeInMillis() + (30 * 60 * 1000));
        mTime.setValue(format.format(date) + ":00");
    }

    public void assignOrder(final Context context, int orderId, int deliveryManId){
        mLoad.setValue(true);
        mCall = Api.order().assignOrder(mUser.getToken(), orderId, deliveryManId);
        mCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                mLoad.setValue(false);
                if (response.isSuccessful() && response.body() != null){
                    try{
                        String output = response.body().string();
                        JSONObject outputObject = new JSONObject(output);
                        if (outputObject.getBoolean("success")){
                            mStatus.setValue(CourseStatus.CODE_ASSIGNED);
                            mStatusHuman.setValue("Assigné");
                            Toasty.success(context, "Course assignée avec succès").show();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        App.handleError(e);
                    }
                }else {
                    if (response.code() == 401){
                        Values.unAuthorizedDialog();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                mLoad.setValue(false);
            }
        });
    }

    public void getDeliveryMen(){
        mCall = Api.order().getDeliveryMen(mUser.getToken());
        mCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null){
                    try{
                        String output = response.body().string();
                        JSONObject outputObject = new JSONObject(output);
                        if (outputObject.getBoolean("success")){
                            JSONArray data = outputObject.getJSONObject("data").getJSONArray("data");
                            List<User> users = new Gson().fromJson(data.toString()
                                    , new TypeToken<List<User>>(){}.getType());
                            mUsers.setValue(users);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        App.handleError(e);
                    }
                }else {
                    if (response.code() == 401){
                        Values.unAuthorizedDialog();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                mLoad.setValue(false);
            }
        });
    }

    public void updateDeliveryTime(final Context context, int orderId, final String date, final TextView textView){
        mLoad.setValue(true);
        mCall = Api.order().updateOrderDeliveryTime(mUser.getToken(), orderId, date);
        mCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                mLoad.setValue(false);
                if (response.isSuccessful() && response.body() != null){
                    textView.setText(date);
                    Toasty.success(context, "Heure de livraison modifié avec success.").show();
                }else {
                    if (response.code() == 401){
                        Values.unAuthorizedDialog();
                    }else {
                        Toasty.error(context, "Une erreur est survenu.").show();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                mLoad.setValue(false);
                Toasty.error(context, "Veuillez vérifier votre connexion internet.").show();
            }
        });
    }

    public void loadOrderFeed(int orderId, final OnFeedAvailable onFeedAvailable) {
        mCall = Api.order().getOrderStatusHistory(mUser.getToken(),orderId);
        mCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        String strResponse = response.body().string();
                        JSONObject jsonResponse = new JSONObject(strResponse);
                        if (jsonResponse.getBoolean("success")){
                            JSONArray feedItemsArray = jsonResponse.getJSONArray("data");
                            ArrayList<Feed> orderActivityFeedItems = new Gson().fromJson(feedItemsArray.toString(),new TypeToken<Feed>(){}.getType());
                            onFeedAvailable.onFeedAvailable(orderActivityFeedItems);
                        }else {

                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        App.handleError(e);
                    }
                }else {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void upsertProduct(int courseId, final Cart cart){
        mLoad.setValue(true);
        Map<String, Object> objectMap = new HashMap<>();
        if (cart.getId() > 0) objectMap.put("id", cart.getId());
        objectMap.put("produit_id", cart.getProduct_id());
        objectMap.put("libelle", cart.getLibelle());
        objectMap.put("prix_unitaire", cart.getPrix_unitaire());
        objectMap.put("quantite", cart.getQuantite());
        objectMap.put("montant_soustotal", cart.getPrix_unitaire());
        Api.order().upsertProductToCart(courseId,
                mUser.getToken(),
                objectMap
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                mLoad.setValue(false);
                if (response.isSuccessful() && response.body() != null){
                    try{
                        String output = response.body().string();
                        if (cart.getId() == 0){
                            JSONObject object = new JSONObject(output);
                            cart.setNewProduct(true);
                            cart.setId(object.getJSONObject("data").getInt("id"));
                        }
                        mCart.setValue(cart);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        App.handleError(e);
                    }
                }else {
                    try {
                        Values.errorDialog(response.errorBody() != null ? response.errorBody().string() :
                                response.body() != null ? response.body().string() : "Erreur");
                    } catch (IOException e) {
                        e.printStackTrace();
                        App.handleError(e);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                mLoad.setValue(false);
            }
        });

    }

    public void updateDeliveryPrice(int courseId, final int newCost){
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("shipping_coast", newCost);
        mLoad.setValue(true);
        Api.order().updateDeliveryPrice(
                courseId,
                mUser.getToken(),
                objectMap
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                mLoad.setValue(false);
                if (response.isSuccessful() && response.body() != null){
                    try{
                        String output = response.body().string();
                        mDeliveryCost.setValue(newCost);
                    } catch (IOException e) {
                        e.printStackTrace();
                        App.handleError(e);
                    }
                }else {
                    try {
                        Values.errorDialog(response.errorBody() != null ? response.errorBody().string() :
                                response.body() != null ? response.body().string() : "Erreur");
                    } catch (IOException e) {
                        e.printStackTrace();
                        App.handleError(e);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                mLoad.setValue(false);
            }
        });
    }

    public void deleteProductFromCart(int courseId, final Cart cart){
        mLoad.setValue(true);
        Api.order().deleteItemFromCart(
                mUser.getToken(),
                courseId,
                cart.getId()
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                mLoad.setValue(false);
                if (response.isSuccessful() && response.body() != null){
                    try{
                        String output = response.body().string();
                        cart.setDeleted(true);
                        mCart.setValue(cart);
                    } catch (IOException e) {
                        e.printStackTrace();
                        App.handleError(e);
                    }
                }else {
                    try {
                        Values.errorDialog(response.errorBody() != null ? response.errorBody().string() :
                                response.body() != null ? response.body().string() : "Erreur");
                    } catch (IOException e) {
                        e.printStackTrace();
                        App.handleError(e);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
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
        mTimePickerDialog = new TimePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT,  this,
                newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);
    }

    public void showDatePicker(){
        mDatePickerDialog.show();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mDate = year + "-" + String.format("%02d-%02d", month + 1, dayOfMonth);
        mTimePickerDialog.show();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mTime.setValue(mDate + " "+ String.format("%02d:%02d:%02d", hourOfDay, minute, 0));
    }

    public interface OnFeedAvailable{
        void onFeedAvailable(ArrayList<Feed> feedItems);
        void onFeedImportFailure();
    }

}
