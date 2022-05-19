package com.israel.livraisonexpresspos.ui.place_order;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.FragmentPlaceOrderBinding;
import com.israel.livraisonexpresspos.models.Order;
import com.israel.livraisonexpresspos.models.OrderWithCarts;
import com.israel.livraisonexpresspos.utils.Values;

import java.util.ArrayList;
import java.util.List;

public class PlaceOrderFragment extends Fragment{
    private PlaceOrderViewModel mViewModel;
    private FragmentPlaceOrderBinding mBinding;
    private OrderAdapter mAdapter;
    private List<OrderWithCarts> mOrders = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_place_order, container, false);
        initUI();
        stream();
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private void initUI() {
        mViewModel = new ViewModelProvider(this).get(PlaceOrderViewModel.class);
        mAdapter = new OrderAdapter(new ArrayList<OrderWithCarts>(), PlaceOrderFragment.this, requireActivity().getApplication());
        LinearLayoutManager manager = new LinearLayoutManager(requireContext());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        mBinding.rvOrder.setLayoutManager(manager);
        mBinding.rvOrder.setAdapter(mAdapter);
    }

    private void stream(){
        mViewModel.getOrders().observe(getViewLifecycleOwner(), orders -> {
            if (orders == null)return;
            mAdapter.setOrders(orders);
            if (mOrders.size() < orders.size()){
                mOrders.clear();
                mOrders.addAll(orders);
                mBinding.rvOrder.smoothScrollToPosition(orders.size() - 1);
            }else {
                mOrders.clear();
                mOrders.addAll(orders);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Values.clearOrder();
    }

    public void deleteOrder(final Order order){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setIcon(R.drawable.ic_round_warning_24)
                .setTitle("Attention!")
                .setMessage("Voulez vous supprimer cette commande?")
                .setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mViewModel.deleteOrder(order);
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    public void duplicateOrder(final OrderWithCarts orderWithCarts){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setIcon(R.drawable.ic_round_warning_24)
                .setTitle("Attention!")
                .setMessage("Voulez vous dupliquer cette commande?")
                .setPositiveButton("Dupliquer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mViewModel.duplicateOrder(orderWithCarts);
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
}