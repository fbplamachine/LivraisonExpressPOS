package com.israel.livraisonexpresspos.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Day implements Parcelable {
    private ArrayList<DayItem> items;
    private boolean opened;
    private boolean enabled;

    protected Day(Parcel in) {
        items = in.createTypedArrayList(DayItem.CREATOR);
        opened = in.readByte() != 0;
        enabled = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(items);
        dest.writeByte((byte) (opened ? 1 : 0));
        dest.writeByte((byte) (enabled ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

    public ArrayList<DayItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<DayItem> items) {
        this.items = items;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
