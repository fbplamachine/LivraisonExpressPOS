package com.israel.livraisonexpresspos.ui.order_detail.comment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.databinding.ItemAnotherCommentBinding;
import com.israel.livraisonexpresspos.databinding.ItemMyCommentBinding;
import com.israel.livraisonexpresspos.models.Comment;
import com.israel.livraisonexpresspos.models.User;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {
    public static final int MY_COMMENT = 0;
    public static final int SOME_ONE_ELSE_COMMENT = 1;
    private List<Comment> mComments = new ArrayList<>();

    public void setComments(List<Comment> comments) {
        mComments = comments;
        notifyDataSetChanged();
    }

    public void addComments(List<Comment> comments) {
        mComments.addAll(comments);
        notifyDataSetChanged();
    }

    public void addComment(Comment comment){
        mComments.add(comment);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == MY_COMMENT){
            ItemMyCommentBinding binding = ItemMyCommentBinding.inflate(inflater, parent, false);
            return new CommentViewHolder(binding);
        }else {
            ItemAnotherCommentBinding binding = ItemAnotherCommentBinding.inflate(inflater, parent, false);
            return new CommentViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = mComments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemViewType(int position) {
        Comment comment = mComments.get(position);
        if (comment.getUsers_id().equals(String.valueOf(User.getCurrentUser(App.currentActivity).getId()))){
            return MY_COMMENT;
        }else {
            return SOME_ONE_ELSE_COMMENT;
        }
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }
}
