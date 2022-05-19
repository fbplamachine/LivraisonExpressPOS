package com.israel.livraisonexpresspos.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductStatistic implements Parcelable {
    private String magasin;
    private String achat;
    private String quantite;
    private String produit;
    private int nb_produits;

    protected ProductStatistic(Parcel in) {
        magasin = in.readString();
        achat = in.readString();
        quantite = in.readString();
        produit = in.readString();
        nb_produits = in.readInt();
    }

    public static final Creator<ProductStatistic> CREATOR = new Creator<ProductStatistic>() {
        @Override
        public ProductStatistic createFromParcel(Parcel in) {
            return new ProductStatistic(in);
        }

        @Override
        public ProductStatistic[] newArray(int size) {
            return new ProductStatistic[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(magasin);
        dest.writeString(achat);
        dest.writeString(quantite);
        dest.writeString(produit);
        dest.writeInt(nb_produits);
    }

    public String getMagasin() {
        return magasin;
    }

    public void setMagasin(String magasin) {
        this.magasin = magasin;
    }

    public String getAchat() {
        return achat;
    }

    public void setAchat(String achat) {
        this.achat = achat;
    }

    public String getQuantite() {
        return quantite;
    }

    public void setQuantite(String quantite) {
        this.quantite = quantite;
    }

    public String getProduit() {
        return produit;
    }

    public void setProduit(String produit) {
        this.produit = produit;
    }

    public int getNb_produits() {
        return nb_produits;
    }

    public void setNb_produits(int nb_produits) {
        this.nb_produits = nb_produits;
    }

    @Override
    public String toString() {
        return "ProductStatistic{" +
                "magasin='" + magasin + '\'' +
                ", achat='" + achat + '\'' +
                ", quantite='" + quantite + '\'' +
                ", produit='" + produit + '\'' +
                ", nb_produits=" + nb_produits +
                '}';
    }
}
