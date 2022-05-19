package com.israel.livraisonexpresspos.models.from_steed_app;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Options implements Serializable, Parcelable {
    private int id;
    private String libelle;
    private int prix;
    private int quantite;

    public Options() {
    }

    public Options(String libelle) {
        this.libelle = libelle;
    }


    protected Options(Parcel in) {
        id = in.readInt();
        libelle = in.readString();
        prix = in.readInt();
        quantite = in.readInt();
    }

    public static final Creator<Options> CREATOR = new Creator<Options>() {
        @Override
        public Options createFromParcel(Parcel in) {
            return new Options(in);
        }

        @Override
        public Options[] newArray(int size) {
            return new Options[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }








    public Map<String, Object> toMapp() {
        Map<String, Object> mapper = new HashMap<>();
        mapper.put("id", getId());
        mapper.put("libelle", getLibelle());
        mapper.put("prix", getPrix());
        return mapper;
    }

    public JSONObject toJSON() {

        return new JSONObject(this.toMapp());
    }

    @Override
    public String toString() {
        return "Options{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                ", prix=" + prix +
                ", quantite=" + quantite +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(libelle);
        dest.writeInt(prix);
        dest.writeInt(quantite);
    }
}
