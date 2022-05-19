package com.israel.livraisonexpresspos.ui.inventory_management.ware_house;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.models.inventory_management_models.MoveByShop;
import com.israel.livraisonexpresspos.models.inventory_management_models.Site;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WareHouseViewModel extends AndroidViewModel {

    private MutableLiveData<List<Site>> mSiteList;

    public WareHouseViewModel(@NonNull Application application) {
        super(application);
        mSiteList = new MutableLiveData<>();
    }

    public MutableLiveData<List<Site>> getSiteList() {
        return mSiteList;
    }

    public void setSiteList(List<Site> siteList) {
        this.mSiteList.setValue(siteList);
    }

    public void loadSites(){
        try {
            /*todo : add the retrofit callback logic here */

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
                    "           \"created_at\": \"2021-10-21 15:29:05\",\n" +
                    "           \"updated_at\": \"2021-10-21 15:29:05\"\n" +
                    "       },\n" +
                    "       {\n" +
                    "           \"id\": 2,\n" +
                    "           \"quartier_id\": 2,\n" +
                    "           \"ville_id\": 1,\n" +
                    "           \"name\": \"coursier de akwa\",\n" +
                    "           \"type_site\": \"mobile\",\n" +
                    "           \"is_site_reference\": 0,\n" +
                    "           \"status\": 1,\n" +
                    "           \"created_at\": \"2021-10-21 15:29:45\",\n" +
                    "           \"updated_at\": \"2021-10-21 15:29:45\"\n" +
                    "       },\n" +
                    "       {\n" +
                    "           \"id\": 3,\n" +
                    "           \"quartier_id\": 20,\n" +
                    "           \"ville_id\": 1,\n" +
                    "           \"name\": \"site de bonaberi\",\n" +
                    "           \"type_site\": \"physique\",\n" +
                    "           \"is_site_reference\": 0,\n" +
                    "           \"status\": 1,\n" +
                    "           \"created_at\": \"2021-10-25 09:10:01\",\n" +
                    "           \"updated_at\": \"2021-10-25 09:10:01\"\n" +
                    "       }\n" +
                    "   ],\n" +
                    "   \"success\": true\n" +
                    "}\n");

            if (jsonResponse.getBoolean("success")) {
                setSiteList(new Gson().fromJson(String.valueOf(jsonResponse.getJSONArray("data")), new TypeToken<List<Site>>() {
                }.getType()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
