package com.israel.livraisonexpresspos.ui.notifications;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.databinding.ActivityNotificationBinding;

public class NotificationActivity extends AppCompatActivity {
    private NotificationViewModel mViewModel;
    private ActivityNotificationBinding mBinding;
    private final NotificationsAdapter mAdapter = new NotificationsAdapter();
    public static final String NOTIFICATION_TYPE_COMMENT="App\\Notifications\\CommentCreatedNotification";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_notification);
        mViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        initUi();
        stream();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.currentActivity = this;
    }

    private void initUi() {
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mBinding.toolbar.setNavigationOnClickListener(view -> finish());
        mBinding.tvNothingToDisplay.setVisibility(View.GONE);
        mBinding.rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvNotifications.setAdapter(mAdapter);
        mViewModel.fetchUserLastNotifications(this);
    }

    private void stream(){
        mViewModel.getNotifications().observe(this, notifications -> {
            if (notifications == null)return;
            mBinding.progressBar.setVisibility(View.GONE);
            mAdapter.addNotifications(notifications);
        });
    }
}