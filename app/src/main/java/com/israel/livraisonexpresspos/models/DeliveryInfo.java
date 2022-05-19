package com.israel.livraisonexpresspos.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class DeliveryInfo implements Parcelable {
    private int id;
    private String ref;
    private String origin;
    private String platform;
    private String date_chargement;
    private String date_livraison;
    private String heure_livraison;
    private String jour_livraison;
    private String ville_livraison;
    private int statut;
    private String statut_human;
    private String type;
    private String distance;
    private String distance_text;
    private String duration;
    private String duration_text;
    private String contenu;
    private String date_creation;
    private List<Integer> coursiers_ids = new ArrayList<>();

    public DeliveryInfo() {
    }

    protected DeliveryInfo(Parcel in) {
        id = in.readInt();
        ref = in.readString();
        origin = in.readString();
        platform = in.readString();
        date_chargement = in.readString();
        date_livraison = in.readString();
        heure_livraison = in.readString();
        jour_livraison = in.readString();
        ville_livraison = in.readString();
        statut = in.readInt();
        statut_human = in.readString();
        type = in.readString();
        distance = in.readString();
        distance_text = in.readString();
        duration = in.readString();
        duration_text = in.readString();
        contenu = in.readString();
        date_creation = in.readString();
        in.readList(coursiers_ids, null);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(ref);
        dest.writeString(origin);
        dest.writeString(platform);
        dest.writeString(date_chargement);
        dest.writeString(date_livraison);
        dest.writeString(heure_livraison);
        dest.writeString(jour_livraison);
        dest.writeString(ville_livraison);
        dest.writeInt(statut);
        dest.writeString(statut_human);
        dest.writeString(type);
        dest.writeString(distance);
        dest.writeString(distance_text);
        dest.writeString(duration);
        dest.writeString(duration_text);
        dest.writeString(contenu);
        dest.writeString(date_creation);
        dest.writeList(coursiers_ids);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeliveryInfo> CREATOR = new Creator<DeliveryInfo>() {
        @Override
        public DeliveryInfo createFromParcel(Parcel in) {
            return new DeliveryInfo(in);
        }

        @Override
        public DeliveryInfo[] newArray(int size) {
            return new DeliveryInfo[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDate_chargement() {
        return date_chargement;
    }

    public void setDate_chargement(String date_chargement) {
        this.date_chargement = date_chargement;
    }

    public String getDate_livraison() {
        return date_livraison;
    }

    public void setDate_livraison(String date_livraison) {
        this.date_livraison = date_livraison;
    }

    public String getHeure_livraison() {
        return heure_livraison;
    }

    public void setHeure_livraison(String heure_livraison) {
        this.heure_livraison = heure_livraison;
    }

    public String getJour_livraison() {
        return jour_livraison;
    }

    public void setJour_livraison(String jour_livraison) {
        this.jour_livraison = jour_livraison;
    }

    public String getVille_livraison() {
        return ville_livraison;
    }

    public void setVille_livraison(String ville_livraison) {
        this.ville_livraison = ville_livraison;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    public String getStatut_human() {
        return statut_human;
    }

    public void setStatut_human(String statut_human) {
        this.statut_human = statut_human;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistance_text() {
        return distance_text;
    }

    public void setDistance_text(String distance_text) {
        this.distance_text = distance_text;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDuration_text() {
        return duration_text;
    }

    public void setDuration_text(String duration_text) {
        this.duration_text = duration_text;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public List<Integer> getCoursiers_ids() {
        return coursiers_ids;
    }

    public void setCoursiers_ids(List<Integer> coursiers_ids) {
        this.coursiers_ids = coursiers_ids;
    }

    public String getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(String date_creation) {
        this.date_creation = date_creation;
    }

    @Override
    public String toString() {
        return "DeliveryInfo{" +
                "id=" + id +
                ", ref='" + ref + '\'' +
                ", origin='" + origin + '\'' +
                ", platform='" + platform + '\'' +
                ", date_chargement='" + date_chargement + '\'' +
                ", date_livraison='" + date_livraison + '\'' +
                ", heure_livraison='" + heure_livraison + '\'' +
                ", jour_livraison='" + jour_livraison + '\'' +
                ", ville_livraison='" + ville_livraison + '\'' +
                ", statut=" + statut +
                ", statut_human='" + statut_human + '\'' +
                ", type='" + type + '\'' +
                ", distance='" + distance + '\'' +
                ", distance_text='" + distance_text + '\'' +
                ", duration='" + duration + '\'' +
                ", duration_text='" + duration_text + '\'' +
                ", contenu='" + contenu + '\'' +
                ", date_creation='" + date_creation + '\'' +
                ", coursiers_ids='" + coursiers_ids.toString() + '\'' +
                '}';
    }
}
