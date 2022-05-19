package com.israel.livraisonexpresspos.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.room.Ignore;

public class Address implements Parcelable {
    private Integer id;
    private Integer client_id;
    private String latitude;
    private String longitude;
    @Ignore
    private double lat;
    @Ignore
    private double lon;
    private String nom;
    private String surnom;
    private String titre;
    private String description;
    private Integer provider_id;
    private String provider_name;
    private String quartier;
    private String latitude_longitude;
    private String ville;
    private boolean est_favorite;
    private Integer ville_id;
    private Integer zone_id;
    private Integer quartier_id;

    public Address(){}

    protected Address(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            client_id = null;
        } else {
            client_id = in.readInt();
        }
        latitude = in.readString();
        longitude = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
        nom = in.readString();
        surnom = in.readString();
        titre = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            provider_id = null;
        } else {
            provider_id = in.readInt();
        }
        provider_name = in.readString();
        quartier = in.readString();
        latitude_longitude = in.readString();
        ville = in.readString();
        est_favorite = in.readByte() != 0;
        if (in.readByte() == 0) {
            ville_id = null;
        } else {
            ville_id = in.readInt();
        }
        if (in.readByte() == 0) {
            zone_id = null;
        } else {
            zone_id = in.readInt();
        }
        if (in.readByte() == 0) {
            quartier_id = null;
        } else {
            quartier_id = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (client_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(client_id);
        }
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeString(nom);
        dest.writeString(surnom);
        dest.writeString(titre);
        dest.writeString(description);
        if (provider_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(provider_id);
        }
        dest.writeString(provider_name);
        dest.writeString(quartier);
        dest.writeString(latitude_longitude);
        dest.writeString(ville);
        dest.writeByte((byte) (est_favorite ? 1 : 0));
        if (ville_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(ville_id);
        }
        if (zone_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(zone_id);
        }
        if (quartier_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(quartier_id);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getLatitude() {
        if (latitude == null)return "0.0";
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        if (longitude == null)return "0.0";
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSurnom() {
        return surnom;
    }

    public void setSurnom(String surnom) {
        this.surnom = surnom;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(Integer provider_id) {
        this.provider_id = provider_id;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getQuartier() {
        return quartier;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }

    public boolean getEst_favorite() {
        return est_favorite;
    }

    public void setEst_favorite(boolean est_favorite) {
        this.est_favorite = est_favorite;
    }

    public void setLatitude_longitude(String latitude_longitude) {
        this.latitude_longitude = latitude_longitude;
    }

    public String getLatitude_longitude() {
        return latitude_longitude;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Integer getVille_id() {
        return ville_id;
    }

    public void setVille_id(Integer ville_id) {
        this.ville_id = ville_id;
    }

    public Integer getZone_id() {
        return zone_id;
    }

    public void setZone_id(Integer zone_id) {
        this.zone_id = zone_id;
    }

    public Integer getQuartier_id() {
        return quartier_id;
    }

    public void setQuartier_id(Integer quartier_id) {
        this.quartier_id = quartier_id;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        if (TextUtils.isEmpty(latitude)) return 0.0;
        return Double.parseDouble(latitude);
    }

    public double getLon() {
        if (TextUtils.isEmpty(longitude)) return 0.0;
        return Double.parseDouble(longitude);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", client_id=" + client_id +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", nom='" + nom + '\'' +
                ", surnom='" + surnom + '\'' +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", provider_id=" + provider_id +
                ", provider_name='" + provider_name + '\'' +
                ", quartier='" + quartier + '\'' +
                ", latitude_longitude='" + latitude_longitude + '\'' +
                ", ville='" + ville + '\'' +
                ", est_favorite=" + est_favorite +
                ", ville_id=" + ville_id +
                ", zone_id=" + zone_id +
                ", quartier_id=" + quartier_id +
                '}';
    }
}
