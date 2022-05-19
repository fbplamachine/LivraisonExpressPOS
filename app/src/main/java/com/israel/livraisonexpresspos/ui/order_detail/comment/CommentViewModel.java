package com.israel.livraisonexpresspos.ui.order_detail.comment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Api;
import com.israel.livraisonexpresspos.data.Room.repository.CommentRepository;
import com.israel.livraisonexpresspos.models.Comment;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.utils.Values;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Comment>> mComments;
    private final MutableLiveData<Boolean> mLoading;
    private final CommentRepository mCommentRepository;
    private Call<ResponseBody> mCall;
    private User mUser;

    public CommentViewModel(@NonNull Application application) {
        super(application);
        mUser = User.getCurrentUser(application);
        mComments = new MutableLiveData<>();
        mLoading = new MutableLiveData<>();
        mCommentRepository = new CommentRepository(application);
    }

    public MutableLiveData<List<Comment>> getComments() {
        return mComments;
    }

    public MutableLiveData<Boolean> getLoading() {
        return mLoading;
    }

    public LiveData<List<Comment>> getComments(int orderId){
        return mCommentRepository.getOrderComments(orderId);
    }

    public void fetchCommentsFromFirebase(int courseId){
        mLoading.setValue(true);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("comments");
        reference.keepSynced(true);
        Query query = reference.orderByChild("commentable_id")
                .equalTo(String.valueOf(courseId)).limitToLast(2);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Comment> comments = new ArrayList<>();
                if (snapshot.exists()){
                    for (DataSnapshot snap : snapshot.getChildren()){
                        Comment comment = snap.getValue(Comment.class);
                        if (comment == null)continue;
                        comment.setFromFirebase(true);
                        comment.setName(comment.getUser().getFullname());
                        comment.setDateTime(Calendar.getInstance().getTimeInMillis());
                        mCommentRepository.insert(comment);
                        comments.add(comment);
                    }
//                    mComments.setValue(comments);
                }
                mLoading.setValue(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void fetchComments(int courseId){
        mCall = Api.order().getComments(mUser.getToken(), courseId);
        mCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null){
                    try{
                        String output = response.body().string();
                        JSONObject object = new JSONObject(output);
                        if (object.getBoolean("success")){
                            List<Comment> comments = new Gson().fromJson(
                                    object.getJSONArray("data").toString()
                                    , new TypeToken<List<Comment>>(){}.getType());
                            mComments.setValue(comments);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        App.handleError(e);
                    }
                }else {
                    if (response.code() == 401){
                        Values.unAuthorizedDialog();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

            }
        });
    }

    public void postComment(int courseId, String comment){
        Comment newComment = new Comment();
        newComment.setFromFirebase(false);
        newComment.setName(mUser.getFullname());
        newComment.setComment(comment);
        newComment.setCommentable_id(String.valueOf(courseId));
        newComment.setUsers_id(String.valueOf(mUser.getId()));
        List<Comment> comments = new ArrayList<>();
        if (mComments.getValue() != null){
            comments.addAll(mComments.getValue());
        }
        comments.add(newComment);
        mComments.setValue(comments);
        if (App.isConnected){
            mCall = Api.order().postComment(mUser.getToken(), "course", courseId,
                    mUser.getId(), comment);
            mCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null && mUser != null){
                        try{
                            String output = response.body().string();
                            JSONObject object = new JSONObject(output);
                            if (object.getBoolean("success")){
                                Comment commentObject = new Gson().fromJson(object.getJSONObject("data").toString(),
                                        new TypeToken<Comment>(){}.getType());
                                commentObject.setName(mUser.getFullname());
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            App.handleError(e);
                        }
                    }else {
                        if (response.code() == 401){
                            Values.unAuthorizedDialog();
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
//                    Toasty.error(getApplication(), "Message non envoyé").show();
                    insertComment(newComment);
                }
            });
        }else {
//            Toasty.error(getApplication(), "Veuillez vérifier votre connexion internet").show();
            insertComment(newComment);
        }
    }

    private void insertComment(Comment comment){
        comment.setDateTime(Calendar.getInstance().getTimeInMillis());
        mCommentRepository.insert(comment);
//        Toast.makeText(getApplication(), comment.getComment(), Toast.LENGTH_SHORT).show();
    }
}
