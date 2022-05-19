package com.israel.livraisonexpresspos.ui.contact_detail.orders;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Api;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.models.from_steed_app.OrderSteed;
import com.israel.livraisonexpresspos.utils.Values;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactOrderViewModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> mLoading;
    private final MutableLiveData<Boolean> mSuccess;
    private final MutableLiveData<List<OrderSteed>> mOrders;

    public ContactOrderViewModel(@NonNull Application application) {
        super(application);
        mLoading = new MutableLiveData<>();
        mSuccess = new MutableLiveData<>();
        mOrders = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getLoading() {
        return mLoading;
    }

    public MutableLiveData<Boolean> getSuccess() {
        return mSuccess;
    }

    public MutableLiveData<List<OrderSteed>> getOrders() {
        return mOrders;
    }

    public void getContactOrder(int id){
        mLoading.setValue(true);
        Api.order().getUserOrder(User.getCurrentUser(getApplication()).getToken(), id, buildDefaultConstraint()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                mLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String output = response.body().string();
                        JSONObject outputObject = new JSONObject(output);
                        JSONArray data = outputObject.getJSONArray("data");
                        List<OrderSteed> orders = new Gson().fromJson(data.toString()
                                , new TypeToken<List<OrderSteed>>(){}.getType());
                        mOrders.setValue(orders);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        App.handleError(e);
                    }
                } else {
                    if (response.code() == 401){
                        Values.unAuthorizedDialog();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                mLoading.setValue(false);
            }
        });
    }

    private Map<String, String> buildDefaultConstraint() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        Date date = Calendar.getInstance().getTime();
        String start = "01" + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1)
                + "/" + Calendar.getInstance().get(Calendar.YEAR);
        Map<String, String> constraint = new HashMap<>();
        constraint.put("ville", "");
        constraint.put("from_date", start);
        constraint.put("to_date", format.format(date));
        constraint.put("quartier", "");
        constraint.put("tir_date", "asc");
        constraint.put("quartier_type", "");
        constraint.put("per_page", "20");
        if (User.getCurrentUser(getApplication()).getRoles().contains("gerant"))constraint.put("is_manager", "true");
        return constraint;
    }
}
