package com.israel.livraisonexpresspos.models;

import android.os.Parcel;
import android.os.Parcelable;

public class DayItem implements Parcelable {
    private boolean enabled;
    private boolean opened;
    private String opened_at;
    private String closed_at;

    protected DayItem(Parcel in) {
        enabled = in.readByte() != 0;
        opened = in.readByte() != 0;
        opened_at = in.readString();
        closed_at = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (enabled ? 1 : 0));
        dest.writeByte((byte) (opened ? 1 : 0));
        dest.writeString(opened_at);
        dest.writeString(closed_at);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DayItem> CREATOR = new Creator<DayItem>() {
        @Override
        public DayItem createFromParcel(Parcel in) {
            return new DayItem(in);
        }

        @Override
        public DayItem[] newArray(int size) {
            return new DayItem[size];
        }
    };

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public String getOpened_at() {
        return opened_at;
    }

    public void setOpened_at(String opened_at) {
        this.opened_at = opened_at;
    }

    public String getClosed_at() {
        return closed_at;
    }

    public void setClosed_at(String closed_at) {
        this.closed_at = closed_at;
    }
}
