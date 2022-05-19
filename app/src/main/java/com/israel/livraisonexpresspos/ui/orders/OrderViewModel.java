package com.israel.livraisonexpresspos.ui.orders;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.MainActivity;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.data.Api;
import com.israel.livraisonexpresspos.data.Room.repository.UnSyncedRepository;
import com.israel.livraisonexpresspos.models.Address;
import com.israel.livraisonexpresspos.models.Comment;
import com.israel.livraisonexpresspos.models.Notification;
import com.israel.livraisonexpresspos.models.Quarter;
import com.israel.livraisonexpresspos.models.UnSynced;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.models.from_steed_app.Badge;
import com.israel.livraisonexpresspos.models.from_steed_app.OrderSteed;
import com.israel.livraisonexpresspos.ui.settings.SettingsFragment;
import com.israel.livraisonexpresspos.utils.CourseStatus;
import com.israel.livraisonexpresspos.utils.PreferenceUtils;
import com.israel.livraisonexpresspos.utils.Values;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderViewModel extends AndroidViewModel {
    public static final String NOTIFICATION_TITLE = "Course mal formaté";
    public static final String CHANNEL_ID = "ORDER_ERROR_NOTIFICATION";
    public static final String CHANNEL_NAME = "CHANNEL_NAME";
    public static final String CHANNEL_DESCRIPTION = "CHANNEL_DESCRIPTION";
    private final MutableLiveData<List<OrderSteed>> mUnassignedList;
    private final MutableLiveData<List<OrderSteed>> mMyOrdersList;
    private final MutableLiveData<List<OrderSteed>> mCancelledList;
    private final MutableLiveData<List<OrderSteed>> mToBeTreatedList;
    private final MutableLiveData<List<OrderSteed>> mTodayList;
    private final MutableLiveData<List<OrderSteed>> mToValidateList;
    private final MutableLiveData<Boolean> mLoading;
    private final MutableLiveData<Boolean> mBottomLoading;
    private final MutableLiveData<Badge> mBadge;

    private int mUnassignedPage = 1;
    private int mToBeTreatedPage = 1;
    private int mMyOrdersPage = 1;
    private int mTodayPage = 1;

    private String mState;
    private Call<ResponseBody> mCall;
    private UnSyncedRepository mUnSyncedRepository;
    private List<OrderSteed> mSteeds;
    private boolean mIsOrderStatistics;
    private final User mUser;


    public OrderViewModel(@NonNull Application application) {
        super(application);
        mUser = User.getCurrentUser(application);
        mUnassignedList = new MutableLiveData<>();
        mMyOrdersList = new MutableLiveData<>();
        mCancelledList = new MutableLiveData<>();
        mToBeTreatedList = new MutableLiveData<>();
        mTodayList = new MutableLiveData<>();
        mToValidateList = new MutableLiveData<>();
        mLoading = new MutableLiveData<>();
        mBottomLoading = new MutableLiveData<>();
        mBadge = new MutableLiveData<>();
        mUnSyncedRepository = new UnSyncedRepository(application);
    }

    private Map<String, String> buildDefaultConstraint(String searchPattern, boolean forBadge) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        Date date = Calendar.getInstance().getTime();
        Map<String, String> constraint = new HashMap<>();
        constraint.put("ville", "");
        constraint.put("from_date", format.format(date));
        constraint.put("to_date", format.format(date));
        constraint.put("search_customer", searchPattern);
        constraint.put("quartier", "");
        constraint.put("tir_date", "asc");
        constraint.put("quartier_type", "");
        constraint.put("per_page", "20");
        if (mUser.getRoles().contains("gerant"))constraint.put("is_manager", "true");
        if(!forBadge)constraint.put("status", setStatusForState(mState));
        return constraint;
    }

    public String setStatusForState(String state) {
        StringBuilder status = new StringBuilder();
        if (TextUtils.equals(state, CourseStatus.BADGE_TO_BE_TREAT)) {
            status.append(CourseStatus.CODE_ASSIGNED).append(",").append(CourseStatus.CODE_STARTED)
                    .append(",").append(CourseStatus.CODE_INPROGRESS).append(",")
                    .append(CourseStatus.CODE_RELAUNCH);
        } else if (TextUtils.equals(state, CourseStatus.BADGE_OF_TODAY)) {
            status.append(CourseStatus.CODE_ASSIGNED).append(",").append(CourseStatus.CODE_STARTED)
                    .append(",").append(CourseStatus.CODE_INPROGRESS).append(",")
                    .append(CourseStatus.CODE_DELIVERED).append(",").append(CourseStatus.CODE_RELAUNCH);
        } else if (TextUtils.equals(state, CourseStatus.BADGE_UNASSIGNED)) {
            status.append(CourseStatus.CODE_UNASSIGNED);
        } else if (TextUtils.equals(state, CourseStatus.BADGE_MY_RACES)) {
            status.append(CourseStatus.CODE_ASSIGNED).append(",").append(CourseStatus.CODE_STARTED)
                    .append(",").append(CourseStatus.CODE_INPROGRESS).append(",")
                    .append(CourseStatus.CODE_DELIVERED).append(",").append(CourseStatus.CODE_RELAUNCH);
        }
        return status.toString();
    }

    public void clearRequests(){
        if (mCall == null)return;
        mCall.cancel();
    }

