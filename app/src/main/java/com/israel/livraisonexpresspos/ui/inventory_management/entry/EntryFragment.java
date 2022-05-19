package com.israel.livraisonexpresspos.ui.inventory_management.entry;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.FragmentEntryBinding;
import com.israel.livraisonexpresspos.models.inventory_management_models.Move;
import com.israel.livraisonexpresspos.ui.inventory_management.MoveListAdapter;
import com.israel.livraisonexpresspos.ui.inventory_management.MovesViewModel;
import com.israel.livraisonexpresspos.ui.inventory_management.StockInventoryActivity;

import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class EntryFragment extends Fragment {
    private FragmentEntryBinding mBinding;
    private MovesViewModel mViewModel;
    private MoveListAdapter mAdapter;
    private StockInventoryActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_entry, container, false);
        initViewModel();
        initUi();
        stream();
        return mBinding.getRoot();
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(requireActivity()).get(MovesViewModel.class);
    }

    private void initUi() {
        initToolbar();
        activity = (StockInventoryActivity) requireActivity();
        mAdapter = new MoveListAdapter(requireActivity());
        mBinding.rvMoves.setAdapter(mAdapter);
    }

    private void stream() {
        mViewModel.getIsEntryMovesLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                mBinding.progressBar.setVisibility(View.VISIBLE);
                mBinding.rvMoves.setVisibility(View.GONE);
            } else {
                mBinding.progressBar.setVisibility(View.GONE);
                mBinding.rvMoves.setVisibility(View.VISIBLE);
            }
        });

        mViewModel.getEntryMoves().observe(getViewLifecycleOwner(), new Observer<List<Move>>() {
            @Override
            public void onChanged(List<Move> moves) {
                if (moves == null || moves.isEmpty()){
                    mBinding.clNoContent.setVisibility(View.VISIBLE);
                    mBinding.rvMoves.setVisibility(View.GONE);
                    return;
                }
                Collections.reverse(moves);
                mAdapter.setMovesList(moves);
                mBinding.clNoContent.setVisibility(View.GONE);
                mBinding.rvMoves.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initToolbar() {
        mBinding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_add) {
                activity.onAddMoveIntent(StockInventoryActivity.STR_ENTRY_MOVE_TYPE);
                /*todo : at the end of this process  take the new added move and add it to the vm list and then to rv lis the refresh data set change */
            }
            return true;
        });
    }
}