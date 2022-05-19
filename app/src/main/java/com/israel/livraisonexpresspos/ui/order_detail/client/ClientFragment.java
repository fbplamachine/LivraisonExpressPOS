package com.israel.livraisonexpresspos.ui.order_detail.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.FragmentClientDetailsBinding;
import com.israel.livraisonexpresspos.models.from_steed_app.Client;
import com.israel.livraisonexpresspos.models.from_steed_app.OrderSteed;
import com.israel.livraisonexpresspos.ui.order_detail.OrderDetailActivity;
import com.israel.livraisonexpresspos.utils.OrderUtils;

public class ClientFragment extends Fragment implements View.OnClickListener {
    private FragmentClientDetailsBinding mBinding;
    private OrderSteed mOrderSteed;
    private Client mClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_client_details, container, false);
        mOrderSteed = ((OrderDetailActivity) requireActivity()).getOrderSteed();
        initUI();
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private void initUI() {
        mClient = mOrderSteed.getClient();
        mBinding.setContact(mClient);
        if (mClient.getAdresses() != null && mClient.getAdresses().size() > 0){
            mBinding.setAddress(mClient.getAdresses().get(0));
        }

        mBinding.clientPhone.setOnClickListener(this);
        mBinding.clientPhone2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBinding.clientPhone.getId()){
            OrderUtils.makePhoneCall(requireActivity(), mClient.getTelephone());
        }else if (id == mBinding.clientPhone2.getId()){
            OrderUtils.makePhoneCall(requireActivity(), mClient.getTelephone_alt());
        }
    }
}
