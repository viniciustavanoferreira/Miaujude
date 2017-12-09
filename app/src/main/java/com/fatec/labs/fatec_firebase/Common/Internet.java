package com.fatec.labs.fatec_firebase.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public abstract class Internet {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connManager.getActiveNetworkInfo();
        return (netInfo !=null && netInfo.isConnected());
    }
}
