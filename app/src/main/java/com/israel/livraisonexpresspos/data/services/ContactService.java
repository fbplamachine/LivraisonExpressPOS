package com.israel.livraisonexpresspos.data.services;

import com.israel.livraisonexpresspos.models.Contact;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ContactService {
    @GET("guests")
    @Headers({"Accept: application/json"})
    Call<ResponseBody> contact (
            @Header("Authorization") String authorization,
            @Query("q") String searchPattern
    );

    @POST("guests/bulk")
    @Headers({"Content-Type: Application/json", "Accept: application/json"})
    Call<ResponseBody> postContacts(
            @Header("Origin") String link,
            @Header("Authorization") String authorization,
            @Body List<Contact> contacts
    );

    @POST("guests")
    @Headers({"Content-Type: Application/json", "Accept: application/json"})
    Call<ResponseBody> postContact(
            @Header("Origin") String link,
            @Header("Authorization") String authorization,
            @Body Contact contact
    );

    @PUT("guests/{id}")
    @Headers({"Content-Type: Application/json", "Accept: application/json"})
    Call<ResponseBody> putContact (
            @Header("Origin") String link,
            @Header("Authorization") String authorization,
            @Path("id") String id,
            @Body Contact contact
    );
}