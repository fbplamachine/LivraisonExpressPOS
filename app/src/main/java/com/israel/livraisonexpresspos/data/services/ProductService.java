package com.israel.livraisonexpresspos.data.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductService {

    @POST("produits")
    @FormUrlEncoded
    @Headers("Accept: application/json")
    Call<ResponseBody> getProductsFromSubcategory(
            @Field("categorie_id") int categoryId,
            @Field("sous_categorie_id") int subcategoryId,
            @Field("magasin_id") int shopId,
            @Query("page")int page
    );

    @POST("produits")
    @FormUrlEncoded
    @Headers("Accept: application/json")
    Call<ResponseBody> getProductsFromCategory(
            @Field("categorie_id") int categoryId,
            @Field("magasin_id") int shopId,
            @Query("page")int page
    );

    @GET("magasins/{shopId}/produits")
    @Headers("Accept: application/json")
    Call<ResponseBody> getProductsFromShop(
            @Path("shopId") int shopId
    );
}
