package com.israel.livraisonexpresspos.models.from_steed_app;

import android.os.Parcel;
import android.os.Parcelable;

import com.israel.livraisonexpresspos.models.Address;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Client implements Serializable, Parcelable {
    private int id;
    private ArrayList<Address> adresses = new ArrayList<>();
    private String fullname;
    private String name;
    private String surname;
    private String email;
    private String telephone;
    private String telephone_alt;
    private String note_interne;
    private String provider_name;

    public Client() {
    }


    protected Client(Parcel in) {
        id = in.readInt();
        adresses = in.createTypedArrayList(Address.CREATOR);
        fullname = in.readString();
        name = in.readString();
        surname = in.readString();
        email = in.readString();
        telephone = in.readString();
        telephone_alt = in.readString();
        note_interne = in.readString();
        provider_name = in.readString();
    }

    public static final Creator<Client> CREATOR = new Creator<Client>() {
        @Override
        public Client createFromParcel(Parcel in) {
            return new Client(in);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
        }
    };

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public ArrayList<Address> getAdresses() {
        return adresses;
    }

    public void setAdresses(ArrayList<Address> adresses) {
        this.adresses = adresses;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public Map<String, Object> toMapp (){
        Map<String, Object> mapper = new HashMap<>();
        mapper.put("id",getId());
        mapper.put("fullname",getFullname());
        mapper.put( "name",getName());
        mapper.put("surname",getSurname());
        mapper.put("telephone",getTelephone());
        mapper.put("telephone_alt",getTelephone_alt());
        mapper.put("email",getEmail());
        mapper.put("note_interne",getNote_interne());
        mapper.put("adresses",getAdresses());
        return mapper;
    }

    public Map<String, Object> toFireBaseMap (){
        Map<String, Object> map = new HashMap<>();
        map.put("id",getId());
        map.put("fullname",getFullname());
        map.put( "name",getName());
        map.put("surname",getSurname());
        map.put("telephone",getTelephone());
        map.put("telephone_alt",getTelephone_alt());
        map.put("email",getEmail());
        map.put("note_interne",getNote_interne());
        return map;
    }

    public JSONObject toJSON (){
        return new JSONObject(this.toMapp());
    }




    @Override
    public String toString() {
        return "_Client{" +
                "id='" + id + '\'' +
                ", adresses=" + adresses.toString() +
                ", fullname='" + fullname + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", telephone_alt='" + telephone_alt + '\'' +
                ", note_interne='" + note_interne + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeTypedList(adresses);
        dest.writeString(fullname);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(email);
        dest.writeString(telephone);
        dest.writeString(telephone_alt);
        dest.writeString(note_interne);
        dest.writeString(provider_name);
    }
}
