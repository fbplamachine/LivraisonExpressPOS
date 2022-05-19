package com.israel.livraisonexpresspos.ui.contacts;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemContactBinding;
import com.israel.livraisonexpresspos.models.Contact;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactViewHolder> {
    public static final String SELECTED_CONTACT = "selected_contact";
    private List<Contact> mContacts;
    private List<Contact> mContactList;
    private boolean mClickable = false;

    public ContactListAdapter(@NonNull List<Contact> contacts) {
        mContacts = contacts;
        mContactList = new ArrayList<>(contacts);
    }

    public ContactListAdapter(@NonNull List<Contact> contacts, boolean clickable) {
        mContacts = contacts;
        mContactList = new ArrayList<>(contacts);
        mClickable = clickable;
    }

    @NotNull
    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemContactBinding binding = ItemContactBinding.inflate(inflater, parent, false);
        return new ContactViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact contact = mContacts.get(position);
        holder.bind(contact, mClickable);
    }

    public void setContacts(List<Contact> contactList) {
        mContacts = contactList;
        mContactList = new ArrayList<>(contactList);
        notifyDataSetChanged();
    }

    public void addContacts(List<Contact> contacts){
        mContacts.addAll(contacts);
        mContactList.addAll(contacts);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public Filter getFilter(){
        return mContactFilter;
    }

    private final Filter mContactFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Contact> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(mContactList);
            }else {
                String pattern = constraint.toString().toLowerCase().trim();
                for (Contact c : mContactList){
                    if (c.getFullname() != null &&
                            c.getFullname().toLowerCase().trim().contains(pattern)){
                        filteredList.add(c);
                    }else if (c.getTelephone() != null
                            && c.getTelephone().toLowerCase().trim().contains(pattern)){
                        filteredList.add(c);
                    }else if (c.getTelephone_alt() != null
                            && c.getTelephone_alt().toLowerCase().trim().contains(pattern)){
                        filteredList.add(c);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mContacts.clear();
            if (results.values == null)return;
            mContacts.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}

