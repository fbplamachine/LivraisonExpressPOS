package com.israel.livraisonexpresspos.ui.address;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.ItemAddressCheckoutBinding;
import com.israel.livraisonexpresspos.models.Address;
import com.israel.livraisonexpresspos.ui.checkout.CheckoutActivity;
import com.israel.livraisonexpresspos.ui.checkout.OnAddressSelectedListener;
import com.israel.livraisonexpresspos.ui.order_detail.OrderDetailActivity;
import com.israel.livraisonexpresspos.utils.CourseStatus;
import com.israel.livraisonexpresspos.utils.DialogUtils;
import com.israel.livraisonexpresspos.utils.OrderUtils;

import es.dmoral.toasty.Toasty;

public class AddressViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private final ItemAddressCheckoutBinding mBinding;
    private OnAddressSelectedListener mSelectedListener;
    private AppCompatDialogFragment mDialog;
    private PopupMenu mPopupMenu;
    private Address mAddress;
    private int mPosition;
    private FragmentManager mManager;

    public AddressViewHolder(@NonNull ItemAddressCheckoutBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
        if (itemView.getContext() instanceof CheckoutActivity){
            mSelectedListener = (CheckoutActivity) itemView.getContext();
        }
    }



    public void bind(Address address, AppCompatDialogFragment dialog, FragmentManager manager, int position){
        mDialog = dialog;
        mAddress = address;
        mManager = manager;
        mPosition = position;
        mBinding.setAddress(address);
        if (itemView.getContext() instanceof CheckoutActivity){
            itemView.setOnClickListener(this);
            mBinding.ivMore.setVisibility(View.GONE);
            return;
        }
        mBinding.ivMore.setOnClickListener(this);
        handlePopupMenu();
    }

    private void handlePopupMenu(){
        mPopupMenu = new PopupMenu(itemView.getContext(), mBinding.ivMore);
        mPopupMenu.inflate(R.menu.address_menu);
        if (mAddress.getLat() == 0.0 || mAddress.getLon() == 0.0
                || !(itemView.getContext() instanceof OrderDetailActivity)){
            mPopupMenu.getMenu().findItem(R.id.action_track).setVisible(false);
        }
        mPopupMenu.setOnMenuItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == itemView.getId()){
            mSelectedListener.addressSelected(mAddress);
            mDialog.dismiss();
        }else if (id == mBinding.ivMore.getId()){
            mPopupMenu.show();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_detail){
            DialogUtils.showAddressDetailsDialog((Activity) itemView.getContext(),"Fermer", mAddress);
        }else if (id == R.id.action_edit){
            if (itemView.getContext() instanceof OrderDetailActivity){
                OrderDetailActivity activity = (OrderDetailActivity) itemView.getContext();
                if (activity.getOrderSteed().getInfos().getStatut() == CourseStatus.CODE_UNASSIGNED) {
                    Toasty.success(activity, activity.getResources().getString(R.string.prohibited_modification_case_unassigned_order), Toast.LENGTH_LONG).show();
                }else {
                    showAddressModificationDialog(getAdapterPosition());
                }
            }else {
                showAddressModificationDialog(getAdapterPosition());
            }

        }else if (id == R.id.action_track){
            OrderDetailActivity activity = (OrderDetailActivity)itemView.getContext();
            LatLng destination = new LatLng(mAddress.getLat(), mAddress.getLon());
            OrderUtils.enableTracking(itemView.getContext(), activity.getLocation(), destination);
        }
        return false;
    }

    private void showAddressModificationDialog(int itemPosition){
        AddressDialogFragment fragment = new AddressDialogFragment();
        Bundle args = new Bundle();
        args.putLong("address_list_position",itemPosition);
        args.putParcelable(AddressDialogFragment.ADDRESS_ARGUMENT, mAddress);
        fragment.setArguments(args);
        fragment.show(mManager, AddressDialogFragment.class.getSimpleName());
    }
}
