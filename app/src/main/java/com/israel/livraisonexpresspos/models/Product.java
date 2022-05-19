package com.israel.livraisonexpresspos.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private int id;
    private int magasin_id;
    private int categorie_id;
    private int prix_unitaire;
    private int prix_total;
    private int quantite_dispo;
    private int temps_preparation;
    private String libelle;
    private String image;
    private String detail;

    public Product(){}

    protected Product(Parcel in) {
        id = in.readInt();
        magasin_id = in.readInt();
        categorie_id = in.readInt();
        prix_unitaire = in.readInt();
        prix_total = in.readInt();
        quantite_dispo = in.readInt();
        temps_preparation = in.readInt();
        libelle = in.readString();
        image = in.readString();
        detail = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(magasin_id);
        dest.writeInt(categorie_id);
        dest.writeInt(prix_unitaire);
        dest.writeInt(prix_total);
        dest.writeInt(quantite_dispo);
        dest.writeInt(temps_preparation);
        dest.writeString(libelle);
        dest.writeString(image);
        dest.writeString(detail);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMagasin_id() {
        return magasin_id;
    }

    public void setMagasin_id(int magasin_id) {
        this.magasin_id = magasin_id;
    }

    public int getCategorie_id() {
        return categorie_id;
    }

    public void setCategorie_id(int categorie_id) {
        this.categorie_id = categorie_id;
    }

    public int getPrix_unitaire() {
        return prix_unitaire;
    }

    public void setPrix_unitaire(int prix_unitaire) {
        this.prix_unitaire = prix_unitaire;
    }

    public int getPrix_total() {
        return prix_total;
    }

    public void setPrix_total(int prix_total) {
        this.prix_total = prix_total;
    }

    public int getQuantite_dispo() {
        return quantite_dispo;
    }

    public void setQuantite_dispo(int quantite_dispo) {
        this.quantite_dispo = quantite_dispo;
    }

    public int getTemps_preparation() {
        return temps_preparation;
    }

    public void setTemps_preparation(int temps_preparation) {
        this.temps_preparation = temps_preparation;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
