package com.israel.livraisonexpresspos.app;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.bugsnag.android.Bugsnag;
import com.israel.livraisonexpresspos.ui.crash.CrashActivity;

import java.io.PrintWriter;
import java.io.StringWriter;

public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {
    public static final String CRASH = "crash";
    private final Activity activity;

    public MyExceptionHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
        Bugsnag.notify(throwable);
        Intent intent = new Intent(activity, CrashActivity.class);
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        intent.putExtra(CRASH, writer.toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        App.handleError(throwable);

        PendingIntent pendingIntent = PendingIntent.getActivity(App.getInstance().getBaseContext()
                , 0, intent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager manager = (AlarmManager)App.getInstance().getBaseContext()
                .getSystemService(Context.ALARM_SERVICE);
        assert manager != null;
        manager.set(AlarmManager.RTC, System.currentTimeMillis()+100, pendingIntent);
        activity.finish();
        System.exit(2);
    }
}
