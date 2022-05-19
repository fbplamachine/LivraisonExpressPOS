package com.israel.livraisonexpresspos.ui.my_contacts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.FragmentMyContactsBinding;
import com.israel.livraisonexpresspos.models.ContactTable;
import com.israel.livraisonexpresspos.ui.contacts.OnContactTableSelected;

import java.util.List;

public class MyContactsFragment extends Fragment implements OnContactTableSelected {
    private MyContactsViewModel mViewModel;
    private FragmentMyContactsBinding mBinding;
    private MyContactsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_contacts, container, false);
        mViewModel = new ViewModelProvider(this).get(MyContactsViewModel.class);
        initUi();
        stream();
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private void initUi() {
        mAdapter = new MyContactsAdapter(this);
        mBinding.rvContacts.setLayoutManager(new LinearLayoutManager(requireActivity()));
        mBinding.rvContacts.setAdapter(mAdapter);
    }

    private void stream() {
        mViewModel.getContacts().observe(getViewLifecycleOwner(), new Observer<List<ContactTable>>() {
            @Override
            public void onChanged(List<ContactTable> contactTables) {
                if (contactTables == null)return;
                mAdapter.updateList(contactTables);
            }
        });
    }

    @Override
    public void delete(ContactTable contactTable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setIcon(R.drawable.ic_round_warning_24).setTitle(getString(R.string.warning))
                .setMessage("Vous êtes sur le point de supprimer cette piece jointe.")
                .setPositiveButton("Supprimer", (dialog, which) -> mViewModel.delete(contactTable))
                .setNegativeButton("Annuler", null);
        builder.create().show();

    }
}
