package com.israel.livraisonexpresspos.ui.inventory_management.search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.models.inventory_management_models.FilterShop;
import com.israel.livraisonexpresspos.models.inventory_management_models.MoveByShop;
import com.israel.livraisonexpresspos.models.inventory_management_models.Site;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MovesFilterViewModel extends AndroidViewModel {
    //    private MutableLiveData<List<Move>> moves;
    private MutableLiveData<List<MoveByShop>> movesByShopsList;
    private MutableLiveData<List<FilterShop>> mShopsList;
    private MutableLiveData<List<Site>> mSiteList;
    private MutableLiveData<Boolean> isLoadingShops;

    public MovesFilterViewModel(@NonNull Application application) {
        super(application);
        mShopsList = new MutableLiveData<>();
        movesByShopsList = new MutableLiveData<>();
        mSiteList = new MutableLiveData<>();
        isLoadingShops = new MutableLiveData<>();
    }


    public MutableLiveData<List<FilterShop>> getShopsList() {
        return mShopsList;
    }

    public void setShopsList(List<FilterShop> shopsList) {
        this.mShopsList.setValue(shopsList);
    }

    public MutableLiveData<Boolean> isLoadingShops() {
        return isLoadingShops;
    }

    public void setLoadingShops(boolean isLoadingShops) {
        this.isLoadingShops.setValue(isLoadingShops);
    }

    public void loadMovesByShops() {
        try {
            /*todo : add the retrofit callback logic here */

            JSONObject jsonResponse = new JSONObject("{\n" +
                    "    \"message\": \"OK\",\n" +
                    "    \"issues\": null,\n" +
                    "    \"data\": [\n" +
                    "        {\n" +
                    "            \"magasin_id\": 26,\n" +
                    "            \"magasin_name\": \"BANTU CARE  DOUALA\",\n" +
                    "            \"produits\": [\n" +
                    "     \n" +
                    "                {\n" +
                    "                    \"id\": 2108,\n" +
                    "                    \"nom_produit\": \"NECTAR D'ADAM\",\n" +
                    "                    \"qte_mouvement\": 10,\n" +
                    "                    \"type_mouvement\": \"sortie\",\n" +
                    "                    \"site_depart\": \"makepè\",\n" +
                    "                    \"site_arrivee\": \"coursier Makepè BM\",\n" +
                    "                    \"created_at\": \"2022-01-21 12:11:58\",\n" +
                    "                    \"updated_at\": \"2022-01-21 12:11:58\",\n" +
                    "                    \"date_creation\": \"2022-01-21 12:11:58\",\n" +
                    "                    \"date_update\": \"2022-01-21 12:11:58\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"id\": 2108,\n" +
                    "                    \"nom_produit\": \"NECTAR D'ADAM\",\n" +
                    "                    \"qte_mouvement\": 2,\n" +
                    "                    \"type_mouvement\": \"sortie\",\n" +
                    "                    \"site_depart\": \"makepè\",\n" +
                    "                    \"site_arrivee\": \"coursier d'Akxa Nord\",\n" +
                    "                    \"created_at\": \"2022-01-21 12:40:50\",\n" +
                    "                    \"updated_at\": \"2022-01-21 12:40:50\",\n" +
                    "                    \"date_creation\": \"2022-01-21 12:40:50\",\n" +
                    "                    \"date_update\": \"2022-01-21 12:40:50\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"id\": 2045,\n" +
                    "                    \"nom_produit\": \"Action ventre plat\",\n" +
                    "                    \"qte_mouvement\": 15,\n" +
                    "                    \"type_mouvement\": \"sortie\",\n" +
                    "                    \"site_depart\": \"courrsier Akwa\",\n" +
                    "                    \"site_arrivee\": \"courrsier Akwa\",\n" +
                    "                    \"created_at\": \"2022-01-26 02:16:49\",\n" +
                    "                    \"updated_at\": \"2022-01-26 02:16:49\",\n" +
                    "                    \"date_creation\": \"2022-01-26 02:16:49\",\n" +
                    "                    \"date_update\": \"2022-01-26 02:16:49\"\n" +
                    "                }\n" +
                    "            ]\n" +
                    "        },\n" +
                    "{\n" +
                    "            \"magasin_id\": 20,\n" +
                    "            \"magasin_name\": \"AGATHE SHOP\",\n" +
                    "            \"produits\": [\n" +
                    "                {\n" +
                    "                    \"id\": 2045,\n" +
                    "                    \"nom_produit\": \"Action ventre plat\",\n" +
                    "                    \"qte_mouvement\": 9,\n" +
                    "                    \"type_mouvement\": \"sortie\",\n" +
                    "                    \"site_depart\": \"makepè\",\n" +
                    "                    \"site_arrivee\": \"coursier Makepè BM\",\n" +
                    "                    \"created_at\": \"2022-01-21 11:02:44\",\n" +
                    "                    \"updated_at\": \"2022-01-21 11:02:44\",\n" +
                    "                    \"date_creation\": \"2022-01-21 11:02:44\",\n" +
                    "                    \"date_update\": \"2022-01-21 11:02:44\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"id\": 2046,\n" +
                    "                    \"nom_produit\": \"Anti-Arthristis Tea\",\n" +
                    "                    \"qte_mouvement\": 10,\n" +
                    "                    \"type_mouvement\": \"sortie\",\n" +
                    "                    \"site_depart\": \"makepè\",\n" +
                    "                    \"site_arrivee\": \"coursier Makepè BM\",\n" +
                    "                    \"created_at\": \"2022-01-21 11:02:44\",\n" +
                    "                    \"updated_at\": \"2022-01-21 11:02:44\",\n" +
                    "                    \"date_creation\": \"2022-01-21 11:02:44\",\n" +
                    "                    \"date_update\": \"2022-01-21 11:02:44\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"id\": 2108,\n" +
                    "                    \"nom_produit\": \"NECTAR D'ADAM\",\n" +
                    "                    \"qte_mouvement\": 2,\n" +
                    "                    \"type_mouvement\": \"sortie\",\n" +
                    "                    \"site_depart\": \"makepè\",\n" +
                    "                    \"site_arrivee\": \"coursier d'Akxa Nord\",\n" +
                    "                    \"created_at\": \"2022-01-21 12:40:50\",\n" +
                    "                    \"updated_at\": \"2022-01-21 12:40:50\",\n" +
                    "                    \"date_creation\": \"2022-01-21 12:40:50\",\n" +
                    "                    \"date_update\": \"2022-01-21 12:40:50\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"id\": 2045,\n" +
                    "                    \"nom_produit\": \"Action ventre plat\",\n" +
                    "                    \"qte_mouvement\": 15,\n" +
                    "                    \"type_mouvement\": \"sortie\",\n" +
                    "                    \"site_depart\": \"courrsier Akwa\",\n" +
                    "                    \"site_arrivee\": \"courrsier Akwa\",\n" +
                    "                    \"created_at\": \"2022-01-26 02:16:49\",\n" +
                    "                    \"updated_at\": \"2022-01-26 02:16:49\",\n" +
                    "                    \"date_creation\": \"2022-01-26 02:16:49\",\n" +
                    "                    \"date_update\": \"2022-01-26 02:16:49\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"id\": 2045,\n" +
                    "                    \"nom_produit\": \"Action ventre plat\",\n" +
                    "                    \"qte_mouvement\": 25,\n" +
                    "                    \"type_mouvement\": \"sortie\",\n" +
                    "                    \"site_depart\": \"courrsier Akwa\",\n" +
                    "                    \"site_arrivee\": \"courrsier Akwa\",\n" +
                    "                    \"created_at\": \"2022-01-26 02:16:49\",\n" +
                    "                    \"updated_at\": \"2022-01-26 02:16:49\",\n" +
                    "                    \"date_creation\": \"2022-01-26 02:16:49\",\n" +
                    "                    \"date_update\": \"2022-01-26 02:16:49\"\n" +
                    "                }\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"success\": true\n" +
                    "}");

            if (jsonResponse.getBoolean("success")) {
                setMovesByShopsList(new Gson().fromJson(String.valueOf(jsonResponse.getJSONArray("data")), new TypeToken<List<MoveByShop>>() {
                }.getType()));
            }

            setLoadingShops(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadShops() {
//        this.isLoadingShops.setValue(true);
//        Call<ResponseBody> call = InventoryManagementApi.filterService().loadShops(User.getCurrentUser(getApplication()).getToken(),User.getCurrentUser(getApplication()).getCity().get(0));
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });


        try {
            JSONObject jsonResponse = new JSONObject("{\n" +
                    "   \"message\": \"OK\",\n" +
                    "   \"issues\": null,\n" +
                    "   \"data\": [\n" +
                    "       {\n" +
                    "           \"id\": 3,\n" +
                    "           \"creator_id\": \"4\",\n" +
                    "           \"updater_id\": \"4\",\n" +
                    "           \"etablissement_id\": 1,\n" +
                    "           \"partenaire_id\": 4,\n" +
                    "           \"module_id\": 1,\n" +
                    "           \"nom\": \"Coursier Express Douala\",\n" +
                    "           \"slug\": \"coursier-express-douala\",\n" +
                    "           \"display_rank\": 1,\n" +
                    "           \"description\": \"Coursier Express Makèpe Douala\",\n" +
                    "           \"contact_id\": 100,\n" +
                    "           \"ville_id\": 1,\n" +
                    "           \"adresse_id\": 100,\n" +
                    "           \"image\": null,\n" +
                    "           \"is_master\": 1,\n" +
                    "           \"horaires\": \"[{\\\"items\\\":[{\\\"enabled\\\":true,\\\"opened\\\":true,\\\"opened_at\\\":\\\"08:00\\\",\\\"closed_at\\\":\\\"17:30\\\"}],\\\"opened\\\":true,\\\"enabled\\\":true},{\\\"items\\\":[{\\\"enabled\\\":true,\\\"opened\\\":true,\\\"opened_at\\\":\\\"08:00\\\",\\\"closed_at\\\":\\\"20:30\\\"}],\\\"opened\\\":true,\\\"enabled\\\":true},{\\\"items\\\":[{\\\"enabled\\\":true,\\\"opened\\\":true,\\\"opened_at\\\":\\\"08:00\\\",\\\"closed_at\\\":\\\"20:30\\\"}],\\\"opened\\\":true,\\\"enabled\\\":true},{\\\"items\\\":[{\\\"enabled\\\":true,\\\"opened\\\":true,\\\"opened_at\\\":\\\"08:00\\\",\\\"closed_at\\\":\\\"20:30\\\"}],\\\"opened\\\":true,\\\"enabled\\\":true},{\\\"items\\\":[{\\\"enabled\\\":true,\\\"opened\\\":true,\\\"opened_at\\\":\\\"08:00\\\",\\\"closed_at\\\":\\\"20:30\\\"}],\\\"opened\\\":true,\\\"enabled\\\":true},{\\\"items\\\":[{\\\"enabled\\\":true,\\\"opened\\\":true,\\\"opened_at\\\":\\\"08:00\\\",\\\"closed_at\\\":\\\"20:30\\\"}],\\\"opened\\\":true,\\\"enabled\\\":true},{\\\"items\\\":[{\\\"enabled\\\":true,\\\"opened\\\":true,\\\"opened_at\\\":\\\"08:00\\\",\\\"closed_at\\\":\\\"20:30\\\"}],\\\"opened\\\":true,\\\"enabled\\\":true}]\",\n" +
                    "           \"tags\": \"magasin\",\n" +
                    "           \"is_active\": 1,\n" +
                    "           \"is_available\": 1,\n" +
                    "           \"base_delivery_meters\": 4000,\n" +
                    "           \"base_delivery_amount\": 500,\n" +
                    "           \"base_delivery_meters_as_step_unit\": 1000,\n" +
                    "           \"base_delivery_amount_per_step\": 100,\n" +
                    "           \"free_shipping_cart_amount\": null,\n" +
                    "           \"shipping_preparation_time\": 20,\n" +
                    "           \"shipping_duration_max_accept_minutes\": 50,\n" +
                    "           \"shipping_distance_max_accept_meters\": 20000,\n" +
                    "           \"created_at\": \"2020-01-13 17:55:43\",\n" +
                    "           \"updated_at\": \"2020-07-12 09:13:35\",\n" +
                    "           \"deleted_at\": null,\n" +
                    "           \"type_cms\": null,\n" +
                    "           \"url\": null,\n" +
                    "           \"api_key\": null,\n" +
                    "           \"version_cms\": null,\n" +
                    "           \"owner_name\": null,\n" +
                    "           \"owner_phone\": null\n" +
                    "       },\n" +
                    "       {\n" +
                    "           \"id\": 4,\n" +
                    "           \"creator_id\": \"4\",\n" +
                    "           \"updater_id\": \"4\",\n" +
                    "           \"etablissement_id\": 1,\n" +
                    "           \"partenaire_id\": 4,\n" +
                    "           \"module_id\": 2,\n" +
                    "           \"nom\": \"Supermarché Express Douala\",\n" +
                    "           \"slug\": \"supermarket-express-douala\",\n" +
                    "           \"display_rank\": 1,\n" +
                    "           \"description\": \"Supermarket Express Makèpe Douala\",\n" +
                    "           \"contact_id\": 103,\n" +
                    "           \"ville_id\": 1,\n" +
                    "           \"adresse_id\": 100,\n" +
                    "           \"image\": \"/storage/images/supermarche/store-market-express.jpg\",\n" +
                    "           \"is_master\": 1,\n" +
                    "           \"horaires\": \"[{\\\"items\\\":[{\\\"enabled\\\":true,\\\"opened\\\":true,\\\"opened_at\\\":\\\"08:00\\\",\\\"closed_at\\\":\\\"19:00\\\"}],\\\"opened\\\":true,\\\"enabled\\\":true},{\\\"items\\\":[{\\\"enabled\\\":true,\\\"opened\\\":true,\\\"opened_at\\\":\\\"08:00\\\",\\\"closed_at\\\":\\\"19:00\\\"}],\\\"opened\\\":true,\\\"enabled\\\":true},{\\\"items\\\":[{\\\"enabled\\\":true,\\\"opened\\\":true,\\\"opened_at\\\":\\\"08:00\\\",\\\"closed_at\\\":\\\"19:00\\\"}],\\\"opened\\\":true,\\\"enabled\\\":true},{\\\"items\\\":[{\\\"opened\\\":true,\\\"enabled\\\":true,\\\"opened_at\\\":\\\"08:00\\\",\\\"closed_at\\\":\\\"19:00\\\"}],\\\"opened\\\":true,\\\"enabled\\\":true},{\\\"items\\\":[{\\\"enabled\\\":true,\\\"opened\\\":true,\\\"opened_at\\\":\\\"08:00\\\",\\\"closed_at\\\":\\\"19:00\\\"}],\\\"opened\\\":true,\\\"enabled\\\":true},{\\\"items\\\":[{\\\"enabled\\\":true,\\\"opened\\\":true,\\\"opened_at\\\":\\\"08:00\\\",\\\"closed_at\\\":\\\"19:00\\\"}],\\\"opened\\\":true,\\\"enabled\\\":true},{\\\"items\\\":[{\\\"enabled\\\":true,\\\"opened\\\":true,\\\"opened_at\\\":\\\"08:00\\\",\\\"closed_at\\\":\\\"19:00\\\"}],\\\"opened\\\":true,\\\"enabled\\\":true}]\",\n" +
                    "           \"tags\": \"magasin\",\n" +
                    "           \"is_active\": 1,\n" +
                    "           \"is_available\": 1,\n" +
                    "           \"base_delivery_meters\": 10000,\n" +
                    "           \"base_delivery_amount\": 1000,\n" +
                    "           \"base_delivery_meters_as_step_unit\": 5000,\n" +
                    "           \"base_delivery_amount_per_step\": 500,\n" +
                    "           \"free_shipping_cart_amount\": null,\n" +
                    "           \"shipping_preparation_time\": 20,\n" +
                    "           \"shipping_duration_max_accept_minutes\": 30,\n" +
                    "           \"shipping_distance_max_accept_meters\": 15000,\n" +
                    "           \"created_at\": \"2020-01-13 17:55:47\",\n" +
                    "           \"updated_at\": \"2020-07-12 09:15:47\",\n" +
                    "           \"deleted_at\": null,\n" +
                    "           \"type_cms\": null,\n" +
                    "           \"url\": null,\n" +
                    "           \"api_key\": null,\n" +
                    "           \"version_cms\": null,\n" +
                    "           \"owner_name\": null,\n" +
                    "           \"owner_phone\": null\n" +
                    "       }\n" +
                    "   ],\n" +
                    "   \"success\": true\n" +
                    "}\n");

            if (jsonResponse.getBoolean("success")) {
                setShopsList(new Gson().fromJson(String.valueOf(jsonResponse.getJSONArray("data")), new TypeToken<List<FilterShop>>() {
                }.getType()));
            }

            setLoadingShops(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void loadSites() {
        try {
            JSONObject jsonResponse = new JSONObject("{\n" +
                    "   \"message\": \"OK\",\n" +
                    "   \"issues\": null,\n" +
                    "   \"data\": [\n" +
                    "       {\n" +
                    "           \"id\": 1,\n" +
                    "           \"quartier_id\": 2,\n" +
                    "           \"ville_id\": 1,\n" +
                    "           \"name\": \"site de akwa\",\n" +
                    "           \"type_site\": \"physique\",\n" +
                    "           \"is_site_reference\": 1,\n" +
                    "           \"status\": 1,\n" +
                    "           \"created_at\": \"2021-10-21T15:29:05.000000Z\",\n" +
                    "           \"updated_at\": \"2021-10-21T15:29:05.000000Z\"\n" +
                    "       },\n" +
                    "       {\n" +
                    "           \"id\": 2,\n" +
                    "           \"quartier_id\": 2,\n" +
                    "           \"ville_id\": 1,\n" +
                    "           \"name\": \"coursier de akwa\",\n" +
                    "           \"type_site\": \"mobile\",\n" +
                    "           \"is_site_reference\": 0,\n" +
                    "           \"status\": 1,\n" +
                    "           \"created_at\": \"2021-10-21T15:29:45.000000Z\",\n" +
                    "           \"updated_at\": \"2021-10-21T15:29:45.000000Z\"\n" +
                    "       },\n" +
                    "       {\n" +
                    "           \"id\": 3,\n" +
                    "           \"quartier_id\": 20,\n" +
                    "           \"ville_id\": 1,\n" +
                    "           \"name\": \"site de bonaberi\",\n" +
                    "           \"type_site\": \"physique\",\n" +
                    "           \"is_site_reference\": 0,\n" +
                    "           \"status\": 1,\n" +
                    "           \"created_at\": \"2021-10-25T09:10:01.000000Z\",\n" +
                    "           \"updated_at\": \"2021-10-25T09:10:01.000000Z\"\n" +
                    "       }\n" +
                    "   ],\n" +
                    "   \"success\": true\n" +
                    "}\n");

            if (jsonResponse.getBoolean("success")) {
                setSiteList(new Gson().fromJson(String.valueOf(jsonResponse.getJSONArray("data")), new TypeToken<List<Site>>() {
                }.getType()));
            }

//            setLoadingShops(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public MutableLiveData<List<MoveByShop>> getMovesByShopsList() {
        return movesByShopsList;
    }

    public void setMovesByShopsList(List<MoveByShop> movesByShopsList) {
        this.movesByShopsList.setValue(movesByShopsList);
    }

    public MutableLiveData<List<Site>> getSiteList() {
        return mSiteList;
    }

    public void setSiteList(List<Site> mSiteList) {
        this.mSiteList.setValue(mSiteList);
    }
}
