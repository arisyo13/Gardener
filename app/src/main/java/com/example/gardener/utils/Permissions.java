package com.example.gardener.utils;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class Permissions {

    private static final int VERIFY_PERMISSION_REQUEST = 1;
    public static final String[] PERMISIONS ={
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    public static final String[] WRITE_STORAGE_PERMISSION = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final String[] READ_STORAGE_PERMISSION = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static final String[] CAMERA = {
            Manifest.permission.CAMERA
    };

    public void verifyPermissions(Activity activity, String[] permissions){
        ActivityCompat.requestPermissions(activity, permissions, VERIFY_PERMISSION_REQUEST);

    }

    public boolean checkPermissionsArray(Activity activity, String[] permissions){
        for (int i=0; i<permissions.length; i++){
            String check = permissions[i];
            if(!checkPermissions(activity, check)){
                return false;
            }
        }
        return true;
    }

    public boolean checkPermissions(Activity activity, String permission){
        int permissionRequest = ActivityCompat.checkSelfPermission(activity, permission);
        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        else{
            return true;
        }
    }
}
