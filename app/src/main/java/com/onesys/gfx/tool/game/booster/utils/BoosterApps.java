package com.onesys.gfx.tool.game.booster.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.onesys.gfx.tool.game.booster.Constants;
import com.onesys.gfx.tool.game.booster.model.AppInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class BoosterApps {

    public static void setBoosterApps(Context context, ArrayList<AppInfo> selectAppInfoList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("booster_apps", new Gson().toJson(selectAppInfoList));
        editor.apply();
    }

    public static ArrayList<AppInfo> getBoosterApps(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        String serializedObject = sharedPreferences.getString("booster_apps", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<AppInfo>>() {}.getType();

            return gson.fromJson(serializedObject, type);
        }
        return null;
    }

}
