package com.israel.livraisonexpresspos.data.services.inventory_management;

//import okhttp3.ResponseBody;
//import retrofit2.Call;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovesManagementService {

    /**
     * This methode return a paginated list of moves regardless of move's type
     *
     * @param authToken : authenticated user token
     * @param page      : the page for pagination purpose
     */
    @GET("mouvements")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json"
    })
    Call<ResponseBody> loadMoves(
            @Header("Authorization") String authToken,
            @Query("page") int page
    );


    /**
     * This methode return a paginated list of moves regardless of move's type; additional details are added to moves
     *
     * @param authToken : authenticated user token
     * @param page      : the page for pagination purpose
     */
    @GET("/mouvements/getAll")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json"
    })
    Call<ResponseBody> loadMovesWithDetails(
            @Header("Authorization") String authToken,
            @Query("page") int page
    );


    /**
     * This methode return a list of moves for the provided move's type
     *
     * @param authToken  : authenticated user token
     * @param actionType : the page for pagination purpose
     */
    @GET("mouvements/getAllByTypeAction/{action}")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json"
    })
    Call<ResponseBody> loadMovesForType(
            @Header("Authorization") String authToken,
            @Path("action") String actionType
    );


    /**
     * This methode return a list of moves for the provided move's type and for a given site
     *
     * @param authToken  : authenticated user token
     * @param actionType : the page for pagination purpose
     * @param siteId     : the site id
     */
    @GET("/mouvements/ getAllByTypeActionAndSite/{action}/{site_id}")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json"
    })
    Call<ResponseBody> loadMovesForType(
            @Header("Authorization") String authToken,
            @Query("action") String actionType,
            @Query("site_id") int siteId
    );


    /**
     * This methode return details on a given move
     *
     * @param authToken : authenticated user token
     * @param moveId    : the site id
     */
    @GET("/mouvements/ getAllByTypeActionAndSite/{action}/{site_id}")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json"
    })
    Call<ResponseBody> loadMoveDetails(
            @Header("Authorization") String authToken,
            @Query("movement_id") int moveId
    );


    /**
     * This methode return a list of managers
     *
     * @param authToken : authenticated user token
     */
    @GET("/mouvements/gestionnaires")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json"
    })
    Call<ResponseBody> loadManagers(
            @Header("Authorization") String authToken
    );


    /**
     * This methode return a list of managers
     *
     * @param authToken : authenticated user token
     */
    @GET("/mouvements/coursiers")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json"
    })
    Call<ResponseBody> loadDeliveryMen(
            @Header("Authorization") String authToken
    );

    /**
     * This methode return a list of available cities
     *
     * @param authToken : authenticated user token
     */
    @GET("/mouvements/ getAllActivesVilles")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json"
    })
    Call<ResponseBody> loadCities(
            @Header("Authorization") String authToken
    );
}
