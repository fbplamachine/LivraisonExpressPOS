package com.israel.livraisonexpresspos.utils;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.models.from_steed_app.OrderSteed;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationService extends Service {
    private final List<OrderSteed> mOrderSteeds = new ArrayList<>();
    private NotificationCompat.Builder mBuilder;
    private Handler mHandler;
    
    private final Runnable checkLateOrdersRunnable = new Runnable() {
        @Override
        public void run() {
            for (OrderSteed o : mOrderSteeds){
                if (o.getInfos().getStatut() == CourseStatus.CODE_UNASSIGNED){
                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
                        Calendar now = Calendar.getInstance();
                        now.set(Calendar.MINUTE, (now.get(Calendar.MINUTE) - 30));
                        Date refDate = now.getTime();
                        Date deliveryDate = formatter.parse(o.getInfos().getDate_livraison());
                        if (!refDate.before(deliveryDate)){
                            showNotification();
                            break;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            mHandler.postDelayed(this, 30 * 60 * 1000);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        User user = User.getCurrentUser(this);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("courses");
        reference.keepSynced(true);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
//        String today = "2021-03-20";
        String today = format.format(Calendar.getInstance().getTime());
        Query query = reference.orderByChild("infos/date_livraison")
                .startAt(today)
                .endAt(today + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                mOrderSteeds.clear();
                if (snapshot.exists() && user != null){
                    for (DataSnapshot snap : snapshot.getChildren()){
                        try {
                            OrderSteed steed = snap.getValue(OrderSteed.class);
                            if (steed == null)return;
                            steed.setKey(snap.getKey());
                            if (User.isManager()
                                    && steed.getInfos().getStatut() != CourseStatus.CODE_CANCELED
                                    && steed.getInfos().getStatut() != CourseStatus.CODE_DELIVERED){
                                mOrderSteeds.add(steed);
                            } else if (User.isDeliveryMan()
                                    && steed.getInfos().getStatut() != CourseStatus.CODE_UNASSIGNED
                                    && steed.getInfos().getStatut() != CourseStatus.CODE_CANCELED
                                    && steed.getInfos().getStatut() != CourseStatus.CODE_DELIVERED
                                    && steed.getInfos().getCoursiers_ids().contains(user.getId())){
                                mOrderSteeds.add(steed);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            App.handleError(e);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler = new Handler();
        mHandler.postDelayed(checkLateOrdersRunnable, 0);
        return super.onStartCommand(intent, flags, startId);
    }

    private void showNotification() {
        String message = "Vous avez des courses en retard. Veuillez les traiter ou changer l'heure de livraison.";
        mBuilder = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_round_warning_24)
                .setContentTitle("Alerte courses en retard")
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(101010, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(null);
        super.onDestroy();
    }
}
