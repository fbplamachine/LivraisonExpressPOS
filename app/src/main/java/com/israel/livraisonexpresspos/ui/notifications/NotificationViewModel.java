package com.israel.livraisonexpresspos.ui.notifications;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.israel.livraisonexpresspos.models.Notification;
import com.israel.livraisonexpresspos.models.User;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class NotificationViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Notification>> mNotifications;
    private String key;

    public NotificationViewModel(@NonNull Application application) {
        super(application);
        mNotifications = new MutableLiveData<>();
    }

    public MutableLiveData<List<Notification>> getNotifications() {
        return mNotifications;
    }

    public void fetchUserLastNotifications(NotificationActivity activity){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("notifications");
        reference.keepSynced(true);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
        String today = format.format(Calendar.getInstance().getTime());
        Query query = reference.orderByChild("notification_created_at")
                .startAt(today)
                .endAt(today + "\uf8ff")
                .limitToLast(30);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                filterNotificationsAsync(activity, snapshot);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    private void filterNotificationsAsync(NotificationActivity activity, DataSnapshot snapshot){
        new Thread(() -> {
            List<Notification> notifications = new ArrayList<>();
            for (DataSnapshot snap: snapshot.getChildren()){
                key = snap.getKey();
                try {
                    Notification notification = snap.getValue(Notification.class);
                    if (User.getCurrentUser(getApplication()) == null
                            || notification == null
                            || notification.getUser_to_notify() == null
                            || notification.getUser_to_notify().isEmpty())return;
                    if (notification.getUser_to_notify().contains(User.getCurrentUser(getApplication()).getId())){
                        notifications.add(notification);
                    }
                }catch (Exception e){
                    e.printStackTrace();
//                    App.handleError(e);
                }
            }
            activity.runOnUiThread(() -> {
                Collections.reverse(notifications);
                mNotifications.setValue(notifications);
            });
        }).start();
    }
}
