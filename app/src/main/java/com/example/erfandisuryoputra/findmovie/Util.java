package com.example.erfandisuryoputra.findmovie;

import android.content.Context;
import android.net.ConnectivityManager;

public class Util {

    static boolean haveConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (wifi.isConnectedOrConnecting() || mobile.isConnectedOrConnecting());
    }
}
