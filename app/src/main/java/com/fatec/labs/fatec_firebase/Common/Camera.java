package com.fatec.labs.fatec_firebase.Common;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;

import com.fatec.labs.fatec_firebase.R;

public  class Camera {

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    public static void dispatchTakePictureIntent(View view, Activity activity ){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Permissions.checkCameraPermission(activity,view.getContext(),true)) {
            if (takePictureIntent.resolveActivity(view.getContext().getPackageManager()) != null) {
                activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                Notify.showNotify(view.getContext(), view.getContext().getString(R.string.message_camera_failed));
            }
        }
    }
}
