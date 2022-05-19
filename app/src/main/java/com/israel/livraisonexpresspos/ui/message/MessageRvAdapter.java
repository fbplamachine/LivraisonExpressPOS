package com.israel.livraisonexpresspos.ui.message;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.models.from_steed_app.MessageModel;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;


public class MessageRvAdapter extends RecyclerView.Adapter<MessageRvAdapter.MessageListViewHolder> {
    private List<MessageModel> messageList;
    private Activity activity;
    private String phoneNumber;
    public MessageRvAdapter(List<MessageModel> messageList, Activity activity, String phoneNumber) {
        this.messageList = messageList;
        this.activity = activity;
        this.phoneNumber = phoneNumber;
    }

    @NonNull
    @Override
    public MessageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(activity).inflate(R.layout.message_item,parent,false);
        return new MessageListViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MessageListViewHolder holder, final int position) {
        if (holder instanceof MessageListViewHolder){
             holder.tvMessageIndex.setText(String.valueOf(messageList.get(position).getIndex()));
             holder.tvMessageTitle.setText(messageList.get(position).getTitle());
             holder.tvMessageContent.setText(messageList.get(position).getMessageContent());
             if (TextUtils.isEmpty(messageList.get(position).getMessageContent())){
                 holder.tvMessageContent.setVisibility(View.GONE);
             }
             holder.cardMessageSender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMessageToClient(phoneNumber,messageList.get(position).getMessageContent());
                }
            });
        }
    }

    private void sendMessageToClient(String phoneNumber, String message) {
        Intent messageIntent = new Intent(Intent.ACTION_VIEW);
        messageIntent.setData(Uri.fromParts("sms",phoneNumber,null));
        messageIntent.putExtra("sms_body",message);
        activity.startActivity(messageIntent);
    }
//todo : remove this viewHolder
    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageListViewHolder extends RecyclerView.ViewHolder{
        private TextView tvMessageIndex,tvMessageTitle;
        private ExpandableTextView tvMessageContent;
        private CardView cardMessageSender;
        private CardView messageItem;
        public MessageListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessageIndex = itemView.findViewById(R.id.tv_message_index);
            tvMessageTitle = itemView.findViewById(R.id.tv_message_title);
            tvMessageContent = itemView.findViewById(R.id.expandable_content);
            cardMessageSender = itemView.findViewById(R.id.card_message_sender);
        }
    }
}
