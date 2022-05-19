package com.israel.livraisonexpresspos.ui.my_contacts;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemMyContactsBinding;
import com.israel.livraisonexpresspos.models.ContactTable;
import com.israel.livraisonexpresspos.ui.contacts.OnContactTableSelected;

import java.util.ArrayList;
import java.util.List;

public class MyContactsAdapter extends RecyclerView.Adapter<MyContactViewHolder> {
    private final List<ContactTable> mContactTables = new ArrayList<>();
    private final OnContactTableSelected mSelectedListener;

    public MyContactsAdapter(OnContactTableSelected selectedListener) {
        mSelectedListener = selectedListener;
    }

    public void updateList(List<ContactTable> contactTables){
        mContactTables.clear();
        mContactTables.addAll(contactTables);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMyContactsBinding binding = ItemMyContactsBinding.inflate(inflater, parent, false);
        return new MyContactViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyContactViewHolder holder, int position) {
        ContactTable contactTable = mContactTables.get(position);
        holder.bind(contactTable, mSelectedListener);
    }

    @Override
    public int getItemCount() {
        return mContactTables.size();
    }
}
