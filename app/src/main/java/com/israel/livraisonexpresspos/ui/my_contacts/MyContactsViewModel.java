package com.israel.livraisonexpresspos.ui.my_contacts;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.israel.livraisonexpresspos.data.Room.repository.ContactRepository;
import com.israel.livraisonexpresspos.models.ContactTable;

import java.util.List;

public class MyContactsViewModel extends AndroidViewModel {
    private final ContactRepository mRepository;
    private final LiveData<List<ContactTable>> mContacts;

    public MyContactsViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ContactRepository(application);
        mContacts = mRepository.getContacts();
    }

    public LiveData<List<ContactTable>> getContacts() {
        return mContacts;
    }

    public void insert(ContactTable contactTable){
        mRepository.insert(contactTable);
    }

    public void delete(ContactTable contactTable){
        mRepository.delete(contactTable);
    }

    public void update(ContactTable contactTable){
        mRepository.update(contactTable);
    }
}
