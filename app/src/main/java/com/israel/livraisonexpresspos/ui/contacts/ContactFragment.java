package com.israel.livraisonexpresspos.ui.contacts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.FragmentContactBinding;
import com.israel.livraisonexpresspos.models.Contact;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment{
    private static final String TAG = ContactFragment.class.getSimpleName();
    private ContactViewModel mContactViewModel;
    private FragmentContactBinding mBinding;
    private ContactListAdapter adapter;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_contact, container, false);
        mContactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        initUI();
        stream();
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private void initUI(){
        adapter = new ContactListAdapter(new ArrayList<Contact>());
        mBinding.rvContacts.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.rvContacts.setAdapter(adapter);
    }

    private void stream(){
        mBinding.progress.setVisibility(View.GONE);
        mContactViewModel.getContactList().observe(requireActivity(), new Observer<List<Contact>>() {
            @Override
            public void onChanged(@Nullable final List<Contact> contacts) {
                if (contacts == null)return;
                adapter.setContacts(contacts);
            }
        });

        mContactViewModel.getLoading().observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == null)return;
                mBinding.progress.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
                mBinding.rvContacts.setVisibility(aBoolean ? View.GONE : View.VISIBLE);
            }
        });

        mContactViewModel.getError().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s == null)return;
                Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
