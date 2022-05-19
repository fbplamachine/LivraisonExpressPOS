package com.israel.livraisonexpresspos.models.from_steed_app;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Paiement implements Serializable, Parcelable {
    private String message;
    private String mode_paiment;
    private String date_paiment;
    private String statut;
    private int montant_total;
    private int id_paiment;
    private String payment_intent;
    private String msisdn_paiement;
    private Cashier cashier;

    public Paiement() {
    }

    protected Paiement(Parcel in) {
        message = in.readString();
        mode_paiment = in.readString();
        date_paiment = in.readString();
        statut = in.readString();
        montant_total = in.readInt();
        id_paiment = in.readInt();
        payment_intent = in.readString();
        msisdn_paiement = in.readString();
        cashier = in.readParcelable(Cashier.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeString(mode_paiment);
        dest.writeString(date_paiment);
        dest.writeString(statut);
        dest.writeInt(montant_total);
        dest.writeInt(id_paiment);
        dest.writeString(payment_intent);
        dest.writeString(msisdn_paiement);
        dest.writeParcelable(cashier, flags);
    }

    public static final Creator<Paiement> CREATOR = new Creator<Paiement>() {
        @Override
        public Paiement createFromParcel(Parcel in) {
            return new Paiement(in);
        }

        @Override
        public Paiement[] newArray(int size) {
            return new Paiement[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMode_paiment() {
        return mode_paiment;
    }

    public void setMode_paiment(String mode_paiment) {
        this.mode_paiment = mode_paiment;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
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

    public int getMontant_total() {
        return montant_total;
    }

    public void setMontant_total(int montant_total) {
        this.montant_total = montant_total;
    }

    public String getMsisdn_paiement() {
        return msisdn_paiement;
    }

    public void setMsisdn_paiement(String msisdn_paiement) {
        this.msisdn_paiement = msisdn_paiement;
    }

    public String getDate_paiment() {
        return date_paiment;
    }

    public void setDate_paiment(String date_paiment) {
        this.date_paiment = date_paiment;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public void setCashier(Cashier cashier) {
        this.cashier = cashier;
    }

    public Map<String, Object> toMapp (){
        Map<String, Object> mapper = new HashMap<>();
        mapper.put("mode_paiment",getMode_paiment());
        mapper.put("statut",getStatut());
        mapper.put("payment_intent",getStatut());
        mapper.put("message",getMessage());
        mapper.put("id_paiement", getId_paiment());
        mapper.put("montant_total", getMontant_total());
        mapper.put("msisdn_paiement", getMsisdn_paiement());
        mapper.put("cashier", getCashier());
        return mapper;
    }

    public JSONObject toJSON (){
        return new JSONObject(this.toMapp());
    }

    @Override
    public String toString() {
        return "_Paiement{" +
                "message='" + message + '\'' +
                ", mode_paiment='" + mode_paiment + '\'' +
                ", date_paiment='" + date_paiment + '\'' +
                ", statut='" + statut + '\'' +
                ", montant_total=" + montant_total +
                ", id_paiement='" + id_paiment + '\'' +
                ", msisdn_paiement='" + msisdn_paiement + '\'' +
                ", cashier='" + cashier + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
