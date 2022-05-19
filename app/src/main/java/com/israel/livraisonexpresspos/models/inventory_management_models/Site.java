package com.israel.livraisonexpresspos.models.inventory_management_models;

public class Site {
     private int id;
     private int quartier_id;
     private int ville_id;
     private String name;
     private String type_site;
     private int is_site_reference;
     private int status;
     private String created_at;
     private String updated_at;


     public int getId() {
          return id;
     }

     public void setId(int id) {
          this.id = id;
     }

     public int getQuartier_id() {
          return quartier_id;
     }

     public void setQuartier_id(int quartier_id) {
          this.quartier_id = quartier_id;
     }

     public int getVille_id() {
          return ville_id;
     }

     public void setVille_id(int ville_id) {
          this.ville_id = ville_id;
     }

     public String getName() {
          return name;
     }

     public void setName(String name) {
          this.name = name;
     }

     public String getType_site() {
          return type_site;
     }

     public void setType_site(String type_site) {
          this.type_site = type_site;
     }

     public int getIs_site_reference() {
          return is_site_reference;
     }

     public void setIs_site_reference(int is_site_reference) {
          this.is_site_reference = is_site_reference;
     }

     public int getStatus() {
          return status;
     }

     public void setStatus(int status) {
          this.status = status;
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
}


