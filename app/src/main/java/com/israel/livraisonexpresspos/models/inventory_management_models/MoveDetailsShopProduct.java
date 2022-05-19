package com.israel.livraisonexpresspos.models.inventory_management_models;

public class MoveDetailsShopProduct {
    private String mouvement_details_id;
    private String product_name;
    private String qty;

    public String getMouvement_details_id() {
        return mouvement_details_id;
    }

    public void setMouvement_details_id(String mouvement_details_id) {
        this.mouvement_details_id = mouvement_details_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getQty() {
        return qty+" x ";
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
