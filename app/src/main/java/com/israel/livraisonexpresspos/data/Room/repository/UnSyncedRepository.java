package com.israel.livraisonexpresspos.data.Room.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.israel.livraisonexpresspos.data.Room.LivrexRoomDatabase;
import com.israel.livraisonexpresspos.data.Room.dao.UnSyncedDao;
import com.israel.livraisonexpresspos.models.UnSynced;

import java.util.List;

public class UnSyncedRepository {
    private final UnSyncedDao mDao;
    private final LiveData<List<UnSynced>> mUnSynced;

    public UnSyncedRepository(Application application) {
        LivrexRoomDatabase db = LivrexRoomDatabase.getDatabase(application);
        mDao = db.mUnSynced();
        mUnSynced = mDao.getUnSyncedQueries();
    }

    public void insert(UnSynced unSynced) {
        insertAsync(unSynced);
    }

    public void delete(UnSynced unSynced) {
        deleteAsync(unSynced);
    }

    public LiveData<List<UnSynced>> getUnSynced() {
        return mUnSynced;
    }

    private void insertAsync(UnSynced unSynced){
        new Thread(() -> mDao.insert(unSynced)).start();
    }

    private void deleteAsync(UnSynced unSynced){
        new Thread(() -> mDao.delete(unSynced)).start();
    }
}
