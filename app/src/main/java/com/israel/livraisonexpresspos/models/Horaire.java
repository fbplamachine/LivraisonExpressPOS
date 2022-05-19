package com.israel.livraisonexpresspos.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Horaire implements Parcelable {
    private ArrayList<Day> items;
    private Day today;
    private Day tomorrow;
    private int dayOfWeek;
    private int tomorrowDayOfWeek;

    protected Horaire(Parcel in) {
        items = in.createTypedArrayList(Day.CREATOR);
        today = in.readParcelable(Day.class.getClassLoader());
        tomorrow = in.readParcelable(Day.class.getClassLoader());
        dayOfWeek = in.readInt();
        tomorrowDayOfWeek = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(items);
        dest.writeParcelable(today, flags);
        dest.writeParcelable(tomorrow, flags);
        dest.writeInt(dayOfWeek);
        dest.writeInt(tomorrowDayOfWeek);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Horaire> CREATOR = new Creator<Horaire>() {
        @Override
        public Horaire createFromParcel(Parcel in) {
            return new Horaire(in);
        }

        @Override
        public Horaire[] newArray(int size) {
            return new Horaire[size];
        }
    };

    public ArrayList<Day> getItems() {
        return items;
    }

    public void setItems(ArrayList<Day> items) {
        this.items = items;
    }

    public Day getToday() {
        return today;
    }

    public void setToday(Day today) {
        this.today = today;
    }

    public Day getTomorrow() {
        return tomorrow;
    }

    public void setTomorrow(Day tomorrow) {
        this.tomorrow = tomorrow;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getTomorrowDayOfWeek() {
        return tomorrowDayOfWeek;
    }

    public void setTomorrowDayOfWeek(int tomorrowDayOfWeek) {
        this.tomorrowDayOfWeek = tomorrowDayOfWeek;
    }
}
