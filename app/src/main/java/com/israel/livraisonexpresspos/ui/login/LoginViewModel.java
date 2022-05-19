package com.israel.livraisonexpresspos.ui.login;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Api;
import com.israel.livraisonexpresspos.models.User;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {
    private final MutableLiveData<String> mError;
    private final MutableLiveData<Boolean> mLoading;
    private final MutableLiveData<User> mUser;

    public LoginViewModel() {
        mError = new MutableLiveData<>();
        mLoading = new MutableLiveData<>();
        mUser = new MutableLiveData<>();
    }

    public MutableLiveData<String> getError() {
        return mError;
    }

    public MutableLiveData<Boolean> getLoading() {
        return mLoading;
    }

    public MutableLiveData<User> getUser() {
        return mUser;
    }

    public void login(final Context context, String email, String password){
        mLoading.setValue(true);
        Api.auth().login(email, password).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body() != null){
                    try{
                        String output = response.body().string();
                        String token = "Bearer ".concat(new JSONObject(output).getString("access_token"));
                        getUser(token);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        App.handleError(e);
                        mLoading.setValue(false);
                        mError.setValue("Une erreur est survenu. Veuillez vous rapprocher de l'équipe technique.");
                    }
                }else {
                    mLoading.setValue(false);
                    mError.setValue("Une erreur est survenu. Veuillez vérifier vos identifiants.\nSi l'erreur persiste veuillez vous rapprocher de l'équipe technique.");
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                mLoading.setValue(false);
                mError.setValue("Veuillez votre connexion internet.");
            }
        });
    }

    public void getUser(final String token){
        Api.auth().user(token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                mLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null){
                    try {
                        String output = response.body().string();
                        JSONObject object = new JSONObject(output).getJSONObject("data");
                        User user = new Gson().fromJson(object.toString(), new TypeToken<User>(){}.getType());
                        user.setToken(token);
                        mUser.setValue(user);
                    }catch (JSONException | IOException e){
                        e.printStackTrace();
                        App.handleError(e);
                        mError.setValue("Une erreur est survenu. Veuillez vous rapprocher de l'équipe technique.");
                    }
                }else {
                    mError.setValue("Une erreur est survenu. Veuillez vérifier vos identifiants.\nSi l'erreur persiste veuillez vous rapprocher de l'équipe technique.");
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                mLoading.setValue(false);
                mError.setValue("Veuillez votre connexion internet.");
            }
        });
    }
}
