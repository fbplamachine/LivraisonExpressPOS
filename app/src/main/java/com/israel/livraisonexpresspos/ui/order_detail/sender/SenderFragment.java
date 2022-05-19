package com.israel.livraisonexpresspos.ui.order_detail.sender;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.databinding.FragmentRetraitDetailsBinding;
import com.israel.livraisonexpresspos.models.Address;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.models.from_steed_app.Client;
import com.israel.livraisonexpresspos.models.from_steed_app.OrderSteed;
import com.israel.livraisonexpresspos.ui.address.AddressAdapter;
import com.israel.livraisonexpresspos.ui.address.AddressDialogFragment;
import com.israel.livraisonexpresspos.ui.address.OnAddressAdded;
import com.israel.livraisonexpresspos.ui.address.OnAddressUpdate;
import com.israel.livraisonexpresspos.ui.message.MessageHelper;
import com.israel.livraisonexpresspos.ui.order_detail.OrderDetailActivity;
import com.israel.livraisonexpresspos.uiComponents.ViewAnimation;
import com.israel.livraisonexpresspos.utils.CourseStatus;
import com.israel.livraisonexpresspos.utils.DialogUtils;
import com.israel.livraisonexpresspos.utils.OrderUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SenderFragment extends Fragment
        implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, OnAddressUpdate, OnAddressAdded {
    public static final String USER_TYPE = "sender";
    private FragmentRetraitDetailsBinding mBinding;
    private SenderViewModel mViewModel;
    private Client mClient;
    private OrderDetailActivity mActivity;
    private AddressAdapter mAdapter;
    private PopupMenu mPopupMenu;
    private boolean seeMoreShown = false;
    private StringRequest stringRequest;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = ((OrderDetailActivity) requireActivity());
        mViewModel = new ViewModelProvider(this).get(SenderViewModel.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_retrait_details, container, false);
        mClient = mActivity.getOrderSteed().getSender();
        initUI();
        streamSender();
        return mBinding.getRoot();
    }

    private void initUI() {
        mBinding.setUserType(USER_TYPE);
        mBinding.setContact(mClient);
        mBinding.setAddress(mClient.getAdresses().get(0));
        mBinding.rvBlocAdressReceiver.setLayoutManager(new LinearLayoutManager(requireContext()));
        mAdapter = new AddressAdapter(new ArrayList<Address>(), requireActivity().getSupportFragmentManager());
        mBinding.rvBlocAdressReceiver.setAdapter(mAdapter);

        mBinding.buttonSeeMore.setOnClickListener(this);
        mBinding.ivMore.setOnClickListener(this);
        mBinding.phoneCall.setOnClickListener(this);
        mBinding.phone2Call.setOnClickListener(this);
        mBinding.phoneMessage.setOnClickListener(this);
        mBinding.phone2Message.setOnClickListener(this);
        mBinding.ivAddAddress.setOnClickListener(this);
        mBinding.ivLocate.setOnClickListener(this);
        handlePopupMenu();
        toggleAddressIntegrityWarning();
        if (User.isPartner()){
            mBinding.cvAddress.setVisibility(View.GONE);
            mBinding.cvGeo.setVisibility(View.GONE);
        }
    }

    private void toggleAddressIntegrityWarning() {
        Address address = mActivity.getOrderSteed().getSender().getAdresses().get(0);
        if (address.getTitre() == null || address.getDescription() == null) return;
        if ((address.getLat() > 0 && address.getLon() > 0) && (address.getTitre().trim().length() > 0) && address.getDescription().trim().length() > 0 && !address.getEst_favorite()) {
            mBinding.ivAlert.setVisibility(View.GONE);
        } else {
            mBinding.ivAlert.setVisibility(View.VISIBLE);
        }
    }

    private void streamSender() {
        mViewModel.getAddresses().observe(getViewLifecycleOwner(), new Observer<List<Address>>() {
            @Override
            public void onChanged(List<Address> addresses) {
                if (addresses == null || addresses.isEmpty()) return;
                ViewAnimation.expand(mBinding.blocMoreAdress);
                int i = 0;
                boolean remove = false;
                for (Address a : addresses) {
                    if (a.getId() == mClient.getAdresses().get(0).getId()) {
                        remove = true;
                        break;
                    }
                    i++;
                }
                if (remove) addresses.remove(i);
                seeMoreShown = true;
                mBinding.buttonSeeMore.setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_round_remove_24));
                mBinding.buttonSeeMore.setText(R.string.see_less);
                mAdapter.setAddresses(addresses);
            }
        });

        mViewModel.getLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == null) return;
                mBinding.progressBar.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
                mBinding.buttonSeeMore.setEnabled(!aBoolean);
            }
        });
    }

    private void handlePopupMenu() {
        mPopupMenu = new PopupMenu(requireContext(), mBinding.ivMore);
        mPopupMenu.inflate(R.menu.address_menu);
        Address address = mClient.getAdresses().get(0);
        if (address.getLat() == 0.0 || address.getLon() == 0.0) {
            mPopupMenu.getMenu().findItem(R.id.action_track).setVisible(false);
        }
        mPopupMenu.setOnMenuItemClickListener(this);
    }

    private void toggleSeeMore() {
        if (!seeMoreShown) {
            if (mViewModel.getAddresses().getValue() != null
                    && !mViewModel.getAddresses().getValue().isEmpty()
                    && !mActivity.isReloadSenderAddresses()) {
                ViewAnimation.expand(mBinding.blocMoreAdress);
            } else {
                mViewModel.fetchAddresses(mClient.getId());
                mActivity.setReloadSenderAddresses(false);
            }
            mBinding.buttonSeeMore.setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_round_remove_24));
            mBinding.buttonSeeMore.setText(R.string.see_less);
        } else {
            ViewAnimation.collapse(mBinding.blocMoreAdress);
            mBinding.buttonSeeMore.setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_round_add_24));
            mBinding.buttonSeeMore.setText(R.string.see_more);
        }
        seeMoreShown = !seeMoreShown;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mBinding.buttonSeeMore.getId()) {
            toggleSeeMore();
        } else if (id == mBinding.ivMore.getId()) {
            mPopupMenu.show();
        } else if (id == mBinding.phoneCall.getId()) {
            OrderUtils.makePhoneCall(requireActivity(), mClient.getTelephone());
        } else if (id == mBinding.phone2Call.getId()) {
            OrderUtils.makePhoneCall(requireActivity(), mClient.getTelephone_alt());
        } else if (id == mBinding.ivLocate.getId()) {
            getDistanceClientDeliveryMan(USER_TYPE, mClient.getAdresses().get(0).getLatitude_longitude());
        } else if (id == mBinding.ivAddAddress.getId()) {

            if (mActivity.getOrderSteed().getInfos().getStatut() == CourseStatus.CODE_UNASSIGNED) {
                Toast.makeText(mActivity, requireActivity().getResources().getString(R.string.prohibited_modification_case_unassigned_order), Toast.LENGTH_SHORT).show();
            } else {
                AddressDialogFragment fragment = new AddressDialogFragment();
                Address address = new Address();
                address.setClient_id(mClient.getId());
                address.setProvider_name(mClient.getProvider_name());
                Bundle args = new Bundle();
                args.putLong("address_list_position", -2); //tomean we're adding address
                args.putParcelable(AddressDialogFragment.ADDRESS_ARGUMENT, address);
                fragment.setArguments(args);
                fragment.show(mActivity.getSupportFragmentManager(), AddressDialogFragment.class.getSimpleName());
            }

        } else if (id == mBinding.phoneMessage.getId()) {
            MessageHelper messageHelper = new MessageHelper();
            messageHelper.displayDialogMessageSelector(mClient.getTelephone(), getActivity(), USER_TYPE);
        } else if (id == mBinding.phone2Message.getId()) {
            MessageHelper messageHelper = new MessageHelper();
            messageHelper.displayDialogMessageSelector(mClient.getTelephone_alt(), getActivity(), USER_TYPE);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_detail) {
            DialogUtils.showAddressDetailsDialog(getActivity(), "Fermer", mActivity.getOrderSteed().getSender().getAdresses().get(0));
        } else if (id == R.id.action_edit) {

            if (mActivity.getOrderSteed().getInfos().getStatut() == CourseStatus.CODE_UNASSIGNED) {
                Toasty.warning(mActivity, requireActivity().getResources().getString(R.string.prohibited_modification_case_unassigned_order), Toast.LENGTH_SHORT).show();
            } else {
                if (!mClient.getAdresses().get(0).getEst_favorite()) {
                    AddressDialogFragment fragment = new AddressDialogFragment();
                    Address address = mClient.getAdresses().get(0);
                    if (address.getClient_id() == 0) address.setClient_id(mClient.getId());
                    Bundle args = new Bundle();
                    args.putLong("address_list_position", -1); // to meant are updating an address
                    args.putParcelable(AddressDialogFragment.ADDRESS_ARGUMENT, address);
                    fragment.setArguments(args);
                    fragment.show(mActivity.getSupportFragmentManager(), AddressDialogFragment.class.getSimpleName());
                } else {
                    Log.e("je suis dans ce cas ", "oui");
                    Toasty.warning(mActivity, "Cette adresse est favorite, elle ne peut être modifiée !!!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (id == R.id.action_track) {
            if (mActivity.getLocation() == null) return false;
            Address address = mClient.getAdresses().get(0);
            LatLng destination = new LatLng(address.getLat(), address.getLon());
            OrderUtils.enableTracking(requireActivity(), mActivity.getLocation(), destination);
        }
        return false;
    }

    @Override
    public void onAddressAdded(Address address) {
        mAdapter.addAddress(address);
    }

    @Override
    public void onAddressAddedPersistence(Address address) {

    }

    @Override
    public void onAddressUpdated(Address address, long addressPosition) {

        if (addressPosition < 0) { //we refresh the displayed main address information
            mClient.getAdresses().remove(0);
            mClient.getAdresses().add(address);
            mBinding.setAddress(address);
            OrderSteed steed = mActivity.getOrderSteed();
            steed.getSender().getAdresses().clear();
            steed.getSender().getAdresses().add(address);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                    .child("courses")
                    .child(steed.getKey());
            reference.setValue(steed);
        } else { //we changes to the view (more addres bloc ) if expanded else modifications will be available once it'll be expanded
            mAdapter.refreshEntry(address, addressPosition);
        }
    }

    @Override
    public void onAddressUpdatesPersistence(Address address, long position) {

    }

    private void getDistanceClientDeliveryMan(final String userType, String clientLatLng) {
        try {
            LatLng deliveryManLatLng = mActivity.getLocation();
            StringBuilder strDeliveryManLatLng = new StringBuilder();
            Log.e("les coordonnée coursier", deliveryManLatLng.toString());
            strDeliveryManLatLng.append(deliveryManLatLng.latitude).append(",").append(deliveryManLatLng.longitude);
            final RequestQueue requestQueue;
            requestQueue = Volley.newRequestQueue(requireActivity());
            String url = "https://maps.googleapis.com/maps/api/directions/json?\n" + "origin=" + strDeliveryManLatLng.toString() + "&destination=" + clientLatLng + "&mode=driving&key=AIzaSyBn_LS3TTqaSsByi5U7poZjoFLB8Egi2Kk";
            stringRequest = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onResponse(String response) {
                            Log.e("suis dedans", "dans la reponse");
                            stringRequest.setTag(userType);
                            JSONObject object;
                            try {
                                object = new JSONObject(response);
                                String status = object.getString("status");
                                if (status.equals("ZERO_RESULTS")) {
                                    Log.e("GoogleStatut1ss****", status);
                                } else {
                                    JSONArray jsonArrayRoutes = object.getJSONArray("routes");
                                    JSONArray jsonArrayLegs = jsonArrayRoutes.getJSONObject(0).getJSONArray("legs");
                                    String distance = jsonArrayLegs.getJSONObject(0).getJSONObject("distance").getString("text");
                                    String duration = jsonArrayLegs.getJSONObject(0).getJSONObject("duration").getString("text");

                                    mBinding.setDistance(distance);
                                    mBinding.setDuration(duration);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                App.handleError(e);
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (requestQueue != null) {
                                requestQueue.cancelAll(userType);
                            }
                        }
                    });
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            App.handleError(e);
        }
    }
}
