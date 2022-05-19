package com.israel.livraisonexpresspos.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CancellationStatistics implements Parcelable {
    private String cancellation_reason;
    private int total;
    private double pourcentage;

    protected CancellationStatistics(Parcel in) {
        cancellation_reason = in.readString();
        total = in.readInt();
        pourcentage = in.readDouble();
    }

    public static final Creator<CancellationStatistics> CREATOR = new Creator<CancellationStatistics>() {
        @Override
        public CancellationStatistics createFromParcel(Parcel in) {
            return new CancellationStatistics(in);
        }

        @Override
        public CancellationStatistics[] newArray(int size) {
            return new CancellationStatistics[size];
        }
    };

    public String getCancellation_reason() {
        return cancellation_reason;
    }

    public void setCancellation_reason(String cancellation_reason) {
        this.cancellation_reason = cancellation_reason;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(double pourcentage) {
        this.pourcentage = pourcentage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cancellation_reason);
        dest.writeInt(total);
        dest.writeDouble(pourcentage);
    }
}
