package com.israel.livraisonexpresspos.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Shop implements Parcelable {
    private int id;
    private int etablissement_id;
    private int partenaire_id;
    private int module_id;
    private String nom;
    private String slug;
    private int contact_id;
    private int ville_id;
    private int adresse_id;
    private String image;
    private Horaire horaires;
    private Contact contact;
    private String tags;
    private String description;
    private int shipping_preparation_time;
    private Address adresse;

    protected Shop(Parcel in) {
        id = in.readInt();
        etablissement_id = in.readInt();
        partenaire_id = in.readInt();
        module_id = in.readInt();
        nom = in.readString();
        slug = in.readString();
        contact_id = in.readInt();
        ville_id = in.readInt();
        adresse_id = in.readInt();
        image = in.readString();
        horaires = in.readParcelable(Horaire.class.getClassLoader());
        contact = in.readParcelable(Contact.class.getClassLoader());
        tags = in.readString();
        description = in.readString();
        shipping_preparation_time = in.readInt();
        adresse = in.readParcelable(Address.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(etablissement_id);
        dest.writeInt(partenaire_id);
        dest.writeInt(module_id);
        dest.writeString(nom);
        dest.writeString(slug);
        dest.writeInt(contact_id);
        dest.writeInt(ville_id);
        dest.writeInt(adresse_id);
        dest.writeString(image);
        dest.writeParcelable(horaires, flags);
        dest.writeParcelable(contact, flags);
        dest.writeString(tags);
        dest.writeString(description);
        dest.writeInt(shipping_preparation_time);
        dest.writeParcelable(adresse, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Shop> CREATOR = new Creator<Shop>() {
        @Override
        public Shop createFromParcel(Parcel in) {
            return new Shop(in);
        }

        @Override
        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEtablissement_id() {
        return etablissement_id;
    }

    public void setEtablissement_id(int etablissement_id) {
        this.etablissement_id = etablissement_id;
    }

    public int getPartenaire_id() {
        return partenaire_id;
    }

    public void setPartenaire_id(int partenaire_id) {
        this.partenaire_id = partenaire_id;
    }

    public int getModule_id() {
        return module_id;
    }

    public void setModule_id(int module_id) {
        this.module_id = module_id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    public int getVille_id() {
        return ville_id;
    }

    public void setVille_id(int ville_id) {
        this.ville_id = ville_id;
    }

    public int getAdresse_id() {
        return adresse_id;
    }

    public void setAdresse_id(int adresse_id) {
        this.adresse_id = adresse_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Horaire getHoraires() {
        return horaires;
    }

    public void setHoraires(Horaire horaires) {
        this.horaires = horaires;
    }

    public Contact getContact() {
        List<Address> addresses = new ArrayList<>();
        addresses.add(adresse);
        contact.setAdresses(addresses);
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getShipping_preparation_time() {
        return shipping_preparation_time;
    }

    public void setShipping_preparation_time(int shipping_preparation_time) {
        this.shipping_preparation_time = shipping_preparation_time;
    }

    public Address getAdresse() {
        return adresse;
    }

    public void setAdresse(Address adresse) {
        this.adresse = adresse;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", etablissement_id=" + etablissement_id +
                ", partenaire_id=" + partenaire_id +
                ", module_id=" + module_id +
                ", nom='" + nom + '\'' +
                ", slug='" + slug + '\'' +
                ", contact_id=" + contact_id +
                ", ville_id=" + ville_id +
                ", adresse_id=" + adresse_id +
                ", image='" + image + '\'' +
                ", horaires=" + horaires +
                ", contact=" + contact +
                ", tags='" + tags + '\'' +
                ", description='" + description + '\'' +
                ", shipping_preparation_time=" + shipping_preparation_time +
                ", adresse=" + adresse +
                '}';
    }
}
