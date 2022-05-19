package com.israel.livraisonexpresspos.data.Room.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Api;
import com.israel.livraisonexpresspos.data.Room.LivrexRoomDatabase;
import com.israel.livraisonexpresspos.models.Contact;
import com.israel.livraisonexpresspos.models.ContactTable;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.utils.OrderStatus;
import com.israel.livraisonexpresspos.utils.Values;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactRemoteRepository {
    private final ContactRepository mRepository;
    private final MutableLiveData<List<Contact>> mContactList;
    private final MutableLiveData<Boolean> mLoading;
    private final MutableLiveData<Boolean> mSuccess;
    private final MutableLiveData<String> mError;
    private final MutableLiveData<Contact> mReceiver;
    private final MutableLiveData<ContactTable> mContactTable;
    private final Application mApplication;

    public ContactRemoteRepository(Application application) {
        LivrexRoomDatabase db = LivrexRoomDatabase.getDatabase(application);
        mApplication = application;
        mRepository = new ContactRepository(application);
        mLoading = new MutableLiveData<>();
        mSuccess = new MutableLiveData<>();
        mError = new MutableLiveData<>();
        mReceiver = new MutableLiveData<>();
        mContactList = new MutableLiveData<>();
        mContactTable = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getSuccess() {
        return mSuccess;
    }

    public MutableLiveData<List<Contact>> getContactList() {
        return mContactList;
    }

    public MutableLiveData<Contact> getReceiver() {
        return mReceiver;
    }

    public MutableLiveData<String> getError() {
        return mError;
    }

    public MutableLiveData<ContactTable> getContactTable() {
        return mContactTable;
    }

    public void fetchContacts(final String token, String searchPattern){
        mLoading.setValue(true);
        Api.contacts().contact(token, searchPattern).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                mLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null){
                    try {
                        String output = response.body().string();
                        JSONArray object = new JSONObject(output).getJSONArray("data");
                        List<Contact> contacts = new Gson().fromJson(object.toString(), new TypeToken<List<Contact>>(){}.getType());
                        mContactList.setValue(contacts);
                    }catch (JSONException | IOException e){
                        e.printStackTrace();
                        App.handleError(e);
                    }
                }else {
                    if (response.code() == 401){
                        Values.unAuthorizedDialog();
                    }
                    mError.setValue("Une erreur est survenue");
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                mLoading.setValue(false);
                mError.setValue("Erreur de connexion internet.");
            }
        });
    }

    public MutableLiveData<Boolean> getLoading() {
        return mLoading;
    }

    public void postContact(final Contact contact){
        mLoading.setValue(true);
        final ContactTable table = new ContactTable();
        table.setStringContact(new Gson().toJson(contact));
        table.setDateTime(Calendar.getInstance().getTimeInMillis());
        if (App.isConnected){
            Api.contacts().postContact("", User.getCurrentUser(mApplication).getToken(), contact).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    mLoading.setValue(false);
                    if (response.isSuccessful() && response.body() != null){
                        try {
                            String output = response.body().string();
                            JSONArray object = new JSONObject(output).getJSONArray("data");
                            List<Contact> contacts = new Gson().fromJson(object.toString(), new TypeToken<List<Contact>>(){}.getType());
                            table.setState(OrderStatus.done.toString());
                            mRepository.upsert(table);
                            mReceiver.setValue(contacts.get(0));
                            mSuccess.setValue(true);
                        }catch (JSONException | IOException e){
                            e.printStackTrace();
                            App.handleError(e);
                            mSuccess.setValue(false);
                        }
                    }else {
                        if (response.code() == 401){
                            Values.unAuthorizedDialog();
                        }
                        mSuccess.setValue(false);
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    mLoading.setValue(false);
                    table.setState(OrderStatus.pending.toString());
                    mRepository.upsert(table);
                    mSuccess.setValue(true);
                }
            });
        } else {
            mLoading.setValue(false);
            table.setState(OrderStatus.pending.toString());
            mRepository.upsert(table);
            mSuccess.setValue(true);
        }
    }

    public void postContact(final ContactTable table){
        mLoading.setValue(true);
        final Contact contact = new Gson().fromJson(table.getStringContact(), new TypeToken<Contact>(){}.getType());
        table.setStringContact(new Gson().toJson(contact));
        table.setDateTime(Calendar.getInstance().getTimeInMillis());
        if (App.isConnected){
            Api.contacts().postContact("", User.getCurrentUser(mApplication).getToken(), contact).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    mLoading.setValue(false);
                    if (response.isSuccessful() && response.body() != null){
                        try {
                            String output = response.body().string();
                            JSONArray object = new JSONObject(output).getJSONArray("data");
                            List<Contact> contacts = new Gson().fromJson(object.toString(), new TypeToken<List<Contact>>(){}.getType());
                            table.setStringContact(new Gson().toJson(contacts.get(0)));
                            table.setState(OrderStatus.done.toString());
                            mRepository.upsert(table);
                            mReceiver.setValue(contacts.get(0));
                            mSuccess.setValue(true);
                            mContactTable.setValue(table);
                        }catch (JSONException | IOException e){
                            e.printStackTrace();
                            App.handleError(e);
                            mSuccess.setValue(false);
                        }
                    }else {
                        if (response.code() == 401){
                            Values.unAuthorizedDialog();
                        }
                        mSuccess.setValue(false);
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    mSuccess.setValue(true);
                    mLoading.setValue(false);
                    table.setState(OrderStatus.pending.toString());
                    mRepository.upsert(table);
                    mSuccess.setValue(true);
                }
            });
        } else {
            table.setState(OrderStatus.pending.toString());
            mRepository.upsert(table);
            mSuccess.setValue(true);
        }
    }




}