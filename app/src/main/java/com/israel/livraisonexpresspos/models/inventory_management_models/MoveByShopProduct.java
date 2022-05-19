package com.israel.livraisonexpresspos.models.inventory_management_models;

import androidx.annotation.NonNull;

public class MoveByShopProduct {
    private long id;
    private String nom_produit;
    private int qte_mouvement;
    private String type_mouvement;
    private String site_depart;
    private String site_arrivee;
    private String date_creation;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom_produit() {
        return qte_mouvement+" x "+nom_produit;
    }

    public void setNom_produit(String nom_produit) {
        this.nom_produit = nom_produit;
    }

    public int getQte_mouvement() {
        return qte_mouvement;
    }

    public void setQte_mouvement(int qte_mouvement) {
        this.qte_mouvement = qte_mouvement;
    }

    public String getType_mouvement() {
        return type_mouvement;
    }

    public void setType_mouvement(String type_mouvement) {
        this.type_mouvement = type_mouvement;
    }

    public String getSite_depart() {
        return site_depart;
    }

    public void setSite_depart(String site_depart) {
        this.site_depart = site_depart;
    }

    public String getSite_arrivee() {
        return site_arrivee;
    }

    public void setSite_arrivee(String site_arrivee) {
        this.site_arrivee = site_arrivee;
    }

    public String getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(String date_creation) {
        this.date_creation = date_creation;
    }

    @NonNull
    @Override
    public String toString() {
        return "MoveByShopProduct{" +
                "id=" + id +
                ", nom_produit='" + nom_produit + '\'' +
                ", qte_mouvement=" + qte_mouvement +
                ", type_mouvement='" + type_mouvement + '\'' +
                ", site_depart='" + site_depart + '\'' +
                ", site_arrivee='" + site_arrivee + '\'' +
                ", date_creation='" + date_creation + '\'' +
                '}';
    }
}
