package com.israel.livraisonexpresspos.ui.statistics.sale;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.models.CancellationStatistics;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Api;
import com.israel.livraisonexpresspos.models.ProductStatistic;
import com.israel.livraisonexpresspos.models.ShopStatistic;
import com.israel.livraisonexpresspos.models.User;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleStatisticsViewModel extends AndroidViewModel {
    private final MutableLiveData<List<ShopStatistic>> mSales;
    private final MutableLiveData<Boolean> mLoad;
    private final MutableLiveData<Boolean> mProductLoad;
    private final MutableLiveData<Boolean> mCancellationLoad;
    private final MutableLiveData<List<ProductStatistic>> mProductStatistics;
    private final MutableLiveData<List<CancellationStatistics>> mCancellationStatistics;

    public SaleStatisticsViewModel(@NonNull Application application) {
        super(application);
        mSales = new MutableLiveData<>();
        mLoad = new MutableLiveData<>();
        mProductLoad = new MutableLiveData<>();
        mCancellationLoad = new MutableLiveData<>();
        mProductStatistics = new MutableLiveData<>();
        mCancellationStatistics = new MutableLiveData<>();
    }

    public MutableLiveData<List<ShopStatistic>> getSales() {
        return mSales;
    }

    public MutableLiveData<Boolean> getLoad() {
        return mLoad;
    }

    public MutableLiveData<Boolean> getProductLoad() {
        return mProductLoad;
    }

    public MutableLiveData<Boolean> getCancellationLoad() {
        return mCancellationLoad;
    }

    public MutableLiveData<List<ProductStatistic>> getProductStatistics() {
        return mProductStatistics;
    }

    public MutableLiveData<List<CancellationStatistics>> getCancellationStatistics() {
        return mCancellationStatistics;
    }

    public void getSaleStatistics(int id){
        mLoad.setValue(true);
        Map<String, Object> map = new HashMap<>();
        map.put("annee", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        map.put("magasin_id", id);
        Api.order().sales(User.getCurrentUser(getApplication()).getToken(), map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                mLoad.setValue(false);
                if (response.isSuccessful() && response.body() != null){
                    try {
                        String output = response.body().string();
                        List<ShopStatistic> statistics = new Gson().fromJson(output, new TypeToken<List<ShopStatistic>>(){}.getType());
                        mSales.setValue(statistics);
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

    public void getMvpProducts(String startDate, String endDate, int shopId){
        mProductLoad.setValue(true);
        Map<String, Object> map = new HashMap<>();
        map.put("date", startDate);
        map.put("endDate", endDate);
        map.put("magasin_id", shopId);
        map.put("statut_code", 4);
        Api.order().products(User.getCurrentUser(getApplication()).getToken(), map)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        mProductLoad.setValue(false);
                        if (response.isSuccessful() && response.body() != null){
                            try{
                                String output = response.body().string();
                                JSONObject outputObject = new JSONObject(output);
                                JSONArray data = outputObject.getJSONArray("magasin_produits_stats");
                                List<ProductStatistic> productStatistics = new Gson().fromJson(data.toString(), new TypeToken<List<ProductStatistic>>(){}.getType());
                                if (productStatistics.size() > 5){
                                    mProductStatistics.setValue(productStatistics.subList(0, 5));
                                }else {
                                    mProductStatistics.setValue(productStatistics);
                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        mProductLoad.setValue(false);
                    }
                });
    }

    public void getCancellationStatistics(String startDate, String endDate, int shopId){
        mCancellationLoad.setValue(true);
        Map<String, Object> map = new HashMap<>();
        map.put("date", startDate);
        map.put("endDate", endDate);
        map.put("magasin_id", shopId);
        Api.order().cancellations(User.getCurrentUser(getApplication()).getToken(), map)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        mCancellationLoad.setValue(false);
                        if (response.isSuccessful() && response.body() != null){
                            try{
                                String output = response.body().string();
                                JSONObject outputObject = new JSONObject(output);
                                JSONArray data = outputObject.getJSONArray("stats");
                                List<CancellationStatistics> cancellationStatistics = new Gson().fromJson(data.toString(), new TypeToken<List<CancellationStatistics>>(){}.getType());
                                if (cancellationStatistics.size() > 5){
                                    mCancellationStatistics.setValue(cancellationStatistics.subList(0, 5));
                                }else {
                                    mCancellationStatistics.setValue(cancellationStatistics);
                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        mCancellationLoad.setValue(false);
                    }
                });
    }

}
