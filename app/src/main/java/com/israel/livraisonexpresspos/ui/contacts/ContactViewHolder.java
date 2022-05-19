package com.israel.livraisonexpresspos.ui.contacts;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.MainActivity;
import com.israel.livraisonexpresspos.databinding.ItemContactBinding;
import com.israel.livraisonexpresspos.models.Contact;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.ui.contact_detail.ContactDetailActivity;
import com.israel.livraisonexpresspos.ui.select_contact.OnContactSelectedListener;
import com.israel.livraisonexpresspos.ui.select_contact.SelectContactActivity;

public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public static final String CONTACT = "contact";
    private final ItemContactBinding mBinding;
    private Contact mContact;
    private OnContactSelectedListener mSelectedListener;
    private PopupMenu mPopupMenu;

    public ContactViewHolder(@NonNull ItemContactBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public void bind(Contact contact, boolean clickable) {
        mBinding.setContact(contact);
        mContact = contact;
        buildPopupMenu();
        if (clickable){
            itemView.setOnClickListener(this);
            mBinding.ivMore.setVisibility(View.GONE);
            if (itemView.getContext() instanceof SelectContactActivity){
                mSelectedListener = (SelectContactActivity) itemView.getContext();
            }
        }else if (itemView.getContext() instanceof MainActivity){
            mSelectedListener = (MainActivity) itemView.getContext();
            mBinding.ivMore.setOnClickListener(this);
        }
    }

    private void buildPopupMenu(){
        mPopupMenu = new PopupMenu(itemView.getContext(), mBinding.ivMore);
        String[] menuItems = new String[]{"Détails", "Commander"};
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
                        Intent intent = new Intent(itemView.getContext(), ContactDetailActivity.class);
                        intent.putExtra(CONTACT, mContact);
                        itemView.getContext().startActivity(intent);
                        break;
                    case 1:
                        mSelectedListener.onContactSelected(mContact, true);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBinding.ivMore.getId()){
            mPopupMenu.show();
        }else {
            mSelectedListener.onContactSelected(mContact, false);
        }
    }
}
