package com.israel.livraisonexpresspos.data.Room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.israel.livraisonexpresspos.models.ContactTable;

import java.util.List;

@Dao
public interface ContactDao {
    @Insert
    void insert(ContactTable item);

    @Delete
    void delete(ContactTable item);

    @Update
    void update(ContactTable item);

    @Query("SELECT * FROM contact_table")
    LiveData<List<ContactTable>> getContacts();

    @Query("SELECT * FROM contact_table WHERE state = :status")
    LiveData<List<ContactTable>> getUnSyncedContacts(String status);
}
