package com.israel.livraisonexpresspos.data.services;

import com.israel.livraisonexpresspos.models.Delivery;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CourseService {
    @POST("user/purchases")
    @Headers({"Content-Type: Application/json", "Accept: application/json"})
    Call<ResponseBody> placeOrder(
            @Header("Origin") String link,
            @Header("Authorization") String authorization,
            @Body Delivery data
    );
}
