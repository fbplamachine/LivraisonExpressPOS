package com.israel.livraisonexpresspos.ui.inventory_management;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.data.services.inventory_management.InventoryManagementApi;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.models.inventory_management_models.Move;
import com.israel.livraisonexpresspos.models.inventory_management_models.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class MovesViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> isEntryMovesLoading;
    private MutableLiveData<List<Move>> entryMoves;
    private int entryMovesPage;

    private MutableLiveData<List<Move>> exitMoves;
    private MutableLiveData<Boolean> isExitMovesLoading;
    private int exitMovesPage;

    private MutableLiveData<List<Move>> returnMoves;
    private MutableLiveData<Boolean> isReturnMovesLoading;
    private int returnMovesPages;

    private MutableLiveData<List<Move>> resumeMoves;
    private MutableLiveData<Boolean> isResumeMovesLoading;
    private int resumeMovesPages;

    private int previousFragmentIndex;
    private int currentFragmentIndex;
    private MutableLiveData<Move> currentViewedMove;
    private MutableLiveData<List<HashMap<String, Object>>> currentViewedMoveProducts;

    public MovesViewModel(@NonNull Application application) {
        super(application);

        isEntryMovesLoading = new MutableLiveData<>(false);
        entryMoves = new MutableLiveData<>();
        entryMovesPage = 0;

        exitMoves = new MutableLiveData<>();
        isExitMovesLoading = new MutableLiveData<>(false);
        exitMovesPage = 0;

        returnMoves = new MutableLiveData<>();
        isReturnMovesLoading = new MutableLiveData<>(false);
        returnMovesPages = 0;

        resumeMoves = new MutableLiveData<>();
        isResumeMovesLoading = new MutableLiveData<>(false);
        resumeMovesPages = 0;

        previousFragmentIndex = 0;
        currentFragmentIndex = 0;
        currentViewedMove = new MutableLiveData<>();
        currentViewedMoveProducts = new MutableLiveData<>();
    }


    /* ============ move list loading state setters ========*/
    public void setIsEntryMovesLoading(boolean isLoading) {
        this.isEntryMovesLoading.setValue(isLoading);
    }

    public void setIsExitMovesLoading(boolean isLoading) {
        this.isExitMovesLoading.setValue(isLoading);
    }

    public void setIsReturnMovesLoading(boolean isLoading) {
        this.isReturnMovesLoading.setValue(isLoading);
    }

    public void setIsResumeMovesLoading(boolean isLoading) {
        this.isResumeMovesLoading.setValue(isLoading);
    }

    /* ============ move list loading state getters ========*/
    public MutableLiveData<Boolean> getIsEntryMovesLoading() {
        return this.isEntryMovesLoading;
    }

    public MutableLiveData<Boolean> getIsExitMovesLoading() {
        return this.isExitMovesLoading;
    }

    public MutableLiveData<Boolean> getIsReturnMovesLoading() {
        return this.isReturnMovesLoading;
    }

    public MutableLiveData<Boolean> getIsResumeMovesLoading() {
        return this.isResumeMovesLoading;
    }


    /* ====== moves list setters =============== */
    public void setEntryMoves(List<Move> moves) {
        this.entryMoves.setValue(moves);
    }

    public void setExitMoves(List<Move> moves) {
        this.exitMoves.setValue(moves);
    }

    public void setReturnMoves(List<Move> moves) {
        this.returnMoves.setValue(moves);
    }

    public void setResumeMoves(List<Move> moves) {
        this.resumeMoves.setValue(moves);
    }


    /* ====== moves list getters =============== */
    public MutableLiveData<List<Move>> getEntryMoves() {
        return this.entryMoves;
    }

    public MutableLiveData<List<Move>> getExitMoves() {
        return this.entryMoves;
    }

    public MutableLiveData<List<Move>> getReturnMoves() {
        return this.returnMoves;
    }

    public MutableLiveData<List<Move>> getResumeMoves() {
        return this.resumeMoves;
    }


    public void loadMoveDetails(int moveId) {
        setIsEntryMovesLoading(true);
        Call<ResponseBody> call = InventoryManagementApi.movesManagementService().loadMovesForType(User.getCurrentUser(getApplication()).getToken(), StockInventoryActivity.STR_RETURN_MOVE_TYPE);
        JSONObject jsonResponse = null;
        try {
            jsonResponse = new JSONObject("{\n" +
                    "    \"message\": \"OK\",\n" +
                    "    \"issues\": null,\n" +
                    "    \"data\": {\n" +
                    "        \"id\": 2,\n" +
                    "        \"site_depart_id\": null,\n" +
                    "        \"site_arrivee_id\": 1,\n" +
                    "        \"gestionnaire_id\": 1,\n" +
                    "        \"type_action\": \"entree\",\n" +
                    "        \"status\": 1,\n" +
                    "        \"created_at\": \"2022-01-18T04:54:36.000000Z\",\n" +
                    "        \"updated_at\": \"2022-01-18T04:54:36.000000Z\",\n" +
                    "        \"gestionnaire\": {\n" +
                    "            \"id\": 1,\n" +
                    "            \"holder_id\": \"1\",\n" +
                    "            \"creator_id\": \"1\",\n" +
                    "            \"updater_id\": \"1\",\n" +
                    "            \"adresse_favorite_id\": null,\n" +
                    "            \"uuid\": \"a1bf216e-e7cf-4a37-bfba-9844de4cb2e9\",\n" +
                    "            \"username\": \"superadmin\",\n" +
                    "            \"avatar\": null,\n" +
                    "            \"fullname\": \"Super Admin Multicanalservices\",\n" +
                    "            \"firstname\": null,\n" +
                    "            \"lastname\": null,\n" +
                    "            \"email\": \"contact@multicanalservices.com\",\n" +
                    "            \"email_verified_at\": \"2020-07-05 02:00:00\",\n" +
                    "            \"phone_verified_at\": \"2020-07-05 05:30:53\",\n" +
                    "            \"password\": \"$2y$10$NlLv6ttS3yc8gSH7XfuJVO2IU9jzWczro.d92vGE5.PJmT9emfME2\",\n" +
                    "            \"telephone\": \"695461461\",\n" +
                    "            \"telephone_alt\": null,\n" +
                    "            \"genre\": null,\n" +
                    "            \"provider_id\": \"1\",\n" +
                    "            \"provider_name\": \"localhost\",\n" +
                    "            \"langue\": null,\n" +
                    "            \"description\": null,\n" +
                    "            \"modules\": null,\n" +
                    "            \"token\": null,\n" +
                    "            \"fcm_token\": null,\n" +
                    "            \"remember_token\": \"nqnTnkpCYkhhRHFrPHsERsoGtTtw36VjBET85UnICNLMhplEnGJIIj878yTC\",\n" +
                    "            \"statut\": 1,\n" +
                    "            \"is_guest\": 0,\n" +
                    "            \"type\": 1,\n" +
                    "            \"created_at\": \"2020-07-05 05:30:49\",\n" +
                    "            \"updated_at\": \"2022-03-17 10:17:10\",\n" +
                    "            \"deleted_at\": null,\n" +
                    "            \"last_login_at\": \"2022-03-17 10:17:10\",\n" +
                    "            \"last_login_ip\": \"127.0.0.1\",\n" +
                    "            \"stripe_id\": null,\n" +
                    "            \"card_brand\": null,\n" +
                    "            \"card_last_four\": null,\n" +
                    "            \"trial_ends_at\": null\n" +
                    "        },\n" +
                    "        \"site_arrivee\": {\n" +
                    "            \"id\": 1,\n" +
                    "            \"quartier_id\": 2,\n" +
                    "            \"ville_id\": 1,\n" +
                    "            \"name\": \"site d'Akwa\",\n" +
                    "            \"type_site\": \"physique\",\n" +
                    "            \"is_site_reference\": 0,\n" +
                    "            \"statut\": 1,\n" +
                    "            \"created_at\": \"2022-01-18T16:40:50.000000Z\",\n" +
                    "            \"updated_at\": \"2022-02-16T16:41:28.000000Z\"\n" +
                    "        },\n" +
                    "        \"site_depart\": null,\n" +
                    "        \"details\": [\n" +
                    "            {\n" +
                    "                \"id\": 3,\n" +
                    "                \"mouvement_id\": 2,\n" +
                    "                \"site_possession_id\": 291,\n" +
                    "                \"qty\": 12,\n" +
                    "                \"created_at\": \"2022-01-18T04:54:37.000000Z\",\n" +
                    "                \"updated_at\": \"2022-01-18T04:54:37.000000Z\",\n" +
                    "                \"possession\": {\n" +
                    "                    \"nom_produit\": \"Ails (Tas)\",\n" +
                    "                    \"detail_produit\": \"\",\n" +
                    "                    \"nom_magasin\": \"Supermarché Express Douala\"\n" +
                    "                },\n" +
                    "                \"date_creation\": \"2022-01-18 04:54:37\",\n" +
                    "                \"date_update\": \"2022-01-18 04:54:37\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"id\": 4,\n" +
                    "                \"mouvement_id\": 2,\n" +
                    "                \"site_possession_id\": 176,\n" +
                    "                \"qty\": 15,\n" +
                    "                \"created_at\": \"2022-01-18T04:54:37.000000Z\",\n" +
                    "                \"updated_at\": \"2022-01-18T04:54:37.000000Z\",\n" +
                    "                \"possession\": {\n" +
                    "                    \"nom_produit\": \"All Milk paquet de 24 unités\",\n" +
                    "                    \"detail_produit\": \"\",\n" +
                    "                    \"nom_magasin\": \"Supermarché Express Douala\"\n" +
                    "                },\n" +
                    "                \"date_creation\": \"2022-01-18 04:54:37\",\n" +
                    "                \"date_update\": \"2022-01-18 04:54:37\"\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"date_creation\": \"2022-01-18 04:54:36\",\n" +
                    "        \"date_update\": \"2022-01-18 04:54:36\"\n" +
                    "    },\n" +
                    "    \"success\": true\n" +
                    "}");

            setCurrentViewedMoveProducts(buildShopWithProducts(new Gson().fromJson(String.valueOf(jsonResponse.getJSONObject("data").getJSONArray("details")), new TypeToken<List<Product>>(){}.getType())));
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        try {
//            assert jsonResponse != null;
//            if (jsonResponse.getBoolean("success")) {
//                setCurrentViewedMoveProducts(new Gson().fromJson(String.valueOf(jsonResponse.getJSONObject("data").getJSONArray("details")), new TypeToken<List<Product>>() {
//                }.getType()));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }



    private List<HashMap<String, Object>> buildShopWithProducts(List<Product> details) { //todo change the current product class to Movedetails_item_shop and then and the MoveDetailsItemShopProduct
        /*todo : take all shops */
        List<String> shopNames = new ArrayList<>();
        List<HashMap<String, Object>> results = new ArrayList<>();

        for (Product detailsItem : details) {
            if (!shopNames.contains(detailsItem.getPossession().getNom_magasin())) shopNames.add(detailsItem.getPossession().getNom_magasin());
        }

        for (String shopName: shopNames) {
             results.add(filterDetails(details, shopName));
        }
        return results;
    }

    private HashMap<String, Object> filterDetails(List<Product> details, String shopName){ //{"shop_name" : "", "products" : [{"mouvement_details_id" : 1, "nom_product" : "", "qty" : 15},{"mouvement_details_id" : 1, "nom_product" : "", "qty" : 15}]}
        HashMap<String, Object> results = new HashMap<>();
        List<HashMap<String, Object>> products = new ArrayList<>();
        for (Product detailsItem: details) {
            if (detailsItem.getPossession().getNom_magasin().equals(shopName)){
                HashMap<String, Object> product = new HashMap<>();
                product.put("product_name",detailsItem.getPossession().getNom_produit());
                product.put("qty",detailsItem.getQty());
                product.put("mouvement_details_id",null);
                products.add(product);
            }
        }
        results.put("shop_name", shopName);
        results.put("products", products);
        return results;
    }

    public void loadEntryMoves() {
        setIsEntryMovesLoading(true);
        Call<ResponseBody> call = InventoryManagementApi.movesManagementService().loadMovesForType(User.getCurrentUser(getApplication()).getToken(), StockInventoryActivity.STR_RETURN_MOVE_TYPE);
        JSONObject jsonResponse = null;
        try {
            jsonResponse = new JSONObject("{\"message\":\"OK\",\"issues\":null,\"data\":[{\"id\":12,\"site_depart_id\":4,\"site_arrivee_id\":4,\"gestionnaire_id\":1,\"type_action\":\"sortie\",\"status\":1,\"created_at\":\"2022-01-26T14:16:49.000000Z\",\"updated_at\":\"2022-01-26T14:16:49.000000Z\",\"gestionnaire\":{\"id\":1,\"holder_id\":\"1\",\"creator_id\":\"1\",\"updater_id\":\"1\",\"adresse_favorite_id\":null,\"uuid\":\"a1bf216e-e7cf-4a37-bfba-9844de4cb2e9\",\"username\":\"superadmin\",\"avatar\":null,\"fullname\":\"Super Admin Multicanalservices\",\"firstname\":null,\"lastname\":null,\"email\":\"contact@multicanalservices.com\",\"email_verified_at\":\"2020-07-05 00:00:00\",\"phone_verified_at\":\"2020-07-05 03:30:53\",\"password\":\"$2y$10$NlLv6ttS3yc8gSH7XfuJVO2IU9jzWczro.d92vGE5.PJmT9emfME2\",\"telephone\":\"695461461\",\"telephone_alt\":null,\"genre\":null,\"provider_id\":\"1\",\"provider_name\":\"localhost\",\"langue\":null,\"description\":null,\"modules\":null,\"token\":null,\"fcm_token\":null,\"remember_token\":\"9vs7GtobD6y44mMSYr1plswK0y1ZeOfZNQWIIVxdAU9gAdp6zPoXhgLcO9UZ\",\"statut\":1,\"is_guest\":0,\"type\":1,\"created_at\":\"2020-07-05 03:30:49\",\"updated_at\":\"2022-03-15 11:37:15\",\"deleted_at\":null,\"last_login_at\":\"2022-03-15 11:37:15\",\"last_login_ip\":\"154.72.171.45\",\"stripe_id\":null,\"card_brand\":null,\"card_last_four\":null,\"trial_ends_at\":null},\"site_arrivee\":{\"id\":4,\"quartier_id\":2,\"ville_id\":1,\"name\":\"courrsier Akwa\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-21T12:36:55.000000Z\",\"updated_at\":\"2022-01-21T12:36:55.000000Z\"},\"site_depart\":{\"id\":4,\"quartier_id\":2,\"ville_id\":1,\"name\":\"courrsier Akwa\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-21T12:36:55.000000Z\",\"updated_at\":\"2022-01-21T12:36:55.000000Z\"},\"nbre_details\":2,\"date_creation\":\"2022-01-26 02:16:49\",\"date_update\":\"2022-01-26 02:16:49\"},{\"id\":6,\"site_depart_id\":3,\"site_arrivee_id\":5,\"gestionnaire_id\":1,\"type_action\":\"sortie\",\"status\":1,\"created_at\":\"2022-01-21T12:40:50.000000Z\",\"updated_at\":\"2022-01-21T12:40:50.000000Z\",\"gestionnaire\":{\"id\":1,\"holder_id\":\"1\",\"creator_id\":\"1\",\"updater_id\":\"1\",\"adresse_favorite_id\":null,\"uuid\":\"a1bf216e-e7cf-4a37-bfba-9844de4cb2e9\",\"username\":\"superadmin\",\"avatar\":null,\"fullname\":\"Super Admin Multicanalservices\",\"firstname\":null,\"lastname\":null,\"email\":\"contact@multicanalservices.com\",\"email_verified_at\":\"2020-07-05 00:00:00\",\"phone_verified_at\":\"2020-07-05 03:30:53\",\"password\":\"$2y$10$NlLv6ttS3yc8gSH7XfuJVO2IU9jzWczro.d92vGE5.PJmT9emfME2\",\"telephone\":\"695461461\",\"telephone_alt\":null,\"genre\":null,\"provider_id\":\"1\",\"provider_name\":\"localhost\",\"langue\":null,\"description\":null,\"modules\":null,\"token\":null,\"fcm_token\":null,\"remember_token\":\"9vs7GtobD6y44mMSYr1plswK0y1ZeOfZNQWIIVxdAU9gAdp6zPoXhgLcO9UZ\",\"statut\":1,\"is_guest\":0,\"type\":1,\"created_at\":\"2020-07-05 03:30:49\",\"updated_at\":\"2022-03-15 11:37:15\",\"deleted_at\":null,\"last_login_at\":\"2022-03-15 11:37:15\",\"last_login_ip\":\"154.72.171.45\",\"stripe_id\":null,\"card_brand\":null,\"card_last_four\":null,\"trial_ends_at\":null},\"site_arrivee\":{\"id\":5,\"quartier_id\":3,\"ville_id\":1,\"name\":\"coursier d'Akxa Nord\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-21T12:38:07.000000Z\",\"updated_at\":\"2022-01-21T12:38:07.000000Z\"},\"site_depart\":{\"id\":3,\"quartier_id\":65,\"ville_id\":1,\"name\":\"makep\\u00e8\",\"type_site\":\"physique\",\"is_site_reference\":1,\"statut\":1,\"created_at\":\"2022-01-20T15:00:22.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"nbre_details\":1,\"date_creation\":\"2022-01-21 12:40:50\",\"date_update\":\"2022-01-21 12:40:50\"},{\"id\":5,\"site_depart_id\":3,\"site_arrivee_id\":4,\"gestionnaire_id\":1,\"type_action\":\"sortie\",\"status\":1,\"created_at\":\"2022-01-21T12:39:44.000000Z\",\"updated_at\":\"2022-01-21T12:39:44.000000Z\",\"gestionnaire\":{\"id\":1,\"holder_id\":\"1\",\"creator_id\":\"1\",\"updater_id\":\"1\",\"adresse_favorite_id\":null,\"uuid\":\"a1bf216e-e7cf-4a37-bfba-9844de4cb2e9\",\"username\":\"superadmin\",\"avatar\":null,\"fullname\":\"Super Admin Multicanalservices\",\"firstname\":null,\"lastname\":null,\"email\":\"contact@multicanalservices.com\",\"email_verified_at\":\"2020-07-05 00:00:00\",\"phone_verified_\n" +
                    "    at\":\"2020-07-05 03:30:53\",\"password\":\"$2y$10$NlLv6ttS3yc8gSH7XfuJVO2IU9jzWczro.d92vGE5.PJmT9emfME2\",\"telephone\":\"695461461\",\"telephone_alt\":null,\"genre\":null,\"provider_id\":\"1\",\"provider_name\":\"localhost\",\"langue\":null,\"description\":null,\"modules\":null,\"token\":null,\"fcm_token\":null,\"remember_token\":\"9vs7GtobD6y44mMSYr1plswK0y1ZeOfZNQWIIVxdAU9gAdp6zPoXhgLcO9UZ\",\"statut\":1,\"is_guest\":0,\"type\":1,\"created_at\":\"2020-07-05 03:30:49\",\"updated_at\":\"2022-03-15 11:37:15\",\"deleted_at\":null,\"last_login_at\":\"2022-03-15 11:37:15\",\"last_login_ip\":\"154.72.171.45\",\"stripe_id\":null,\"card_brand\":null,\"card_last_four\":null,\"trial_ends_at\":null},\"site_arrivee\":{\"id\":4,\"quartier_id\":2,\"ville_id\":1,\"name\":\"courrsier Akwa\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-21T12:36:55.000000Z\",\"updated_at\":\"2022-01-21T12:36:55.000000Z\"},\"site_depart\":{\"id\":3,\"quartier_id\":65,\"ville_id\":1,\"name\":\"makep\\u00e8\",\"type_site\":\"physique\",\"is_site_reference\":1,\"statut\":1,\"created_at\":\"2022-01-20T15:00:22.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"nbre_details\":1,\"date_creation\":\"2022-01-21 12:39:44\",\"date_update\":\"2022-01-21 12:39:44\"},{\"id\":4,\"site_depart_id\":3,\"site_arrivee_id\":2,\"gestionnaire_id\":1,\"type_action\":\"sortie\",\"status\":1,\"created_at\":\"2022-01-21T12:11:58.000000Z\",\"updated_at\":\"2022-01-21T12:11:58.000000Z\",\"gestionnaire\":{\"id\":1,\"holder_id\":\"1\",\"creator_id\":\"1\",\"updater_id\":\"1\",\"adresse_favorite_id\":null,\"uuid\":\"a1bf216e-e7cf-4a37-bfba-9844de4cb2e9\",\"username\":\"superadmin\",\"avatar\":null,\"fullname\":\"Super Admin Multicanalservices\",\"firstname\":null,\"lastname\":null,\"email\":\"contact@multicanalservices.com\",\"email_verified_at\":\"2020-07-05 00:00:00\",\"phone_verified_at\":\"2020-07-05 03:30:53\",\"password\":\"$2y$10$NlLv6ttS3yc8gSH7XfuJVO2IU9jzWczro.d92vGE5.PJmT9emfME2\",\"telephone\":\"695461461\",\"telephone_alt\":null,\"genre\":null,\"provider_id\":\"1\",\"provider_name\":\"localhost\",\"langue\":null,\"description\":null,\"modules\":null,\"token\":null,\"fcm_token\":null,\"remember_token\":\"9vs7GtobD6y44mMSYr1plswK0y1ZeOfZNQWIIVxdAU9gAdp6zPoXhgLcO9UZ\",\"statut\":1,\"is_guest\":0,\"type\":1,\"created_at\":\"2020-07-05 03:30:49\",\"updated_at\":\"2022-03-15 11:37:15\",\"deleted_at\":null,\"last_login_at\":\"2022-03-15 11:37:15\",\"last_login_ip\":\"154.72.171.45\",\"stripe_id\":null,\"card_brand\":null,\"card_last_four\":null,\"trial_ends_at\":null},\"site_arrivee\":{\"id\":2,\"quartier_id\":265,\"ville_id\":1,\"name\":\"coursier Makep\\u00e8 BM\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-20T14:58:20.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"site_depart\":{\"id\":3,\"quartier_id\":65,\"ville_id\":1,\"name\":\"makep\\u00e8\",\"type_site\":\"physique\",\"is_site_reference\":1,\"statut\":1,\"created_at\":\"2022-01-20T15:00:22.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"nbre_details\":1,\"date_creation\":\"2022-01-21 12:11:58\",\"date_update\":\"2022-01-21 12:11:58\"},{\"id\":2,\"site_depart_id\":3,\"site_arrivee_id\":2,\"gestionnaire_id\":1,\"type_action\":\"sortie\",\"status\":1,\"created_at\":\"2022-01-21T11:02:44.000000Z\",\"updated_at\":\"2022-01-21T11:02:44.000000Z\",\"gestionnaire\":{\"id\":1,\"holder_id\":\"1\",\"creator_id\":\"1\",\"updater_id\":\"1\",\"adresse_favorite_id\":null,\"uuid\":\"a1bf216e-e7cf-4a37-bfba-9844de4cb2e9\",\"username\":\"superadmin\",\"avatar\":null,\"fullname\":\"Super Admin Multicanalservices\",\"firstname\":null,\"lastname\":null,\"email\":\"contact@multicanalservices.com\",\"email_verified_at\":\"2020-07-05 00:00:00\",\"phone_verified_at\":\"2020-07-05 03:30:53\",\"password\":\"$2y$10$NlLv6ttS3yc8gSH7XfuJVO2IU9jzWczro.d92vGE5.PJmT9emfME2\",\"telephone\":\"695461461\",\"telephone_alt\":null,\"genre\":null,\"provider_id\":\"1\",\"provider_name\":\"localhost\",\"langue\":null,\"description\":null,\"modules\":null,\"token\":null,\"fcm_token\":null,\"remember_token\":\"9vs7GtobD6y44mMSYr1plswK0y1ZeOfZNQWIIVxdAU9gAdp6zPoXhgLcO9UZ\",\"statut\":1,\"is_guest\":0,\"type\":1,\"created_at\":\"2020-07-05 03:30:49\",\"updated_at\":\"2022-03-15 11:37:15\",\"deleted_at\":null,\"last_login_at\":\"2022-03-15 11:37:15\",\"last_login_ip\":\"154.72.171.45\",\"stripe_id\":null,\"card_brand\n" +
                    "    \":null,\"card_last_four\":null,\"trial_ends_at\":null},\"site_arrivee\":{\"id\":2,\"quartier_id\":265,\"ville_id\":1,\"name\":\"coursier Makep\\u00e8 BM\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-20T14:58:20.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"site_depart\":{\"id\":3,\"quartier_id\":65,\"ville_id\":1,\"name\":\"makep\\u00e8\",\"type_site\":\"physique\",\"is_site_reference\":1,\"statut\":1,\"created_at\":\"2022-01-20T15:00:22.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"nbre_details\":2,\"date_creation\":\"2022-01-21 11:02:44\",\"date_update\":\"2022-01-21 11:02:44\"}],\"success\":true}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (jsonResponse.getBoolean("success")) {
                setEntryMoves(new Gson().fromJson(String.valueOf(jsonResponse.getJSONArray("data")), new TypeToken<List<Move>>() {
                }.getType()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setIsEntryMovesLoading(false);
        //        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                setIsEntryMovesLoading(false);
//                try {
//                    if (response.isSuccessful() && response.body() != null) {
//                        String strResponse = response.body().string();
////                        JSONObject jsonResponse = new JSONObject(strResponse);
//                        JSONObject jsonResponse = new JSONObject("{\"message\":\"OK\",\"issues\":null,\"data\":[{\"id\":12,\"site_depart_id\":4,\"site_arrivee_id\":4,\"gestionnaire_id\":1,\"type_action\":\"sortie\",\"status\":1,\"created_at\":\"2022-01-26T14:16:49.000000Z\",\"updated_at\":\"2022-01-26T14:16:49.000000Z\",\"gestionnaire\":{\"id\":1,\"holder_id\":\"1\",\"creator_id\":\"1\",\"updater_id\":\"1\",\"adresse_favorite_id\":null,\"uuid\":\"a1bf216e-e7cf-4a37-bfba-9844de4cb2e9\",\"username\":\"superadmin\",\"avatar\":null,\"fullname\":\"Super Admin Multicanalservices\",\"firstname\":null,\"lastname\":null,\"email\":\"contact@multicanalservices.com\",\"email_verified_at\":\"2020-07-05 00:00:00\",\"phone_verified_at\":\"2020-07-05 03:30:53\",\"password\":\"$2y$10$NlLv6ttS3yc8gSH7XfuJVO2IU9jzWczro.d92vGE5.PJmT9emfME2\",\"telephone\":\"695461461\",\"telephone_alt\":null,\"genre\":null,\"provider_id\":\"1\",\"provider_name\":\"localhost\",\"langue\":null,\"description\":null,\"modules\":null,\"token\":null,\"fcm_token\":null,\"remember_token\":\"9vs7GtobD6y44mMSYr1plswK0y1ZeOfZNQWIIVxdAU9gAdp6zPoXhgLcO9UZ\",\"statut\":1,\"is_guest\":0,\"type\":1,\"created_at\":\"2020-07-05 03:30:49\",\"updated_at\":\"2022-03-15 11:37:15\",\"deleted_at\":null,\"last_login_at\":\"2022-03-15 11:37:15\",\"last_login_ip\":\"154.72.171.45\",\"stripe_id\":null,\"card_brand\":null,\"card_last_four\":null,\"trial_ends_at\":null},\"site_arrivee\":{\"id\":4,\"quartier_id\":2,\"ville_id\":1,\"name\":\"courrsier Akwa\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-21T12:36:55.000000Z\",\"updated_at\":\"2022-01-21T12:36:55.000000Z\"},\"site_depart\":{\"id\":4,\"quartier_id\":2,\"ville_id\":1,\"name\":\"courrsier Akwa\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-21T12:36:55.000000Z\",\"updated_at\":\"2022-01-21T12:36:55.000000Z\"},\"nbre_details\":2,\"date_creation\":\"2022-01-26 02:16:49\",\"date_update\":\"2022-01-26 02:16:49\"},{\"id\":6,\"site_depart_id\":3,\"site_arrivee_id\":5,\"gestionnaire_id\":1,\"type_action\":\"sortie\",\"status\":1,\"created_at\":\"2022-01-21T12:40:50.000000Z\",\"updated_at\":\"2022-01-21T12:40:50.000000Z\",\"gestionnaire\":{\"id\":1,\"holder_id\":\"1\",\"creator_id\":\"1\",\"updater_id\":\"1\",\"adresse_favorite_id\":null,\"uuid\":\"a1bf216e-e7cf-4a37-bfba-9844de4cb2e9\",\"username\":\"superadmin\",\"avatar\":null,\"fullname\":\"Super Admin Multicanalservices\",\"firstname\":null,\"lastname\":null,\"email\":\"contact@multicanalservices.com\",\"email_verified_at\":\"2020-07-05 00:00:00\",\"phone_verified_at\":\"2020-07-05 03:30:53\",\"password\":\"$2y$10$NlLv6ttS3yc8gSH7XfuJVO2IU9jzWczro.d92vGE5.PJmT9emfME2\",\"telephone\":\"695461461\",\"telephone_alt\":null,\"genre\":null,\"provider_id\":\"1\",\"provider_name\":\"localhost\",\"langue\":null,\"description\":null,\"modules\":null,\"token\":null,\"fcm_token\":null,\"remember_token\":\"9vs7GtobD6y44mMSYr1plswK0y1ZeOfZNQWIIVxdAU9gAdp6zPoXhgLcO9UZ\",\"statut\":1,\"is_guest\":0,\"type\":1,\"created_at\":\"2020-07-05 03:30:49\",\"updated_at\":\"2022-03-15 11:37:15\",\"deleted_at\":null,\"last_login_at\":\"2022-03-15 11:37:15\",\"last_login_ip\":\"154.72.171.45\",\"stripe_id\":null,\"card_brand\":null,\"card_last_four\":null,\"trial_ends_at\":null},\"site_arrivee\":{\"id\":5,\"quartier_id\":3,\"ville_id\":1,\"name\":\"coursier d'Akxa Nord\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-21T12:38:07.000000Z\",\"updated_at\":\"2022-01-21T12:38:07.000000Z\"},\"site_depart\":{\"id\":3,\"quartier_id\":65,\"ville_id\":1,\"name\":\"makep\\u00e8\",\"type_site\":\"physique\",\"is_site_reference\":1,\"statut\":1,\"created_at\":\"2022-01-20T15:00:22.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"nbre_details\":1,\"date_creation\":\"2022-01-21 12:40:50\",\"date_update\":\"2022-01-21 12:40:50\"},{\"id\":5,\"site_depart_id\":3,\"site_arrivee_id\":4,\"gestionnaire_id\":1,\"type_action\":\"sortie\",\"status\":1,\"created_at\":\"2022-01-21T12:39:44.000000Z\",\"updated_at\":\"2022-01-21T12:39:44.000000Z\",\"gestionnaire\":{\"id\":1,\"holder_id\":\"1\",\"creator_id\":\"1\",\"updater_id\":\"1\",\"adresse_favorite_id\":null,\"uuid\":\"a1bf216e-e7cf-4a37-bfba-9844de4cb2e9\",\"username\":\"superadmin\",\"avatar\":null,\"fullname\":\"Super Admin Multicanalservices\",\"firstname\":null,\"lastname\":null,\"email\":\"contact@multicanalservices.com\",\"email_verified_at\":\"2020-07-05 00:00:00\",\"phone_verified_\n" +
//                                "    at\":\"2020-07-05 03:30:53\",\"password\":\"$2y$10$NlLv6ttS3yc8gSH7XfuJVO2IU9jzWczro.d92vGE5.PJmT9emfME2\",\"telephone\":\"695461461\",\"telephone_alt\":null,\"genre\":null,\"provider_id\":\"1\",\"provider_name\":\"localhost\",\"langue\":null,\"description\":null,\"modules\":null,\"token\":null,\"fcm_token\":null,\"remember_token\":\"9vs7GtobD6y44mMSYr1plswK0y1ZeOfZNQWIIVxdAU9gAdp6zPoXhgLcO9UZ\",\"statut\":1,\"is_guest\":0,\"type\":1,\"created_at\":\"2020-07-05 03:30:49\",\"updated_at\":\"2022-03-15 11:37:15\",\"deleted_at\":null,\"last_login_at\":\"2022-03-15 11:37:15\",\"last_login_ip\":\"154.72.171.45\",\"stripe_id\":null,\"card_brand\":null,\"card_last_four\":null,\"trial_ends_at\":null},\"site_arrivee\":{\"id\":4,\"quartier_id\":2,\"ville_id\":1,\"name\":\"courrsier Akwa\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-21T12:36:55.000000Z\",\"updated_at\":\"2022-01-21T12:36:55.000000Z\"},\"site_depart\":{\"id\":3,\"quartier_id\":65,\"ville_id\":1,\"name\":\"makep\\u00e8\",\"type_site\":\"physique\",\"is_site_reference\":1,\"statut\":1,\"created_at\":\"2022-01-20T15:00:22.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"nbre_details\":1,\"date_creation\":\"2022-01-21 12:39:44\",\"date_update\":\"2022-01-21 12:39:44\"},{\"id\":4,\"site_depart_id\":3,\"site_arrivee_id\":2,\"gestionnaire_id\":1,\"type_action\":\"sortie\",\"status\":1,\"created_at\":\"2022-01-21T12:11:58.000000Z\",\"updated_at\":\"2022-01-21T12:11:58.000000Z\",\"gestionnaire\":{\"id\":1,\"holder_id\":\"1\",\"creator_id\":\"1\",\"updater_id\":\"1\",\"adresse_favorite_id\":null,\"uuid\":\"a1bf216e-e7cf-4a37-bfba-9844de4cb2e9\",\"username\":\"superadmin\",\"avatar\":null,\"fullname\":\"Super Admin Multicanalservices\",\"firstname\":null,\"lastname\":null,\"email\":\"contact@multicanalservices.com\",\"email_verified_at\":\"2020-07-05 00:00:00\",\"phone_verified_at\":\"2020-07-05 03:30:53\",\"password\":\"$2y$10$NlLv6ttS3yc8gSH7XfuJVO2IU9jzWczro.d92vGE5.PJmT9emfME2\",\"telephone\":\"695461461\",\"telephone_alt\":null,\"genre\":null,\"provider_id\":\"1\",\"provider_name\":\"localhost\",\"langue\":null,\"description\":null,\"modules\":null,\"token\":null,\"fcm_token\":null,\"remember_token\":\"9vs7GtobD6y44mMSYr1plswK0y1ZeOfZNQWIIVxdAU9gAdp6zPoXhgLcO9UZ\",\"statut\":1,\"is_guest\":0,\"type\":1,\"created_at\":\"2020-07-05 03:30:49\",\"updated_at\":\"2022-03-15 11:37:15\",\"deleted_at\":null,\"last_login_at\":\"2022-03-15 11:37:15\",\"last_login_ip\":\"154.72.171.45\",\"stripe_id\":null,\"card_brand\":null,\"card_last_four\":null,\"trial_ends_at\":null},\"site_arrivee\":{\"id\":2,\"quartier_id\":265,\"ville_id\":1,\"name\":\"coursier Makep\\u00e8 BM\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-20T14:58:20.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"site_depart\":{\"id\":3,\"quartier_id\":65,\"ville_id\":1,\"name\":\"makep\\u00e8\",\"type_site\":\"physique\",\"is_site_reference\":1,\"statut\":1,\"created_at\":\"2022-01-20T15:00:22.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"nbre_details\":1,\"date_creation\":\"2022-01-21 12:11:58\",\"date_update\":\"2022-01-21 12:11:58\"},{\"id\":2,\"site_depart_id\":3,\"site_arrivee_id\":2,\"gestionnaire_id\":1,\"type_action\":\"sortie\",\"status\":1,\"created_at\":\"2022-01-21T11:02:44.000000Z\",\"updated_at\":\"2022-01-21T11:02:44.000000Z\",\"gestionnaire\":{\"id\":1,\"holder_id\":\"1\",\"creator_id\":\"1\",\"updater_id\":\"1\",\"adresse_favorite_id\":null,\"uuid\":\"a1bf216e-e7cf-4a37-bfba-9844de4cb2e9\",\"username\":\"superadmin\",\"avatar\":null,\"fullname\":\"Super Admin Multicanalservices\",\"firstname\":null,\"lastname\":null,\"email\":\"contact@multicanalservices.com\",\"email_verified_at\":\"2020-07-05 00:00:00\",\"phone_verified_at\":\"2020-07-05 03:30:53\",\"password\":\"$2y$10$NlLv6ttS3yc8gSH7XfuJVO2IU9jzWczro.d92vGE5.PJmT9emfME2\",\"telephone\":\"695461461\",\"telephone_alt\":null,\"genre\":null,\"provider_id\":\"1\",\"provider_name\":\"localhost\",\"langue\":null,\"description\":null,\"modules\":null,\"token\":null,\"fcm_token\":null,\"remember_token\":\"9vs7GtobD6y44mMSYr1plswK0y1ZeOfZNQWIIVxdAU9gAdp6zPoXhgLcO9UZ\",\"statut\":1,\"is_guest\":0,\"type\":1,\"created_at\":\"2020-07-05 03:30:49\",\"updated_at\":\"2022-03-15 11:37:15\",\"deleted_at\":null,\"last_login_at\":\"2022-03-15 11:37:15\",\"last_login_ip\":\"154.72.171.45\",\"stripe_id\":null,\"card_brand\n" +
//                                "    \":null,\"card_last_four\":null,\"trial_ends_at\":null},\"site_arrivee\":{\"id\":2,\"quartier_id\":265,\"ville_id\":1,\"name\":\"coursier Makep\\u00e8 BM\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-20T14:58:20.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"site_depart\":{\"id\":3,\"quartier_id\":65,\"ville_id\":1,\"name\":\"makep\\u00e8\",\"type_site\":\"physique\",\"is_site_reference\":1,\"statut\":1,\"created_at\":\"2022-01-20T15:00:22.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"nbre_details\":2,\"date_creation\":\"2022-01-21 11:02:44\",\"date_update\":\"2022-01-21 11:02:44\"}],\"success\":true}");
//                        if (jsonResponse.getBoolean("success")) {
////                            entryMovesPage = jsonResponse.getJSONObject("data").getInt("current_page");
//                            setEntryMoves(new Gson().fromJson(String.valueOf(jsonResponse.getJSONArray("data")), new TypeToken<List<Move>>() {
//                            }.getType()));
//                        }
//                    }
//                } catch (JSONException | IOException e) {
//                    e.printStackTrace();
//                    Log.e("TAG", "onResponse here : ", e);
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                setIsEntryMovesLoading(false);
//                /*todo : handle the error here */
//            }
//        });
    }

    public void loadExitMoves() {
        setIsExitMovesLoading(true);
        JSONObject jsonResponse = null;
        try {
            jsonResponse = new JSONObject("{\"message\":\"OK\",\"issues\":null,\"data\":[{\"id\":12,\"site_depart_id\":4,\"site_arrivee_id\":4,\"gestionnaire_id\":1,\"type_action\":\"sortie\",\"status\":1,\"created_at\":\"2022-01-26T14:16:49.000000Z\",\"updated_at\":\"2022-01-26T14:16:49.000000Z\",\"gestionnaire\":{\"id\":1,\"holder_id\":\"1\",\"creator_id\":\"1\",\"updater_id\":\"1\",\"adresse_favorite_id\":null,\"uuid\":\"a1bf216e-e7cf-4a37-bfba-9844de4cb2e9\",\"username\":\"superadmin\",\"avatar\":null,\"fullname\":\"Super Admin Multicanalservices\",\"firstname\":null,\"lastname\":null,\"email\":\"contact@multicanalservices.com\",\"email_verified_at\":\"2020-07-05 00:00:00\",\"phone_verified_at\":\"2020-07-05 03:30:53\",\"password\":\"$2y$10$NlLv6ttS3yc8gSH7XfuJVO2IU9jzWczro.d92vGE5.PJmT9emfME2\",\"telephone\":\"695461461\",\"telephone_alt\":null,\"genre\":null,\"provider_id\":\"1\",\"provider_name\":\"localhost\",\"langue\":null,\"description\":null,\"modules\":null,\"token\":null,\"fcm_token\":null,\"remember_token\":\"9vs7GtobD6y44mMSYr1plswK0y1ZeOfZNQWIIVxdAU9gAdp6zPoXhgLcO9UZ\",\"statut\":1,\"is_guest\":0,\"type\":1,\"created_at\":\"2020-07-05 03:30:49\",\"updated_at\":\"2022-03-15 11:37:15\",\"deleted_at\":null,\"last_login_at\":\"2022-03-15 11:37:15\",\"last_login_ip\":\"154.72.171.45\",\"stripe_id\":null,\"card_brand\":null,\"card_last_four\":null,\"trial_ends_at\":null},\"site_arrivee\":{\"id\":4,\"quartier_id\":2,\"ville_id\":1,\"name\":\"courrsier Akwa\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-21T12:36:55.000000Z\",\"updated_at\":\"2022-01-21T12:36:55.000000Z\"},\"site_depart\":{\"id\":4,\"quartier_id\":2,\"ville_id\":1,\"name\":\"courrsier Akwa\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-21T12:36:55.000000Z\",\"updated_at\":\"2022-01-21T12:36:55.000000Z\"},\"nbre_details\":2,\"date_creation\":\"2022-01-26 02:16:49\",\"date_update\":\"2022-01-26 02:16:49\"},{\"id\":6,\"site_depart_id\":3,\"site_arrivee_id\":5,\"gestionnaire_id\":1,\"type_action\":\"sortie\",\"status\":1,\"created_at\":\"2022-01-21T12:40:50.000000Z\",\"updated_at\":\"2022-01-21T12:40:50.000000Z\",\"gestionnaire\":{\"id\":1,\"holder_id\":\"1\",\"creator_id\":\"1\",\"updater_id\":\"1\",\"adresse_favorite_id\":null,\"uuid\":\"a1bf216e-e7cf-4a37-bfba-9844de4cb2e9\",\"username\":\"superadmin\",\"avatar\":null,\"fullname\":\"Super Admin Multicanalservices\",\"firstname\":null,\"lastname\":null,\"email\":\"contact@multicanalservices.com\",\"email_verified_at\":\"2020-07-05 00:00:00\",\"phone_verified_at\":\"2020-07-05 03:30:53\",\"password\":\"$2y$10$NlLv6ttS3yc8gSH7XfuJVO2IU9jzWczro.d92vGE5.PJmT9emfME2\",\"telephone\":\"695461461\",\"telephone_alt\":null,\"genre\":null,\"provider_id\":\"1\",\"provider_name\":\"localhost\",\"langue\":null,\"description\":null,\"modules\":null,\"token\":null,\"fcm_token\":null,\"remember_token\":\"9vs7GtobD6y44mMSYr1plswK0y1ZeOfZNQWIIVxdAU9gAdp6zPoXhgLcO9UZ\",\"statut\":1,\"is_guest\":0,\"type\":1,\"created_at\":\"2020-07-05 03:30:49\",\"updated_at\":\"2022-03-15 11:37:15\",\"deleted_at\":null,\"last_login_at\":\"2022-03-15 11:37:15\",\"last_login_ip\":\"154.72.171.45\",\"stripe_id\":null,\"card_brand\":null,\"card_last_four\":null,\"trial_ends_at\":null},\"site_arrivee\":{\"id\":5,\"quartier_id\":3,\"ville_id\":1,\"name\":\"coursier d'Akxa Nord\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-21T12:38:07.000000Z\",\"updated_at\":\"2022-01-21T12:38:07.000000Z\"},\"site_depart\":{\"id\":3,\"quartier_id\":65,\"ville_id\":1,\"name\":\"makep\\u00e8\",\"type_site\":\"physique\",\"is_site_reference\":1,\"statut\":1,\"created_at\":\"2022-01-20T15:00:22.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"nbre_details\":1,\"date_creation\":\"2022-01-21 12:40:50\",\"date_update\":\"2022-01-21 12:40:50\"},{\"id\":5,\"site_depart_id\":3,\"site_arrivee_id\":4,\"gestionnaire_id\":1,\"type_action\":\"sortie\",\"status\":1,\"created_at\":\"2022-01-21T12:39:44.000000Z\",\"updated_at\":\"2022-01-21T12:39:44.000000Z\",\"gestionnaire\":{\"id\":1,\"holder_id\":\"1\",\"creator_id\":\"1\",\"updater_id\":\"1\",\"adresse_favorite_id\":null,\"uuid\":\"a1bf216e-e7cf-4a37-bfba-9844de4cb2e9\",\"username\":\"superadmin\",\"avatar\":null,\"fullname\":\"Super Admin Multicanalservices\",\"firstname\":null,\"lastname\":null,\"email\":\"contact@multicanalservices.com\",\"email_verified_at\":\"2020-07-05 00:00:00\",\"phone_verified_\n" +
                    "    at\":\"2020-07-05 03:30:53\",\"password\":\"$2y$10$NlLv6ttS3yc8gSH7XfuJVO2IU9jzWczro.d92vGE5.PJmT9emfME2\",\"telephone\":\"695461461\",\"telephone_alt\":null,\"genre\":null,\"provider_id\":\"1\",\"provider_name\":\"localhost\",\"langue\":null,\"description\":null,\"modules\":null,\"token\":null,\"fcm_token\":null,\"remember_token\":\"9vs7GtobD6y44mMSYr1plswK0y1ZeOfZNQWIIVxdAU9gAdp6zPoXhgLcO9UZ\",\"statut\":1,\"is_guest\":0,\"type\":1,\"created_at\":\"2020-07-05 03:30:49\",\"updated_at\":\"2022-03-15 11:37:15\",\"deleted_at\":null,\"last_login_at\":\"2022-03-15 11:37:15\",\"last_login_ip\":\"154.72.171.45\",\"stripe_id\":null,\"card_brand\":null,\"card_last_four\":null,\"trial_ends_at\":null},\"site_arrivee\":{\"id\":4,\"quartier_id\":2,\"ville_id\":1,\"name\":\"courrsier Akwa\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-21T12:36:55.000000Z\",\"updated_at\":\"2022-01-21T12:36:55.000000Z\"},\"site_depart\":{\"id\":3,\"quartier_id\":65,\"ville_id\":1,\"name\":\"makep\\u00e8\",\"type_site\":\"physique\",\"is_site_reference\":1,\"statut\":1,\"created_at\":\"2022-01-20T15:00:22.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"nbre_details\":1,\"date_creation\":\"2022-01-21 12:39:44\",\"date_update\":\"2022-01-21 12:39:44\"},{\"id\":4,\"site_depart_id\":3,\"site_arrivee_id\":2,\"gestionnaire_id\":1,\"type_action\":\"sortie\",\"status\":1,\"created_at\":\"2022-01-21T12:11:58.000000Z\",\"updated_at\":\"2022-01-21T12:11:58.000000Z\",\"gestionnaire\":{\"id\":1,\"holder_id\":\"1\",\"creator_id\":\"1\",\"updater_id\":\"1\",\"adresse_favorite_id\":null,\"uuid\":\"a1bf216e-e7cf-4a37-bfba-9844de4cb2e9\",\"username\":\"superadmin\",\"avatar\":null,\"fullname\":\"Super Admin Multicanalservices\",\"firstname\":null,\"lastname\":null,\"email\":\"contact@multicanalservices.com\",\"email_verified_at\":\"2020-07-05 00:00:00\",\"phone_verified_at\":\"2020-07-05 03:30:53\",\"password\":\"$2y$10$NlLv6ttS3yc8gSH7XfuJVO2IU9jzWczro.d92vGE5.PJmT9emfME2\",\"telephone\":\"695461461\",\"telephone_alt\":null,\"genre\":null,\"provider_id\":\"1\",\"provider_name\":\"localhost\",\"langue\":null,\"description\":null,\"modules\":null,\"token\":null,\"fcm_token\":null,\"remember_token\":\"9vs7GtobD6y44mMSYr1plswK0y1ZeOfZNQWIIVxdAU9gAdp6zPoXhgLcO9UZ\",\"statut\":1,\"is_guest\":0,\"type\":1,\"created_at\":\"2020-07-05 03:30:49\",\"updated_at\":\"2022-03-15 11:37:15\",\"deleted_at\":null,\"last_login_at\":\"2022-03-15 11:37:15\",\"last_login_ip\":\"154.72.171.45\",\"stripe_id\":null,\"card_brand\":null,\"card_last_four\":null,\"trial_ends_at\":null},\"site_arrivee\":{\"id\":2,\"quartier_id\":265,\"ville_id\":1,\"name\":\"coursier Makep\\u00e8 BM\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-20T14:58:20.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"site_depart\":{\"id\":3,\"quartier_id\":65,\"ville_id\":1,\"name\":\"makep\\u00e8\",\"type_site\":\"physique\",\"is_site_reference\":1,\"statut\":1,\"created_at\":\"2022-01-20T15:00:22.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"nbre_details\":1,\"date_creation\":\"2022-01-21 12:11:58\",\"date_update\":\"2022-01-21 12:11:58\"},{\"id\":2,\"site_depart_id\":3,\"site_arrivee_id\":2,\"gestionnaire_id\":1,\"type_action\":\"sortie\",\"status\":1,\"created_at\":\"2022-01-21T11:02:44.000000Z\",\"updated_at\":\"2022-01-21T11:02:44.000000Z\",\"gestionnaire\":{\"id\":1,\"holder_id\":\"1\",\"creator_id\":\"1\",\"updater_id\":\"1\",\"adresse_favorite_id\":null,\"uuid\":\"a1bf216e-e7cf-4a37-bfba-9844de4cb2e9\",\"username\":\"superadmin\",\"avatar\":null,\"fullname\":\"Super Admin Multicanalservices\",\"firstname\":null,\"lastname\":null,\"email\":\"contact@multicanalservices.com\",\"email_verified_at\":\"2020-07-05 00:00:00\",\"phone_verified_at\":\"2020-07-05 03:30:53\",\"password\":\"$2y$10$NlLv6ttS3yc8gSH7XfuJVO2IU9jzWczro.d92vGE5.PJmT9emfME2\",\"telephone\":\"695461461\",\"telephone_alt\":null,\"genre\":null,\"provider_id\":\"1\",\"provider_name\":\"localhost\",\"langue\":null,\"description\":null,\"modules\":null,\"token\":null,\"fcm_token\":null,\"remember_token\":\"9vs7GtobD6y44mMSYr1plswK0y1ZeOfZNQWIIVxdAU9gAdp6zPoXhgLcO9UZ\",\"statut\":1,\"is_guest\":0,\"type\":1,\"created_at\":\"2020-07-05 03:30:49\",\"updated_at\":\"2022-03-15 11:37:15\",\"deleted_at\":null,\"last_login_at\":\"2022-03-15 11:37:15\",\"last_login_ip\":\"154.72.171.45\",\"stripe_id\":null,\"card_brand\n" +
                    "    \":null,\"card_last_four\":null,\"trial_ends_at\":null},\"site_arrivee\":{\"id\":2,\"quartier_id\":265,\"ville_id\":1,\"name\":\"coursier Makep\\u00e8 BM\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-20T14:58:20.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"site_depart\":{\"id\":3,\"quartier_id\":65,\"ville_id\":1,\"name\":\"makep\\u00e8\",\"type_site\":\"physique\",\"is_site_reference\":1,\"statut\":1,\"created_at\":\"2022-01-20T15:00:22.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"nbre_details\":2,\"date_creation\":\"2022-01-21 11:02:44\",\"date_update\":\"2022-01-21 11:02:44\"}],\"success\":true}");

            if (jsonResponse.getBoolean("success")) {
                setExitMoves(new Gson().fromJson(String.valueOf(jsonResponse.getJSONArray("data")), new TypeToken<List<Move>>() {
                }.getType()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setIsExitMovesLoading(false);
    }

    public void loadReturnMoves() {
        setIsReturnMovesLoading(true);
        JSONObject jsonResponse = null;
        try {
            jsonResponse = new JSONObject("{\"message\":\"OK\",\"issues\":null,\"data\":[{\"id\":12,\"site_depart_id\":4,\"site_arrivee_id\":4,\"gestionnaire_id\":1,\"type_action\":\"sortie\",\"status\":1,\"created_at\":\"2022-01-26T14:16:49.000000Z\",\"updated_at\":\"2022-01-26T14:16:49.000000Z\",\"gestionnaire\":{\"id\":1,\"holder_id\":\"1\",\"creator_id\":\"1\",\"updater_id\":\"1\",\"adresse_favorite_id\":null,\"uuid\":\"a1bf216e-e7cf-4a37-bfba-9844de4cb2e9\",\"username\":\"superadmin\",\"avatar\":null,\"fullname\":\"Super Admin Multicanalservices\",\"firstname\":null,\"lastname\":null,\"email\":\"contact@multicanalservices.com\",\"email_verified_at\":\"2020-07-05 00:00:00\",\"phone_verified_at\":\"2020-07-05 03:30:53\",\"password\":\"$2y$10$NlLv6ttS3yc8gSH7XfuJVO2IU9jzWczro.d92vGE5.PJmT9emfME2\",\"telephone\":\"695461461\",\"telephone_alt\":null,\"genre\":null,\"provider_id\":\"1\",\"provider_name\":\"localhost\",\"langue\":null,\"description\":null,\"modules\":null,\"token\":null,\"fcm_token\":null,\"remember_token\":\"9vs7GtobD6y44mMSYr1plswK0y1ZeOfZNQWIIVxdAU9gAdp6zPoXhgLcO9UZ\",\"statut\":1,\"is_guest\":0,\"type\":1,\"created_at\":\"2020-07-05 03:30:49\",\"updated_at\":\"2022-03-15 11:37:15\",\"deleted_at\":null,\"last_login_at\":\"2022-03-15 11:37:15\",\"last_login_ip\":\"154.72.171.45\",\"stripe_id\":null,\"card_brand\":null,\"card_last_four\":null,\"trial_ends_at\":null},\"site_arrivee\":{\"id\":4,\"quartier_id\":2,\"ville_id\":1,\"name\":\"courrsier Akwa\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-21T12:36:55.000000Z\",\"updated_at\":\"2022-01-21T12:36:55.000000Z\"},\"site_depart\":{\"id\":4,\"quartier_id\":2,\"ville_id\":1,\"name\":\"courrsier Akwa\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-21T12:36:55.000000Z\",\"updated_at\":\"2022-01-21T12:36:55.000000Z\"},\"nbre_details\":2,\"date_creation\":\"2022-01-26 02:16:49\",\"date_update\":\"2022-01-26 02:16:49\"},{\"id\":6,\"site_depart_id\":3,\"site_arrivee_id\":5,\"gestionnaire_id\":1,\"type_action\":\"sortie\",\"status\":1,\"created_at\":\"2022-01-21T12:40:50.000000Z\",\"updated_at\":\"2022-01-21T12:40:50.000000Z\",\"gestionnaire\":{\"id\":1,\"holder_id\":\"1\",\"creator_id\":\"1\",\"updater_id\":\"1\",\"adresse_favorite_id\":null,\"uuid\":\"a1bf216e-e7cf-4a37-bfba-9844de4cb2e9\",\"username\":\"superadmin\",\"avatar\":null,\"fullname\":\"Super Admin Multicanalservices\",\"firstname\":null,\"lastname\":null,\"email\":\"contact@multicanalservices.com\",\"email_verified_at\":\"2020-07-05 00:00:00\",\"phone_verified_at\":\"2020-07-05 03:30:53\",\"password\":\"$2y$10$NlLv6ttS3yc8gSH7XfuJVO2IU9jzWczro.d92vGE5.PJmT9emfME2\",\"telephone\":\"695461461\",\"telephone_alt\":null,\"genre\":null,\"provider_id\":\"1\",\"provider_name\":\"localhost\",\"langue\":null,\"description\":null,\"modules\":null,\"token\":null,\"fcm_token\":null,\"remember_token\":\"9vs7GtobD6y44mMSYr1plswK0y1ZeOfZNQWIIVxdAU9gAdp6zPoXhgLcO9UZ\",\"statut\":1,\"is_guest\":0,\"type\":1,\"created_at\":\"2020-07-05 03:30:49\",\"updated_at\":\"2022-03-15 11:37:15\",\"deleted_at\":null,\"last_login_at\":\"2022-03-15 11:37:15\",\"last_login_ip\":\"154.72.171.45\",\"stripe_id\":null,\"card_brand\":null,\"card_last_four\":null,\"trial_ends_at\":null},\"site_arrivee\":{\"id\":5,\"quartier_id\":3,\"ville_id\":1,\"name\":\"coursier d'Akxa Nord\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-21T12:38:07.000000Z\",\"updated_at\":\"2022-01-21T12:38:07.000000Z\"},\"site_depart\":{\"id\":3,\"quartier_id\":65,\"ville_id\":1,\"name\":\"makep\\u00e8\",\"type_site\":\"physique\",\"is_site_reference\":1,\"statut\":1,\"created_at\":\"2022-01-20T15:00:22.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"nbre_details\":1,\"date_creation\":\"2022-01-21 12:40:50\",\"date_update\":\"2022-01-21 12:40:50\"},{\"id\":5,\"site_depart_id\":3,\"site_arrivee_id\":4,\"gestionnaire_id\":1,\"type_action\":\"sortie\",\"status\":1,\"created_at\":\"2022-01-21T12:39:44.000000Z\",\"updated_at\":\"2022-01-21T12:39:44.000000Z\",\"gestionnaire\":{\"id\":1,\"holder_id\":\"1\",\"creator_id\":\"1\",\"updater_id\":\"1\",\"adresse_favorite_id\":null,\"uuid\":\"a1bf216e-e7cf-4a37-bfba-9844de4cb2e9\",\"username\":\"superadmin\",\"avatar\":null,\"fullname\":\"Super Admin Multicanalservices\",\"firstname\":null,\"lastname\":null,\"email\":\"contact@multicanalservices.com\",\"email_verified_at\":\"2020-07-05 00:00:00\",\"phone_verified_\n" +
                    "    at\":\"2020-07-05 03:30:53\",\"password\":\"$2y$10$NlLv6ttS3yc8gSH7XfuJVO2IU9jzWczro.d92vGE5.PJmT9emfME2\",\"telephone\":\"695461461\",\"telephone_alt\":null,\"genre\":null,\"provider_id\":\"1\",\"provider_name\":\"localhost\",\"langue\":null,\"description\":null,\"modules\":null,\"token\":null,\"fcm_token\":null,\"remember_token\":\"9vs7GtobD6y44mMSYr1plswK0y1ZeOfZNQWIIVxdAU9gAdp6zPoXhgLcO9UZ\",\"statut\":1,\"is_guest\":0,\"type\":1,\"created_at\":\"2020-07-05 03:30:49\",\"updated_at\":\"2022-03-15 11:37:15\",\"deleted_at\":null,\"last_login_at\":\"2022-03-15 11:37:15\",\"last_login_ip\":\"154.72.171.45\",\"stripe_id\":null,\"card_brand\":null,\"card_last_four\":null,\"trial_ends_at\":null},\"site_arrivee\":{\"id\":4,\"quartier_id\":2,\"ville_id\":1,\"name\":\"courrsier Akwa\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-21T12:36:55.000000Z\",\"updated_at\":\"2022-01-21T12:36:55.000000Z\"},\"site_depart\":{\"id\":3,\"quartier_id\":65,\"ville_id\":1,\"name\":\"makep\\u00e8\",\"type_site\":\"physique\",\"is_site_reference\":1,\"statut\":1,\"created_at\":\"2022-01-20T15:00:22.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"nbre_details\":1,\"date_creation\":\"2022-01-21 12:39:44\",\"date_update\":\"2022-01-21 12:39:44\"},{\"id\":4,\"site_depart_id\":3,\"site_arrivee_id\":2,\"gestionnaire_id\":1,\"type_action\":\"sortie\",\"status\":1,\"created_at\":\"2022-01-21T12:11:58.000000Z\",\"updated_at\":\"2022-01-21T12:11:58.000000Z\",\"gestionnaire\":{\"id\":1,\"holder_id\":\"1\",\"creator_id\":\"1\",\"updater_id\":\"1\",\"adresse_favorite_id\":null,\"uuid\":\"a1bf216e-e7cf-4a37-bfba-9844de4cb2e9\",\"username\":\"superadmin\",\"avatar\":null,\"fullname\":\"Super Admin Multicanalservices\",\"firstname\":null,\"lastname\":null,\"email\":\"contact@multicanalservices.com\",\"email_verified_at\":\"2020-07-05 00:00:00\",\"phone_verified_at\":\"2020-07-05 03:30:53\",\"password\":\"$2y$10$NlLv6ttS3yc8gSH7XfuJVO2IU9jzWczro.d92vGE5.PJmT9emfME2\",\"telephone\":\"695461461\",\"telephone_alt\":null,\"genre\":null,\"provider_id\":\"1\",\"provider_name\":\"localhost\",\"langue\":null,\"description\":null,\"modules\":null,\"token\":null,\"fcm_token\":null,\"remember_token\":\"9vs7GtobD6y44mMSYr1plswK0y1ZeOfZNQWIIVxdAU9gAdp6zPoXhgLcO9UZ\",\"statut\":1,\"is_guest\":0,\"type\":1,\"created_at\":\"2020-07-05 03:30:49\",\"updated_at\":\"2022-03-15 11:37:15\",\"deleted_at\":null,\"last_login_at\":\"2022-03-15 11:37:15\",\"last_login_ip\":\"154.72.171.45\",\"stripe_id\":null,\"card_brand\":null,\"card_last_four\":null,\"trial_ends_at\":null},\"site_arrivee\":{\"id\":2,\"quartier_id\":265,\"ville_id\":1,\"name\":\"coursier Makep\\u00e8 BM\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-20T14:58:20.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"site_depart\":{\"id\":3,\"quartier_id\":65,\"ville_id\":1,\"name\":\"makep\\u00e8\",\"type_site\":\"physique\",\"is_site_reference\":1,\"statut\":1,\"created_at\":\"2022-01-20T15:00:22.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"nbre_details\":1,\"date_creation\":\"2022-01-21 12:11:58\",\"date_update\":\"2022-01-21 12:11:58\"},{\"id\":2,\"site_depart_id\":3,\"site_arrivee_id\":2,\"gestionnaire_id\":1,\"type_action\":\"sortie\",\"status\":1,\"created_at\":\"2022-01-21T11:02:44.000000Z\",\"updated_at\":\"2022-01-21T11:02:44.000000Z\",\"gestionnaire\":{\"id\":1,\"holder_id\":\"1\",\"creator_id\":\"1\",\"updater_id\":\"1\",\"adresse_favorite_id\":null,\"uuid\":\"a1bf216e-e7cf-4a37-bfba-9844de4cb2e9\",\"username\":\"superadmin\",\"avatar\":null,\"fullname\":\"Super Admin Multicanalservices\",\"firstname\":null,\"lastname\":null,\"email\":\"contact@multicanalservices.com\",\"email_verified_at\":\"2020-07-05 00:00:00\",\"phone_verified_at\":\"2020-07-05 03:30:53\",\"password\":\"$2y$10$NlLv6ttS3yc8gSH7XfuJVO2IU9jzWczro.d92vGE5.PJmT9emfME2\",\"telephone\":\"695461461\",\"telephone_alt\":null,\"genre\":null,\"provider_id\":\"1\",\"provider_name\":\"localhost\",\"langue\":null,\"description\":null,\"modules\":null,\"token\":null,\"fcm_token\":null,\"remember_token\":\"9vs7GtobD6y44mMSYr1plswK0y1ZeOfZNQWIIVxdAU9gAdp6zPoXhgLcO9UZ\",\"statut\":1,\"is_guest\":0,\"type\":1,\"created_at\":\"2020-07-05 03:30:49\",\"updated_at\":\"2022-03-15 11:37:15\",\"deleted_at\":null,\"last_login_at\":\"2022-03-15 11:37:15\",\"last_login_ip\":\"154.72.171.45\",\"stripe_id\":null,\"card_brand\n" +
                    "    \":null,\"card_last_four\":null,\"trial_ends_at\":null},\"site_arrivee\":{\"id\":2,\"quartier_id\":265,\"ville_id\":1,\"name\":\"coursier Makep\\u00e8 BM\",\"type_site\":\"mobile\",\"is_site_reference\":0,\"statut\":1,\"created_at\":\"2022-01-20T14:58:20.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"site_depart\":{\"id\":3,\"quartier_id\":65,\"ville_id\":1,\"name\":\"makep\\u00e8\",\"type_site\":\"physique\",\"is_site_reference\":1,\"statut\":1,\"created_at\":\"2022-01-20T15:00:22.000000Z\",\"updated_at\":\"2022-01-20T15:00:22.000000Z\"},\"nbre_details\":2,\"date_creation\":\"2022-01-21 11:02:44\",\"date_update\":\"2022-01-21 11:02:44\"}],\"success\":true}");

            if (jsonResponse.getBoolean("success")) {
                setReturnMoves(new Gson().fromJson(String.valueOf(jsonResponse.getJSONArray("data")), new TypeToken<List<Move>>() {
                }.getType()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setIsReturnMovesLoading(false);
    }

    public void loadResumeMoves() {

    }


    public int getCurrentFragmentIndex() {
        return currentFragmentIndex;
    }

    public void setCurrentFragmentIndex(int currentFragmentIndex) {
        this.currentFragmentIndex = currentFragmentIndex;
    }

    public MutableLiveData<Move> getCurrentViewedMove() {
        return currentViewedMove;
    }

    public void setCurrentViewedMove(Move move) {
        this.currentViewedMove.setValue(move);
    }

    public int getPreviousFragmentIndex() {
        return previousFragmentIndex;
    }

    public void setPreviousFragmentIndex(int previousFragmentIndex) {
        this.previousFragmentIndex = previousFragmentIndex;
    }

    public MutableLiveData<List<HashMap<String, Object>>> getCurrentViewedMoveProducts() {
        return currentViewedMoveProducts;
    }

//    public void setCurrentViewedMoveProducts(List<Product> currentViewedMoveProducts) {
//        this.currentViewedMoveProducts.setValue(currentViewedMoveProducts);
//    }


    private void setCurrentViewedMoveProducts(List<HashMap<String, Object>> shopWithProducts) {
        this.currentViewedMoveProducts.setValue(shopWithProducts);
    }

}
