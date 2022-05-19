package com.israel.livraisonexpresspos.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Quarter implements Parcelable {
    private int id;
    private String libelle;
    private String ville;
    private int city_id;
    private int is_active;
    public static List<Quarter> DOUALA = new ArrayList<>();
    public static List<Quarter> YAOUNDE = new ArrayList<>();

    protected Quarter(Parcel in) {
        id = in.readInt();
        libelle = in.readString();
        ville = in.readString();
        city_id = in.readInt();
        is_active = in.readInt();
    }

    public static final Creator<Quarter> CREATOR = new Creator<Quarter>() {
        @Override
        public Quarter createFromParcel(Parcel in) {
            return new Quarter(in);
        }

        @Override
        public Quarter[] newArray(int size) {
            return new Quarter[size];
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

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(libelle);
        dest.writeString(ville);
        dest.writeInt(city_id);
        dest.writeInt(is_active);
    }
}
