package com.ankit.volleywrapper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

/**
 * Created by ankitagrawal on 6/7/16. yay
 */
public class Utils {
    /**
     * checks Network connectivity
     *
     * @param context the context to check
     * @return true/false
     */

    public static boolean isConnected(@NonNull Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
