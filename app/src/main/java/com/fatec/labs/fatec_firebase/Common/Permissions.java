package com.fatec.labs.fatec_firebase.Common;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import com.fatec.labs.fatec_firebase.R;

public class Permissions {

    public static int REQUEST_GPS = 1000;
    public static int REQUEST_CAMERA = 2000;
    public static int REQUEST_PHONE = 3000;

    public static boolean isGPSPermitted = false;
    public static boolean isCameraPermitted = false;
    public static boolean isPhonePermitted = false;

    //permissão de GPS
    public static boolean checkGPSPermission(Activity activity, Context context, boolean showExplanation) {
        try {
            //TODO: está dando erro no método checkPermission
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (showExplanation) {
                    Notify.showNotify(context, context.getString(R.string.message_gps_needed));
                }
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GPS);
                isGPSPermitted = (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED);
            } else {
                isGPSPermitted = true;
            }
            return isGPSPermitted;
        } catch (Exception e) {
            e.printStackTrace();
            Notify.showNotify(context, e.getMessage().toString());
        }
        return false;
    }

    public static boolean checkCameraPermission(Activity activity, Context context, boolean showExplanation){
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (showExplanation) {
                    Notify.showNotify(context, context.getString(R.string.message_camera_needed));
                }
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                return true;
            }
        } catch (Exception e) {
            Notify.showNotify(context, e.getMessage());
        }
        return false;
    }

    public static void checkPhonePermission(Activity activity, Context context, boolean showExplanation) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (showExplanation) {
                Notify.showNotify(context, context.getString(R.string.message_phone_needed));
            }
            ActivityCompat.requestPermissions(activity, new String [] {Manifest.permission.CALL_PHONE},REQUEST_PHONE);
            isPhonePermitted = (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED);
        } else {
            isPhonePermitted = true;
        }

    }
}
