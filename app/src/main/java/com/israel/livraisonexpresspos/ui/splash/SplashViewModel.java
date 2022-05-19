package com.israel.livraisonexpresspos.ui.splash;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Api;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.utils.PreferenceUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashViewModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> logged;

    public SplashViewModel(Application application) {
        super(application);
        logged = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getLogged() {
        return logged;
    }


    public void tryLog(final Context context){
        final String token = PreferenceUtils.getString(context, PreferenceUtils.ACCESS_TOKEN);
        if (token.equals("")){
            logged.setValue(false);
            return;
        }
        Api.auth().user(token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null){
                    try {
                        String output = response.body().string();
                        JSONObject object = new JSONObject(output).getJSONObject("data");
                        User user = new Gson().fromJson(object.toString(), new TypeToken<User>(){}.getType());
                        user.setToken(token);
                        User.setCurrentUser(user);
                        PreferenceUtils.setString(context, PreferenceUtils.CURRENT_USER, new Gson().toJson(user));
                        int roleCode = User.getRoleCode();
                        if (roleCode == 0){
                            logged.setValue(false);
                        }else {
                            PreferenceUtils.setLong(context, PreferenceUtils.LAST_CONNECTION, Calendar.getInstance().getTimeInMillis());
                            logged.setValue(true);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        App.handleError(e);
                        logged.setValue(false);
                    }
                }else {
                    logged.setValue(false);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                User.setCurrentUser(new Gson().fromJson(PreferenceUtils.getString(context, PreferenceUtils.CURRENT_USER),
                        new TypeToken<User>(){}.getType()));
                logged.setValue(User.getCurrentUser(getApplication()) != null);
            }
        });
    }
}
