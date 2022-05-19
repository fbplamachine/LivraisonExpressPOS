package com.israel.livraisonexpresspos.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "un_synced_table")
public class UnSynced implements Parcelable {
    @Ignore
    public static final String ADDRESS = "address";
    @Ignore
    public static final String STATUS = "status";
    @Ignore
    public static final String ERROR = "error";
//    @Ignore
//    public static final String COMMENT = "comment";

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String type;
    private String object;
    private long dateTime;

    public UnSynced() {
    }

    protected UnSynced(Parcel in) {
        id = in.readInt();
        type = in.readString();
        object = in.readString();
        dateTime = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(type);
        dest.writeString(object);
        dest.writeLong(dateTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UnSynced> CREATOR = new Creator<UnSynced>() {
        @Override
        public UnSynced createFromParcel(Parcel in) {
            return new UnSynced(in);
        }

        @Override
        public UnSynced[] newArray(int size) {
            return new UnSynced[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }
}
