package com.israel.livraisonexpresspos.models.from_steed_app;

import android.os.Parcel;
import android.os.Parcelable;

public class Cashier implements Parcelable {
    private int id;
    private String name;
    private String agent;
    private String comment;

    public Cashier() {
    }

    protected Cashier(Parcel in) {
        id = in.readInt();
        name = in.readString();
        agent = in.readString();
        comment = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(agent);
        dest.writeString(comment);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Cashier> CREATOR = new Creator<Cashier>() {
        @Override
        public Cashier createFromParcel(Parcel in) {
            return new Cashier(in);
        }

        @Override
        public Cashier[] newArray(int size) {
            return new Cashier[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Cashier{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", agent='" + agent + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
