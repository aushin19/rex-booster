package com.onemosys.gfx.tool.game.booster.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class CheckInternetConnection {
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
