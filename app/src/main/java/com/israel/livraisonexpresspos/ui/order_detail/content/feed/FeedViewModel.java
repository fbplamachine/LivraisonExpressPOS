package com.israel.livraisonexpresspos.ui.order_detail.content.feed;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Api;
import com.israel.livraisonexpresspos.models.Feed;
import com.israel.livraisonexpresspos.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedViewModel extends AndroidViewModel {
    private List<Feed> feedItems;
    public FeedViewModel(@NonNull Application application) {
        super(application);
        feedItems = new ArrayList<>();
    }

    public List<Feed> getFeedItems() {
        return feedItems;
    }

    public void setFeedItems(List<Feed> feedItems) {
        this.feedItems.clear();
        this.feedItems.addAll(feedItems);
    }

    public void loadOrderFeed(int orderId,final OnFeedRequestResponse onFeedRequestResponse) {
        Call<ResponseBody> call = Api.order().getOrderStatusHistory(User.getCurrentUser(getApplication()).getToken(),orderId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        String strResponse = response.body().string();
                        JSONObject jsonResponse = new JSONObject(strResponse);
                        if (jsonResponse.getBoolean("success")){
                            JSONArray feedItemsArray = jsonResponse.getJSONArray("data");
//                            ArrayList<OrderActivityFeedItem> orderActivityFeedItems = new Gson().fromJson(feedItemsArray.toString(),new TypeToken<List<OrderActivityFeedItem>>(){}.getType());
                            if (feedItemsArray.length()>0){
                                Log.e("je suis dans la repose", "oui");
                                ArrayList<Feed> orderActivityFeedItems = new Gson().fromJson(feedItemsArray.toString()
                                        , new TypeToken<List<Feed>>(){}.getType());
                                onFeedRequestResponse.onFeedAvailable(orderActivityFeedItems);
                            }
                        }else {
                            onFeedRequestResponse.onFeedUnAvailable();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        App.handleError(e);
                    }
                }else {
                    onFeedRequestResponse.onFeedUnAvailable();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                onFeedRequestResponse.onFeedUnAvailable();
            }
        });
    }

    public  interface OnFeedRequestResponse {
        void onFeedUnAvailable();
        void onFeedAvailable(List<Feed> feedItems);
    }
}
