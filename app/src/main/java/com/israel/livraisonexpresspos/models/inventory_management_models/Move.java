package com.israel.livraisonexpresspos.models.inventory_management_models;

public class Move {
    private int id;
    private int site_depart_id;
    private int site_arrivee_id;
    private int gestionnaire_id;
    private String type_action;
    private int status;
    private String date_creation;
    private String date_update;
    private String created_at;
    private String updated_at;
    private int nbre_details;
    private User gestionnaire;
    private Site site_depart;
    private Site site_arrivee;

    private boolean isExpanded;

    public void setIsExpanded(boolean isExpanded){
        this.isExpanded = isExpanded;
    }

    public boolean isExpanded(){
        return this.isExpanded;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSite_depart_id() {
        return site_depart_id;
    }

    public void setSite_depart_id(int site_depart_id) {
        this.site_depart_id = site_depart_id;
    }

    public int getSite_arrivee_id() {
        return site_arrivee_id;
    }

    public void setSite_arrivee_id(int site_arrivee_id) {
        this.site_arrivee_id = site_arrivee_id;
    }

    public int getGestionnaire_id() {
        return gestionnaire_id;
    }

    public void setGestionnaire_id(int gestionnaire_id) {
        this.gestionnaire_id = gestionnaire_id;
    }

    public String getType_action() {
        return type_action;
    }

    public void setType_action(String type_action) {
        this.type_action = type_action;
    }

    public String getStatus() {
        if (status == 0) return "Non reçu ";
        return "Reçu";
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(String date_creation) {
        this.date_creation = date_creation;
    }

    public String getDate_update() {
        return date_update;
    }

    public void setDate_update(String date_update) {
        this.date_update = date_update;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getNbre_details() {
        return nbre_details;
    }

    public void setNbre_details(int nbre_details) {
        this.nbre_details = nbre_details;
    }

    public Site getSite_depart() {
        return site_depart;
    }

    public void setSite_depart(Site site_depart) {
        this.site_depart = site_depart;
    }

    public Site getSite_arrivee() {
        return site_arrivee;
    }

    public void setSite_arrivee(Site site_arrivee) {
        this.site_arrivee = site_arrivee;
    }

    public User getGestionnaire() {
        return gestionnaire;
    }

    public void setGestionnaire(User gestionnaire) {
        this.gestionnaire = gestionnaire;
    }
}
