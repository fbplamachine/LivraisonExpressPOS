package com.israel.livraisonexpresspos.ui.order_detail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Api;
import com.israel.livraisonexpresspos.data.Room.repository.UnSyncedRepository;
import com.israel.livraisonexpresspos.databinding.ActivityOrderDetailsBinding;
import com.israel.livraisonexpresspos.models.Address;
import com.israel.livraisonexpresspos.models.Comment;
import com.israel.livraisonexpresspos.models.UnSynced;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.models.from_steed_app.OrderSteed;
import com.israel.livraisonexpresspos.ui.address.OnAddressAdded;
import com.israel.livraisonexpresspos.ui.address.OnAddressUpdate;
import com.israel.livraisonexpresspos.ui.order_detail.attach.AttachFragment;
import com.israel.livraisonexpresspos.ui.order_detail.client.ClientFragment;
import com.israel.livraisonexpresspos.ui.order_detail.comment.CommentFragment;
import com.israel.livraisonexpresspos.ui.order_detail.comment.CommentViewModel;
import com.israel.livraisonexpresspos.ui.order_detail.content.ContentFragment;
import com.israel.livraisonexpresspos.ui.order_detail.content.delivery_men.OnDeliveryMenSelectedListener;
import com.israel.livraisonexpresspos.ui.order_detail.content.feed.FeedDialogFragment;
import com.israel.livraisonexpresspos.ui.order_detail.receiver.ReceiverFragment;
import com.israel.livraisonexpresspos.ui.order_detail.resume.ResumeFragment;
import com.israel.livraisonexpresspos.ui.order_detail.sender.SenderFragment;
import com.israel.livraisonexpresspos.ui.orders.OrderListAdapter;
import com.israel.livraisonexpresspos.uiComponents.ViewPagerAdapter;
import com.israel.livraisonexpresspos.utils.OrderUtils;
import com.israel.livraisonexpresspos.utils.Values;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity implements LocationListener, OnAddressUpdate,
        OnAddressAdded, ViewPager.OnPageChangeListener, OnDeliveryMenSelectedListener, Toolbar.OnMenuItemClickListener {
    public static final String ORDER_ID = "orderId";
    private ActivityOrderDetailsBinding mBinding;
    private OrderSteed mOrderSteed;
    private LatLng mLocation;
    private boolean mReloadSenderAddresses;
    private boolean mReloadReceiverAddresses;
    private boolean mFromNotification;
    private boolean mFromComment;
    private ViewPagerAdapter mAdapter;
    private UnSyncedRepository mUnSyncedRepository;
    private boolean firstTime = true;
    private View mCommentTab;
    private LocationManager mManager;
    private User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);
        mBinding.setIsSetOrder(false);
        mUnSyncedRepository = new UnSyncedRepository(getApplication());
        mUser = User.getCurrentUser(this);
        initToolbar();
        getOrder();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mManager == null)return;
        mManager.removeUpdates(this);
        mManager = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.currentActivity = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    private void initComments() {
        CommentViewModel viewModel = new ViewModelProvider(this).get(CommentViewModel.class);
        viewModel.fetchCommentsFromFirebase(mOrderSteed.getInfos().getId());
        viewModel.getComments(mOrderSteed.getInfos().getId()).observe(this, new Observer<List<Comment>>() {
            @Override
            public void onChanged(List<Comment> comments) {
                if (comments == null || comments.isEmpty())return;
                if (firstTime){
                    firstTime = false;
                }else if(mBinding.viewPager.getCurrentItem() != 0){
                    View viewNewComment = mCommentTab.findViewById(R.id.viewNewMessage);
                    viewNewComment.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void clearNewCommentView(){
        View viewNewComment = mCommentTab.findViewById(R.id.viewNewMessage);
        viewNewComment.setVisibility(View.GONE);
    }

    private void getOrder() {
        Intent intent = getIntent();
        mFromNotification = intent.getBooleanExtra(App.FROM_NOTIFICATION, false);
        mFromComment = intent.getBooleanExtra(App.FROM_COMMENT, false);
        if (intent.hasExtra(OrderListAdapter.CURRENT_ORDER)){
            mOrderSteed = getIntent().getParcelableExtra(OrderListAdapter.CURRENT_ORDER);
            initUI();
        } else if (intent.hasExtra(ORDER_ID)){
            int orderId = intent.getIntExtra(ORDER_ID, 0);
            if (orderId > 0){
                fetchOrder(orderId);
            }
        }
    }

    private void initLocation() {
        if (OrderUtils.locationEnabled()){
            mManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }else {
                mManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000
                        , 100, this);
            }
        }else {
            OrderUtils.enableLocationDialog(this);
        }
    }

    private void initToolbar() {
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mBinding.toolbar.setOnMenuItemClickListener(this);
        if (User.isManager() || User.isPartner()){
            mBinding.toolbar.getMenu().getItem(0).setVisible(true);
        }
    }

    private void initUI() {
        mCommentTab = LayoutInflater.from(this).inflate(R.layout.custom_comment_tab, null);
        mBinding.setIsSetOrder(true);
        setupViewPager();

        mBinding.tabLayout.getTabAt(0).setCustomView(mCommentTab);
        mBinding.viewPager.setCurrentItem(mFromComment ? 0 : 1);
        mBinding.viewPager.addOnPageChangeListener(this);
        initComments();
        Log.e("ORDER", mOrderSteed.toString());
    }

    private void setupViewPager() {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new CommentFragment(), "");
        mAdapter.addFragment(new ContentFragment(), getString(R.string.content_details_fragment_title));
        mAdapter.addFragment(new ReceiverFragment(), getString(R.string.destination));
        if (User.isManager()){
            mAdapter.addFragment(new ResumeFragment(), getString(R.string.resume));
        }
        mAdapter.addFragment(new AttachFragment(), getString(R.string.pj));
        if(!User.isPartner()){
            mAdapter.addFragment(new ClientFragment(), getResources().getString(R.string.client_details_fragment_title));
        }
        if (!User.isDeliveryMan() || User.isManager()){
            mAdapter.addFragment(new SenderFragment(), getResources().getString(R.string.sender_details_fragment_title));
        }
        mBinding.viewPager.setAdapter(mAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
    }

    public OrderSteed getOrderSteed() {
        return mOrderSteed;
    }

    public LatLng getLocation() {
        return mLocation;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 1){
            boolean allGranted = true;
            for (int result : grantResults){
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted){
                initLocation();
            }else {
               finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        mLocation = new LatLng(location.getLatitude(),location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    public boolean isReloadSenderAddresses() {
        return mReloadSenderAddresses;
    }

    public void setReloadSenderAddresses(boolean reloadSenderAddresses) {
        mReloadSenderAddresses = reloadSenderAddresses;
    }

    public boolean isReloadReceiverAddresses() {
        return mReloadReceiverAddresses;
    }

    public void setReloadReceiverAddresses(boolean reloadReceiverAddresses) {
        mReloadReceiverAddresses = reloadReceiverAddresses;
    }

    public ActivityOrderDetailsBinding getBinding() {
        return mBinding;
    }

    @Override
    public void onAddressAdded(Address address) {
        int currentPosition = mBinding.viewPager.getCurrentItem();
        ViewPagerAdapter adapter = (ViewPagerAdapter) mBinding.viewPager.getAdapter();
        OnAddressAdded onAddressAdded = (OnAddressAdded) adapter.getItem(currentPosition);
        onAddressAdded.onAddressAdded(address);
    }

    @Override
    public void onAddressAddedPersistence(Address address) {
        //ask for address persistence in our custom cache
        UnSynced unSyncedAddress = new UnSynced();
        unSyncedAddress.setDateTime(Calendar.getInstance().getTimeInMillis());
        unSyncedAddress.setType(UnSynced.ADDRESS);
        unSyncedAddress.setObject(new Gson().toJson(address));
        mUnSyncedRepository.insert(unSyncedAddress); //the following should maybe be include in a callback function
        int currentPosition = mBinding.viewPager.getCurrentItem();
        ViewPagerAdapter adapter = (ViewPagerAdapter) mBinding.viewPager.getAdapter();
        OnAddressAdded onAddressAdded = (OnAddressAdded) adapter.getItem(currentPosition);
        onAddressAdded.onAddressAdded(address);
    }

    @Override
    public void onAddressUpdated(Address address, long addressPosition) {
        int currentItemPosition = mBinding.viewPager.getCurrentItem();
        ViewPagerAdapter adapter = (ViewPagerAdapter) mBinding.viewPager.getAdapter();
        OnAddressUpdate onAddressUpdate = (OnAddressUpdate) adapter.getItem(currentItemPosition);
        onAddressUpdate.onAddressUpdated(address,addressPosition);
    }

    @Override
    public void onAddressUpdatesPersistence(final Address address, final long addressPosition) {
        //ask for FS persistence
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("courses")
                .child(mOrderSteed.getKey());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //persist manually
                UnSynced unSyncedAddress = new UnSynced();
                unSyncedAddress.setDateTime(Calendar.getInstance().getTimeInMillis());
                unSyncedAddress.setObject(new Gson().toJson(address));
                unSyncedAddress.setType(UnSynced.ADDRESS);
                mUnSyncedRepository.insert(unSyncedAddress); //doubt about insertion should callback to test the status before proceeding
                int currentItemPosition = mBinding.viewPager.getCurrentItem();
                ViewPagerAdapter adapter = (ViewPagerAdapter) mBinding.viewPager.getAdapter();
                if (adapter == null)return;
                Fragment fragment = adapter.getItem(currentItemPosition);
                if (fragment instanceof OnAddressUpdate){
                    OnAddressUpdate onAddressUpdate = (OnAddressUpdate) fragment;
                    onAddressUpdate.onAddressUpdated(address,addressPosition);
                    if (currentItemPosition == 1 && addressPosition < 0){ // change has been made from sender fragment
                        mOrderSteed.getSender().getAdresses().remove(0);
                        mOrderSteed.getSender().getAdresses().add(address);
                    }else if (currentItemPosition == 2 && addressPosition < 0){ // change has been made from sender fragment
                        mOrderSteed.getReceiver().getAdresses().remove(0);
                        mOrderSteed.getReceiver().getAdresses().add(address);
                    }
                    if (!isFinishing())Toasty.success(getApplicationContext(), "L'adresse a été modifiée avec succès...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (!isFinishing())Toasty.success(getApplicationContext(), "L'adresse n'a pas été modifiée, recommencez plus tard...", Toast.LENGTH_LONG).show();
            }
        });
        reference.setValue(mOrderSteed);
    }


    private void fetchOrder(int id){
        if (mUser == null){
            mUser = User.getCurrentUser(this);
            if (mUser == null || mUser.getToken() == null){
                Values.reset(this);
                return;
            }
        }
        Api.order().getOrder(mUser.getToken(), id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null){
                    try {
                        String output = response.body().string();
                        JSONObject object = new JSONObject(output);
                        if (object.getBoolean("success")){
                            mOrderSteed = new Gson().fromJson(object.getJSONObject("data").toString(),
                                    new TypeToken<OrderSteed>(){}.getType());
                            initUI();
                        }else {
                            if (!isFinishing()) Toasty.error(getApplicationContext(), "Une erreur est survenu").show();
                            mBinding.progressBar.setVisibility(View.GONE);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        App.handleError(e);
                    }
                }else {
                    if (!isFinishing())Toasty.error(getApplicationContext(), "Une erreur est survenu").show();
                    mBinding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                if (!isFinishing())Toasty.error(getApplicationContext(), "Vérifiez votre connexion internet").show();
                mBinding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mFromNotification){
            if (User.getCurrentUser(this) == null){
                Values.reset(this);
            }else {
                Values.home(this);
            }
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == 0){
            clearNewCommentView();
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0){
            clearNewCommentView();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onDeliveryMenSelected(User user) {
        int currentPosition = mBinding.viewPager.getCurrentItem();
        OnDeliveryMenSelectedListener listener = (OnDeliveryMenSelectedListener) mAdapter.getItem(currentPosition);
        listener.onDeliveryMenSelected(user);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.item_feed_displayer){
            if(mOrderSteed != null){
                //todo : we display the feed dialog
                FeedDialogFragment feedDialogFragment = new FeedDialogFragment();
                Bundle arguments = new Bundle();
                arguments.putBoolean("config_changed", false);
                arguments.putInt("order_id",mOrderSteed.getInfos().getId());
                feedDialogFragment.setArguments(arguments);
                feedDialogFragment.show(this.getSupportFragmentManager(),FeedDialogFragment.class.getSimpleName());
            }
        }
        return false;
    }
}
