package com.israel.livraisonexpresspos.ui.order_detail.sender;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Api;
import com.israel.livraisonexpresspos.models.Address;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.utils.Values;

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

public class SenderViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Address>> mAddressesSender;
    private final MutableLiveData<Boolean> mLoadingSender;

    public SenderViewModel(@NonNull Application application) {
        super(application);
        mAddressesSender = new MutableLiveData<>();
        mLoadingSender = new MutableLiveData<>();
    }

    public MutableLiveData<List<Address>> getAddresses() {
        return mAddressesSender;
    }

    public MutableLiveData<Boolean> getLoading() {
        return mLoadingSender;
    }

    public void fetchAddresses(int id){
        mLoadingSender.setValue(true);
        Call<ResponseBody> call = Api.order().getClientAddresses(User.getCurrentUser(getApplication()).getToken(), id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                mLoadingSender.setValue(false);
                if (response.isSuccessful() && response.body() != null){
                    try{
                        String output = response.body().string();
                        JSONObject outputObject = new JSONObject(output);
                        if (outputObject.getBoolean("success")){
                            JSONArray array = outputObject.getJSONArray("data");
                            if (array.length() > 1){
                                List<Address> addresses = new Gson().fromJson(array.toString()
                                        , new TypeToken<List<Address>>(){}.getType());
                                mAddressesSender.setValue(addresses);
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
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                mLoadingSender.setValue(false);
            }
        });
    }
}
