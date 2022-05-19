package com.israel.livraisonexpresspos.ui.order_detail.attach;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.ItemAttachBinding;
import com.israel.livraisonexpresspos.models.Attachment;
import com.israel.livraisonexpresspos.ui.order_detail.attach.full_screen.FullScreenActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder> {
    private final List<Attachment> mAttachments = new ArrayList<>();
    private OnDeleteAttachment mOnDeleteAttachment;

    public void updateList(List<Attachment> attachments){
        mAttachments.clear();
        mAttachments.addAll(attachments);
        notifyDataSetChanged();
    }

    public void setOnDeleteAttachment(OnDeleteAttachment onDeleteAttachment) {
        mOnDeleteAttachment = onDeleteAttachment;
    }

    @NonNull
    @NotNull
    @Override
    public AttachmentViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemAttachBinding binding = ItemAttachBinding.inflate(inflater, parent, false);
        return new AttachmentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AttachmentViewHolder holder, int position) {
        holder.mBinding.setAttachment(mAttachments.get(position));
    }

    @Override
    public int getItemCount() {
        return mAttachments.size();
    }

    public class AttachmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private boolean isShown = false;
        private final ItemAttachBinding mBinding;
        public AttachmentViewHolder(@NonNull @NotNull ItemAttachBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            binding.tvAttachmentName.setOnClickListener(this);
            binding.ivDelete.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.tvAttachmentName){
                if (isShown){
                    mBinding.ivAttachment.setVisibility(View.GONE);
                }else {
                    mBinding.ivAttachment.setVisibility(View.VISIBLE);
                    mBinding.ivAttachment.setClipToOutline(true);
                    CircularProgressDrawable drawable = new CircularProgressDrawable(itemView.getContext());
                    drawable.setStrokeWidth(5f);
                    drawable.setCenterRadius(40f);
                    drawable.setTint(itemView.getContext().getColor(R.color.overlay_dark_30));
                    drawable.start();
                    Glide.with(mBinding.ivAttachment.getContext())
                            .load(mBinding.getAttachment().getFull_url())
                            .placeholder(drawable)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    mBinding.ivAttachment.setOnClickListener(v -> {
                                        Intent intent = new Intent(itemView.getContext(), FullScreenActivity.class);
                                        intent.putExtra("image", mBinding.getAttachment().getFull_url());
                                        itemView.getContext().startActivity(intent);
                                    });
                                    return false;
                                }
                            })
                            .into(mBinding.ivAttachment);
                }
                isShown = !isShown;
            } else  if (id == R.id.ivDelete){
                mOnDeleteAttachment.delete(mBinding.getAttachment().getId());
            }
        }
    }
}
