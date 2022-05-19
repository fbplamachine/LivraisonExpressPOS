package com.israel.livraisonexpresspos.ui.my_shops;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Api;
import com.israel.livraisonexpresspos.models.Shop;
import com.israel.livraisonexpresspos.models.User;

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

public class MyShopsViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Shop>> mShops;
    private final MutableLiveData<Boolean> mLoad;
    private final MutableLiveData<String> mError;

    public MyShopsViewModel(@NonNull Application application) {
        super(application);
        mShops = new MutableLiveData<>();
        mLoad = new MutableLiveData<>();
        mError = new MutableLiveData<>();
    }

    public MutableLiveData<List<Shop>> getShops() {
        return mShops;
    }

    public MutableLiveData<Boolean> getLoad() {
        return mLoad;
    }

    public MutableLiveData<String> getError() {
        return mError;
    }

    public void getUserShops(){
        mLoad.setValue(true);
        Api.order().getShops(User.getCurrentUser(getApplication()).getToken()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                mLoad.setValue(false);
                if (response.isSuccessful() && response.body() != null){
                    try{
                        String output = response.body().string();
                        JSONObject object = new JSONObject(output);
                        if (object.getBoolean("success")){
                            JSONArray data = object.getJSONArray("data");
                            List<Shop> shops = new Gson().fromJson(data.toString(), new TypeToken<List<Shop>>(){}.getType());
                            mShops.setValue(shops);
                        }
                    } catch (IOException | JSONException e) {
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
}
