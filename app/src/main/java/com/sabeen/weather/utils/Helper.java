package com.sabeen.weather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;

/**
 * Helper Utility
 * Created by sabeen on 6/13/16.
 */

public class Helper {

    /**
     * Method to check whether device android version is greater than marshmallow
     * @return true or false
     */
    public static boolean isGreaterThanMarshmallow() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    /**
     * Method to detect the network connection
     * @param context for getting system service
     * @return true of false
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
