package com.israel.livraisonexpresspos.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "comment_table", indices = {@Index(value = {"id", "comment"}, unique = true)})
public class Comment implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int localId;
    private int id;
    private String users_id;
    private String commentable_id;
    @Ignore private User user;
    private String comment;
    private String date;
    private String name;
    private boolean fromFirebase;
    Long dateTime;

    public Comment() {
    }


    protected Comment(Parcel in) {
        id = in.readInt();
        localId = in.readInt();
        users_id = in.readString();
        commentable_id = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        comment = in.readString();
        date = in.readString();
        name = in.readString();
        fromFirebase = in.readByte() != 0;
        dateTime = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(localId);
        dest.writeString(users_id);
        dest.writeString(commentable_id);
        dest.writeParcelable(user, flags);
        dest.writeString(comment);
        dest.writeString(date);
        dest.writeString(name);
        dest.writeLong(dateTime);
        dest.writeByte((byte) (fromFirebase ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsers_id() {
        return users_id;
    }

    public void setUsers_id(String users_id) {
        this.users_id = users_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommentable_id() {
        return commentable_id;
    }

    public void setCommentable_id(String commentable_id) {
        this.commentable_id = commentable_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isFromFirebase() {
        return fromFirebase;
    }

    public void setFromFirebase(boolean fromFirebase) {
        this.fromFirebase = fromFirebase;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "localIs=" + localId +
                "id=" + id +
                ", users_id=" + users_id +
                ", commentable_id=" + commentable_id +
                ", user=" + user +
                ", comment='" + comment + '\'' +
                ", date='" + date + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
