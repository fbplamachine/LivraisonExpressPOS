package com.israel.livraisonexpresspos.data.Room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.israel.livraisonexpresspos.models.UnSynced;

import java.util.List;

@Dao
public interface UnSyncedDao {
    @Insert
    void insert(UnSynced unSynced);

    @Delete()
    void delete(UnSynced unSynced);

    @Query("SELECT * FROM un_synced_table")
    LiveData<List<UnSynced>> getUnSyncedQueries();
}
