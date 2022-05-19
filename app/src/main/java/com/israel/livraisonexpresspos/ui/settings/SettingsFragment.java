package com.israel.livraisonexpresspos.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.databinding.FragmentSettingsBinding;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.utils.PreferenceUtils;
import com.onesignal.OneSignal;

public class SettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    public static final String YAOUNDE_STATUS = "yaoundeStatus";
    public static final String DOUALA_STATUS = "doualaStatus";
    public static final String SUBSCRIPTION_STATUS = "subscriptionStatus";
    private FragmentSettingsBinding mBinding;
    private OnCityFilterChangeListener mOnCityFilterChangeListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        mOnCityFilterChangeListener = (OnCityFilterChangeListener) requireActivity();
        initUI();
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private void initUI() {
        mBinding.switchDouala.setOnCheckedChangeListener(this);
        mBinding.switchYaounde.setOnCheckedChangeListener(this);
        mBinding.switchNotification.setOnCheckedChangeListener(this);
        mBinding.switchYaounde.setChecked(PreferenceUtils.getTrueBoolean(requireContext(), YAOUNDE_STATUS));
        mBinding.switchDouala.setChecked(PreferenceUtils.getTrueBoolean(requireContext(), DOUALA_STATUS));
        mBinding.switchNotification.setChecked(PreferenceUtils.getTrueBoolean(requireContext(), SUBSCRIPTION_STATUS));
        if (User.isDeliveryMan() && !User.isManager()){
            mBinding.layoutCitySettings.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == mBinding.switchDouala.getId()){
            PreferenceUtils.setBoolean(requireContext(), DOUALA_STATUS, isChecked);
            mOnCityFilterChangeListener.onCityFilterChange();
        }else if (id == mBinding.switchYaounde.getId()){
            PreferenceUtils.setBoolean(requireContext(), YAOUNDE_STATUS, isChecked);
            mOnCityFilterChangeListener.onCityFilterChange();
        }else if (id == mBinding.switchNotification.getId()){
            OneSignal.setSubscription(isChecked);
            PreferenceUtils.setBoolean(requireContext(), SUBSCRIPTION_STATUS, isChecked);
        }
    }
}
