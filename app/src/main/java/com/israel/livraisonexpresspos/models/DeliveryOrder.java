package com.israel.livraisonexpresspos.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class DeliveryOrder implements Parcelable {
    private String module;
    private String description;
    private int montant_livraison;
    private int montant_total;
    private int magasin_id;
    private String code_promo;
    private String commentaire;
    private List<Cart> liste_articles;

    public DeliveryOrder() {
    }


    protected DeliveryOrder(Parcel in) {
        module = in.readString();
        description = in.readString();
        montant_livraison = in.readInt();
        montant_total = in.readInt();
        magasin_id = in.readInt();
        code_promo = in.readString();
        commentaire = in.readString();
        liste_articles = in.createTypedArrayList(Cart.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(module);
        dest.writeString(description);
        dest.writeInt(montant_livraison);
        dest.writeInt(montant_total);
        dest.writeInt(magasin_id);
        dest.writeString(code_promo);
        dest.writeString(commentaire);
        dest.writeTypedList(liste_articles);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeliveryOrder> CREATOR = new Creator<DeliveryOrder>() {
        @Override
        public DeliveryOrder createFromParcel(Parcel in) {
            return new DeliveryOrder(in);
        }

        @Override
        public DeliveryOrder[] newArray(int size) {
            return new DeliveryOrder[size];
        }
    };

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMontant_livraison() {
        return montant_livraison;
    }

    public void setMontant_livraison(int montant_livraison) {
        this.montant_livraison = montant_livraison;
    }

    public int getMontant_total() {
        return montant_total;
    }

    public void setMontant_total(int montant_total) {
        this.montant_total = montant_total;
    }

    public int getMagasin_id() {
        return magasin_id;
    }

    public void setMagasin_id(int magasin_id) {
        this.magasin_id = magasin_id;
    }

    public String getCode_promo() {
        return code_promo;
    }

    public void setCode_promo(String code_promo) {
        this.code_promo = code_promo;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public List<Cart> getListe_articles() {
        return liste_articles;
    }

    public void setListe_articles(List<Cart> liste_articles) {
        this.liste_articles = liste_articles;
    }

    @Override
    public String toString() {
        return "DeliveryOrder{" +
                "module='" + module + '\'' +
                ", description='" + description + '\'' +
                ", montant_livraison=" + montant_livraison +
                ", montant_total=" + montant_total +
                ", magasin_id=" + magasin_id +
                ", code_promo='" + code_promo + '\'' +
                ", commentaire='" + commentaire + '\'' +
                ", liste_articles=" + liste_articles +
                '}';
    }
}
