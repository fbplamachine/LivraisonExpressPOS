package com.israel.livraisonexpresspos.ui.contact_detail.contact;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Api;
import com.israel.livraisonexpresspos.models.Contact;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.utils.Values;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactDetailViewModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> mLoading;
    private final MutableLiveData<Boolean> mSuccess;

    public ContactDetailViewModel(@NonNull Application application) {
        super(application);
        mLoading = new MutableLiveData<>();
        mSuccess = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getLoading() {
        return mLoading;
    }

    public MutableLiveData<Boolean> getSuccess() {
        return mSuccess;
    }

    public void putContact(final Contact contact){
        mLoading.setValue(true);
        Api.contacts().putContact("", User.getCurrentUser(getApplication()).getToken(), contact.getId(), contact).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                mLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null){
                    try {
                        String output = response.body().string();
                        JSONObject object = new JSONObject(output).getJSONObject("data");
                        Contact newContact = new Gson().fromJson(object.toString(), new TypeToken<Contact>(){}.getType());
                        mSuccess.setValue(true);
                    }catch (JSONException | IOException e){
                        mSuccess.setValue(false);
                        App.handleError(e);
                        e.printStackTrace();
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
                contact.setSynced(false);
                mSuccess.setValue(true);
                mLoading.setValue(false);
            }
        });
    }
}
