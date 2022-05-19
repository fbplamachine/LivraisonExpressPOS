package com.israel.livraisonexpresspos.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;

public class OrderUtils {

    public static void makePhoneCall(Activity activity, String number) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + number));
        activity.startActivity(callIntent);
    }

    public static void enableTracking(Context context, LatLng origin, LatLng destination) {
        if (origin == null || destination == null) {
            Toast.makeText(context, "Impossible de recupérer votre position.", Toast.LENGTH_SHORT).show();
            return;
        }
        String str_origin = origin.latitude + "," + origin.longitude;
        String str_dest = destination.latitude + "," + destination.longitude;
        String uri = "https://www.google.com/maps/dir/?api=1&origin=" + str_origin + "&destination=" + str_dest;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(intent);
    }

    public static boolean locationEnabled(){
        LocationManager manager = (LocationManager) App.getInstance()
                .getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);;
        boolean networkEnabled = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return gpsEnabled && networkEnabled;
    }

    public static void enableLocationDialog(final Activity context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_round_warning_24)
                .setTitle(R.string.warning)
                .setCancelable(false)
                .setMessage("Veuillez vérifier que votre localisation est activée et que c'est en mode haute précision.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        context.finish();
                    }
                });
        if (!context.isFinishing())builder.create().show();
    }
}
