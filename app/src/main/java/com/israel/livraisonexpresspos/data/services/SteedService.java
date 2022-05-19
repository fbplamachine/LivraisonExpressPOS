package com.israel.livraisonexpresspos.data.services;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SteedService {

    /**
     * Retrieve badges from server according the filter_badges_constraints
     *
     * @param auth                    : the authenticate user token
     * @param badge_filter_constraint : built badge filter constraint
     */
    @POST("user/steed/courses/badges/v1.1") //todo : replace this one in the brackets
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json",
    })
    Call<ResponseBody> getBadges(
            @Header("Authorization") String auth,
            @Body Map<String, String> badge_filter_constraint
    );


    /**
     * Retrieve order's list from the server side for a specific user according the provide filter constraint.
     * this list could be paginated or not according the constraint status
     *
     * @param auth              the authenticate user token
     * @param filter_constraint : the built filter constraint
     * @param page              : the page for the pagination purpose
     */
    @POST("user/steed/courses/mycourses")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json",
    })
    Call<ResponseBody> getOrders(
            @Header("Authorization") String auth,
            @Body Map<String, String> filter_constraint,
            @Query("page") int page
    );

    @GET("courses/{id}")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json",
    })
    Call<ResponseBody> getOrder(
            @Header("Authorization") String auth,
            @Path("id") int id
    );

    @POST("user/manager/courses/{course_id}/purchases")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json",
    })
    Call<ResponseBody> upsertProductToCart(
            @Path("course_id") int courseId,
            @Header("Authorization") String auth,
            @Body Map<String, Object> objectMap
    );

    @GET("user/magasins")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json",
    })
    Call<ResponseBody> getShops(
            @Header("Authorization") String auth
    );


    @GET("magasins")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json",
    })
    Call<ResponseBody> getShops(
            @Query("simplified") boolean simplified
    );



    @PUT("user/manager/courses/{course_id}/update-shipping-coast")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json",
    })
    Call<ResponseBody> updateDeliveryPrice(
            @Path("course_id") int courseId,
            @Header("Authorization") String auth,
            @Body Map<String, Object> objectMap
    );

    @DELETE("user/manager/courses/{course_id}/purchases/{achat_id}")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json",
    })
    Call<ResponseBody> deleteItemFromCart(
            @Header("Authorization") String auth,
            @Path("course_id") int courseId,
            @Path("achat_id") int orderId
    );

    @DELETE("user/manager/courses/{course_id}/update-shipping-coast")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json",
    })
    Call<ResponseBody> editShippingCost(
            @Header("Authorization") String auth,
            @Path("course_id") int courseId
    );

    @POST("user/magasins/{magasins_id}/courses/badges")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json",
    })
    Call<ResponseBody> getShopStatistics(
            @Path("magasins_id") int id,
            @Header("Authorization") String auth,
            @Body Map<String, String> filter
    );

    @GET("user/steed/courses/{id}")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json",
    })
    Call<ResponseBody> getSteedOrder(
            @Header("Authorization") String auth,
            @Path("id") int id
    );

    /**
     * Change the date of the specific order
     *
     * @param auth :the authenticate user token
     * @param id   : the order id
     * @param date : the new date
     */
    @PUT("user/steed/courses/{id}/relaunch")
    @Headers({"Accept: application/json"})
    Call<ResponseBody> rescheduleOrder(
            @Header("Authorization") String auth,
            @Path("id") int id,
            @Body Map<String, Object> date
    );

    /**
     * Change the state of the specific order
     *
     * @param auth          :the authenticate user token
     * @param id            : the order id
     * @param state         : the new state
     * @param montant_total the coast of the order
     */
    @PUT("user/steed/courses/{id}/{state}")
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    Call<ResponseBody> updateOrderStatus(
            @Header("Authorization") String auth,
            @Path("id") int id,
            @Path("state") String state,
            @Field("montant_total") Integer montant_total
    );

    @PUT("user/manager/courses/{id}/{status}")
    @Headers({"Accept: application/json"})
    Call<ResponseBody> managerOrderStatusChange(
            @Header("Authorization") String auth,
            @Path("id") int id,
            @Path("status") String status,
            @Body Map<String, Object> body
    );

    @PUT("user/manager/courses/{course_id}/steed/{steed_id}/assign")
    @Headers({"Accept: application/json"})
    Call<ResponseBody> assignOrder(
            @Header("Authorization") String auth,
            @Path("course_id") int orderId,
            @Path("steed_id") int deliveryManId
    );

    /**
     * Reject a specific order
     *
     * @param auth   :the authenticate user token
     * @param id     : the order id
     * @param reason the reason why the user is rejecting the order
     */
    @PUT("user/steed/courses/{id}/reject")
    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    Call<ResponseBody> rejectOrder(
            @Header("Authorization") String auth,
            @Path("id") int id,
            @Field("reason") String reason
    );

    @POST("user/manager/courses/{id}/update_date")
    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    Call<ResponseBody> updateOrderDeliveryTime(
            @Header("Authorization") String auth,
            @Path("id") int id,
            @Field("date") String date
    );

    @GET("user/steed")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json",
    })
    Call<ResponseBody> getDeliveryMen(
            @Header("Authorization") String auth
    );

    /**
     * Add a new address
     *
     * @param auth          : the authenticated user token
     * @param id            : the client id (to who we're creating address), it could be a sender, receiver a client himself
     * @param client_id     : the client id (to who we're creating address), it could be a sender, receiver a client himself
     * @param creator_id    : the user who is is creating the address
     * @param ville_id      : the city's id the user is located
     * @param latitude
     * @param longitude
     * @param quartier
     * @param nom           : the address name from the map
     * @param surnom        : the address title
     * @param description
     * @param est_favorite
     * @param provider_name
     */
    @POST("guests/{id}/address")
    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    Call<ResponseBody> addAddress(
            @Header("Authorization") String auth,
            @Path("id") int id,
            @Field("client_id") long client_id,
            @Field("creator_id") long creator_id,
            @Field("ville_id") int ville_id,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude,
            @Field("quartier") String quartier,
            @Field("nom") String nom,
            @Field("surnom") String surnom,
            @Field("titre") String titre,
            @Field("description") String description,
            @Field("est_favorite") int est_favorite,
            @Field("provider_name") String provider_name

    );

    /**
     * Update and existing address
     *
     * @param auth         : the authenticated user token
     * @param id           : the user id
     * @param address_id
     * @param client_id    : client to who belongs the address
     * @param updater_id   : the user id too
     * @param latitude
     * @param longitude
     * @param quartier
     * @param nom          : the address name from the map
     * @param surnom       : the address title
     * @param titre        : the address title
     * @param description
     * @param est_favorite
     */
    @PUT("guests/{customer_id}/address/{address_id}")
    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    Call<ResponseBody> updateAddress(
            @Header("Authorization") String auth,
            @Path("customer_id") int id,
            @Path("address_id") int address_id,
            @Field("client_id") long client_id,
            @Field("updater_id") long updater_id,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude,
            @Field("quartier") String quartier,
            @Field("nom") String nom,
            @Field("surnom") String surnom,
            @Field("titre") String titre,
            @Field("description") String description,
            @Field("est_favorite") int est_favorite
    );

    /**Delete an address
     * @param auth       : the authenticated user token
     * @param id         : the user id
     * @param address_id
     */
    @DELETE("guests/{id}/address/{address_id}")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json",
    })
    Call<ResponseBody> deleteAddress(
            @Header("Authorization") String auth,
            @Path("id") int id,
            @Path("address_id") int address_id
    );

    /**Retrieve all the client address
     * @param auth : the authenticated user token
     * @param id : the client id*/
    @GET("guests/{id}/address")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json",
    })
    Call<ResponseBody> getClientAddresses(
            @Header("Authorization") String auth,
            @Path("id")int id
    );

    @GET("comments/course/{id}")
    @Headers({"Accept: application/json"})
    Call<ResponseBody> getComments(
            @Header("Authorization") String auth,
            @Path("id")int id
    );

    @GET("quartiers")
    @Headers({"Accept: application/json"})
    Call<ResponseBody> getQuarters();

    @POST("comments")
    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    Call<ResponseBody> postComment(
            @Header("Authorization") String auth,
            @Field("commentable_type") String type,
            @Field("commentable_id") int course_id,
            @Field("users_id") int user_id,
            @Field("comment") String comment
    );

    @GET("user/courses/{id}/changelog")
    @Headers({"Content-Type: Application/json", "Accept: application/json"})
    Call<ResponseBody> getOrderStatusHistory(
            @Header("Authorization") String authorization,
            @Path("id")int id
    );

    @GET("/api/v1.1/user/notifications")
    @Headers({"Content-Type: Application/json", "Accept: application/json"})
    Call<ResponseBody> getNotifications(
            @Header("Authorization") String authorization
    );

    @POST("user/manager/guests/{guest_id}/courses")
    @Headers({"Content-Type: Application/json", "Accept: application/json"})
    Call<ResponseBody> getUserOrder(
            @Header("Authorization") String authorization,
            @Path("guest_id")int id,
            @Body Map<String, String> filter_constraint
    );

    @PUT("user/manager/courses/{id}/relaunch")
    @Headers({"Content-Type: Application/json", "Accept: application/json"})
    Call<ResponseBody> managerRelaunch(
            @Header("Authorization") String authorization,
            @Path("guest_id")int id,
            @Body Map<String, String> dateTime
    );

    //statistics
    @POST("statistics/magasins/ventes")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json",
    })
    Call<ResponseBody> shopSales(
            @Header("Authorization") String auth,
            @Body Map<String, Object> objectMap
    );

    @POST("statistics/courses")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json",
    })
    Call<ResponseBody> shopOrders(
            @Header("Authorization") String auth,
            @Body Map<String, Object> objectMap
    );

    @POST("statistics/magasins/produits")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json",
    })
    Call<ResponseBody> products(
            @Header("Authorization") String auth,
            @Body Map<String, Object> objectMap
    );

    @POST("statistics/coursiers")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json",
    })
    Call<ResponseBody> deliveryMen(
            @Header("Authorization") String auth,
            @Body Map<String, Object> objectMap
    );

    @POST("statistics/villes")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json",
    })
    Call<ResponseBody> cities(
            @Header("Authorization") String auth,
            @Body Map<String, Object> objectMap
    );


    @POST("statistics/ventes-annuelle")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json",
    })
    Call<ResponseBody> sales(
            @Header("Authorization") String auth,
            @Body Map<String, Object> objectMap
    );

    @POST("statistics/pourcentages-annulations")
    @Headers({
            "Content-Type: Application/json",
            "Accept: application/json",
    })
    Call<ResponseBody> cancellations(
            @Header("Authorization") String auth,
            @Body Map<String, Object> objectMap
    );

    @Multipart
    @POST("courses/{id}/attachment")
    @Headers({
            "Accept: application/json",
    })
    Call<ResponseBody> postAttachment(
            @Header("Authorization") String authorization,
            @Path("id") int id,
            @Part MultipartBody.Part image
    );

    @GET("courses/{id}/attachments")
    @Headers({"Content-Type: Application/json", "Accept: application/json"})
    Call<ResponseBody> getAttachments(
            @Header("Authorization") String authorization,
            @Path("id") int id
    );

    @DELETE("courses/{orderId}/attachments/{id}")
    @Headers({"Content-Type: Application/json", "Accept: application/json"})
    Call<ResponseBody> deleteAttachments(
            @Header("Authorization") String authorization,
            @Path("orderId") int orderId,
            @Path("id") int id
    );

}

