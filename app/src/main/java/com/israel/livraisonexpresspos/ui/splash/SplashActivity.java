package com.israel.livraisonexpresspos.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.israel.livraisonexpresspos.MainActivity;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.app.MyExceptionHandler;
import com.israel.livraisonexpresspos.ui.login.LoginActivity;

import static com.israel.livraisonexpresspos.utils.Values.sleep;

public class SplashActivity extends AppCompatActivity {
    protected App mApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mApp = (App) getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        sleep(2000);

        SplashViewModel viewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        viewModel.tryLog(this);
        viewModel.getLogged().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == null)return;
                if (aBoolean){
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.currentActivity = this;
    }

}