package com.israel.livraisonexpresspos.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import com.israel.livraisonexpresspos.app.App;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static boolean isConnected(){
        ConnectivityManager manager = getConnectivityManager();
        Network[] networks = manager.getAllNetworks();
        NetworkInfo info;
        for (Network network : networks){
            info = manager.getNetworkInfo(network);
            if (info != null && (info.getTypeName().equalsIgnoreCase("WIFI") || info.getTypeName().equalsIgnoreCase("MOBILE"))
                    && info.isConnected() && info.isAvailable() && isInternetAvailable()){
                return true;
            }
        }
        return false;
    }

    public static boolean isInternetAvailable() {
        try(Socket socket = new Socket()){
            socket.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;

        }
    }

    public static ConnectivityManager getConnectivityManager(){
        return (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
    }
}
