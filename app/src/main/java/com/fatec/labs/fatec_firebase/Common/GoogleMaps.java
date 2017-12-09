package com.fatec.labs.fatec_firebase.Common;

import android.graphics.Bitmap;

import android.graphics.Canvas;

import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import android.media.ThumbnailUtils;


import com.fatec.labs.fatec_firebase.Model.Cat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public abstract class GoogleMaps {

    public static void clearMap(GoogleMap googlemap) {
        googlemap.clear();
    }

    public static void addPointerWithShape(GoogleMap googlemap, LatLng latlng, Cat cat, Bitmap bitmap){
        MarkerOptions markeroptions = new MarkerOptions();
        markeroptions.position(latlng);
        markeroptions.title(cat.getCatName());
        markeroptions.snippet(cat.getCatUserOwner().getUserName());
        if (bitmap==null) {
            markeroptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        } else {
            Bitmap thumbnail = ThumbnailUtils.extractThumbnail(getCroppedBitmap(bitmap),200,200);
            markeroptions.icon(BitmapDescriptorFactory.fromBitmap(thumbnail));
        }
        Marker marker = googlemap.addMarker(markeroptions);
        marker.setTag(cat);
    }

    private static Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static void addPointerWithCatTag(GoogleMap googlemap, LatLng latlng, Cat cat, Bitmap bitmap){
        MarkerOptions markeroptions = new MarkerOptions();
        markeroptions.position(latlng);
        markeroptions.title(cat.getCatName());
        markeroptions.snippet(cat.getCatUserOwner().getUserName());
        if (bitmap==null) {
            markeroptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        } else {
            markeroptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        }
        Marker marker = googlemap.addMarker(markeroptions);
        marker.setTag(cat);
    }

    public static void addPointerWithBitmap(GoogleMap googlemap, LatLng latlng, String title, String snippet, Bitmap bitmap){
        MarkerOptions markeroptions = new MarkerOptions();
        markeroptions.position(latlng);
        markeroptions.title(title);
        markeroptions.snippet(snippet);
        if (bitmap==null) {
            markeroptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        } else {
            markeroptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        }
        Marker marker = googlemap.addMarker(markeroptions);
    }

    public static void addPointerWithImage(GoogleMap googlemap, LatLng latlng, String title, String snippet, String file){
        MarkerOptions markeroptions = new MarkerOptions();
        markeroptions.position(latlng);
        markeroptions.title(title);
        markeroptions.snippet(snippet);
        if (file==null) {
            markeroptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        } else {
            markeroptions.icon(BitmapDescriptorFactory.fromFile(file));
        }
        Marker marker = googlemap.addMarker(markeroptions);
    }

    public static void goToPointer(GoogleMap googlemap, LatLng latlng, String title) {
        MarkerOptions markeroptions = new MarkerOptions();
        markeroptions.position(latlng);
        markeroptions.title(title);
        googlemap.addMarker(markeroptions);
        googlemap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,12.0f));
    }

}
