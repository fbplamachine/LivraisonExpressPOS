package com.israel.livraisonexpresspos.models.from_steed_app;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Article implements Serializable, Parcelable {
    private int id;
    private int magasin_id;
    private int categorie_id;
    private int souscategorie_id;
    private String libelle;
    private String description;
    private String taille;
    private int prix_unitaire;
    private int prix_total;
    private int montant_soustotal;
    private Integer quantite;
    private ArrayList<Attributes> attributes;
    public Article() {
    }


    protected Article(Parcel in) {
        id = in.readInt();
        magasin_id = in.readInt();
        categorie_id = in.readInt();
        souscategorie_id = in.readInt();
        libelle = in.readString();
        description = in.readString();
        taille = in.readString();
        prix_unitaire = in.readInt();
        prix_total = in.readInt();
        montant_soustotal = in.readInt();
        if (in.readByte() == 0) {
            quantite = null;
        } else {
            quantite = in.readInt();
        }
        attributes = in.createTypedArrayList(Attributes.CREATOR);
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
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

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMagasin_id() {
        return magasin_id;
    }

    public void setMagasin_id(Integer magasin_id) {
        this.magasin_id = magasin_id;
    }

    public Integer getCategorie_id() {
        return categorie_id;
    }

    public void setCategorie_id(Integer categorie_id) {
        this.categorie_id = categorie_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Attributes> getattributs() {
        return attributes;
    }

    public void setattributs(ArrayList<Attributes> attributs) {
        this.attributes = attributs;
    }

    public Integer getSouscategorie_id() {
        return souscategorie_id;
    }

    public void setSouscategorie_id(Integer souscategorie_id) {
        this.souscategorie_id = souscategorie_id;
    }

    public String getTaille() {
        return taille;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public int getMontant_soustotal() {
        return montant_soustotal;
    }

    public void setMontant_soustotal(int montant_soustotal) {
        this.montant_soustotal = montant_soustotal;
    }


    public Map<String, Object> toMapp (){
        Map<String, Object> mapper = new HashMap<>();
        mapper.put("magasin_id" ,getMagasin_id());
        mapper.put("categorie_id"  ,getCategorie_id());
        mapper.put("souscategorie_id" ,getSouscategorie_id());
        mapper.put("libelle"   ,getLibelle());
        mapper.put("description"  ,getDescription());
        mapper.put("taille"  ,getTaille());
        mapper.put("quantite"  ,getQuantite());
        mapper.put("prix_unitaire"  ,getPrix_unitaire());
        mapper.put("montant_soustotal"  ,getMontant_soustotal());
        mapper.put("attributs"  ,getattributs());
        return mapper;
    }

    public JSONObject toJSON (){

        return new JSONObject(this.toMapp());
    }




    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", magasin_id=" + magasin_id +
                ", categorie_id=" + categorie_id +
                ", souscategorie_id=" + souscategorie_id +
                ", libelle='" + libelle + '\'' +
                ", description='" + description + '\'' +
                ", taille='" + taille + '\'' +
                ", prix_unitaire=" + prix_unitaire +
                ", prix_total=" + prix_total +
                ", montant_soustotal=" + montant_soustotal +
                ", quantite=" + quantite +
                ", attributes=" + attributes +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(magasin_id);
        dest.writeInt(categorie_id);
        dest.writeInt(souscategorie_id);
        dest.writeString(libelle);
        dest.writeString(description);
        dest.writeString(taille);
        dest.writeInt(prix_unitaire);
        dest.writeInt(prix_total);
        dest.writeInt(montant_soustotal);
        if (quantite == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(quantite);
        }
        dest.writeTypedList(attributes);
    }
}
