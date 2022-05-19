package com.israel.livraisonexpresspos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Api;
import com.israel.livraisonexpresspos.data.Room.repository.CommentRepository;
import com.israel.livraisonexpresspos.data.Room.repository.ContactRemoteRepository;
import com.israel.livraisonexpresspos.data.Room.repository.ContactRepository;
import com.israel.livraisonexpresspos.data.Room.repository.OrderRepository;
import com.israel.livraisonexpresspos.data.Room.repository.UnSyncedRepository;
import com.israel.livraisonexpresspos.listeners.CheckConnection;
import com.israel.livraisonexpresspos.listeners.OnConnectionStateChangeListener;
import com.israel.livraisonexpresspos.models.Comment;
import com.israel.livraisonexpresspos.models.Contact;
import com.israel.livraisonexpresspos.models.ContactTable;
import com.israel.livraisonexpresspos.models.Delivery;
import com.israel.livraisonexpresspos.models.Order;
import com.israel.livraisonexpresspos.models.UnSynced;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.models.from_steed_app.Badge;
import com.israel.livraisonexpresspos.models.from_steed_app.OrderSteed;
import com.israel.livraisonexpresspos.receivers.ConnectivityReceiver;
import com.israel.livraisonexpresspos.ui.checkout.CheckoutViewModel;
import com.israel.livraisonexpresspos.ui.contacts.ContactViewModel;
import com.israel.livraisonexpresspos.ui.filter.FilterViewModel;
import com.israel.livraisonexpresspos.ui.inventory_management.StockInventoryActivity;
import com.israel.livraisonexpresspos.ui.modules.ModuleActivity;
import com.israel.livraisonexpresspos.ui.new_contact.NewContactActivity;
import com.israel.livraisonexpresspos.ui.notifications.NotificationActivity;
import com.israel.livraisonexpresspos.ui.order_detail.OrderDetailActivity;
import com.israel.livraisonexpresspos.ui.orders.OrderListAdapter;
import com.israel.livraisonexpresspos.ui.orders.OrderViewModel;
import com.israel.livraisonexpresspos.ui.select_contact.OnContactSelectedListener;
import com.israel.livraisonexpresspos.ui.settings.OnCityFilterChangeListener;
import com.israel.livraisonexpresspos.uiComponents.Utilities;
import com.israel.livraisonexpresspos.uiComponents.ViewAnimation;
import com.israel.livraisonexpresspos.utils.CourseStatus;
import com.israel.livraisonexpresspos.utils.PreferenceUtils;
import com.israel.livraisonexpresspos.utils.Values;
import com.onesignal.OneSignal;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        NavController.OnDestinationChangedListener, OnContactSelectedListener, OnConnectionStateChangeListener, OnCityFilterChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String CONTACT = "contact";
    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView mNavigationView;
    private FloatingActionButton mFab;
    private OrderViewModel mOrderViewModel;
    private FilterViewModel mFilterViewModel;
    private MutableLiveData<List<OrderSteed>> mOrderList;
    private ConnectivityReceiver mReceiver;
    private TextView mTextViewUnassigned, mTextViewToBeTreated, mTextViewMyOrder, mTextViewToday, mTextViewCancel, mTextViewToValidate;
    private EditText mEditTextSearch;
    private Toast mBackToast;
    private long mBackPressedTime;
    private Timer mTimer;
    private boolean mContactFragment;
    private NavController mNavController;
    private List<String> objects = new ArrayList<>();
    private DrawerLayout mDrawerLayout;
    private final List<Integer> mCommentIds = new ArrayList<>();

    private MutableLiveData<String> filterConstraint = new MutableLiveData<>();
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewModel();
        mUser = User.getCurrentUser(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFab = findViewById(R.id.fab);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);

        mTextViewUnassigned = mNavigationView.getMenu().findItem(R.id.nav_unassigned).getActionView().findViewById(R.id.tvCounter);
        mTextViewToBeTreated = mNavigationView.getMenu().findItem(R.id.nav_to_be_treated).getActionView().findViewById(R.id.tvCounter);
        mTextViewMyOrder = mNavigationView.getMenu().findItem(R.id.nav_my_orders).getActionView().findViewById(R.id.tvCounter);
        mTextViewToday = mNavigationView.getMenu().findItem(R.id.nav_today).getActionView().findViewById(R.id.tvCounter);
        mTextViewCancel = mNavigationView.getMenu().findItem(R.id.nav_cancel).getActionView().findViewById(R.id.tvCounter);
        mTextViewToValidate = mNavigationView.getMenu().findItem(R.id.nav_to_validate).getActionView().findViewById(R.id.tvCounter);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_unassigned, R.id.nav_to_be_treated, R.id.nav_today, R.id.nav_my_shops,
                R.id.nav_my_orders, R.id.nav_place_order, R.id.nav_client,R.id.nav_filter,
                R.id.nav_settings, R.id.nav_stat_delivery_men, R.id.nav_cancel, R.id.nav_to_validate, R.id.nav_stat_sales,
                R.id.nav_my_contacts
        ).setOpenableLayout(mDrawerLayout).build();

        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        handleRoles(mNavigationView.getMenu());

        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(mNavigationView, mNavController);

        mNavController.addOnDestinationChangedListener(this);
        if (mUser != null){
            OneSignal.setEmail(mUser.getEmail(), mUser.getOnesignal_email_auth_hash(), new OneSignal.EmailUpdateHandler() {
                @Override
                public void onSuccess() {
                    Log.e("SUCCESS", "TRUE");
                }

                @Override
                public void onFailure(OneSignal.EmailUpdateError error) {
                    Log.e("SUCCESS", "FALSE");
                }
            });
        }

        mTimer = new Timer();
        mTimer.schedule(new CheckConnection(this), 0, 5000);
        mNavigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mDrawerLayout.closeDrawers();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.ic_round_warning_24)
                        .setTitle("Attention!")
                        .setMessage("Vous allez être déconnecté.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Values.logout(MainActivity.this);
                            }
                        })
                        .setNegativeButton("Annuler", null);
                builder.create().show();
                return false;
            }
        });

        /*added by marlonne */
        mNavigationView.getMenu().findItem(R.id.nav_stock).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(getBaseContext(), StockInventoryActivity.class);
                startActivity(intent);
                return false;
            }
        });


