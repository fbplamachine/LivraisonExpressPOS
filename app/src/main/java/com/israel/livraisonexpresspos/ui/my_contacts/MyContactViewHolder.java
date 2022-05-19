package com.israel.livraisonexpresspos.ui.my_contacts;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.ItemMyContactsBinding;
import com.israel.livraisonexpresspos.models.Contact;
import com.israel.livraisonexpresspos.models.ContactTable;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.ui.contact_detail.ContactDetailActivity;
import com.israel.livraisonexpresspos.ui.contacts.OnContactTableSelected;
import com.israel.livraisonexpresspos.ui.new_contact.NewContactActivity;
import com.israel.livraisonexpresspos.utils.OrderStatus;

public class MyContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public static final String CONTACT = "contact";
    private final ItemMyContactsBinding mBinding;
    private PopupMenu mPopupMenu;
    private OnContactTableSelected mSelectedListener;
    private Contact mContact;
    private ContactTable mContactTable;

    public MyContactViewHolder(@NonNull ItemMyContactsBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
        mBinding.ivMore.setOnClickListener(this);
    }

    public void bind(ContactTable contactTable, OnContactTableSelected listener){
        mContactTable = contactTable;
        mContact = new Gson().fromJson(mContactTable.getStringContact(), new TypeToken<Contact>(){}.getType());
        String state = "";
        mBinding.setContact(mContact);
        if (contactTable.getState().equals(OrderStatus.pending.toString())){
            state = "En attente";
        }else if (contactTable.getState().equals(OrderStatus.saved.toString())){
            state = "Brouillon";
        }else if (contactTable.getState().equals(OrderStatus.done.toString())){
            state = "Envoyé";
        }
        mBinding.setState(state);
        buildPopupMenu();
        mSelectedListener = listener;
        manageContactStatus();
    }

    private void manageContactStatus() {
        int color = R.color.whatsappGreen;
        if (mContactTable.getState().equals(OrderStatus.pending.toString())){
            color = R.color.grey_40;
        }else if (mContactTable.getState().equals(OrderStatus.saved.toString())){
            color = R.color.colorAccent;
        }
        ViewCompat.setBackgroundTintList(mBinding.tvStatus,
                ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), color)));
    }

    private void buildPopupMenu(){
        mPopupMenu = new PopupMenu(itemView.getContext(), mBinding.ivMore);
        String[] menuItems = new String[]{"Détails", "Supprimer"};
        for (int i = 0; i < menuItems.length; i++){
            String item = menuItems[i];
            mPopupMenu.getMenu().add(0, i, 0, item);
        }

        mPopupMenu.getMenu().findItem(1).setVisible(User.getCurrentUser(itemView.getContext()).getRoles().contains("gerant"));

        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case 0:
                        Intent intent;
                        if(mContact.getId() == null){
                            intent = new Intent(itemView.getContext(), NewContactActivity.class);
                        }else {
                            intent = new Intent(itemView.getContext(), ContactDetailActivity.class);
                        }
                        intent.putExtra(CONTACT, mContact);
                        itemView.getContext().startActivity(intent);
                        break;
                    case 1:
                        mSelectedListener.delete(mContactTable);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ivMore){
            mPopupMenu.show();
        }
    }
}
