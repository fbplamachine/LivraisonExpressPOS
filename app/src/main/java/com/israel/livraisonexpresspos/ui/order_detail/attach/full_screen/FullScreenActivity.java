package com.israel.livraisonexpresspos.ui.order_detail.attach.full_screen;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;

public class FullScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));
        String image = getIntent().getStringExtra("image");
        ImageView imageView = findViewById(R.id.imageView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        Glide.with(this)
                .load(image)
                .into(imageView);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.currentActivity = this;
    }
}