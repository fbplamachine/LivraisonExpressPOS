package com.israel.livraisonexpresspos.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SaleStatistic implements Parcelable {
    private String achat;
    private String livraison;
    private String total;

    protected SaleStatistic(Parcel in) {
        achat = in.readString();
        livraison = in.readString();
        total = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(achat);
        dest.writeString(livraison);
        dest.writeString(total);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SaleStatistic> CREATOR = new Creator<SaleStatistic>() {
        @Override
        public SaleStatistic createFromParcel(Parcel in) {
            return new SaleStatistic(in);
        }

        @Override
        public SaleStatistic[] newArray(int size) {
            return new SaleStatistic[size];
        }
    };

    public String getAchat() {
        return achat;
    }

    public String getLivraison() {
        return livraison;
    }

    public String getTotal() {
        return total;
    }
}
