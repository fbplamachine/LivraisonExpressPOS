package com.israel.livraisonexpresspos.models.inventory_management_models;

public class Product {
    private int id;
    private int mouvement_id;
    private int site_possession_id;
    private int qty;
    private Posession possession;

    public int getId() {
        return  id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMouvement_id() {
        return mouvement_id;
    }

    public void setMouvement_id(int mouvement_id) {
        this.mouvement_id = mouvement_id;
    }

    public int getSite_possession_id() {
        return site_possession_id;
    }

    public void setSite_possession_id(int site_possession_id) {
        this.site_possession_id = site_possession_id;
    }

    public String getQty() {
        return String.valueOf(qty);
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Posession getPossession() {
        return possession;
    }

    public void setPossession(Posession possession) {
        this.possession = possession;
    }


    public class Posession {
        private String nom_produit;
        private String detail_produit;
        private String nom_magasin;

        public String getNom_magasin() {
            return nom_magasin;
        }

        public void setNom_magasin(String nom_magasin) {
            this.nom_magasin = nom_magasin;
        }

        public String getDetail_produit() {
            return detail_produit;
        }

        public void setDetail_produit(String detail_produit) {
            this.detail_produit = detail_produit;
        }

        public String getNom_produit() {
            return nom_produit;
        }

        public void setNom_produit(String nom_produit) {
            this.nom_produit = nom_produit;
        }
    }
}
