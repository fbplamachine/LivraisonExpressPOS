package com.israel.livraisonexpresspos.data.Room.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.israel.livraisonexpresspos.data.Room.LivrexRoomDatabase;
import com.israel.livraisonexpresspos.data.Room.dao.CommentDao;
import com.israel.livraisonexpresspos.models.Comment;

import java.util.Calendar;
import java.util.List;

public class CommentRepository {
    private final CommentDao mCommentDao;

    public CommentRepository(Application application){
        LivrexRoomDatabase db = LivrexRoomDatabase.getDatabase(application);
        mCommentDao = db.mCommentDao();
        long currentTimeMillis = Calendar.getInstance().getTimeInMillis();
        deleteOldCommentsAsync(currentTimeMillis);
    }

    public LiveData<List<Comment>> getOrderComments(int orderId){
        return mCommentDao.getLiveItems(orderId);
    }

    public LiveData<List<Comment>> getUnSyncedComments(){
        return mCommentDao.getUnSyncedItems(false);
    }

    public void insert(Comment comment){
        insertAsync(comment);
    }

    public void delete(Comment comment){
        deleteAsync(comment);
    }

    private void insertAsync(Comment comment){
        new Thread(() -> mCommentDao.insert(comment)).start();
    }

    private void deleteAsync(Comment comment){
        new Thread(() -> mCommentDao.delete(comment)).start();
    }

    private void deleteOldCommentsAsync(long currentTime){
        new Thread(() -> mCommentDao.deleteOldComments(currentTime)).start();
    }

}
