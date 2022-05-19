package com.israel.livraisonexpresspos.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ShopStatistic implements Parcelable {
    private final int magasin_id;
    private final String magasin;
    private final List<MonthStatistic> stats;


    protected ShopStatistic(Parcel in) {
        magasin_id = in.readInt();
        magasin = in.readString();
        stats = in.createTypedArrayList(MonthStatistic.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(magasin_id);
        dest.writeString(magasin);
        dest.writeTypedList(stats);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShopStatistic> CREATOR = new Creator<ShopStatistic>() {
        @Override
        public ShopStatistic createFromParcel(Parcel in) {
            return new ShopStatistic(in);
        }

        @Override
        public ShopStatistic[] newArray(int size) {
            return new ShopStatistic[size];
        }
    };

    public int getMagasin_id() {
        return magasin_id;
    }

    public String getMagasin() {
        return magasin;
    }

    public List<MonthStatistic> getStats() {
        return stats;
    }
}
