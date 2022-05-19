package com.israel.livraisonexpresspos.ui.order_detail.receiver;

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
import androidx.appcompat.app.AlertDialog;
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
import com.israel.livraisonexpresspos.data.Room.repository.UnSyncedRepository;
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

public class ReceiverFragment extends Fragment
        implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, OnAddressUpdate, OnAddressAdded {
    public static final String USER_TYPE = "receiver";
    private FragmentRetraitDetailsBinding mBinding;
    private ReceiverViewModel mViewModel;
    private OrderDetailActivity mActivity;
    private Client mClient;
    private boolean seeMoreShown = false;
    private AddressAdapter mAdapter;
    private PopupMenu mPopupMenu;
    private StringRequest stringRequest;
    private UnSyncedRepository mUnSyncedRepository;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = ((OrderDetailActivity) requireActivity());
        mViewModel = new ViewModelProvider(this).get(ReceiverViewModel.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
        mPopupMenu = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_retrait_details, container, false);
        mClient = mActivity.getOrderSteed().getReceiver();
        initUI();
        stream();
        return mBinding.getRoot();
    }

    private void initUI() {
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
        Address address = mActivity.getOrderSteed().getReceiver().getAdresses().get(0);
        if (address.getTitre() == null || address.getDescription() == null) return;
        if ((address.getLat() > 0 && address.getLon() > 0) && (address.getTitre().trim().length() > 0) && address.getDescription().trim().length() > 0) {
            mBinding.ivAlert.setVisibility(View.GONE);
        } else {
            mBinding.ivAlert.setVisibility(View.VISIBLE);
        }
    }

    private void stream() {
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
                    && !mActivity.isReloadReceiverAddresses()) {
                ViewAnimation.expand(mBinding.blocMoreAdress);
                mBinding.buttonSeeMore.setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_round_remove_24));
                mBinding.buttonSeeMore.setText(R.string.see_less);
            } else {
                mViewModel.fetchAddresses(mClient.getId());
                mActivity.setReloadReceiverAddresses(false);
            }

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
                Toasty.warning(mActivity, requireActivity().getResources().getString(R.string.prohibited_modification_case_unassigned_order), Toast.LENGTH_SHORT).show();
            } else {
                AddressDialogFragment fragment = new AddressDialogFragment();
                Address address = new Address();
                address.setProvider_name(mClient.getProvider_name());
                address.setClient_id(mClient.getId());
                Bundle args = new Bundle();
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
            DialogUtils.showAddressDetailsDialog(getActivity(), "Fermer", mActivity.getOrderSteed().getReceiver().getAdresses().get(0));
        } else if (id == R.id.action_edit) {
            if (mActivity.getOrderSteed().getInfos().getStatut() == CourseStatus.CODE_UNASSIGNED) {
                Toasty.warning(mActivity, requireActivity().getResources().getString(R.string.prohibited_modification_case_unassigned_order), Toast.LENGTH_SHORT).show();
            } else {
                if (!mClient.getAdresses().get(0).getEst_favorite()) {
                    Address address = mClient.getAdresses().get(0);
                    if (address.getClient_id() == null) address.setClient_id(mClient.getId());
                    AddressDialogFragment fragment = new AddressDialogFragment();
                    Bundle args = new Bundle();
                    args.putLong("address_list_position", -1); //the -1 is uesd for the main
                    args.putParcelable(AddressDialogFragment.ADDRESS_ARGUMENT, address);
                    fragment.setArguments(args);
                    fragment.show(mActivity.getSupportFragmentManager(), AddressDialogFragment.class.getSimpleName());
                } else {
                    Toasty.warning(requireContext(), "Cette adresse est favorite, elle ne peut être modifiée !!!",Toast.LENGTH_LONG).show();
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
        if(!((OrderDetailActivity) requireContext()).isFinishing())
            Toasty.success(requireContext(), "L'adress a été modifié avec succès...", Toast.LENGTH_LONG).show();
        if (addressPosition < 0) { //we refresh the displayed main address information
            mClient.getAdresses().remove(0);
            mClient.getAdresses().add(address);
            mBinding.setAddress(address);
            if (App.isConnected) {
                OrderSteed steed = mActivity.getOrderSteed();
                steed.getReceiver().getAdresses().clear();
                steed.getReceiver().getAdresses().add(address);
                if (steed.getKey() == null || steed.getKey().isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                            .setCancelable(false)
                            .setIcon(R.drawable.ic_round_warning_24)
                            .setTitle(R.string.warning)
                            .setMessage("Une erreur est survenue. Vous allez être redirigé à la page précédente.")
                            .setPositiveButton("Ok", (dialog, which) -> mActivity.finish());
                    if(!((OrderDetailActivity) requireContext()).isFinishing())
                        builder.create().show();
                }else {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                            .child("courses")
                            .child(steed.getKey());
                    reference.setValue(steed);
                }
            }
        } else {
            mAdapter.refreshEntry(address, addressPosition);
        }
    }

    @Override
    public void onAddressUpdatesPersistence(final Address address, long position) {
    }

    private void getDistanceClientDeliveryMan(final String userType, String clientLatLng) {
        try {
            if (mActivity.getLocation() == null)return;
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
