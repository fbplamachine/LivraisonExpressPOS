package com.israel.livraisonexpresspos.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.bugsnag.android.Bugsnag;
import com.google.firebase.database.FirebaseDatabase;
import com.israel.livraisonexpresspos.BuildConfig;
import com.israel.livraisonexpresspos.data.Room.LivrexRoomDatabase;
import com.israel.livraisonexpresspos.data.Room.repository.UnSyncedRepository;
import com.israel.livraisonexpresspos.models.UnSynced;
import com.israel.livraisonexpresspos.ui.login.LoginActivity;
import com.israel.livraisonexpresspos.ui.order_detail.OrderDetailActivity;
import com.israel.livraisonexpresspos.utils.PreferenceUtils;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

public class App extends Application implements LifecycleObserver {
    public static final String CHANNEL_ID = "MY_CHANNEL";
    private static App instance;
    public static final String FROM_NOTIFICATION = "fromNotification";
    public static final String FROM_COMMENT = "fromComment";
    public static boolean isConnected;
    public static Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        instance = this;
        init();
    }

    private void init() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Bugsnag.init(this);
        LivrexRoomDatabase.getDatabase(this);
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.NONE, OneSignal.LOG_LEVEL.NONE);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new OneSignal.NotificationOpenedHandler() {
                    @Override
                    public void notificationOpened(OSNotificationOpenResult result) {
                        Log.e("TAG", "notificationOpened: " + result.toJSONObject().toString());
                        try {
                            JSONObject additionalData = result.toJSONObject().getJSONObject("notification")
                                    .getJSONObject("payload")
                                    .getJSONObject("additionalData");
                            Intent intent = new Intent(getInstance(), OrderDetailActivity.class);
                            intent.putExtra(FROM_NOTIFICATION, true);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                            if (additionalData.has("order")){
                                JSONObject object = additionalData.getJSONObject("order");
                                intent.putExtra(OrderDetailActivity.ORDER_ID, object.getInt("id"));
                            }else if (additionalData.has("comment")){
                                JSONObject object = additionalData.getJSONObject("comment");
                                intent.putExtra(OrderDetailActivity.ORDER_ID, object.getInt("commentable_id"));
                                intent.putExtra(FROM_COMMENT, true);
                            }
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .init();
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public static App getInstance() {
        return instance;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded(){
        long now = Calendar.getInstance().getTimeInMillis();
        long lastConnection = PreferenceUtils.getLong(getApplicationContext(), PreferenceUtils.LAST_CONNECTION);
        if (now - lastConnection >= (12 * 60 * 60 * 1000)){
            PreferenceUtils.setString(getApplicationContext(), PreferenceUtils.ACCESS_TOKEN, "");
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded(){
        String token = PreferenceUtils.getString(getApplicationContext(), PreferenceUtils.ACCESS_TOKEN);
        ActivityManager manager = (ActivityManager)instance.getSystemService(Context.ACTIVITY_SERVICE);
        if (token.equals("") && !manager.getRunningTasks(1).get(0).topActivity.getClassName().toLowerCase().contains("loginactivity")){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public static void handleError(Throwable throwable){
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        String stack = "";
        for(int i = 0; i < throwable.getStackTrace().length; i++){
            StackTraceElement e = throwable.getStackTrace()[i];
            stack = stack.concat(e.getClassName() + "(" + e.getMethodName() + ")");
            if (i < throwable.getStackTrace().length - 1){
                stack = stack.concat(" -> ");
            }
        }
        try{
            JSONObject body = new JSONObject();
            body.put("exception", writer.toString());
            body.put("exception_type", throwable.getClass().getSimpleName() + " ( " + App.currentActivity.getClass().getSimpleName() + ", " + BuildConfig.VERSION_NAME + ")");
            body.put("system_class_cause", stack);
            body.put("platform", "Mobile-POS");
            UnSynced unSynced = new UnSynced();
            unSynced.setDateTime(Calendar.getInstance().getTimeInMillis());
            unSynced.setObject(body.toString());
            unSynced.setType(UnSynced.ERROR);
            UnSyncedRepository repository = new UnSyncedRepository(getInstance());
            repository.insert(unSynced);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "LATE_ORDERS_NOTIFICATION";
            String description = "Notify user when he or she has late orders in his or her panel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}