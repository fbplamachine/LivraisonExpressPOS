package com.israel.livraisonexpresspos.models.from_steed_app;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class OrderExtras implements Parcelable, Serializable {
    private String facture_download;
    private String facture_link;
    private String commandes_achats;
    private String produits;
    private String facture_achats_download;
    private String facture_achats_link;

    public OrderExtras() {
    }


    protected OrderExtras(Parcel in) {
        facture_download = in.readString();
        facture_link = in.readString();
        commandes_achats = in.readString();
        produits = in.readString();
        facture_achats_download = in.readString();
        facture_achats_link = in.readString();
    }

    public static final Creator<OrderExtras> CREATOR = new Creator<OrderExtras>() {
        @Override
        public OrderExtras createFromParcel(Parcel in) {
            return new OrderExtras(in);
        }

        @Override
        public OrderExtras[] newArray(int size) {
            return new OrderExtras[size];
        }
    };

    public String getFacture_download() {
        return facture_download;
    }

    public void setFacture_download(String facture_download) {
        this.facture_download = facture_download;
    }

    public String getFacture_link() {
        return facture_link;
    }

    public void setFacture_link(String facture_link) {
        this.facture_link = facture_link;
    }

    public String getCommandes_achats() {
        return commandes_achats;
    }

    public void setCommandes_achats(String commandes_achats) {
        this.commandes_achats = commandes_achats;
    }

    public String getProduits() {
        return produits;
    }

    public void setProduits(String produits) {
        this.produits = produits;
    }

    public String getFacture_achats_download() {
        return facture_achats_download;
    }

    public void setFacture_achats_download(String facture_achats_download) {
        this.facture_achats_download = facture_achats_download;
    }

    public String getFacture_achats_link() {
        return facture_achats_link;
    }

    public void setFacture_achats_link(String facture_achats_link) {
        this.facture_achats_link = facture_achats_link;
    }

    @Override
    public String toString() {
        return "OrderExtras{" +
                "facture_download='" + facture_download + '\'' +
                ", facture_link='" + facture_link + '\'' +
                ", commandes_achats='" + commandes_achats + '\'' +
                ", produits='" + produits + '\'' +
                ", facture_achats_download='" + facture_achats_download + '\'' +
                ", facture_achats_link='" + facture_achats_link + '\'' +
                '}';
    }

    public Map<String, Object> toMapp() {
        Map<String, Object> map = new HashMap<>();
        map.put("facture_download", getFacture_download());
        map.put("facture_link", getFacture_link());
        map.put("commandes_achats", getCommandes_achats());
        map.put("produits", getProduits());
        map.put("facture_achats_download", getFacture_achats_download());
        map.put("facture_achats_link", getFacture_achats_link());
        return map;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(facture_download);
        dest.writeString(facture_link);
        dest.writeString(commandes_achats);
        dest.writeString(produits);
        dest.writeString(facture_achats_download);
        dest.writeString(facture_achats_link);
    }
}
