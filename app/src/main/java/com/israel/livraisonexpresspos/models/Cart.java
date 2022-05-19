package com.israel.livraisonexpresspos.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "cart_table",
        foreignKeys = @ForeignKey(
                entity = Order.class,
                parentColumns = "id",
                childColumns = "orderId",
                onDelete = CASCADE
        ),
        indices = {@Index(value = {"orderId"}), @Index(value = {"id"}, unique = true)}
)
public class Cart implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String libelle;
    private int prix_unitaire;
    private String montant_total;
    @Ignore
    private String montant_soustotal;
    private int quantite;
    private String image;
    private int product_id;
    private Integer categorie_id;
    @ColumnInfo(name = "attributes")
    private String attribute;
    private int orderId;
    private int magasin_id;
    @Ignore
    private boolean isDeleted, isNewProduct;

    public Cart() {
    }


    protected Cart(Parcel in) {
        id = in.readInt();
        libelle = in.readString();
        prix_unitaire = in.readInt();
        montant_total = in.readString();
        montant_soustotal = in.readString();
        quantite = in.readInt();
        image = in.readString();
        product_id = in.readInt();
        if (in.readByte() == 0) {
            categorie_id = null;
        } else {
            categorie_id = in.readInt();
        }
        attribute = in.readString();
        orderId = in.readInt();
        magasin_id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(libelle);
        dest.writeInt(prix_unitaire);
        dest.writeString(montant_total);
        dest.writeString(montant_soustotal);
        dest.writeInt(quantite);
        dest.writeString(image);
        dest.writeInt(product_id);
        if (categorie_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(categorie_id);
        }
        dest.writeString(attribute);
        dest.writeInt(orderId);
        dest.writeInt(magasin_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };

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

    public int getPrix_unitaire() {
        return prix_unitaire;
    }

    public void setPrix_unitaire(int prix_unitaire) {
        this.prix_unitaire = prix_unitaire;
    }

    public String getMontant_total() {
        return montant_total;
    }

    public int getMontantTotal(){
        if (TextUtils.isEmpty(montant_total))return 0;
        if (montant_total.contains(".")){
            montant_total = montant_total.split("\\.")[0];
        }
        return Integer.parseInt(montant_total);
    }

    public void setMontant_total(String montant_total) {
        this.montant_total = montant_total;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public Integer getCategorie_id() {
        return categorie_id;
    }

    public void setCategorie_id(Integer categorie_id) {
        this.categorie_id = categorie_id;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getMagasin_id() {
        return magasin_id;
    }

    public void setMagasin_id(int magasin_id) {
        this.magasin_id = magasin_id;
    }

    public String getMontant_soustotal() {
        return montant_soustotal;
    }

    public void setMontant_soustotal(String montant_soustotal) {
        this.montant_soustotal = montant_soustotal;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isNewProduct() {
        return isNewProduct;
    }

    public void setNewProduct(boolean newProduct) {
        isNewProduct = newProduct;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", name='" + libelle + '\'' +
                ", unitPrice=" + prix_unitaire +
                ", totalPrice=" + montant_total +
                ", quantity=" + quantite +
                ", image='" + image + '\'' +
                ", productId=" + product_id +
                ", categoryId=" + categorie_id +
                ", attributes='" + attribute + '\'' +
                ", orderId=" + orderId +
                ", shopId=" + magasin_id +
                '}';
    }

    public static List<Cart> toArticles(List<Cart> carts){
        for (Cart c : carts){
            c.setId(c.getProduct_id());
            c.setMontant_soustotal(String.valueOf(c.getMontant_total()));
        }
        return carts;
    }
}
