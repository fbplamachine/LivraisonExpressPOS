package com.israel.livraisonexpresspos.ui.shops;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.data.Api;
import com.israel.livraisonexpresspos.models.Shop;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopDialogViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Shop>> mShops;
    private final MutableLiveData<Boolean> mLoading;

    public ShopDialogViewModel(@NonNull @NotNull Application application) {
        super(application);
        mShops = new MutableLiveData<>();
        mLoading = new MutableLiveData<>();
    }


    public MutableLiveData<List<Shop>> getShops() {
        return mShops;
    }

    public MutableLiveData<Boolean> getLoading() {
        return mLoading;
    }

    public void fetchShops(){
        mLoading.setValue(true);
        Api.order().getShops(true).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                mLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null){
                    try {
                        String output = response.body().string();
                        JSONArray data = new JSONObject(output).getJSONArray("data");
                        List<Shop> shops = new Gson().fromJson(data.toString()
                                , new TypeToken<List<Shop>>(){}.getType());
                        mShops.setValue(shops);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                mLoading.setValue(false);
                Toast.makeText(getApplication(), "Veuillez vérifier votre connexion internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
