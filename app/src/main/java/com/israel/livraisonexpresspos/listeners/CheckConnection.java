package com.israel.livraisonexpresspos.listeners;

import android.content.Context;

import com.israel.livraisonexpresspos.MainActivity;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.utils.NetworkUtils;

import java.util.TimerTask;

public class CheckConnection extends TimerTask {
    private static final String TAG = CheckConnection.class.getSimpleName();
    private OnConnectionStateChangeListener mChangeListener;

    public CheckConnection(Context context) {
        if (context instanceof MainActivity){
            mChangeListener = (MainActivity)context;
        }
    }

    @Override
    public void run() {
        if (mChangeListener == null)return;
        boolean isConnected = NetworkUtils.isConnected();
        if (App.isConnected != isConnected){
            App.isConnected = isConnected;
            mChangeListener.onConnectionChange(isConnected);
        }
//        Log.e(TAG, "run: "+isConnected);
    }
}
