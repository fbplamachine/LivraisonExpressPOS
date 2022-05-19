package com.israel.livraisonexpresspos.data.services.inventory_management;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface StockFilterService {

    /**
     * This methode return a list of available shop in the given city
     *
     * @param authToken : authenticated user token
     * @param cityId
     */
    @GET("mouvements")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json"
    })
    Call<ResponseBody> loadShops(
            @Header("Authorization") String authToken,
            @Query("city_id") Integer cityId);



}
