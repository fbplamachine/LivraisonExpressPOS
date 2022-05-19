package com.israel.livraisonexpresspos.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Notification implements Parcelable {
    private String id_notification_for;
    private String message;
    private String notification_created_at;
    private String notification_for;
    private String subject;
    private String type_notification;
    private User user_cause;
    private List<Integer> user_to_notify;

    public Notification() { }

    protected Notification(Parcel in) {
        id_notification_for = in.readString();
        message = in.readString();
        notification_created_at = in.readString();
        notification_for = in.readString();
        subject = in.readString();
        type_notification = in.readString();
        user_cause = in.readParcelable(User.class.getClassLoader());
        in.readList(user_to_notify, null);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_notification_for);
        dest.writeString(message);
        dest.writeString(notification_created_at);
        dest.writeString(notification_for);
        dest.writeString(subject);
        dest.writeString(type_notification);
        dest.writeParcelable(user_cause, flags);
        dest.writeList(user_to_notify);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public String getId_notification_for() {
        return id_notification_for;
    }

    public void setId_notification_for(String id_notification_for) {
        this.id_notification_for = id_notification_for;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotification_created_at() {
        return notification_created_at;
    }

    public void setNotification_created_at(String notification_created_at) {
        this.notification_created_at = notification_created_at;
    }

    public String getNotification_for() {
        return notification_for;
    }

    public void setNotification_for(String notification_for) {
        this.notification_for = notification_for;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType_notification() {
        return type_notification;
    }

    public void setType_notification(String type_notification) {
        this.type_notification = type_notification;
    }

    public User getUser_cause() {
        return user_cause;
    }

    public void setUser_cause(User user_cause) {
        this.user_cause = user_cause;
    }

    public List<Integer> getUser_to_notify() {
        return user_to_notify;
    }

    public void setUser_to_notify(List<Integer> user_to_notify) {
        this.user_to_notify = user_to_notify;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id_notification_for='" + id_notification_for + '\'' +
                ", message='" + message + '\'' +
                ", notification_created_at='" + notification_created_at + '\'' +
                ", notification_for='" + notification_for + '\'' +
                ", subject='" + subject + '\'' +
                ", type_notification='" + type_notification + '\'' +
                ", user_cause=" + user_cause +
                ", user_to_notify=" + user_to_notify +
                '}';
    }
}
