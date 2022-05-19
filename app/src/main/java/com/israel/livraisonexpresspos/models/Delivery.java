package com.israel.livraisonexpresspos.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Delivery implements Parcelable {
    private DeliveryInfo infos;
    private DeliveryOrder orders;
    private DeliveryPayment paiement;
    private Contact client;
    private Contact sender;
    private Contact receiver;

    public Delivery() {
    }

    protected Delivery(Parcel in) {
        infos = in.readParcelable(DeliveryInfo.class.getClassLoader());
        orders = in.readParcelable(DeliveryOrder.class.getClassLoader());
        paiement = in.readParcelable(DeliveryPayment.class.getClassLoader());
        client = in.readParcelable(Contact.class.getClassLoader());
        sender = in.readParcelable(Contact.class.getClassLoader());
        receiver = in.readParcelable(Contact.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(infos, flags);
        dest.writeParcelable(orders, flags);
        dest.writeParcelable(paiement, flags);
        dest.writeParcelable(client, flags);
        dest.writeParcelable(sender, flags);
        dest.writeParcelable(receiver, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Delivery> CREATOR = new Creator<Delivery>() {
        @Override
        public Delivery createFromParcel(Parcel in) {
            return new Delivery(in);
        }

        @Override
        public Delivery[] newArray(int size) {
            return new Delivery[size];
        }
    };

    public DeliveryInfo getInfos() {
        return infos;
    }

    public void setInfos(DeliveryInfo infos) {
        this.infos = infos;
    }

    public DeliveryOrder getOrders() {
        return orders;
    }

    public void setOrders(DeliveryOrder orders) {
        this.orders = orders;
    }

    public DeliveryPayment getPaiement() {
        return paiement;
    }

    public void setPaiement(DeliveryPayment paiement) {
        this.paiement = paiement;
    }

    public Contact getClient() {
        return client;
    }

    public void setClient(Contact client) {
        this.client = client;
    }

    public Contact getSender() {
        return sender;
    }

    public void setSender(Contact sender) {
        this.sender = sender;
    }

    public Contact getReceiver() {
        return receiver;
    }

    public void setReceiver(Contact receiver) {
        this.receiver = receiver;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "infos=" + infos +
                ", orders=" + orders +
                ", paiement=" + paiement +
                ", client=" + client +
                ", sender=" + sender +
                ", receiver=" + receiver +
                '}';
    }
}
