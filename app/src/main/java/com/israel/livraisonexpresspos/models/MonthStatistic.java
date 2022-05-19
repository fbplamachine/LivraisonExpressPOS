package com.israel.livraisonexpresspos.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MonthStatistic implements Parcelable {
    private final String nom;
    private final int numero;
    private final SaleStatistic stats;


    protected MonthStatistic(Parcel in) {
        nom = in.readString();
        numero = in.readInt();
        stats = in.readParcelable(SaleStatistic.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeInt(numero);
        dest.writeParcelable(stats, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MonthStatistic> CREATOR = new Creator<MonthStatistic>() {
        @Override
        public MonthStatistic createFromParcel(Parcel in) {
            return new MonthStatistic(in);
        }

        @Override
        public MonthStatistic[] newArray(int size) {
            return new MonthStatistic[size];
        }
    };

    public String getNom() {
        return nom;
    }

    public int getNumero() {
        return numero;
    }

    public SaleStatistic getStats() {
        return stats;
    }
}
