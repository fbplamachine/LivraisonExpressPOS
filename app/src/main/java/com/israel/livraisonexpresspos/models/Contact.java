package com.israel.livraisonexpresspos.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Ignore;

import java.util.ArrayList;
import java.util.List;

public class Contact implements Parcelable {
    private String id;
    private String fullname;
    private String email = "";
    private String telephone = "";
    private String telephone_alt;
    private String note_interne;
    private String provider_id;
    private String provider_name;
    private String modules;
    private boolean synced = true;
    @Ignore
    private List<Address> adresses = new ArrayList<>();

    public Contact() {
    }


    protected Contact(Parcel in) {
        id = in.readString();
        fullname = in.readString();
        email = in.readString();
        telephone = in.readString();
        telephone_alt = in.readString();
        note_interne = in.readString();
        provider_id = in.readString();
        provider_name = in.readString();
        modules = in.readString();
        synced = in.readByte() != 0;
        adresses = in.createTypedArrayList(Address.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(fullname);
        dest.writeString(email);
        dest.writeString(telephone);
        dest.writeString(telephone_alt);
        dest.writeString(note_interne);
        dest.writeString(provider_id);
        dest.writeString(provider_name);
        dest.writeString(modules);
        dest.writeByte((byte) (synced ? 1 : 0));
        dest.writeTypedList(adresses);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Address> getAdresses() {
        return adresses;
    }

    public void setAdresses(List<Address> adresses) {
        this.adresses = adresses;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getNote_interne() {
        return note_interne;
    }

    public void setNote_interne(String note_interne) {
        this.note_interne = note_interne;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public String getModules() {
        return modules;
    }

    public void setModules(String modules) {
        this.modules = modules;
    }

    @Override
    public String toString() {
        return "Contact{" +
                ", id='" + id + '\'' +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", telephone_alt='" + telephone_alt + '\'' +
                ", note_interne='" + note_interne + '\'' +
                ", provider_id='" + provider_id + '\'' +
                ", provider_name='" + provider_name + '\'' +
                ", module='" + modules + '\'' +
                ", synced=" + synced +
                ", adresses=" + adresses +
                '}';
    }

    public static Contact userToContact(User user){
        Contact contact = new Contact();
        contact.setId(String.valueOf(user.getId()));
        contact.setEmail(user.getEmail());
        contact.setTelephone(user.getTelephone());
        contact.setTelephone_alt(user.getTelephone_alt());
        contact.setProvider_id(user.getProvider_id());
        contact.setProvider_name(user.getProvider_name());
        return contact;
    }
}
