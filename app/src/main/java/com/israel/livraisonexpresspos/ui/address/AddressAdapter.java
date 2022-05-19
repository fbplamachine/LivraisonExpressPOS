package com.israel.livraisonexpresspos.ui.address;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.israel.livraisonexpresspos.databinding.ItemAddressCheckoutBinding;
import com.israel.livraisonexpresspos.models.Address;
import com.israel.livraisonexpresspos.ui.checkout.SelectAddressDialog;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressViewHolder> {
    private List<Address> mAddresses;
    private SelectAddressDialog mDialog;
    private FragmentManager mFragmentManager;

    public AddressAdapter(@NonNull List<Address> addresses, SelectAddressDialog dialog) {
        mAddresses = addresses;
        mDialog = dialog;
    }


    public AddressAdapter(List<Address> addresses) {
        mAddresses = addresses;
    }

    public AddressAdapter(List<Address> addresses, FragmentManager fragmentManager) {
        mAddresses = addresses;
        mFragmentManager = fragmentManager;
    }

    public void setAddresses(List<Address> addresses) {
        mAddresses = addresses;
        notifyDataSetChanged();
    }

    public void addNewAddress(Address address){
        mAddresses.add(address);
        notifyDataSetChanged();
    }

    public void refreshEntry(Address address, long position) {
        mAddresses.remove((int) position);
        mAddresses.add((int) position, address);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemAddressCheckoutBinding binding = ItemAddressCheckoutBinding.inflate(inflater, parent, false);
        return new AddressViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = mAddresses.get(position);
        holder.bind(address, mDialog, mFragmentManager, position);
    }

    @Override
    public int getItemCount() {
        return mAddresses.size();
    }


    public void addAddress(Address address) {
        mAddresses.add(0,address);
        notifyDataSetChanged();
    }
}