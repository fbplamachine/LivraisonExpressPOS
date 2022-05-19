package com.israel.livraisonexpresspos.ui.statistics.shop;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Api;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.models.from_steed_app.Badge;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticViewModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> mLoading;
    private final MutableLiveData<Badge> mBadge;
    private String mStartDate, mEndDate;

    public StatisticViewModel(@NonNull Application application) {
        super(application);
        mLoading = new MutableLiveData<>();
        mBadge = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getLoading() {
        return mLoading;
    }

    public MutableLiveData<Badge> getBadge() {
        return mBadge;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public void getShopStatistics(int id, String startDate, String endDate){
        mStartDate = startDate;
        mEndDate = endDate;
        mLoading.setValue(true);
        Map<String, String> body = new HashMap<>();
        body.put("ville", "");
        body.put("quartier_type", "0");
        body.put("quartier", "");
        body.put("from_date", startDate);
        body.put("to_date", endDate);
        Api.order().getShopStatistics(id, User.getCurrentUser(getApplication()).getToken(), body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                mLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null){
                    try{
                        String output = response.body().string();
                        JSONObject object = new JSONObject(output).getJSONObject("data");
                        Badge badge = new Gson().fromJson(object.toString(), new TypeToken<Badge>(){}.getType());
                        mBadge.setValue(badge);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        App.handleError(e);
                    }
                }else{

                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                mLoading.setValue(false);
            }
        });
    }

    public void getDeliveryMenStatistics(String date){
        mLoading.setValue(true);
        Map<String, Object> body = new HashMap<>();
        body.put("date", "2021-03-15 23:00:00");
        Api.order().deliveryMen(User.getCurrentUser(getApplication()).getToken(), body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                mLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null){
                    try{
                        String output = response.body().string();
                        JSONObject object = new JSONObject(output).getJSONObject("data");
//                        Badge badge = new Gson().fromJson(object.toString(), new TypeToken<Badge>(){}.getType());
//                        mBadge.setValue(badge);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        App.handleError(e);
                    }
                }else{

                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

            }
        });
    }
}
