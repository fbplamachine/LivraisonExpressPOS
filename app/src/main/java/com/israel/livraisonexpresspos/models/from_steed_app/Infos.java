package com.israel.livraisonexpresspos.models.from_steed_app;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Infos implements Serializable, Parcelable {
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
    private List<Integer> coursiers_ids = new ArrayList<>();
    private String coursier_name;
    private String type;
    private int distance;
    private String distance_text;
    private int duration;
    private String duration_text;
    private String contenu;
    private String date_creation;
    private Boolean is_urgent;
    private String badge;

    public Infos() {
    }

    protected Infos(Parcel in) {
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
        in.readList(coursiers_ids, null);
        coursier_name = in.readString();
        type = in.readString();
        distance = in.readInt();
        distance_text = in.readString();
        duration = in.readInt();
        duration_text = in.readString();
        contenu = in.readString();
        date_creation = in.readString();
        byte tmpIs_urgent = in.readByte();
        is_urgent = tmpIs_urgent == 0 ? null : tmpIs_urgent == 1;
        badge = in.readString();
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
        dest.writeList(coursiers_ids);
        dest.writeString(coursier_name);
        dest.writeString(type);
        dest.writeInt(distance);
        dest.writeString(distance_text);
        dest.writeInt(duration);
        dest.writeString(duration_text);
        dest.writeString(contenu);
        dest.writeString(date_creation);
        dest.writeByte((byte) (is_urgent == null ? 0 : is_urgent ? 1 : 2));
        dest.writeString(badge);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Infos> CREATOR = new Creator<Infos>() {
        @Override
        public Infos createFromParcel(Parcel in) {
            return new Infos(in);
        }

        @Override
        public Infos[] newArray(int size) {
            return new Infos[size];
        }
    };

    public Boolean getIs_urgent() {
        return is_urgent;
    }

    public void setIs_urgent(Boolean is_urgent) {
        this.is_urgent = is_urgent;
    }



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

    public String getDate_livraison() {
        return date_livraison;
    }

    public void setDate_livraison(String date_livraison) {
        this.date_livraison = date_livraison;
    }

    public String getDate_chargement() {
        return date_chargement;
    }

    public void setDate_chargement(String date_chargement) {
        this.date_chargement = date_chargement;
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

    public String getVille_livraison() {
        return ville_livraison;
    }

    public void setVille_livraison(String ville_livraison) {
        this.ville_livraison = ville_livraison;
    }

    public String getStatut_human() {
        return statut_human;
    }

    public void setStatut_human(String statut_human) {
        this.statut_human = statut_human;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getDistance_text() {
        return distance_text;
    }

    public void setDistance_text(String distance_text) {
        this.distance_text = distance_text;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
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

    public String getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(String date_creation) {
        this.date_creation = date_creation;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public List<Integer> getCoursiers_ids() {
        return coursiers_ids;
    }

    public void setCoursiers_ids(List<Integer> coursiers_ids) {
        this.coursiers_ids = coursiers_ids;
    }

    public String getCoursier_name() {
        return coursier_name;
    }

    public void setCoursier_name(String coursier_name) {
        this.coursier_name = coursier_name;
    }

    public Map<String, Object> toMapp (){
        Map<String, Object> mapper = new HashMap<>();
        mapper.put("origin" ,getOrigin());
        mapper.put("platform" ,getPlatform());
        mapper.put("ref" , getRef());
        mapper.put("id" , getId());
        mapper.put("date_chargement" ,getDate_chargement());
        mapper.put("date_livraison" ,getDate_livraison());
        mapper.put("heure_livraison" ,getHeure_livraison());
        mapper.put("jour_livraison" ,getJour_livraison());
        mapper.put("ville_livraison" ,getVille_livraison());
        mapper.put("statut_human" , getStatut_human());
        mapper.put("statut" , getStatut());
        mapper.put("type" ,getType());
        mapper.put("distance" ,getDistance());
        mapper.put("distance_text" ,getDistance_text());
        mapper.put("duration" ,getDuration());
        mapper.put("duration_text" ,getDuration_text());
        mapper.put("contenu" ,getContenu());
        mapper.put("date_creation" ,getDate_creation());
        mapper.put("is_urgent" ,getIs_urgent());
        mapper.put("badge" ,getBadge());
        return mapper;
    }

    public JSONObject toJSON (){
        return new JSONObject(this.toMapp());
    }

    @Override
    public String toString() {
        return "Infos{" +
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
                ", is_urgent=" + is_urgent +
                ", badge='" + badge + '\'' +
                '}';
    }

    public void addDeliveryMan(int id){
        boolean add = true;
        for(int coursierId : coursiers_ids){
            if (coursierId == id){
                add = false;
                break;
            }
        }
        if (add){
            coursiers_ids.add(id);
        }
    }
}