//        Intent intent = new Intent(this, NotificationService.class);
//        startService(intent);
        initHeader();
    }



    public void performFilter(String constraint) {
        this.filterConstraint.setValue(constraint);
        if (!constraint.isEmpty()) {
                mFilterViewModel.getOrders(constraint);
                mFilterViewModel.setIsLoading(true);
        }else {
            mFilterViewModel.setConstraint("");
        }
    }

    private void initHeader() {
        View v = mNavigationView.getHeaderView(0);
        TextView textViewFullName = v.findViewById(R.id.tvFullName);
        TextView textViewEmail = v.findViewById(R.id.tvEmail);
        TextView textViewVersion = v.findViewById(R.id.tvVersion);
        User user = mUser;
        if (user == null)return;
        textViewFullName.setText(user.getFullname());
        textViewEmail.setText(user.getEmail());
        textViewVersion.setText("Version " + BuildConfig.VERSION_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OrderListAdapter.REQUEST_DETAILS) {
            List<OrderSteed> newList = mOrderList.getValue();
            if (resultCode == RESULT_OK && data != null && newList != null) {
//                mOrderViewModel.fetchOrderBadges();
                OrderSteed retrievedOrder = data.getParcelableExtra("current_order");
                int retrievedOrderPosition = data.getIntExtra("current_list_position",0);
                if (TextUtils.equals(mOrderViewModel.getState(), CourseStatus.BADGE_UNASSIGNED)){
                    if (retrievedOrder.getInfos().getStatut() != CourseStatus.CODE_UNASSIGNED){
                        newList.remove(retrievedOrderPosition);
                        mOrderList.setValue(newList);
                        mOrderViewModel.getUnassignedList().setValue(newList);
                    }
                }else if (TextUtils.equals(mOrderViewModel.getState(),CourseStatus.BADGE_TO_BE_TREAT)){
                    if (retrievedOrder.getInfos().getStatut() == CourseStatus.CODE_DELIVERED){
                        newList.remove(retrievedOrderPosition);
                        mOrderList.setValue(newList);
                        mOrderViewModel.getToBeTreatedList().setValue(newList);
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.currentActivity = this;
    }

    private void initViewModel() {
        mOrderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        mFilterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);
        mOrderList = new MutableLiveData<>();
        mOrderViewModel.fetchFromFirebase(this);
        clearSessionSearchPatterns();
        handleOrdersSearch();
        handleBadges();
        mOrderViewModel.getQuarters();
    }

    private void handleBadges() {
        mOrderViewModel.getBadge().observe(this, new Observer<Badge>() {
            @Override
            public void onChanged(Badge badge) {
                if (badge == null)return;
                mTextViewUnassigned.setText(String.valueOf(badge.getUnassigned()));
                mTextViewMyOrder.setText(String.valueOf(badge.getMyOrders()));
                mTextViewToBeTreated.setText(String.valueOf(badge.getToBeTreated()));
                mTextViewToday.setText(String.valueOf(badge.getOftoday()));
                mTextViewCancel.setText(String.valueOf(badge.getCancelled()));
                mTextViewToValidate.setText(String.valueOf(badge.getToValidate()));
            }
        });
    }

    private void clearSessionSearchPatterns() {
        PreferenceUtils.setString(this, PreferenceUtils.SEARCH_PATTERN_ + CourseStatus.BADGE_UNASSIGNED, "");
        PreferenceUtils.setString(this, PreferenceUtils.SEARCH_PATTERN_ + CourseStatus.BADGE_MY_RACES, "");
        PreferenceUtils.setString(this, PreferenceUtils.SEARCH_PATTERN_ + CourseStatus.BADGE_TO_BE_TREAT, "");
        PreferenceUtils.setString(this, PreferenceUtils.SEARCH_PATTERN_ + CourseStatus.BADGE_OF_TODAY, "");
        PreferenceUtils.setString(this, PreferenceUtils.SEARCH_PATTERN_ + CourseStatus.BADGE_CANCELLED, "");
        PreferenceUtils.setString(this, PreferenceUtils.SEARCH_PATTERN_ + CourseStatus.BADGE_TO_VALIDATE, "");
        PreferenceUtils.setString(this, PreferenceUtils.SEARCH_PATTERN_ + CONTACT, "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_notification) {
            Intent intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @SuppressLint({"NonConstantResourceId", "RestrictedApi"})
    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        mContactFragment = false;
        mFab.setVisibility(View.GONE);
        mFab.setOnClickListener(null);
        mEditTextSearch.clearFocus();
        LinearLayout layoutSearch = findViewById(R.id.layoutSearch);
        layoutSearch.setVisibility(View.VISIBLE);
        mOrderViewModel.clearRequests();
        Utilities.hideKeyBoard(this);
        switch (destination.getId()) {
            case R.id.nav_unassigned:
                mOrderViewModel.setState(CourseStatus.BADGE_UNASSIGNED);
                mOrderList = mOrderViewModel.getUnassignedList();
                break;
            case R.id.nav_to_validate:
                mOrderViewModel.setState(CourseStatus.BADGE_TO_VALIDATE);
                mOrderList = mOrderViewModel.getToValidateList();
                break;
            case R.id.nav_my_orders:
                mOrderViewModel.setState(CourseStatus.BADGE_MY_RACES);
                mOrderList = mOrderViewModel.getMyOrdersList();
                break;
            case R.id.nav_to_be_treated:
                mOrderViewModel.setState(CourseStatus.BADGE_TO_BE_TREAT);
                mOrderList = mOrderViewModel.getToBeTreatedList();
                break;
            case R.id.nav_today:
                mOrderViewModel.setState(CourseStatus.BADGE_OF_TODAY);
                mOrderList = mOrderViewModel.getTodayList();
                break;
            case R.id.nav_cancel:
                mOrderViewModel.setState(CourseStatus.BADGE_CANCELLED);
                mOrderList = mOrderViewModel.getCancelledList();
                break;
            case R.id.nav_place_order:
                layoutSearch.setVisibility(View.GONE);
                ViewAnimation.fadeIn(mFab);
                mFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Values.order = null;
                        startActivity(new Intent(getApplicationContext(), ModuleActivity.class));
                    }
                });
                break;
            case R.id.nav_client:
                mContactFragment = true;
                ViewAnimation.fadeIn(mFab);
                mFab.setOnClickListener(v ->
                        startActivity(new Intent(getApplicationContext(), NewContactActivity.class)));
                break;
            case R.id.nav_my_shops:
                layoutSearch.setVisibility(View.GONE);
                mOrderViewModel.setOrderStatistics(true);
                break;
            case R.id.nav_stat_sales:
                layoutSearch.setVisibility(View.GONE);
                mOrderViewModel.setOrderStatistics(false);
                break;
            case R.id.nav_my_contacts:
                layoutSearch.setVisibility(View.GONE);
                ViewAnimation.fadeIn(mFab);
                mFab.setOnClickListener(v ->
                        startActivity(new Intent(getApplicationContext(), NewContactActivity.class)));
                break;
            case R.id.nav_settings:
            case R.id.nav_filter:
            case R.id.nav_stat_delivery_men:
                layoutSearch.setVisibility(View.GONE);
                //todo :
                break;
        }
        if (mContactFragment){
            mEditTextSearch.setText(PreferenceUtils.getString(this, PreferenceUtils.SEARCH_PATTERN_ + CONTACT));
        }else {
            mEditTextSearch.setText(PreferenceUtils.getString(this, PreferenceUtils.SEARCH_PATTERN_ + mOrderViewModel.getState()));
        }
        invalidateOptionsMenu();
    }

    private void handleRoles(Menu navMenu) {
        NavInflater inflater = mNavController.getNavInflater();
        NavGraph navGraph = inflater.inflate(R.navigation.mobile_navigation);
        User.getCurrentUser(this);
        if (User.isCommercial()){
            mNavigationView.setCheckedItem(R.id.nav_client);
            navGraph.setStartDestination(R.id.nav_client);
            navMenu.findItem(R.id.group_client).setVisible(true);
        }
        if (User.isDeliveryMan()){
            mNavigationView.setCheckedItem(R.id.nav_unassigned);
            navGraph.setStartDestination(R.id.nav_unassigned);
            navMenu.findItem(R.id.group_delivery).setVisible(true);
        }
        if (User.isPartner()){
            mNavigationView.setCheckedItem(R.id.nav_today);
            navGraph.setStartDestination(R.id.nav_today);
            navMenu.findItem(R.id.group_delivery).setVisible(true);
            navMenu.findItem(R.id.group_pos).setVisible(true);
            navMenu.findItem(R.id.nav_filter).setVisible(true);
            navMenu.findItem(R.id.nav_cancel).setVisible(true);
            navMenu.findItem(R.id.nav_place_order).setVisible(false);
            navMenu.findItem(R.id.nav_my_shops).setVisible(true);
            navMenu.findItem(R.id.nav_stat_sales).setVisible(true);
            if (!User.isDeliveryMan()){
                navMenu.findItem(R.id.nav_my_orders).setVisible(false);
                navMenu.findItem(R.id.nav_unassigned).setVisible(false);
                navMenu.findItem(R.id.nav_to_be_treated).setVisible(false);
            }
        }
        if (User.isManager()){
            mNavigationView.setCheckedItem(R.id.nav_place_order);
            navGraph.setStartDestination(R.id.nav_place_order);
            navMenu.findItem(R.id.group_delivery).setVisible(true);
            navMenu.findItem(R.id.group_pos).setVisible(true);
            navMenu.findItem(R.id.group_client).setVisible(true);
            if(!User.isDeliveryMan())navMenu.findItem(R.id.nav_my_orders).setVisible(false);
            navMenu.findItem(R.id.nav_filter).setVisible(true);
            navMenu.findItem(R.id.nav_cancel).setVisible(true);
            navMenu.findItem(R.id.nav_to_validate).setVisible(true);
        }
        mNavController.setGraph(navGraph);
    }

    @Override
    protected void onDestroy() {
//        mReceiver.unregister();
//        Intent intent = new Intent(this, NotificationService.class);
//        stopService(intent);
        mTimer.cancel();
        super.onDestroy();
    }

    @Override
    public void onContactSelected(Contact contact, boolean newOrder) {
        if (newOrder){
            Values.order = null;
            Values.receiver = contact;
            startActivity(new Intent(this, ModuleActivity.class));
        }
    }

    private void handleOrdersSearch(){
        mEditTextSearch = findViewById(R.id.etSearch);
        ImageButton buttonSearch = findViewById(R.id.ibSearch);
        final ContactViewModel contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.hideKeyBoard(MainActivity.this);
                mEditTextSearch.clearFocus();
                String searchPattern = "";
                if (!TextUtils.isEmpty(mEditTextSearch.getText())){
                    searchPattern = mEditTextSearch.getText().toString();
                }
                if (mContactFragment){
                    PreferenceUtils.setString(MainActivity.this, PreferenceUtils.SEARCH_PATTERN_ + CONTACT, searchPattern);
                    contactViewModel.fetchContacts(searchPattern);
                }else {
                    PreferenceUtils.setString(MainActivity.this, PreferenceUtils.SEARCH_PATTERN_ + mOrderViewModel.getState(), searchPattern);
                    mOrderViewModel.filter(mOrderList);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawers();
        }else {
            if (mNavController.getCurrentDestination().getId()
                    == mNavController.getGraph().getStartDestination()){
                if (mBackPressedTime + 3000 > System.currentTimeMillis()){
                    if(mBackToast != null)mBackToast.cancel();
                    super.onBackPressed();
                }else {
                    mBackToast = Toast.makeText(this, "Cliquez 2 fois pour quitter", Toast.LENGTH_SHORT);
                    mBackToast.show();
                }
            } else {
                super.onBackPressed();
            }

            mBackPressedTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onConnectionChange(final boolean isConnected) {
        Log.e(TAG, "onConnectionChange: "+isConnected );
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OrderRepository orderRepository = new OrderRepository(getApplication());
                ContactRepository contactRepository = new ContactRepository(getApplication());
                CommentRepository commentRepository = new CommentRepository(getApplication());
                UnSyncedRepository unSyncedRepository = new UnSyncedRepository(getApplication());
                LiveData<List<Order>> orders = orderRepository.getUnSyncedOrders();
                LiveData<List<ContactTable>> contacts = contactRepository.getUnSyncedContacts();
                LiveData<List<Comment>> comments = commentRepository.getUnSyncedComments();
                LiveData<List<UnSynced>> unSyncedList = unSyncedRepository.getUnSynced();
                if (isConnected){
                    syncOrders(orders);
                    syncContacts(contacts);
                    syncStatusAndAddress(unSyncedList);
                    syncComments(comments);
                }else {
                    orders.removeObservers(MainActivity.this);
                    unSyncedList.removeObservers(MainActivity.this);
                    objects.clear();
                }
            }
        });
    }

    private void syncStatusAndAddress(LiveData<List<UnSynced>> unSyncedList){
        Log.e("UNSYNCED", "YEP");
        if (unSyncedList.hasObservers())return;
        unSyncedList.observe(this, unSynceds -> {
            if (unSynceds == null)return;
            syncDataAsync(unSynceds);
        });
    }

    @Override
    public void onCityFilterChange() {
        mOrderViewModel.setOrders(mOrderViewModel.getSteeds());
    }

    private void syncDataAsync(List<UnSynced> unSynceds){
        new Thread(() -> {
            for (UnSynced unSynced : unSynceds){
                boolean canAdd = true;
                for (String s : objects){
                    if (s.equals(unSynced.getObject())) {
                        canAdd = false;
                        break;
                    }
                }
                if (canAdd){
                    objects.add(unSynced.getObject());
                    switch (unSynced.getType()) {
                        case UnSynced.STATUS:
                            mOrderViewModel.syncStatus(unSynced);
                            break;
                        case UnSynced.ADDRESS:
                            mOrderViewModel.syncAddress(unSynced);
                            break;
                        case UnSynced.ERROR:
                            postError(unSynced);
                            break;
                        default:
                            break;
                    }
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void syncOrders(LiveData<List<Order>> orders) {
        final CheckoutViewModel viewModel = new ViewModelProvider(this).get(CheckoutViewModel.class);
        orders.observe(this, new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                if (orders == null) return;
                for (Order order : orders) {
                    Delivery delivery = new Gson().fromJson(order.getStringDelivery(), new TypeToken<Delivery>() {
                    }.getType());
                    Log.e("======DELIVERY", delivery.toString());
                    viewModel.placeOrder(order, delivery);
                }
            }
        });

        viewModel.getSuccess().observe(this, new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                if (order == null) return;
                viewModel.updateOrder(order);
            }
        });
    }

    private void syncComments(LiveData<List<Comment>> comments) {
        if (comments.hasObservers())return;
        comments.observe(this, (commentList) -> {
            if (commentList == null || commentList.isEmpty()) return;
            for (Comment c : commentList){
                if (!mCommentIds.contains(c.getLocalId())){
                    postComment(c);
                    mCommentIds.add(c.getLocalId());
                }
            }

        });
    }

    private void syncContacts(LiveData<List<ContactTable>> liveContacts) {
        if (liveContacts.hasActiveObservers())return;
        final ContactRemoteRepository remoteRepository = new ContactRemoteRepository(getApplication());
        final ContactRepository repository = new ContactRepository(getApplication());
        liveContacts.observe(this, contacts -> {
            if (contacts == null)return;
            for (ContactTable table : contacts){
                remoteRepository.postContact(table);
            }
        });

        remoteRepository.getContactTable().observe(this, contact -> {
            if (contact == null)return;
            repository.upsert(contact);
        });
    }

    private void postError(UnSynced unSynced){
        Map<String, String> body = new Gson().fromJson(unSynced.getObject(), new TypeToken<HashMap<String, String>>(){}.getType());
        if (mUser == null)return;
        Api.error().postError(mUser.getToken(), body)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null){
                            UnSyncedRepository repository = new UnSyncedRepository(getApplication());
                            repository.delete(unSynced);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

                    }
                });
    }

    private void postComment(Comment comment){
        if (mCommentIds.contains(comment.getLocalId()))return;
        Log.e("POST COMMENT", comment.getComment());
        int i = 0;
        int index = 0;

        for (Integer id : mCommentIds){
            if (id == comment.getLocalId()){
                index = i;
                break;
            }
            i++;
        }
        try {
            int finalIndex = index;
            Api.order().postComment(mUser.getToken(), "course", Integer.parseInt(comment.getCommentable_id()), mUser.getId(), comment.getComment())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                            if (response.isSuccessful() && response.body() != null){
                                CommentRepository repository = new CommentRepository(getApplication());
                                repository.delete(comment);
                            }
                            mCommentIds.remove(finalIndex);
                        }

                        @Override
                        public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                            mCommentIds.remove(finalIndex);
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
            App.handleError(e);
            mCommentIds.remove(index);
        }

    }
}