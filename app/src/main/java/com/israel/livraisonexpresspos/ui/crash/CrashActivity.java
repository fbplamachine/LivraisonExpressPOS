package com.israel.livraisonexpresspos.ui.crash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.app.MyExceptionHandler;
import com.israel.livraisonexpresspos.ui.splash.SplashActivity;

public class CrashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);
        String error = getIntent().getStringExtra(MyExceptionHandler.CRASH);
        TextView textView = findViewById(R.id.tvCrash);
        Button button = findViewById(R.id.buttonRestart);
        textView.setText(R.string.generic_crash_message);
//        textView.setText(error);
        button.setOnClickListener(v -> {
            startActivity(new Intent(CrashActivity.this, SplashActivity.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.currentActivity = this;
    }
}