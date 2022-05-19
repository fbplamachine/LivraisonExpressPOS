package com.israel.livraisonexpresspos.data.Room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.israel.livraisonexpresspos.models.Comment;

import java.util.List;

@Dao
public interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Comment comment);

    @Delete
    void delete(Comment comment);

    @Query("SELECT * FROM comment_table WHERE commentable_id = :orderId")
    LiveData<List<Comment>> getLiveItems(Integer orderId);

    @Query("SELECT * FROM comment_table WHERE fromFirebase = :fromFirebase")
    LiveData<List<Comment>> getUnSyncedItems(boolean fromFirebase);

    @Query("DELETE FROM comment_table WHERE (:currentTime - dateTime) >= 86400000")
    void deleteOldComments(long currentTime);
}
