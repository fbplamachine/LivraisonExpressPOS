package com.israel.livraisonexpresspos.models;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "order_table", indices = {@Index(value = {"id"}, unique = true)})
public class Order {
    @PrimaryKey(autoGenerate = true)
    int id;
    int shopId;
    String ref;
    String city;
    String shop;
    String moduleSlug;
    String status;
    String stringDelivery;
    Long dateTime;
    String shopObject;
    String moduleObject;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getStringDelivery() {
        return stringDelivery;
    }

    public void setStringDelivery(String stringDelivery) {
        this.stringDelivery = stringDelivery;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getModuleSlug() {
        return moduleSlug;
    }

    public void setModuleSlug(String moduleSlug) {
        this.moduleSlug = moduleSlug;
    }

    public String getShopObject() {
        return shopObject;
    }

    public void setShopObject(String shop) {
        this.shopObject = shop;
    }

    public String getModuleObject() {
        return moduleObject;
    }

    public void setModuleObject(String moduleObject) {
        this.moduleObject = moduleObject;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", ref='" + ref + '\'' +
                ", city='" + city + '\'' +
                ", shop='" + shop + '\'' +
                ", moduleSlug='" + moduleSlug + '\'' +
                ", status='" + status + '\'' +
                ", stringDelivery='" + stringDelivery + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}

