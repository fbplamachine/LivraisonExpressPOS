package com.israel.livraisonexpresspos.models.from_steed_app;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.israel.livraisonexpresspos.app.App;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrderSteed implements Parcelable, Comparable<OrderSteed>  {
    private Infos infos;
    private _Order orders;
    private Client client;
    private Client sender;
    private Client receiver;
    private Paiement paiement;
    private OrderExtras extra;
    private String key;

    public OrderSteed() {
    }


    protected OrderSteed(Parcel in) {
        infos = in.readParcelable(Infos.class.getClassLoader());
        orders = in.readParcelable(_Order.class.getClassLoader());
        client = in.readParcelable(Client.class.getClassLoader());
        sender = in.readParcelable(Client.class.getClassLoader());
        receiver = in.readParcelable(Client.class.getClassLoader());
        paiement = in.readParcelable(Paiement.class.getClassLoader());
        extra = in.readParcelable(OrderExtras.class.getClassLoader());
        key = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(infos, flags);
        dest.writeParcelable(orders, flags);
        dest.writeParcelable(client, flags);
        dest.writeParcelable(sender, flags);
        dest.writeParcelable(receiver, flags);
        dest.writeParcelable(paiement, flags);
        dest.writeParcelable(extra, flags);
        dest.writeString(key);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderSteed> CREATOR = new Creator<OrderSteed>() {
        @Override
        public OrderSteed createFromParcel(Parcel in) {
            return new OrderSteed(in);
        }

        @Override
        public OrderSteed[] newArray(int size) {
            return new OrderSteed[size];
        }
    };

    public Infos getInfos() {
        return infos;
    }

    public void setInfos(Infos infos) {
        this.infos = infos;
    }

    public _Order getOrders() {
        return orders;
    }

    public void setOrders(_Order orders) {
        this.orders = orders;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getSender() {
        return sender;
    }

    public void setSender(Client sender) {
        this.sender = sender;
    }

    public Client getReceiver() {
        return receiver;
    }

    public void setReceiver(Client receiver) {
        this.receiver = receiver;
    }

    public Paiement getPaiement() {
        return paiement;
    }

    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
    }

    public OrderExtras getExtra() {
        return extra;
    }

    public void setExtra(OrderExtras extra) {
        this.extra = extra;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, Object> toMapp (){
        Map<String, Object> mapper = new HashMap<>();
        mapper.put("infos", getInfos().toMapp());
        mapper.put("orders", getOrders().toMapp());
        mapper.put("paiement", getPaiement().toMapp());
        mapper.put("client", getClient().toMapp());
        mapper.put("sender", getSender().toMapp());
        mapper.put("receiver", getReceiver().toMapp());
        return mapper;
    }




    @Override
    public String toString() {
        return "Order{" +
                "infos=" + infos +
                ", orders=" + orders +
                ", client=" + client +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", paiement=" + paiement +
                ", extra=" + extra +
                '}';
    }

    @Override
    public int compareTo(OrderSteed orderSteed) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date date1 = format.parse(infos.getDate_livraison());
            Date date2 = format.parse(orderSteed.getInfos().getDate_livraison());
            if (date1 != null && date2 != null){
                if (date1.after(date2)){
                    return -1;
                }else if (date1.before(date2)){
                    return 1;
                }else {
                    return 0;
                }
            }
        }catch (ParseException e){
            e.printStackTrace();
            App.handleError(e);
        }
        return 0;
    }
}
