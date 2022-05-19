package com.israel.livraisonexpresspos.ui.contacts;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.israel.livraisonexpresspos.data.Room.repository.ContactRemoteRepository;
import com.israel.livraisonexpresspos.models.Contact;
import com.israel.livraisonexpresspos.models.User;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    private final ContactRemoteRepository mRepository;
    private final LiveData<List<Contact>> mContactList;
    private final MutableLiveData<Boolean> mLoading;
    private final MutableLiveData<String> mError;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ContactRemoteRepository(application);
        mContactList = mRepository.getContactList();
        mLoading = mRepository.getLoading();
        mError = mRepository.getError();
    }

    public LiveData<List<Contact>> getContactList() {
        return mContactList;
    }

    public void fetchContacts(String searchPattern){
        mRepository.fetchContacts(User.getCurrentUser(getApplication()).getToken(), searchPattern);
    }

    public MutableLiveData<Boolean> getLoading() {
        return mLoading;
    }

    public MutableLiveData<String> getError() {
        return mError;
    }
}