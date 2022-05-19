package com.israel.livraisonexpresspos.ui.order_detail.attach;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Api;
import com.israel.livraisonexpresspos.models.Attachment;
import com.israel.livraisonexpresspos.models.User;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttachViewModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> load;
    private final MutableLiveData<Boolean> postLoad;
    private final MutableLiveData<Boolean> success;
    private final MutableLiveData<List<Attachment>> mAttachments;

    public AttachViewModel(Application application) {
        super(application);
        success = new MutableLiveData<>();
        load = new MutableLiveData<>();
        postLoad = new MutableLiveData<>();
        mAttachments = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getLoad() {
        return load;
    }

    public MutableLiveData<Boolean> getPostLoad() {
        return postLoad;
    }

    public MutableLiveData<Boolean> getSuccess() {
        return success;
    }

    public MutableLiveData<List<Attachment>> getAttachments() {
        return mAttachments;
    }

    public void postImage(int orderId, MultipartBody.Part body, RequestBody name){
        postLoad.setValue(true);
        Api.order().postAttachment(User.getCurrentUser(getApplication()).getToken(), orderId, body)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        postLoad.setValue(false);
                        if (response.isSuccessful() && response.body() != null){
                            try{
                                String output = response.body().string();
                                JSONObject object = new JSONObject(output);
                                JSONArray data = object.getJSONArray("data");
                                List<Attachment> attachments = new Gson().fromJson(data.toString(), new TypeToken<List<Attachment>>(){}.getType());
                                mAttachments.setValue(new ArrayList<>(attachments));
                                success.setValue(true);
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                                App.handleError(e);
                                success.setValue(false);
                            }
                        }else {
                            success.setValue(false);
                        }

                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        t.printStackTrace();
                        postLoad.setValue(false);
                        success.setValue(false);
                    }
                });
    }

    public void deleteAttachment(int orderId, int attachmentId){
        if (mAttachments.getValue() == null)return;
        load.setValue(true);
        List<Attachment> list = mAttachments.getValue();
        int index = -1;
        for (int i = 0; i < list.size(); i++){
            Attachment attachment = list.get(i);
            if (attachment.getId() == attachmentId){
                index = i;
                break;
            }
        }

        if (index >= 0)list.remove(index);
        mAttachments.setValue(list);
        Api.order().deleteAttachments(User.getCurrentUser(getApplication()).getToken(), orderId, attachmentId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        load.setValue(false);
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        load.setValue(false);
                    }
                });
    }

    public void getAttachments(int orderId){
        load.setValue(true);
        Api.order().getAttachments(User.getCurrentUser(getApplication()).getToken(), orderId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        load.setValue(false);
                        if (response.isSuccessful() && response.body() != null){
                            try {
                                String output = response.body().string();
                                JSONObject object = new JSONObject(output);
                                List<Attachment> attachments = new Gson().fromJson(object.getJSONArray("data").toString(),
                                        new TypeToken<List<Attachment>>(){}.getType());
                                mAttachments.setValue(attachments);
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                                App.handleError(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        t.printStackTrace();
                        load.setValue(false);
                    }
                });
    }
}
