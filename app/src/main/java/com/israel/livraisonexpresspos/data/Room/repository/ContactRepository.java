package com.israel.livraisonexpresspos.data.Room.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.israel.livraisonexpresspos.data.Room.LivrexRoomDatabase;
import com.israel.livraisonexpresspos.data.Room.dao.ContactDao;
import com.israel.livraisonexpresspos.models.ContactTable;
import com.israel.livraisonexpresspos.utils.OrderStatus;

import java.util.List;

public class ContactRepository {
    private final ContactDao mDao;

    public ContactRepository(Application application){
        LivrexRoomDatabase db = LivrexRoomDatabase.getDatabase(application);
        mDao = db.mContactDao();
    }

    public void insert(ContactTable contactTable){
        insertAsync(contactTable);
    }

    public void upsert(ContactTable contactTable){
        if (contactTable.getId() != 0){
            update(contactTable);
        }else {
            insert(contactTable);
        }
    }

    private void insertAsync(ContactTable contactTable){
        new Thread(() -> mDao.insert(contactTable)).start();
    }

    private void updateAsync(ContactTable contactTable){
        new Thread(() -> mDao.update(contactTable)).start();
    }

    private void deleteAsync(ContactTable contactTable){
        new Thread(() -> mDao.delete(contactTable)).start();
    }

    public void delete(ContactTable contactTable){
        deleteAsync(contactTable);
    }

    public void update(ContactTable contactTable){
        updateAsync(contactTable);
    }

    public LiveData<List<ContactTable>> getContacts(){
        return mDao.getContacts();
    }


    public LiveData<List<ContactTable>> getUnSyncedContacts(){
        return mDao.getUnSyncedContacts(OrderStatus.pending.toString());
    }
}
