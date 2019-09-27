package com.essensys.JB089.Utility;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AvailableNetwork {
    private Context _context;
    public static boolean isConnectingToInternet(Context c) {
        ConnectivityManager connectivity = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }
//    public static boolean isOnline(Context c) {
//        ConnectivityManager cm = (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);
//        try {
//            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
//        } catch(NullPointerException n) {
//            return false;
//        }
//    }
}
