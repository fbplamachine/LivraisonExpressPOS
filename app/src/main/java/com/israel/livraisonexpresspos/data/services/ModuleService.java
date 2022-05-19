package com.israel.livraisonexpresspos.data.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ModuleService {
    @GET("preload")
    @Headers("Accept: application/json")
    Call<ResponseBody> getModuleConfigs(
            @Query("city")String city
    );
}
