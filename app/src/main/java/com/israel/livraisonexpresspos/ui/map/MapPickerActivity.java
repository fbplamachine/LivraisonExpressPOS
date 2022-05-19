package com.israel.livraisonexpresspos.ui.map;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.israel.livraisonexpresspos.ui.address.AddressDialogFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class MapPickerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final static int LOCATION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private boolean permissionDenied = false;
    private double lastKnownLat = 0, lastKnownLng = 0, selectedLat = 0, selectedLng = 0; //todo : move it to vm
    private Button btnConfirmSelection, btnCancelSelection;
    private String selectedAddress = null, country = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_picker);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (getIntent().hasExtra(AddressDialogFragment.LONGITUDE) && getIntent().hasExtra(AddressDialogFragment.LATITUDE)){
            lastKnownLat = Double.parseDouble(getIntent().getStringExtra(AddressDialogFragment.LATITUDE));
            lastKnownLng = Double.valueOf(getIntent().getStringExtra(AddressDialogFragment.LONGITUDE));
        }

        btnCancelSelection = findViewById(R.id.btn_cancel_map_selection);
        btnConfirmSelection = findViewById(R.id.btn_confirm_map_selection);
        setListeners();
    }

    private void setListeners() {
        btnConfirmSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo check the country and notify
                if ((selectedAddress == null || selectedAddress.equals("")) && selectedLat == 0.0 || selectedLng == 0.0) {
                    showInformationDialog("Nous ne detectons aucune selection ou ne parvenons pas a recupérer cette adresse!! Cela peut être dû a l'état du reseau ou au referencement de la carte dans votre region. Merci de bien voilor sortir de la carte puis retentez une selection.");
                    selectedAddress = "";
                }
                if (!(country == null)) {
                    if (!country.equals("Cameroun") && !country.equals("Cameroon")) {
                        Toasty.error(getApplicationContext(), "Cette géolocalisation ne se trouve pas au Cameroun", 500).show();
                    } else {
                        if (lastKnownLat != 0) {
                            showWarningDialog("Vous êtes sur le point de modifier une adresse déja géolocalisée!! Voulez-vous vraiment le faire ?");
                        } else {
                            stepBackToTheForm();
                        }
                    }
                }
            }
        });

        btnCancelSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetMapPicker();
            }
        });
    }

    private void showInformationDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapPickerActivity.this)
                .setIcon(R.drawable.ic_round_warning_24)
                .setTitle("Ooops !!")
                .setCancelable(false)
                .setMessage(msg)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void resetMapPicker() {
        mMap.clear();
        selectedAddress = null;
        country = null;
        selectedLat = 0;
        selectedLng = 0;
        lastKnownLat = 0;
        lastKnownLng = 0;
    }

    private void showWarningDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapPickerActivity.this)
                .setIcon(R.drawable.ic_round_warning_24)
                .setTitle("Attention !!")
                .setCancelable(false)
                .setMessage(msg)
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getResources().getString(R.string.continu), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        stepBackToTheForm();
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void stepBackToTheForm() {
        //i move back to DetailCourseActivity
        Intent moveBackToDetailCourseWithResult = new Intent();
        moveBackToDetailCourseWithResult.putExtra(AddressDialogFragment.ADDRESS_NAME, selectedAddress);
        moveBackToDetailCourseWithResult.putExtra(AddressDialogFragment.LONGITUDE, selectedLng);
        moveBackToDetailCourseWithResult.putExtra(AddressDialogFragment.LATITUDE, selectedLat);
        this.setResult(RESULT_OK, moveBackToDetailCourseWithResult);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //still checking the permissions in case the user turn them of while the activity is in pause
        //here you can weather "onMissingPermissionError" or "checkPermissions"; both can do the job
        //    checkPermission();
        App.currentActivity = this;
        if (permissionDenied) {
            onMissingPermissionError();
            permissionDenied = false;
        }
    }

    /**
     * display the dialog explaining that the permission is missing
     */
    private void onMissingPermissionError() {
        PermissionRefusedDialog.newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    private void checkPermission() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            //if i have bith of the required permissions
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            askForPermission(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE, true); //i need the context, array of required permissions, the request code an a boolean wich will indicate if we should end the activity or not
        }
    }

    private void askForPermission(AppCompatActivity activity, String[] permissions, int requestCode, boolean finishActivity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MapPickerActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                && ActivityCompat.shouldShowRequestPermissionRationale(MapPickerActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
            //means the user has refused to give the permission before , in other words it's not the first time for us to ask ask him his permissions
            // todo : show a dialog explaining that the permission is very important
            ExplanationDialog.newInstance(requestCode, finishActivity).show(activity.getSupportFragmentManager(), "dialog");
        } else {
            //it's the first time for us to ask his permission
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                //since i'm asking for two permissions i gonna loop trough the grantResult table to check if all are granted then act
                if (arePermissionsGranted(permissions, grantResults, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})) {
                    checkPermission();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    askForPermission(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE, true);
                    permissionDenied = true;
                }
            }
            default:
                return;
        }
    }

    private boolean arePermissionsGranted(String[] grantedPermissions, int[] grantResults, String[] askedPermissions) {
        int permissionCount = 0;
        boolean result = false;
        for (int i = 0; i < grantedPermissions.length; i++) {
            for (int k = 0; k < askedPermissions.length; k++) {
                if (grantedPermissions[i].equals(askedPermissions[k])) {
                    permissionCount++;
                }
            }
        }
        if (permissionCount > 0) {
            result = true;
        }
        return result;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        checkPermission();

        // add map marker to the last location
        if (lastKnownLat > 0 && lastKnownLng > 0){
            addMarkerToThelastCoordinates(lastKnownLat, lastKnownLng); //inside set click listener that triger a snipset with some informations and center the marker relatively to the map
        }
        //handle the new adress selection within a map clic
        selectNewAdress();
        //since i've taken the device current location, then set a on my location changed listener
        onLocationChange();
    }

    private void onLocationChange() {
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng ltlng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ltlng, 16f);
                mMap.animateCamera(cameraUpdate);
            }
        });
    }

    private void selectNewAdress() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(getAddress(latLng));
                mMap.clear();
                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                mMap.animateCamera(location);
                mMap.addMarker(markerOptions);
            }
        });
    }

    /**
     * this function add amarker to the last localized position, it'll maybe be the current position of the delivery man
     *
     * @param lastKnownLat
     * @param lastKnownLng
     */
    private void addMarkerToThelastCoordinates(double lastKnownLat, double lastKnownLng) {
        LatLng position = new LatLng(lastKnownLat, lastKnownLng);//retrive datas fron intent and build a new LatLng
        //add the marker to map
        final Marker lastLocation = mMap.addMarker(new MarkerOptions()
                .position(position)
                .title("Dernière adresse connue")
                .snippet("Adresse est la dernière renseignée pour ce client !!")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .infoWindowAnchor(0.5f, 0.5f));
        //set click listener to this marker
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // This causes the marker at the last location to bounce into position when it is clicked. made it for fun ;-)
                final Handler handler = new Handler();
                final long start = SystemClock.uptimeMillis();
                final long duration = 1500;
                final Interpolator interpolator = new BounceInterpolator();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        long elapsed = SystemClock.uptimeMillis() - start;
                        float t = Math.max(1 - interpolator.getInterpolation((float) elapsed / duration), 0);
                        lastLocation.setAnchor(0.5f, 1.0f + 2 * t);

                        if (t > 0.0) {
                            // Post again 16ms later.
                            handler.postDelayed(this, 16);
                        }
                    }
                });
                return false;
            }
        });
    }

    /**
     * this function retrive the adress corresponding to the clic location on the map .
     * instead of taking the five adress proposition, we just take the first then send it to the confirmAdress dialog.
     * arguments could be added according the needs of datas once on the dialog
     */
    private String getAddress(LatLng latLng) {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            selectedLat = latLng.latitude;
            selectedLng = latLng.longitude;
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present then only, check with max available address lines by getMaxAddressLineIndex()
            country = addresses.get(0).getCountryName();
            selectedAddress = address;
            return address;
        } catch (IOException e) {
            e.printStackTrace();
            App.handleError(e);
            return "Le reseau peut être defectueux; sortez et recharger la carte de nouveau";
        }
    }


    /**
     * This dialog explain the use of location and why the permissions ares requested
     */
    public static class ExplanationDialog extends androidx.fragment.app.DialogFragment {

        private static final String ARGUMENT_PERMISSION_REQUEST_CODE = "requestCode";
        private static final String ARGUMENT_FINISH_ACTIVITY = "finish";
        private boolean finishActivity = false;

        public static ExplanationDialog newInstance(int requestCode, boolean finishActivity) {
            Bundle arguments = new Bundle();
            arguments.putInt(ARGUMENT_PERMISSION_REQUEST_CODE, requestCode);
            arguments.putBoolean(ARGUMENT_FINISH_ACTIVITY, finishActivity);
            ExplanationDialog dialog = new ExplanationDialog();
            dialog.setArguments(arguments);
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            final int requestCode = arguments.getInt(ARGUMENT_PERMISSION_REQUEST_CODE);
            finishActivity = arguments.getBoolean(ARGUMENT_FINISH_ACTIVITY);

            return new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.permission_rationale_location)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // After click on Ok, request the permission.
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    requestCode);
                            // Do not finish the Activity while requesting permission.
                            finishActivity = false;
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .create();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
            if (finishActivity) {
                Toast.makeText(getActivity(),
                        R.string.permission_required_toast,
                        Toast.LENGTH_SHORT)
                        .show();
                getActivity().finish();
            }
        }
    }

    /**
     * A dialog that displays a permission refused message.
     */
    public static class PermissionRefusedDialog extends androidx.fragment.app.DialogFragment {

        private static final String ARGUMENT_FINISH_ACTIVITY = "finish";

        private boolean finishActivity = false;

        /**
         * Creates a new instance of this dialog and optionally finishes the calling Activity
         * when the 'Ok' button is clicked.
         */
        public static PermissionRefusedDialog newInstance(boolean finishActivity) {
            Bundle arguments = new Bundle();
            arguments.putBoolean(ARGUMENT_FINISH_ACTIVITY, finishActivity);

            PermissionRefusedDialog dialog = new PermissionRefusedDialog();
            dialog.setArguments(arguments);
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            finishActivity = getArguments().getBoolean(ARGUMENT_FINISH_ACTIVITY);

            return new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.location_permission_denied)
                    .setPositiveButton(android.R.string.ok, null)
                    .create();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
            if (finishActivity) {
                Toast.makeText(getActivity(), R.string.permission_required_toast,
                        Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }
}
