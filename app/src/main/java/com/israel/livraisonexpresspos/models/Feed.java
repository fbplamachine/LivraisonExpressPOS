package com.israel.livraisonexpresspos.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Feed implements Parcelable {
    private int id;
    private String description;
    private int subject_id;
    private String subject_type;
    private int causer_id;
    private String causer_type;
    private String created_at;
    private String updated_at;





    public Feed() {
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public void setSubject_type(String subject_type) {
        this.subject_type = subject_type;
    }

    public void setCauser_id(int causer_id) {
        this.causer_id = causer_id;
    }

    public void setCauser_type(String causer_type) {
        this.causer_type = causer_type;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    //getters
    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public String getSubject_type() {
        return subject_type;
    }

    public int getCauser_id() {
        return causer_id;
    }

    public String getCauser_type() {
        return causer_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public static Creator<Feed> getCREATOR() {
        return CREATOR;
    }

    protected Feed(Parcel in) {
        id = in.readInt();
        description = in.readString();
        subject_id = in.readInt();
        subject_type = in.readString();
        causer_id = in.readInt();
        causer_type = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(description);
        dest.writeInt(subject_id);
        dest.writeString(subject_type);
        dest.writeInt(causer_id);
        dest.writeString(causer_type);
        dest.writeString(created_at);
        dest.writeString(updated_at);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Feed> CREATOR = new Creator<Feed>() {
        @Override
        public Feed createFromParcel(Parcel in) {
            return new Feed(in);
        }

        @Override
        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };

    @Override
    public String toString() {
        return "OrderFeedItem{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", subject_id=" + subject_id +
                ", subject_type='" + subject_type + '\'' +
                ", causer_id=" + causer_id +
                ", causer_type='" + causer_type + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
