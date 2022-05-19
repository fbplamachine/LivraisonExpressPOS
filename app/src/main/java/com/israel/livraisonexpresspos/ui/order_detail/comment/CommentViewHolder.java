package com.israel.livraisonexpresspos.ui.order_detail.comment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemAnotherCommentBinding;
import com.israel.livraisonexpresspos.databinding.ItemMyCommentBinding;
import com.israel.livraisonexpresspos.models.Comment;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    private ItemMyCommentBinding mMyCommentBinding;
    private ItemAnotherCommentBinding mAnotherCommentBinding;

    public CommentViewHolder(@NonNull ItemMyCommentBinding binding) {
        super(binding.getRoot());
        mMyCommentBinding = binding;
    }

    public CommentViewHolder(@NonNull ItemAnotherCommentBinding binding) {
        super(binding.getRoot());
        mAnotherCommentBinding = binding;
    }

    public void bind(Comment comment){
        if (mMyCommentBinding == null){
            mAnotherCommentBinding.setComment(comment);
        }else {
            mMyCommentBinding.setComment(comment);
        }
    }
}
