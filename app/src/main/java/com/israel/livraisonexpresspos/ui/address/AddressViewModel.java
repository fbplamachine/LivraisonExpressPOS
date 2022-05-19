package com.israel.livraisonexpresspos.ui.address;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Api;
import com.israel.livraisonexpresspos.data.Room.repository.ContactRemoteRepository;
import com.israel.livraisonexpresspos.databinding.DialogAddressBinding;
import com.israel.livraisonexpresspos.models.Address;
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

public class AddressViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> mLoad;
    private MutableLiveData<Boolean> mSuccess;
    private ContactRemoteRepository mRepository;
    private OnAddressUpdate onAddressUpdate;
    private OnAddressAdded onAddressAdded;
    private final User mUser;

    public AddressViewModel(@NonNull Application application) {
        super(application);
        mUser = User.getCurrentUser(application);
        mRepository = new ContactRemoteRepository(application);
        mLoad = new MutableLiveData<>();
        mSuccess = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getLoad() {
        return mLoad;
    }

    public MutableLiveData<Boolean> getSuccess() {
        return mSuccess;
    }

    public void setActivity(Activity activity){
        try{
            onAddressAdded = (OnAddressAdded) activity;
            onAddressUpdate = (OnAddressUpdate) activity;
        }catch (Exception e){
            App.handleError(e);
        }
    }

    @Override
    protected void onCleared() {
        mLoad = new MutableLiveData<>();
        mSuccess = new MutableLiveData<>();
        super.onCleared();
    }

    public void addAddress(final Address address){
        mLoad.setValue(true);
        if (App.isConnected) {
            Call<ResponseBody> call = Api.order().addAddress(
                    mUser.getToken(),
                    address.getClient_id(),
                    address.getClient_id(),
                    mUser.getId(),
                    address.getVille_id(),
                    address.getLat(),
                    address.getLon(),
                    address.getQuartier(),
                    address.getNom(),
                    address.getSurnom(),
                    address.getSurnom(),
                    address.getDescription(),
                    0,
                    address.getProvider_name()
            );
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    mLoad.setValue(false);
                    if (response.isSuccessful() && response.body() != null){
                        try{
                            String output = response.body().string();
                            JSONObject object = new JSONObject(output);
                            if (object.getBoolean("success")){
                                mSuccess.setValue(true);
                                JSONObject data = object.getJSONObject("data");
                                Address addressFromServer = new Gson().fromJson(data.toString(),new TypeToken<Address>(){}.getType());
                                onAddressAdded.onAddressAdded(addressFromServer);
                            }
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
                    mLoad.setValue(false);
                    mSuccess.setValue(false);
                }
            });
        }else {
         onAddressAdded.onAddressAddedPersistence(address);
            mLoad.setValue(false);
            mSuccess.setValue(true);
        }
    }

    public void updateAddress(final Address address, final long addressPosition){
        mLoad.setValue(true);
        if (App.isConnected) {
            Call<ResponseBody> call = Api.order().updateAddress(
                    mUser.getToken(),
                    address.getClient_id(),
                    address.getId(),
                    address.getClient_id(),
                    mUser.getId(),
                    address.getLat(),
                    address.getLon(),
                    address.getQuartier(),
                    address.getNom(),
                    address.getTitre(),
                    address.getTitre(),
                    address.getDescription(),
                    0
            );
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    mLoad.setValue(false);
                    if (response.isSuccessful() && response.body() != null){
                        try{
                            String output = response.body().string();
                            JSONObject object = new JSONObject(output);
                            if (object.getBoolean("success")){
                                mSuccess.setValue(true);
                                //the callback here is meant to refresh the view once data are updated
                                onAddressUpdate.onAddressUpdated(address, addressPosition);
                            }
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
                    mLoad.setValue(false);
                    mSuccess.setValue(false);
                }
            });
        }else {
            onAddressUpdate.onAddressUpdatesPersistence(address, addressPosition);
            mLoad.setValue(false);
            mSuccess.setValue(true);
        }
    }

    public boolean validate(DialogAddressBinding binding){
        boolean validator = true;
//        if (TextUtils.isEmpty(binding.etAddressTitle.getText())){
//            validator = false;
//            binding.tilTitle.setError("Veuillez entrer le titre");
//        }else {
//            binding.tilTitle.setError(null);
//        }
        if (TextUtils.isEmpty(binding.etQuarter.getText())){
            validator = false;
            binding.tilQuarter.setError("Veuillez entrer le quartier");
        }else {
            binding.tilQuarter.setError(null);
        }
        if (TextUtils.isEmpty(binding.etDescription.getText())){
            validator = false;
            binding.tilDescription.setError("Veuillez entrer la description");
        }else {
            binding.tilDescription.setError(null);
        }
//        if (TextUtils.isEmpty(binding.etLatitude.getText())){
//            validator = false;
//            binding.etLatitude.setError("Veuillez géolocaliser l'adresse");
//        }else {
//            binding.etLatitude.setError(null);
//        }
//        if (TextUtils.isEmpty(binding.etAddressTitle.getText())){
//            validator = false;
//            binding.etLongitude.setError("Veuillez géolocaliser l'adresse");
//        }else {
//            binding.etLongitude.setError(null);
//        }

        return validator;
    }

    public Address getAddress(DialogAddressBinding binding, Address address){
        address.setTitre(binding.etAddressTitle.getText().toString());
        address.setQuartier(binding.etQuarter.getText().toString());
        address.setDescription(binding.etDescription.getText().toString());
        address.setLatitude(binding.etLatitude.getText().toString());
        address.setLongitude(binding.etLongitude.getText().toString());

        return address;
    }

}
