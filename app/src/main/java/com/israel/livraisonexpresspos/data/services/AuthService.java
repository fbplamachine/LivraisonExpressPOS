package com.israel.livraisonexpresspos.data.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthService {

    @POST("login")
    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    Call<ResponseBody> login(
            @Field("email") String username,
            @Field("password") String password
    );

    @GET("user")
    @Headers({"Accept: application/json"})
    Call<ResponseBody> user (
            @Header("Authorization") String authorization
    );
}
