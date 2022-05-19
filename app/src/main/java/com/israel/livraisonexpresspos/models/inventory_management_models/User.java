package com.israel.livraisonexpresspos.models.inventory_management_models;

public class User {
      private int id;
      private String fullname;
      private String username;
      private String email;
      private String telephone;
      private String telephone_alt;

      public int getId() {
            return id;
      }

      public void setId(int id) {
            this.id = id;
      }

      public String getFullname() {
            return fullname;
      }

      public void setFullname(String fullname) {
            this.fullname = fullname;
      }

      public String getUsername() {
            return username;
      }

      public void setUsername(String username) {
            this.username = username;
      }

      public String getEmail() {
            return email;
      }

      public void setEmail(String email) {
            this.email = email;
      }

      public String getTelephone() {
            return telephone;
      }

      public void setTelephone(String telephone) {
            this.telephone = telephone;
      }

      public String getTelephone_alt() {
            return telephone_alt;
      }

      public void setTelephone_alt(String telephone_alt) {
            this.telephone_alt = telephone_alt;
      }
}
