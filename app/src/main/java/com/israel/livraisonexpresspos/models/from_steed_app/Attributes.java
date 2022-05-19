package com.israel.livraisonexpresspos.models.from_steed_app;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Attributes implements Serializable, Parcelable {
    private Integer id = null;
    private String libelle;
    private int required_choices_quota;
    private ArrayList<Options> options = new ArrayList<>();

    public Attributes() {
    }

    public Attributes(String libelle, Integer required_choices_quota, ArrayList<Options> options) {
        this.libelle = libelle;
        this.required_choices_quota = required_choices_quota;
        this.options = options;
    }

    public Attributes(String libelle, Integer required_choices_quota) {
        this.libelle = libelle;
        this.required_choices_quota = required_choices_quota;
    }

    protected Attributes(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        libelle = in.readString();
        required_choices_quota = in.readInt();
        options = in.createTypedArrayList(Options.CREATOR);
    }

    public static final Creator<Attributes> CREATOR = new Creator<Attributes>() {
        @Override
        public Attributes createFromParcel(Parcel in) {
            return new Attributes(in);
        }

        @Override
        public Attributes[] newArray(int size) {
            return new Attributes[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Integer getMax_items() {
        return required_choices_quota;
    }

    public void setMax_items(Integer required_choices_quota) {
        this.required_choices_quota = required_choices_quota;
    }

    public ArrayList<Options> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<Options> options) {
        this.options = options;
    }





   public Map<String, Object> toMapp (){
        Map<String, Object> mapper = new HashMap<>();
        mapper.put("id",getId());
        mapper.put("titre" , getLibelle());
        mapper.put("max_items", getMax_items());
        mapper.put("options",getOptions());
        return mapper;
    }

    public JSONObject toJSON (){
        return new JSONObject(this.toMapp());
    }

    @Override
    public String toString() {
        return "_Attributs{" +
                "id=" + id +
                ", titre='" + libelle + '\'' +
                ", max_items=" + required_choices_quota +
                ", options=" + options +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(libelle);
        dest.writeInt(required_choices_quota);
        dest.writeTypedList(options);
    }
}