//    public void deleteOldOrdersFromFirebase(){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("courses/infos/date_livraison");
//        reference.keepSynced(true);
//        reference.limitToFirst(1);
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                deleteOrlOrdersAsync(snapshot);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//    private void deleteOrlOrdersAsync(DataSnapshot snapshot){
//        new Thread(() -> {
//            final Calendar now = Calendar.getInstance();
//            now.set(Calendar.DAY_OF_MONTH, (now.get(Calendar.DAY_OF_MONTH) - 3));
//            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
//            Date refDate = now.getTime();
//            if (snapshot.exists()){
//                int num = 0;
//                for (DataSnapshot snap : snapshot.getChildren()){
//                    OrderSteed steed = snap.getValue(OrderSteed.class);
//                    if (steed == null)continue;
//                    String orderDeliveryDateString = steed.getInfos().getDate_livraison();
//                    try {
//                        Date orderDeliveryDate =  format.parse(orderDeliveryDateString);
//                        if (orderDeliveryDate == null)continue;
//                        if (orderDeliveryDate.before(refDate)){
//                            num++;
////                            snap.getRef().removeValue();
////                            Log.e("DELETED ORDER N°", String.valueOf(num));
//                            Log.e("*************----date livrason courses**--", String.valueOf(orderDeliveryDate));
//                        }
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                        App.handleError(e);
//                    }
//                }
//            }
//        }).start();
//    }

