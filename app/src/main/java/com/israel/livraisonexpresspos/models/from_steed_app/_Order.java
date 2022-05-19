package com.israel.livraisonexpresspos.models.from_steed_app;

import android.os.Parcel;
import android.os.Parcelable;

import com.israel.livraisonexpresspos.models.Cart;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class _Order implements Serializable, Parcelable {
    private String module;
    private String description;
    private int montant_livraison;
    private int montant_achat;
    private int magasin_id;
    private String code_promo;
    private String commentaire;
    private String magasin;
    private ArrayList<Cart> liste_articles;
    public _Order() {
    }


    protected _Order(Parcel in) {
        module = in.readString();
        description = in.readString();
        montant_livraison = in.readInt();
        montant_achat = in.readInt();
        magasin_id = in.readInt();
        code_promo = in.readString();
        commentaire = in.readString();
        magasin = in.readString();
        liste_articles = in.createTypedArrayList(Cart.CREATOR);
    }

    public static final Creator<_Order> CREATOR = new Creator<_Order>() {
        @Override
        public _Order createFromParcel(Parcel in) {
            return new _Order(in);
        }

        @Override
        public _Order[] newArray(int size) {
            return new _Order[size];
        }
    };

    public ArrayList<Cart> getliste_articles() {
        return liste_articles;
    }

    public void setliste_articles(ArrayList<Cart> liste_articles) {
        this.liste_articles = liste_articles;
    }

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

    public int getMagasin_id() {
        return magasin_id;
    }

    public void setMagasin_id(int magasin_id) {
        this.magasin_id = magasin_id;
    }

    public int getMontant_achat() {
        return montant_achat;
    }

    public void setMontant_achat(int montant_achat) {
        this.montant_achat = montant_achat;
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

    public String getMagasin() {
        return magasin;
    }

    public void setMagasin(String magasin) {
        this.magasin = magasin;
    }

    public Map<String, Object> toMapp() {
        Map<String, Object> mapper = new HashMap<>();
        mapper.put("module", getModule());
        mapper.put("liste_articles", getliste_articles());
        mapper.put("description", getDescription());
        mapper.put("montant_livraison", getMontant_livraison());
        mapper.put("montant_achat", getMontant_achat());
        mapper.put("code_promo", getCode_promo());
        mapper.put("commentaire", getCommentaire());
        return mapper;
    }

    public Map<String, Object> toFireStoreMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("module", getModule());
        map.put("liste_articles", getliste_articles());
        map.put("description", getDescription());
        map.put("montant_livraison", getMontant_livraison());
        map.put("montant_achat", getMontant_achat());
        map.put("code_promo", getCode_promo());
        map.put("commentaire", getCommentaire());
        map.put("liste_aticles",this.getliste_articles());
        return map;
    }

    public JSONObject toJSON() {

        return new JSONObject(this.toMapp());
    }

    @Override
    public String toString() {
        return "_Order{" +
                "module='" + module + '\'' +
                ", description='" + description + '\'' +
                ", montant_livraison=" + montant_livraison +
                ", montant_achat=" + montant_achat +
                ", magasin_id=" + magasin_id +
                ", code_promo='" + code_promo + '\'' +
                ", commentaire='" + commentaire + '\'' +
                ", liste_articles=" + liste_articles +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(module);
        dest.writeString(description);
        dest.writeInt(montant_livraison);
        dest.writeInt(montant_achat);
        dest.writeInt(magasin_id);
        dest.writeString(code_promo);
        dest.writeString(commentaire);
        dest.writeString(magasin);
        dest.writeTypedList(liste_articles);
    }
}
