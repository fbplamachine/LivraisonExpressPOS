package com.israel.livraisonexpresspos.models;

import java.util.ArrayList;
import java.util.List;

public class Module{
    private int id;
    private String libelle;
    private String slug;
    private String heure_ouverture;
    private String heure_fermeture;
    private String image;
    private String module_color;
    private int is_active;
    private int is_open;
    private boolean is_active_in_city;
    private List<Shop> shops;
    private ArrayList<String> available_in_cities;


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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getHeure_ouverture() {
        return heure_ouverture;
    }

    public void setHeure_ouverture(String heure_ouverture) {
        this.heure_ouverture = heure_ouverture;
    }

    public String getHeure_fermeture() {
        return heure_fermeture;
    }

    public void setHeure_fermeture(String heure_fermeture) {
        this.heure_fermeture = heure_fermeture;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getModule_color() {
        return module_color;
    }

    public void setModule_color(String module_color) {
        this.module_color = module_color;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public int getIs_open() {
        return is_open;
    }

    public void setIs_open(int is_open) {
        this.is_open = is_open;
    }

    public boolean isIs_active_in_city() {
        return is_active_in_city;
    }

    public void setIs_active_in_city(boolean is_active_in_city) {
        this.is_active_in_city = is_active_in_city;
    }

    public List<Shop> getShops() {
        return shops;
    }

    public void setShops(ArrayList<Shop> shops) {
        this.shops = shops;
    }

    public ArrayList<String> getAvailable_in_cities() {
        return available_in_cities;
    }

    public void setAvailable_in_cities(ArrayList<String> available_in_cities) {
        this.available_in_cities = available_in_cities;
    }
}