//    public void deleteCommentsFromFirebase(){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("comments");
//        reference.limitToFirst(1);
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                deleteOldCommentsAsync(snapshot);
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    private void deleteOldCommentsAsync(DataSnapshot snapshot){
//        new Thread(() -> {
//            final Calendar now = Calendar.getInstance();
//            now.set(Calendar.DAY_OF_MONTH, (now.get(Calendar.DAY_OF_MONTH) - 7));
//            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
//            try {
//                Date refDate = now.getTime();
//                if (snapshot.exists()){
//                    int num = 0;
//                    for (DataSnapshot snap : snapshot.getChildren()){
//                        Comment comment = snap.getValue(Comment.class);
//                        if (comment == null)continue;
//                        String stringDate = comment.getDate();
//                        Date date = format.parse(stringDate);
//                        if (date == null)continue;
//                        if (date.before(refDate)){
//                            num++;
//                            snap.getRef().removeValue();
//                            Log.e("DELETED COMMENT N°", String.valueOf(num));
//                        }
//                    }
//                }
//            }catch (Exception e){
//                App.handleError(e);
//                e.printStackTrace();
//            }
//        }).start();
//    }
//
//    public void deleteNotificationsFromFirebase(){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("notifications");
//        reference.limitToFirst(1);
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                deleteOldNotificationsAsync(snapshot);
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    private void deleteOldNotificationsAsync(DataSnapshot snapshot){
//        new Thread(() -> {
//            final Calendar now = Calendar.getInstance();
//            now.set(Calendar.DAY_OF_MONTH, (now.get(Calendar.DAY_OF_MONTH) - 2));
//            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
//            try {
//                Date refDate = now.getTime();
//                if (snapshot.exists()){
//                    int num = 0;
//                    for (DataSnapshot snap : snapshot.getChildren()){
//                        Notification notification = snap.getValue(Notification.class);
//                        if (notification == null)continue;
//                        String stringDate = notification.getNotification_created_at();
//                        Date date = format.parse(stringDate);
//                        if (date == null)continue;
//                        if (date.before(refDate)){
//                            num++;
//                            snap.getRef().removeValue();
//                            Log.e("DELETED Notification N°", String.valueOf(num));
//                        }
//                    }
//                }
//            }catch (Exception e){
//                App.handleError(e);
//                e.printStackTrace();
//            }
//        }).start();
//    }

    public void fetchUsersZone(){
        if (mUser == null)return;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        reference.limitToFirst(1);
        reference.keepSynced(true);
        Query query = reference.orderByChild("email")
                .equalTo(mUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot snap: snapshot.getChildren()){
                        try{
                            List<Integer> zones = new ArrayList<>();
                            for (DataSnapshot shot: snap.child("zones").getChildren()){
                                zones.add(Integer.parseInt(shot.getValue().toString()));
                            }
                            mUser.setZones(zones);
                            setOrders(mSteeds);
                        }catch (Exception e){
                            e.printStackTrace();
                            App.handleError(e);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void fetchFromFirebase(MainActivity activity){
//        deleteOldOrdersFromFirebase();
//        deleteCommentsFromFirebase();
//        deleteNotificationsFromFirebase();
        fetchUsersZone();
        mLoading.setValue(true);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("courses");
        reference.limitToFirst(1);
        reference.keepSynced(true);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
//        String today = "2021-03-20";
        String today = format.format(Calendar.getInstance().getTime());
        Query query = reference.orderByChild("infos/date_livraison")
                .startAt(today)
                .endAt(today + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mSteeds = new ArrayList<>();
                if (snapshot.exists() && mUser != null){
//                    new FilterOrders().execute(snapshot);
                    filterOrdersAsync(activity, snapshot);
                }else {
                    Badge badge = new Badge();
                    mBadge.setValue(badge);
                    mLoading.setValue(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filterOrdersAsync(MainActivity activity, DataSnapshot snapshot){
        new Thread(() -> {
            List<OrderSteed> steeds = new ArrayList<>();
            for (DataSnapshot snap : snapshot.getChildren()){
                try{
                    OrderSteed steed = snap.getValue(OrderSteed.class);
                    if (steed == null)return;
                    steed.setKey(snap.getKey());
                    Integer zoneId = 0;
                    Integer cityId = 0;
                    if(steed.getReceiver().getAdresses() != null && !steed.getReceiver().getAdresses().isEmpty()){
                        zoneId = steed.getReceiver().getAdresses().get(0).getZone_id();
                        cityId = steed.getReceiver().getAdresses().get(0).getVille_id();
                    }else{
                        Log.e("ORDER_PROBLEM", steed.toString());
                    }
                    List<Integer> userZones = mUser.getZones();
                    List<Integer> userCities = mUser.getCity();
                    boolean isUnAssigned = steed.getInfos().getStatut() == CourseStatus.CODE_UNASSIGNED;
                    if (User.isManager()
                            || User.isPartner()
                            || (isUnAssigned && zoneId == 0 && cityId == 0)
                            || (isUnAssigned && User.isDeliveryMan() && userCities.isEmpty() && (mUser.getZones().isEmpty()))
                            || (isUnAssigned && User.isDeliveryMan() && zoneId == 0 && userCities.contains(cityId))
                            || (isUnAssigned && User.isDeliveryMan() && userCities.contains(cityId) && (userZones == null || userZones.isEmpty()) )
                            || (isUnAssigned && User.isDeliveryMan() && userZones.contains(zoneId))
                            || steed.getInfos().getStatut() == CourseStatus.CODE_CANCELED
                            || steed.getInfos().getCoursiers_ids().contains(mUser.getId())){
                        steeds.add(steed);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    App.handleError(e);
                }
            }

            activity.runOnUiThread(() -> {
                mSteeds = steeds;
                setOrders(steeds);
                mLoading.setValue(false);
            });
        }).start();
    }

    public void setOrders(List<OrderSteed> orders){
        if (orders == null || mUser == null)return;
        List<OrderSteed> unassigned = new ArrayList<>();
        List<OrderSteed> toBeTreated = new ArrayList<>();
        List<OrderSteed> myOrders = new ArrayList<>();
        List<OrderSteed> cancelled = new ArrayList<>();
        List<OrderSteed> toValidate = new ArrayList<>();
        List<OrderSteed> ordersByCity = new ArrayList<>();
        for (OrderSteed steed : orders){
            try{
                if (steed.getReceiver().getAdresses().get(0).getVille_id() == null || steed.getReceiver().getAdresses().get(0).getVille_id() == 0) {
                    ordersByCity.add(steed);
                }else if(steed.getReceiver().getAdresses().get(0).getVille_id() == 1
                        && PreferenceUtils.getTrueBoolean(getApplication(), SettingsFragment.DOUALA_STATUS)){
                    ordersByCity.add(steed);
                }else if (steed.getReceiver().getAdresses().get(0).getVille_id() == 2
                        && PreferenceUtils.getTrueBoolean(getApplication(), SettingsFragment.YAOUNDE_STATUS)){
                    ordersByCity.add(steed);
                }
            }catch (Exception e){
                if (User.isManager()){
                    popNotification(steed);
                }
                e.printStackTrace();
                App.handleError(e);
            }

        }
        for (OrderSteed order : ordersByCity){
            int status = order.getInfos().getStatut();
            if (status == CourseStatus.CODE_UNASSIGNED){
                unassigned.add(order);
            }else if (status == CourseStatus.CODE_ASSIGNED || status == CourseStatus.CODE_STARTED
                    || status == CourseStatus.CODE_INPROGRESS || status == CourseStatus.CODE_RELAUNCH){
                toBeTreated.add(order);
                if (order.getInfos().getCoursiers_ids().contains(mUser.getId()))myOrders.add(order);
            }else if (status == CourseStatus.CODE_DELIVERED){
                if (order.getInfos().getCoursiers_ids().contains(mUser.getId()))myOrders.add(order);
            }else if (status == CourseStatus.CODE_CANCELED){
                cancelled.add(order);
            }else if (status == CourseStatus.CODE_TO_VALIDATE){
                toValidate.add(order);
            }
        }

        Badge badge = new Badge();
        badge.setUnassigned(unassigned.size());
        badge.setToBeTreated(toBeTreated.size());
        badge.setToValidate(toValidate.size());
        badge.setMyOrders(myOrders.size());
        if (User.isPartner()){
            List<OrderSteed> partnerTodayOrders = new ArrayList<>();
            List<OrderSteed> partnerCancelledOrders = new ArrayList<>();
            for (OrderSteed o : ordersByCity) {
                for (int id : mUser.getMagasins_ids()) {
                    try {
                        if (o.getOrders().getliste_articles().get(0).getMagasin_id() == id){
                            partnerTodayOrders.add(o);
                        }
                    }catch (Exception e){
                        App.handleError(e);
                    }
                }
            }
            for (OrderSteed o : cancelled) {
                for (int id : mUser.getMagasins_ids()) {
                    try {
                        if (o.getOrders().getMagasin_id() == id){
                            partnerCancelledOrders.add(o);
                        }
                    }catch (Exception e){
                        App.handleError(e);
                    }
                }
            }
            badge.setOftoday(partnerTodayOrders.size());
            badge.setCancelled(partnerCancelledOrders.size());
            mTodayList.setValue(partnerTodayOrders);
            mCancelledList.setValue(partnerCancelledOrders);
        }else {
            badge.setOftoday(ordersByCity.size());
            badge.setCancelled(cancelled.size());
            mTodayList.setValue(ordersByCity);
            mCancelledList.setValue(cancelled);
        }

        mUnassignedList.setValue(unassigned);
        mToValidateList.setValue(toValidate);
        mToBeTreatedList.setValue(toBeTreated);
        mMyOrdersList.setValue(myOrders);
        mBadge.setValue(badge);
    }

    private void popNotification(OrderSteed steed) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            createNotificationChannel();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplication(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_round_warning_24)
                    .setContentTitle(NOTIFICATION_TITLE)
                    .setContentText("La course avec numéro: " + steed.getInfos().getRef() + " et id: " + steed.getInfos().getId() + ", est mal formatté. Veuillez en informer le service technique.")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("La course avec numéro: " + steed.getInfos().getRef() + " et id: " + steed.getInfos().getId() + ", est mal formatté. Veuillez en informer le service technique."))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setVibrate(new long[0]);
            NotificationManagerCompat.from(getApplication()).notify(1, builder.build());
        }
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager notificationManager = getApplication().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void filter(MutableLiveData<List<OrderSteed>> list){
        List<OrderSteed> orderSteeds = list.getValue();
        list.setValue(new ArrayList<>());
        list.setValue(orderSteeds);
    }

    public List<OrderSteed> getSteeds() {
        return mSteeds;
    }

    public void syncStatus(final UnSynced unSynced){
        Log.e("SEND", "GO");
        try {
            JSONObject object = new JSONObject(unSynced.getObject());
            Call<ResponseBody> call = Api.order().updateOrderStatus(
                    mUser.getToken(),
                    object.getInt("order_id"),
                    object.getString("status"),
                    object.getInt("total_price")
            );
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null){
                        try {
                            String output = response.body().string();
                            JSONObject outputObject = new JSONObject(output);
                            if (outputObject.getBoolean("success")){
                                mUnSyncedRepository.delete(unSynced);
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            App.handleError(e);
                        }
                    }else {
                        if (response.code() == 401){
                            Values.unAuthorizedDialog();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            App.handleError(e);
        }
    }

    public void syncAddress(final UnSynced unSynced) {
        Log.e("je sync les address", unSynced.getObject());
        Address address = new Gson().fromJson(unSynced.getObject(),new TypeToken<Address>(){}.getType());
        if (address.getId() == null) {
            addAddress(address,unSynced);
        }else {
            updateAddress(address,unSynced);
        }
    }

    private void addAddress(Address address, final UnSynced unSynced) {
        Call<ResponseBody> call = Api.order().addAddress(
                mUser.getToken(),
                address.getClient_id(),
                address.getClient_id(),
                mUser.getId(),
                address.getVille_id(),
                address.getLat(),
                address.getLon(),
                address.getQuartier(),
                address.getNom(),
                address.getSurnom(),
                address.getSurnom(),
                address.getDescription(),
                0,
                address.getProvider_name()
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null){
                    try{
                        String output = response.body().string();
                        JSONObject object = new JSONObject(output);
                        if (object.getBoolean("success")){
                            mUnSyncedRepository.delete(unSynced);
                        }
                    }catch (JSONException | IOException e){
                        e.printStackTrace();
                        App.handleError(e);
                    }
                }else {
                    if (response.code() == 401){
                        Values.unAuthorizedDialog();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

            }
        });
    }

    private void updateAddress(Address address, final UnSynced unSynced) {
        Call<ResponseBody> call = Api.order().updateAddress(
                mUser.getToken(),
                address.getClient_id(),
                address.getId(),
                address.getClient_id(),
                mUser.getId(),
                address.getLat(),
                address.getLon(),
                address.getQuartier(),
                address.getNom(),
                address.getTitre(),
                address.getTitre(),
                address.getDescription(),
                0
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null){
                    try{
                        String output = response.body().string();
                        JSONObject object = new JSONObject(output);
                        if (object.getBoolean("success")){
                            mUnSyncedRepository.delete(unSynced);
                        }
                    }catch (JSONException | IOException e){
                        e.printStackTrace();
                        App.handleError(e);
                    }
                }else {
                    if (response.code() == 401){
                        Values.unAuthorizedDialog();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

            }
        });
    }

    public void getQuarters(){
        getQuartersFromCache();
        Api.order().getQuarters().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null){
                    try{
                        String output = response.body().string();
                        JSONArray data = new JSONObject(output).getJSONArray("data");
                        List<Quarter> quarters = new Gson().fromJson(data.toString(), new TypeToken<List<Quarter>>(){}.getType());
                        PreferenceUtils.setString(getApplication(), PreferenceUtils.QUARTERS, data.toString());
                        setQuartersByCity(quarters);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        App.handleError(e);
                        getQuartersFromCache();
                    }
                }else {
                    getQuartersFromCache();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                getQuartersFromCache();
            }
        });
    }

    private void setQuartersByCity(List<Quarter> quarters){
        if (quarters == null || quarters.isEmpty())return;
        List<Quarter> douala = new ArrayList<>();
        List<Quarter> yaounde = new ArrayList<>();
        for(Quarter q : quarters){
            if (q.getCity_id() == 1){
                douala.add(q);
            }else if(q.getCity_id() == 2){
                yaounde.add(q);
            }
        }
        Quarter.DOUALA = douala;
        Quarter.YAOUNDE = yaounde;
    }

    private void getQuartersFromCache(){
        try{
            String quartersArray = PreferenceUtils.getString(getApplication(), PreferenceUtils.QUARTERS);
            List<Quarter> quarters = new Gson().fromJson(quartersArray, new TypeToken<List<Quarter>>(){}.getType());
            setQuartersByCity(quarters);
        }catch (Exception e){
            e.printStackTrace();
            App.handleError(e);
        }

    }


    public int getUnassignedPage() {
        return mUnassignedPage;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public int getToBeTreatedPage() {
        return mToBeTreatedPage;
    }

    public int getMyOrdersPage() {
        return mMyOrdersPage;
    }

    public int getTodayPage() {
        return mTodayPage;
    }

    public MutableLiveData<List<OrderSteed>> getToValidateList() {
        return mToValidateList;
    }

    public MutableLiveData<List<OrderSteed>> getCancelledList() {
        return mCancelledList;
    }

    public MutableLiveData<List<OrderSteed>> getUnassignedList() {
        return mUnassignedList;
    }

    public MutableLiveData<List<OrderSteed>> getMyOrdersList() {
        return mMyOrdersList;
    }

    public MutableLiveData<List<OrderSteed>> getToBeTreatedList() {
        return mToBeTreatedList;
    }

    public MutableLiveData<List<OrderSteed>> getTodayList() {
        return mTodayList;
    }

    public MutableLiveData<Boolean> getLoading() {
        return mLoading;
    }

    public MutableLiveData<Badge> getBadge() {
        return mBadge;
    }

    public boolean isOrderStatistics() {
        return mIsOrderStatistics;
    }

    public void setOrderStatistics(boolean orderStatistics) {
        mIsOrderStatistics = orderStatistics;
    }
}
