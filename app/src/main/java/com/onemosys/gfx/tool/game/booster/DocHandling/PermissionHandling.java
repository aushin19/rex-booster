package com.onemosys.gfx.tool.game.booster.DocHandling;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionHandling {

    public static final int REQUEST_PERMISSION_KEY = 1;
    public static int REQUEST_ACTION_OPEN_DOCUMENT_TREE_DATA = 101;
    public static int REQUEST_ACTION_OPEN_DOCUMENT_TREE_OBB = 102;
    public static SharedPreferences sharedPreferences;

    public static boolean isGranted(Context context){
        sharedPreferences = context.getSharedPreferences("MySharedPref",MODE_PRIVATE);
        if(sharedPreferences.getString("Data","").equals(""))
            return true;

        return false;
    }

    public static boolean isGrantedOBB(Context context){
        sharedPreferences = context.getSharedPreferences("MySharedPref",MODE_PRIVATE);
        if(sharedPreferences.getString("OBB","").equals(""))
            return true;

        return false;
    }

    public static boolean checkAndRequestPermissions(Context context) {
        int writePerm = ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPerm = ContextCompat.checkSelfPermission(context.getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (writePerm != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if(readPerm != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_PERMISSION_KEY);
            return false;
        }
        return true;
    }

}
