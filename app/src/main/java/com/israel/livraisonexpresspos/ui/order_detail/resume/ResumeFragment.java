package com.israel.livraisonexpresspos.ui.order_detail.resume;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.FragmentResumeBinding;
import com.israel.livraisonexpresspos.models.from_steed_app.Client;
import com.israel.livraisonexpresspos.models.from_steed_app.OrderSteed;
import com.israel.livraisonexpresspos.ui.order_detail.OrderDetailActivity;

public class ResumeFragment extends Fragment implements View.OnClickListener {
    private FragmentResumeBinding mBinding;
    private OrderDetailActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_resume, container, false);
        mActivity = ((OrderDetailActivity) requireActivity());
        initUI();
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private void initUI() {
        OrderSteed orderSteed = mActivity.getOrderSteed();
        Client receiver = orderSteed.getReceiver();
        String builder = "Code course: " + orderSteed.getInfos().getRef() +
                "\n\n" + "Date de livraison: " + orderSteed.getInfos().getDate_livraison() +
                "\n\n" + "Nom: " + receiver.getFullname() +
                "\n\n" + "Téléphone: " + receiver.getTelephone() +
                "\n\n" + "Quartier: " + receiver.getAdresses().get(0).getQuartier() +
                "\n\n" + "Contenu: " + orderSteed.getInfos().getContenu() +
                "\n\n" + "Montant achat: " + orderSteed.getOrders().getMontant_achat() + " FCFA" +
                "\n\n" + "Montant Livraison: " + orderSteed.getOrders().getMontant_livraison() + " FCFA" +
                "\n\n" + "Montant Total: " + orderSteed.getPaiement().getMontant_total() + " FCFA";
        mBinding.tvResume.setText(builder);

        mBinding.ivCopy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBinding.ivCopy.getId()){
            ClipboardManager clipboard = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", mBinding.tvResume.getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(mActivity, "Copiée", Toast.LENGTH_SHORT).show();
        }
    }
}
