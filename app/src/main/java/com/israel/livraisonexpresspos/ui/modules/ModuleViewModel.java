package com.israel.livraisonexpresspos.ui.modules;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Api;
import com.israel.livraisonexpresspos.data.Room.repository.CartRepository;
import com.israel.livraisonexpresspos.models.Cart;
import com.israel.livraisonexpresspos.models.City;
import com.israel.livraisonexpresspos.models.Module;
import com.israel.livraisonexpresspos.utils.PreferenceUtils;
import com.israel.livraisonexpresspos.utils.Values;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModuleViewModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> mLoading;
    private final MutableLiveData<List<Module>> mModules;
    private final MutableLiveData<List<City>> mCities;
    private final MutableLiveData<String> mError;
    private final CartRepository mCartRepository;

    public ModuleViewModel(@NotNull Application application) {
        super(application);
        mLoading = new MutableLiveData<>();
        mModules = new MutableLiveData<>();
        mCities = new MutableLiveData<>();
        mError = new MutableLiveData<>();
        mCartRepository = new CartRepository(application);
    }

    public LiveData<List<Cart>> getCartItems(){
        return mCartRepository.getAllLiveItems(Values.order.getId(), 0);
    }

    public MutableLiveData<Boolean> getLoading() {
        return mLoading;
    }

    public MutableLiveData<List<Module>> getModules() {
        return mModules;
    }

    public MutableLiveData<String> getError() {
        return mError;
    }

    public MutableLiveData<List<City>> getCities() {
        return mCities;
    }


    public void getConfigs(final Context context, final String city){
        if (App.isConnected){
            mLoading.setValue(true);
            Api.preload().getModuleConfigs(city).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    mLoading.setValue(false);
                    if (response.isSuccessful() && response.body() != null){
                        try{
                            String output = response.body().string();
                            JSONObject object = new JSONObject(output);
                            if (object.getBoolean("success")){
                                PreferenceUtils.setString(context, PreferenceUtils.SELECTED_CITY, city.toUpperCase());
                                JSONObject data = object.getJSONObject("data");
                                ArrayList<Module> modules = new Gson()
                                        .fromJson(data.getJSONArray("modules").toString(),
                                                new TypeToken<ArrayList<Module>>(){}.getType());
                                ArrayList<City> cities = new Gson()
                                        .fromJson(data.getJSONArray("cities").toString(),
                                                new TypeToken<ArrayList<City>>(){}.getType());
                                int genericProductId = data.getJSONObject("configuration").getInt("produit_generique");
                                int internOrderId = data.getJSONObject("configuration").getInt("course_interne");

                                JsonArray moduleArray = new Gson().toJsonTree(modules,
                                        new TypeToken<ArrayList<Module>>(){}.getType()).getAsJsonArray();
                                JsonArray cityArray = new Gson().toJsonTree(cities,
                                        new TypeToken<ArrayList<Module>>(){}.getType()).getAsJsonArray();

                                PreferenceUtils.setString(context, PreferenceUtils.MODULES,
                                        moduleArray.toString());
                                PreferenceUtils.setString(context, PreferenceUtils.MODULES_ + city,
                                        moduleArray.toString());
                                PreferenceUtils.setString(context, PreferenceUtils.CITIES,
                                        cityArray.toString());
                                PreferenceUtils.setInt(context, PreferenceUtils.GENERIC_PRODUCT, genericProductId);
                                PreferenceUtils.setInt(context, PreferenceUtils.INTERN_ORDER, internOrderId);

                                mModules.setValue(modules);
                                mCities.setValue(cities);
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            App.handleError(e);
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    mLoading.setValue(false);
                    getConfigsFromCache(context, city);
                }
            });
        }else {
            getConfigsFromCache(context, city);
        }
    }

    private void getConfigsFromCache(Context context, String city){
        String stringModules = PreferenceUtils.getString(context, PreferenceUtils.MODULES_ + city);
        String stringCities = PreferenceUtils.getString(context, PreferenceUtils.CITIES);
        if (!stringModules.equals("") && !stringCities.equals("")) {
            ArrayList<Module> modules = new Gson()
                    .fromJson(stringModules,
                            new TypeToken<ArrayList<Module>>() {
                            }.getType());
            ArrayList<City> cities = new Gson()
                    .fromJson(stringCities,
                            new TypeToken<ArrayList<City>>() {
                            }.getType());
            mModules.setValue(modules);
            mCities.setValue(cities);
        }else {
            Values.alertDialog(context.getString(R.string.check_internet_connection));
        }
    }
}
