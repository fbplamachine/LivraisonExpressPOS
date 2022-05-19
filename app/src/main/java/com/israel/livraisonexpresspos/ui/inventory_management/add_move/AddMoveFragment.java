package com.israel.livraisonexpresspos.ui.inventory_management.add_move;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.FragmentAddMoveBinding;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AddMoveFragment extends Fragment {
    private FragmentAddMoveBinding mBinding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_move, container, false);
        initViewModel();
        initUi();
        return mBinding.getRoot();
    }

    private void initUi(){

    }

    private void initViewModel(){

    }

}