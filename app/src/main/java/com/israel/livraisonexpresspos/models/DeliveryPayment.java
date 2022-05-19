package com.israel.livraisonexpresspos.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.israel.livraisonexpresspos.models.from_steed_app.Cashier;

public class DeliveryPayment implements Parcelable {
    private String message;
    private String mode_paiement;
    private String statut;
    private int montant_total;
    private int id_paiment;
    private String payment_intent;
    private String date_paiement;
    private String msisdn_paiement;
    private Cashier cashier;

    public DeliveryPayment() {
    }


    protected DeliveryPayment(Parcel in) {
        message = in.readString();
        mode_paiement = in.readString();
        statut = in.readString();
        montant_total = in.readInt();
        id_paiment = in.readInt();
        payment_intent = in.readString();
        date_paiement = in.readString();
        msisdn_paiement = in.readString();
        cashier = in.readParcelable(Cashier.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeString(mode_paiement);
        dest.writeString(statut);
        dest.writeInt(montant_total);
        dest.writeInt(id_paiment);
        dest.writeString(payment_intent);
        dest.writeString(date_paiement);
        dest.writeString(msisdn_paiement);
        dest.writeParcelable(cashier, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeliveryPayment> CREATOR = new Creator<DeliveryPayment>() {
        @Override
        public DeliveryPayment createFromParcel(Parcel in) {
            return new DeliveryPayment(in);
        }

        @Override
        public DeliveryPayment[] newArray(int size) {
            return new DeliveryPayment[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMode_paiement() {
        return mode_paiement;
    }

    public void setMode_paiement(String mode_paiement) {
        this.mode_paiement = mode_paiement;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public int getMontant_total() {
        return montant_total;
    }

    public void setMontant_total(int montant_total) {
        this.montant_total = montant_total;
    }

    public int getId_paiment() {
        return id_paiment;
    }

    public void setId_paiment(int id_paiment) {
        this.id_paiment = id_paiment;
    }

    public String getPayment_intent() {
        return payment_intent;
    }

    public void setPayment_intent(String payment_intent) {
        this.payment_intent = payment_intent;
    }

    public String getDate_paiement() {
        return date_paiement;
    }

    public void setDate_paiement(String date_paiement) {
        this.date_paiement = date_paiement;
    }

    public String getMsisdn_paiement() {
        return msisdn_paiement;
    }

    public void setMsisdn_paiement(String msisdn_paiement) {
        this.msisdn_paiement = msisdn_paiement;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public void setCashier(Cashier cashier) {
        this.cashier = cashier;
    }

    @Override
    public String toString() {
        return "DeliveryPayment{" +
                "message='" + message + '\'' +
                ", mode_paiement='" + mode_paiement + '\'' +
                ", statut='" + statut + '\'' +
                ", montant_total=" + montant_total +
                ", id_paiment=" + id_paiment +
                ", payment_intent='" + payment_intent + '\'' +
                ", date_paiement='" + date_paiement + '\'' +
                ", cashier='" + cashier + '\'' +
                '}';
    }
}
