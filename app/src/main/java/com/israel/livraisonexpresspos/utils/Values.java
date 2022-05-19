package com.israel.livraisonexpresspos.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.israel.livraisonexpresspos.MainActivity;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.models.Address;
import com.israel.livraisonexpresspos.models.City;
import com.israel.livraisonexpresspos.models.Contact;
import com.israel.livraisonexpresspos.models.Delivery;
import com.israel.livraisonexpresspos.models.Module;
import com.israel.livraisonexpresspos.models.Order;
import com.israel.livraisonexpresspos.models.Shop;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.ui.error.ErrorActivity;
import com.israel.livraisonexpresspos.ui.login.LoginActivity;
import com.israel.livraisonexpresspos.ui.splash.SplashActivity;
import com.israel.livraisonexpresspos.uiComponents.Tools;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.List;

import static com.israel.livraisonexpresspos.ui.checkout.CheckoutActivity.FROM_CHECKOUT;

public class Values {
    public static final String PROVIDER_NAME = "livraison-express";
    public static final String ERROR = "error";
    public static Module module;
    public static Shop shop;
    public static Order order;
    public static Contact sender;
    public static String city = "douala";
    public static Contact receiver;

    public static void clear(){
        module = null;
        shop = null;
    }

    public static void setSender(Shop s) {
        if (s == null)return;
        Contact c = s.getContact();
        List<Address> addresses = new ArrayList<>();
        addresses.add(s.getAdresse());
        c.setAdresses(addresses);
        sender = c;
        shop = s;
    }

    public static Delivery stringToDelivery(String string){
        Delivery delivery = new Gson().fromJson(string
                , new TypeToken<Delivery>(){}.getType());
        return delivery == null ? new Delivery() : delivery;
    }

    public static String deliveryToString(Delivery delivery){
        return new Gson().toJson(delivery);
    }

    public static void clearOrder(){
        order = null;
        receiver = null;
        sender = null;
    }

    public static int getColorDark(){
        return Color.parseColor(Tools.shadeColor(module.getModule_color(), -20));
    }

    public static int getColor(){
        return Color.parseColor(module.getModule_color());
    }

    public static int getCityId(Context context){
        String stringCities = PreferenceUtils.getString(context, PreferenceUtils.CITIES);
        if (!stringCities.equals("")) {
            ArrayList<City> cities = new Gson()
                    .fromJson(stringCities, new TypeToken<ArrayList<City>>() {}.getType());
            for (City c : cities){
                if (c.getName().equalsIgnoreCase(city)){
                    return c.getId();
                }
            }
        }
        return 0;
    }

    public static void unAuthorizedDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(App.currentActivity)
                .setIcon(R.drawable.ic_round_warning_24)
                .setTitle(R.string.oops)
                .setMessage("Votre session a expirée.")
                .setCancelable(false)
                .setPositiveButton("Connexion", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        logout(App.currentActivity);
                        OneSignal.logoutEmail();
                    }
                });
        if(!App.currentActivity.isFinishing())builder.create().show();
    }

    public static void alertDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(App.currentActivity)
                .setIcon(R.drawable.ic_round_warning_24)
                .setTitle(R.string.warning)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        if(!App.currentActivity.isFinishing())builder.create().show();
    }

    public static void errorDialog(final String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(App.currentActivity)
                .setIcon(R.drawable.ic_round_warning_24)
                .setTitle(R.string.oops)
                .setMessage("Une erreur est survenue.")
                .setPositiveButton("En savoir plus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(App.currentActivity, ErrorActivity.class);
                        intent.putExtra(ERROR, message);
                        App.currentActivity.startActivity(intent);
                    }
                })
                .setNegativeButton("Fermer", null);
        builder.create().show();
    }



    public static void logout(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        User.setCurrentUser(null);
        PreferenceUtils.setString(context, PreferenceUtils.ACCESS_TOKEN, "");
        OneSignal.logoutEmail();
    }

    public static void home(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(FROM_CHECKOUT, true);
        context.startActivity(intent);
    }

    public static void reset(Context context){
        Intent intent = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void sleep(int timeMillis) {
        try {
            Thread.sleep(timeMillis, 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
