package com.israel.livraisonexpresspos.ui.filter;

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

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterViewModel extends AndroidViewModel {
    private final MutableLiveData<List<OrderSteed>> orderSteeds;
    private final MutableLiveData<Boolean> mIsLoading;
    private int mIssueType;
    private final MutableLiveData<String> mConstraint ;
    public static final int ISSUE_TYPE_FAILURE = 1;


    public FilterViewModel(@NonNull Application application) {
        super(application);

        orderSteeds = new MutableLiveData<>();
        mIsLoading =   new MutableLiveData<>();
        mIssueType = 0;
        mConstraint = new MutableLiveData<>();
        mConstraint.setValue("");
    }

    public MutableLiveData<List<OrderSteed>> getOrderSteeds() {
        return orderSteeds;
    }

    public void setOrderSteeds(List<OrderSteed> orderSteeds) {
        this.orderSteeds.setValue(orderSteeds);
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return mIsLoading;
    }

    public void setIsLoading(Boolean loading) {
        this.mIsLoading.setValue(loading);
    }

    public int getIssueType() {
        return mIssueType;
    }

    public void setIssueType(int mIssueType) {
        this.mIssueType = mIssueType;
    }

    public void getOrders(final String constraint) {
        mIsLoading.setValue(true);
        Call<ResponseBody> call = Api.order().getOrders(User.getCurrentUser(getApplication()).getToken(), toMap(constraint), 1);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                setIsLoading(false);
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String outPut = response.body().string();
                        JSONObject jsonObject = new JSONObject(outPut);
                        if (jsonObject.getBoolean("success")) {
                            ArrayList<OrderSteed> orderSteeds = new Gson().fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<List<OrderSteed>>() {
                            }.getType());
                            setOrderSteeds(orderSteeds);
                            storeConstraint(constraint);
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    App.handleError(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                setIssueType(ISSUE_TYPE_FAILURE);
                setIsLoading(false);
            }
        });
    }

    private void storeConstraint(String constraint) {
        mConstraint.setValue(constraint);
    }

    private Map<String, String> toMap(String constraint) {
        Map<String, String> mapConstraint = new HashMap<>();
        try {
            JSONObject jsonConstraint = new JSONObject(constraint);
            Iterator<String> keysItr = jsonConstraint.keys();
            while (keysItr.hasNext()) {
                String key = keysItr.next();
                String value = jsonConstraint.getString(key);
                mapConstraint.put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            App.handleError(e);
        }
        return mapConstraint;
    }

    public MutableLiveData<String> getConstraint() {
        return mConstraint;
    }

    public void setConstraint(String constraint) {
        mConstraint.setValue(constraint);
    }
}
