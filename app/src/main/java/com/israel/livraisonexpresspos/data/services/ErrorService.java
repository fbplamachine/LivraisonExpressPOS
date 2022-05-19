package com.israel.livraisonexpresspos.data.services;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ErrorService {

    @POST("user/errors")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json",
    })
    Call<ResponseBody> postError(
            @Header("Authorization") String auth,
            @Body Map<String, String> error
    );
}
