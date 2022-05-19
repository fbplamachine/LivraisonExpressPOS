package com.israel.livraisonexpresspos.ui.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.databinding.ActivityMapBinding;
import com.israel.livraisonexpresspos.ui.address.AddressDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMyLocationChangeListener, GoogleMap.OnMapClickListener {
    public static final int LOCATION_REQUEST_CODE = 1111;
    private ActivityMapBinding mBinding;
    private GoogleMap mGoogleMap;
    private double mLastLatitude, mLastLongitude, mSelectedLatitude, mSelectedLongitude;
    private String mAddress, mCountry;
    private boolean mAddressSelected;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) return;
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        if (intent == null) return;
        if (intent.hasExtra(AddressDialogFragment.LONGITUDE) && intent.hasExtra(AddressDialogFragment.LATITUDE)) {
            mLastLatitude = Double.parseDouble(intent.getStringExtra(AddressDialogFragment.LATITUDE));
            mLastLongitude = Double.parseDouble(intent.getStringExtra(AddressDialogFragment.LONGITUDE));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.currentActivity = this;
        checkPermissions();
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                    , Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            initUi();
        }
    }

    private void initUi() {
        mBinding.buttonSave.setOnClickListener(this);
        mBinding.buttonClear.setOnClickListener(this);
    }

    private void reset() {
        mGoogleMap.clear();
        mSelectedLatitude = 0;
        mSelectedLongitude = 0;
        mAddressSelected = false;
        mCountry = null;
        mAddress = null;
    }

    private String getStringAddress(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses == null || addresses.isEmpty() || addresses.get(0) == null) return null;
            Address selectedAddress = addresses.get(0);
            mAddress = selectedAddress.getAddressLine(0);
            mCountry = selectedAddress.getCountryName();
            return mAddress;
        } catch (IOException e) {
            e.printStackTrace();
//            App.handleError(e);
            String message;
            if (App.isConnected){
                message = "Nous n'arrivons pas à determiner le pays de l'adresse sélectionné";
            }else {
                message = "Suite à des problèmes d'internet, nous n'arrivons pas à déterminer le pays de cette address.";
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.warning))
                    .setIcon(R.drawable.ic_round_warning_24)
                    .setCancelable(false)
                    .setMessage(message + "\nEtes vous sur quelle se trouve au Cameroun dans la ville souhaitée?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        mCountry = "Cameroun";
                        mAddress = "Inconnu";
                    })
                    .setNegativeButton("Non", ((dialog, which) -> reset()));
            builder.create().show();
            return "Inconnu";
        }
    }

    private void enableLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (mGoogleMap != null){
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setOnMyLocationChangeListener(this);
        mGoogleMap.setOnMapClickListener(this);
        enableLocation();
        if (mLastLatitude != 0 && mLastLongitude != 0){
            try {
                final Marker positionMarker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(mLastLatitude, mLastLongitude))
                        .title("Dernière adresse géolocalisée")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .infoWindowAnchor(0.5f, 0.5f)
                );
                mGoogleMap.setOnMapClickListener(latLng -> {
                    final Handler handler = new Handler();
                    final long start = SystemClock.uptimeMillis();
                    final long duration = 1500;
                    final Interpolator interpolator = new BounceInterpolator();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            long elapsed = SystemClock.uptimeMillis() - start;
                            float t = Math.max(1 - interpolator.getInterpolation((float) elapsed / duration), 0);
                            positionMarker.setAnchor(0.5f, 1.0f + 2 * t);

                            if (t > 0.0) {
                                // Post again 16ms later.
                                handler.postDelayed(this, 16);
                            }
                        }
                    });
                });
            }catch (Exception e){
                App.handleError(e);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.buttonClear) {
            reset();
        } else if (id == R.id.buttonSave) {
            if (mAddressSelected){
                boolean someThingWentWrong = mAddress == null || mAddress.equals("")
                        || mSelectedLatitude == 0.0 || mSelectedLongitude == 0.0 || mCountry == null;
                if (someThingWentWrong){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.warning))
                            .setIcon(R.drawable.ic_round_warning_24)
                            .setCancelable(false)
                            .setMessage("Veuillez selectionner un point a géolocaliser sur la carte.")
                            .setPositiveButton("Ok", null);
                    builder.create().show();
                } else {
                    if (mCountry.equalsIgnoreCase("Cameroun") || mCountry.equalsIgnoreCase("Cameroon")){
                        saveSelectedAddress();
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                                .setTitle(getString(R.string.warning))
                                .setIcon(R.drawable.ic_round_warning_24)
                                .setCancelable(false)
                                .setMessage("L'adresse que vous avez sélectionné ne se trouve pas au Cameroun.\nVeuillez selectionner une adresse correcte.")
                                .setPositiveButton("Ok", null);
                        builder.create().show();
                    }
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.warning))
                        .setIcon(R.drawable.ic_round_warning_24)
                        .setCancelable(false)
                        .setMessage("Veuillez selectionner un point a géolocaliser sur la carte.")
                        .setPositiveButton("Ok", null);
                builder.create().show();
            }
        }
    }

    private void saveSelectedAddress(){
        Intent intent = new Intent();
        intent.putExtra(AddressDialogFragment.ADDRESS_NAME, mAddress);
        intent.putExtra(AddressDialogFragment.LATITUDE, mSelectedLatitude);
        intent.putExtra(AddressDialogFragment.LONGITUDE, mSelectedLongitude);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onMyLocationChange(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17f);
        mGoogleMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        options.title(getStringAddress(latLng));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17f);
        mGoogleMap.clear();
        mGoogleMap.addMarker(options);
        mGoogleMap.animateCamera(cameraUpdate);
        mSelectedLatitude = latLng.latitude;
        mSelectedLongitude = latLng.longitude;
        mAddressSelected = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (allPermissionsGranted) {
                enableLocation();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.warning))
                        .setIcon(R.drawable.ic_round_warning_24)
                        .setCancelable(false)
                        .setMessage("Nous avons besoin de votre position pour mieux vous servir.\n Veuillez nous accorder l'access à votre position.")
                        .setPositiveButton("Ok", (dialog, which) -> checkPermissions());
                builder.create().show();
            }
        }
    }
}
