package com.israel.livraisonexpresspos.models.inventory_management_models;

import androidx.annotation.NonNull;

import java.util.List;

public class MoveByShop {

    private long id;
    private String magasin_name;
    private List<MoveByShopProduct> produits;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMagasin_name() {
        return magasin_name;
    }

    public void setMagasin_name(String magasin_name) {
        this.magasin_name = magasin_name;
    }

    public List<MoveByShopProduct> getProduits() {
        return produits;
    }

    public void setProduits(List<MoveByShopProduct> produits) {
        this.produits = produits;
    }

    @NonNull
    @Override
    public String toString() {
        return "MoveByShop{" +
                "id=" + id +
                ", magasin_name='" + magasin_name + '\'' +
                ", produits=" + produits +
                '}';
    }
}
