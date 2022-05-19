package com.israel.livraisonexpresspos.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Api;
import com.israel.livraisonexpresspos.databinding.ActivityFilterResultBinding;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.models.from_steed_app.OrderSteed;
import com.israel.livraisonexpresspos.ui.orders.OrderListAdapter;

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

public class FilterResultActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityFilterResultBinding mBinding;
    private OrderListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_filter_result);
        initUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.currentActivity = this;
    }

    private void initUi() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new OrderListAdapter(this);
        mBinding.rvOrder.setLayoutManager(layoutManager);
        mBinding.rvOrder.setAdapter(adapter);
        getOrders();
        mBinding.fabFilter.setOnClickListener(this);
        mBinding.imgBtnStepBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == mBinding.imgBtnStepBack.getId()){

        }else if (id == mBinding.fabFilter.getId()){

        }
    }

    private void getOrders() {
        Call<ResponseBody> call = Api.order().getOrders(User.getCurrentUser(this).getToken(), toMap(getIntent().getStringExtra("filterConstraint")), 1);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mBinding.progressBar.setVisibility(View.GONE);
                mBinding.fabFilter.setCount(1);
                try {
                    if (response.isSuccessful()) {
                        String outPut = response.body().string();
                        JSONObject jsonObject = new JSONObject(outPut);
                        if (jsonObject.getBoolean("success")) {
                            ArrayList<OrderSteed> orderSteeds = new Gson().fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<List<OrderSteed>>(){}.getType());

                            if (orderSteeds != null){
                                if (orderSteeds.size() > 0){
                                    adapter.setOrderSteeds(orderSteeds);
                                }else {
                                    //todo : no race to display
                                }
                            }else {
                                //todo : no race to display
                            }
                        }
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    App.handleError(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mBinding.progressBar.setVisibility(View.GONE);
                mBinding.fabFilter.setCount(1);
                //todo : something went wrong
            }
        });
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


}