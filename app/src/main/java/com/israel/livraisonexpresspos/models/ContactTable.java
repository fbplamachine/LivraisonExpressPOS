package com.israel.livraisonexpresspos.models;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "contact_table", indices = {@Index(value = {"id"}, unique = true)})
public class ContactTable {
    @PrimaryKey(autoGenerate = true)
    int id;
    String state;
    String stringContact;
    Long dateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStringContact() {
        return stringContact;
    }

    public void setStringContact(String stringContact) {
        this.stringContact = stringContact;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "ContactTable{" +
                "id=" + id +
                ", state='" + state + '\'' +
                ", stringContact='" + stringContact + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
