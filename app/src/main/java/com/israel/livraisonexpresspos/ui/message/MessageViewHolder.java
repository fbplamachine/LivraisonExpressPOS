package com.israel.livraisonexpresspos.ui.message;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    //todo : replace the view holder embeded in the message adapter by this
    private TextView tvMessageIndex,tvMessageTitle;
    private ExpandableTextView tvMessageContent;
    private CardView cardMessageSender;
    private CardView messageItem;
    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        tvMessageIndex = itemView.findViewById(R.id.tv_message_index);
        tvMessageTitle = itemView.findViewById(R.id.tv_message_title);
        tvMessageContent = itemView.findViewById(R.id.expandable_content);
        cardMessageSender = itemView.findViewById(R.id.card_message_sender);
    }
}
