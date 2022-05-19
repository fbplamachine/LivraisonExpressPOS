package com.israel.livraisonexpresspos.data.services.inventory_management;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SiteManagementService {

    /**
     * This methode create a new site
     * @param authToken : authenticated user token
     */
    @POST("/site")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json"
    })
    Call<ResponseBody> createSite(
            @Header("Authorization") String authToken,
            @Body Map<String, String> site
    );




    /**
     * This methode update site
     * @param authToken : authenticated user token
     */
    @POST("/site/{id}")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json"
    })
    Call<ResponseBody> updateSite(
            @Header("Authorization") String authToken,
            @Body Map<String, String> site
    );




    /**
     * This methode return a list of available sites
     *
     * @param authToken : authenticated user token
     */
    @GET("/sites")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json"
    })
    Call<ResponseBody> loadSites(
            @Header("Authorization") String authToken
    );



    /**
     * This methode return a details for a given site
     *
     * @param authToken : authenticated user token
     */
    @GET("/site/{site_id}")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json"
    })
    Call<ResponseBody> loadSitesDetails(
            @Header("Authorization") String authToken,
            @Query("site_id") int siteId
    );



    /**
     * This methode return a list of sites for a given city
     *
     * @param authToken : authenticated user token
     */
    @GET("/site/byVille/{ville_id}")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json"
    })
    Call<ResponseBody> loadSitesForCity(
            @Header("Authorization") String authToken,
            @Query("ville_id") int villeId
    );




    /**
     * This methode return a list of neighborhood for a given city
     *
     * @param authToken : authenticated user token
     */
    @GET("/site/ getAllQuartiersByVille/{ville_id}")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json"
    })
    Call<ResponseBody> loadNeighborhoodForCity(
            @Header("Authorization") String authToken,
            @Query("ville_id") int villeId
    );





    /**
     * This methode return a list of shops for a given site
     *
     * @param authToken : authenticated user token
     */
    @GET("/site/magasinsBySite/{site_id}")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json"
    })
    Call<ResponseBody> loadShopForSite(
            @Header("Authorization") String authToken,
            @Query("site_id") int siteId
    );




    /**
     * This methode return a product list of shops on a specific move
     *
     * @param authToken : authenticated user token
     */
    @GET("/site/produitsByMagasin/{magasin_id}/{site_depart_id}/{site_arrivee_id}")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json"
    })
    Call<ResponseBody> loadProductForMove(
            @Header("Authorization") String authToken,
            @Query("site_id") int siteId
    );


}
