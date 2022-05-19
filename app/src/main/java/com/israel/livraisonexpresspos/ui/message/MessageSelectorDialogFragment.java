package com.israel.livraisonexpresspos.ui.message;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.models.from_steed_app.MessageModel;
import com.israel.livraisonexpresspos.ui.order_detail.receiver.ReceiverFragment;
import com.israel.livraisonexpresspos.ui.order_detail.sender.SenderFragment;

import java.util.ArrayList;
import java.util.List;

public class MessageSelectorDialogFragment extends AppCompatDialogFragment {
    private String phoneNumber;
    private RecyclerView rvMessage;
    private ImageButton imgBtnMessageDialogCloser;

    public MessageSelectorDialogFragment() {}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_message_selector,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        initUi(view);
        return builder.create();
    }
    private void initUi(View view) {
        Bundle argument = getArguments();
        phoneNumber = argument.getString("phone_number","");
        imgBtnMessageDialogCloser = view.findViewById(R.id.img_btn_message_dialog_closer);

        rvMessage = view.findViewById(R.id.rv_message);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvMessage.setLayoutManager(layoutManager);
        MessageRvAdapter messageAdapter = new MessageRvAdapter(buildMessageList(),getActivity(),phoneNumber);
        rvMessage.setAdapter(messageAdapter);
        imgBtnMessageDialogCloser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private List<MessageModel> buildMessageList() {
        //todo : maybe differentiate the sender message and the receiver message
        List<MessageModel> messageList = new ArrayList<>();
        String userType = getArguments().getString("user_type");
        if (userType.equals(SenderFragment.USER_TYPE)) { // the client is from the type sender
        //todo : build custum messages for the sender
        }else if (userType.equals(ReceiverFragment.USER_TYPE)) { // the client is from the type receiver
            messageList.add(new MessageModel(1,getActivity().getResources().getString(R.string.before_starting_client_available_title),getActivity().getResources().getString(R.string.before_starting_client_available_msg)));
            messageList.add(new MessageModel(2,getActivity().getResources().getString(R.string.before_starting_client_unavailable_title),getActivity().getResources().getString(R.string.before_starting_client_unavailable_msg)));
            messageList.add(new MessageModel(3,getActivity().getResources().getString(R.string.once_on_delivery_area_client_unavailable_title),getActivity().getResources().getString(R.string.once_on_delivery_area_client_unavailable_msg)));
            messageList.add(new MessageModel(4,getActivity().getResources().getString(R.string.once_on_delivery_area_title_after_waiting_delay_title),getActivity().getResources().getString(R.string.once_on_delivery_area_msg_after_waiting_delay)));
            messageList.add(new MessageModel(5,getActivity().getResources().getString(R.string.on_cancellation_title),getActivity().getResources().getString(R.string.on_cancellation_msg)));
            messageList.add(new MessageModel(6,getActivity().getResources().getString(R.string.msg_for_other_purpose_title),""));
        }
        return messageList;
    }
}
