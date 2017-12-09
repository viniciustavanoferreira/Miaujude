package com.fatec.labs.fatec_firebase.DAO;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.fatec.labs.fatec_firebase.Common.GoogleMaps;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class FirebaseStorageUtils {
    //TODO: revisar se a classe realmente é necessária
    private static StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private static FirebaseStorageUtils instance;
    public static FirebaseStorageUtils getInstance (){
        return instance == null ? instance = new FirebaseStorageUtils() : instance;
    }
    private FirebaseStorageUtils (){}

    public void setImageViewByID(String strImageID, final ImageView imgView) throws IOException {
        //this function get the image on Firebase Storage and convert it to bitmap
        storageReference.child("cat/" + strImageID + ".png");
        final File imgFile = File.createTempFile("img",strImageID + ".png");
        storageReference.getFile(imgFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bmp;
                bmp = BitmapFactory.decodeFile(imgFile.getPath());
                setImageViewFromBitmap(bmp,imgView);
            }
        });
    }
    protected void setImageViewFromBitmap(Bitmap bmp, ImageView imgView) {
        if (bmp!=null) {
            imgView.setImageBitmap(bmp);
        }
    }

    public void setBitmapImageByID(String strImageID, final Bitmap bitmap) throws IOException {
        //this function get the image on Firebase Storage and convert it to bitmap
        storageReference.child("cat/" + strImageID + ".png");
        final File imgFile = File.createTempFile("img",strImageID + ".png");
        storageReference.getFile(imgFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bmpTemp;
                bmpTemp = BitmapFactory.decodeFile(imgFile.getPath());
                setBitmapFromBitmap(bmpTemp,bitmap);
            }
        });
    }
    protected void setBitmapFromBitmap(Bitmap bmpTemp, Bitmap bmp) {
        bmp = bmpTemp;
    }

    public void setImageFileByID(String strImageID, final String strFile) throws IOException {
        storageReference.child("cat/" + strImageID + ".png");
        final File imgFile = File.createTempFile("img",strImageID + ".png");
        storageReference.getFile(imgFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                String strFileTemp;
                strFileTemp = imgFile.getPath();
                setFileNameFromFilePath(strFileTemp,strFile);
            }
        });
    }
    protected void setFileNameFromFilePath(String strFileTemp, String strFile) {
        strFile = strFileTemp;
    }

    public void setPointerMarkerWithImage(final GoogleMap googlemap, final LatLng latlng,
                                          final String title, final String snippet, final String file) throws IOException {
        String tempFile = "UlhaaFZtRm5ZV0poWm5sSldsQkxWV2h3TTFWWmVYWmhUa041WTNRNWN6Rk9UalY2TVE9PQ==.png";
        //storageReference.child("cat/" + file);
        storageReference.child("cat/" + tempFile);

        //final File imgFile = File.createTempFile("img",file);
        final File imgFile = File.createTempFile("img",tempFile);

        OnSuccessListener successListener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                String strFileTemp;
                strFileTemp = imgFile.getPath();
                GoogleMaps.addPointerWithImage(googlemap,latlng,title,snippet,strFileTemp);
            }
        };
        storageReference.getFile(imgFile).addOnSuccessListener(successListener);
    }
}
