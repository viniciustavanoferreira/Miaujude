package com.fatec.labs.fatec_firebase.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.fatec.labs.fatec_firebase.Common.GPSTracker;
import com.fatec.labs.fatec_firebase.Common.GoogleMaps;
import com.fatec.labs.fatec_firebase.Common.Notify;
import com.fatec.labs.fatec_firebase.Common.Permissions;
import com.fatec.labs.fatec_firebase.DAO.CatDAO;
import com.fatec.labs.fatec_firebase.Model.Cat;
import com.fatec.labs.fatec_firebase.Model.CatUser;
import com.fatec.labs.fatec_firebase.R;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;

//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
public class MapsActivity extends AppCompatActivity implements
        OnInfoWindowClickListener, OnMarkerClickListener, OnMapReadyCallback {

    private GoogleMap mMap;
    protected ArrayList<Cat> arrListCat;
    private DatabaseReference refFirebase;

    static String userID;
    static String userName;
    static String userPhone;
    static String userEmail;
    static String catID;
    static String catName;
    static String catRace;
    static String catDescription;
    static CatUser catUserOwner;
    static String catImageFile;
    static double catLatitude;
    static double catLongitude;
    static boolean catAdopted;
    boolean isMapReady = false;
    Bitmap catPic;

    public void showMeOnMap(){
        GPSTracker gps = new GPSTracker(this);
        GoogleMaps.goToPointer(mMap,new LatLng(gps.getLatitude(), gps.getLongitude()),"Here I am");
    }

    public void updateCatList(){
        getCatListFromQuery();
        if (arrListCat != null) {
            Notify.showNotify(this,"Qtd gatos: " + arrListCat.size());
        }
    }

    public void showCatsOnMap() throws IOException {
        //Show the cats on the map
        //Turn the Cat arraylist accessible for everyone
        CatDAO.arrListCat = arrListCat;

        for (final Cat cat : arrListCat){
            catID = cat.getId();
            catName = cat.getCatName();
            userName = cat.getCatUserOwner().getUserName();
            catLatitude = cat.getCatLatitude();
            catLongitude = cat.getCatLongitude();
            catImageFile = catID + ".png";
            
            //Put the image on marker
            catName = cat.getCatName();
            userName = cat.getCatUserOwner().getUserName();
            catLatitude = cat.getCatLatitude();
            catLongitude = cat.getCatLongitude();
            String id = cat.getId();
            final LatLng latlng = new LatLng(catLatitude,catLongitude);

            try {
                StorageReference fotoReference = FirebaseStorage.getInstance().getReference().child("cat/" + id + ".png");
                final File temp = File.createTempFile(id,null,null);

                OnFailureListener failureListener = new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Notify.showNotify(MapsActivity.this,"Cant get the file");
                        catPic = null;
                        cat.setCatPicture(catPic);
                        GoogleMaps.addPointerWithShape(mMap,latlng ,cat,catPic);

                    }
                };
                OnSuccessListener<FileDownloadTask.TaskSnapshot> successListener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        catPic = (BitmapFactory.decodeFile(temp.getPath()));
                        cat.setCatPicture(catPic);
                        GoogleMaps.addPointerWithShape(mMap,latlng ,cat,catPic);
                    }
                };

                fotoReference.getFile(temp).addOnSuccessListener(successListener);
                fotoReference.getFile(temp).addOnFailureListener(failureListener);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }

        }
    }

    public void getCatListFromQuery() {
        //Instancia o Firebase
        refFirebase = FirebaseDatabase
                .getInstance()
                .getReference("/cat");

        Query query = refFirebase.orderByKey();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    arrListCat = new ArrayList<>();

                    Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                    while (iterator.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        System.out.println("Value = " + next.child("id").getValue());

                        userID = next.child("catUserOwner").child("id").getValue().toString();
                        userName = next.child("catUserOwner").child("userName").getValue().toString();
                        userPhone = next.child("catUserOwner").child("userPhone").getValue().toString();
                        userEmail = next.child("catUserOwner").child("userEmail").getValue().toString();
                        catUserOwner = new CatUser(userID, userName, userPhone, userEmail);

                        catID = next.child("id").getValue().toString();
                        catName = next.child("catName").getValue().toString();
                        catRace = next.child("catRace").getValue().toString();
                        catDescription = next.child("catDescription").getValue().toString();
                        catLatitude = Double.parseDouble(next.child("catLatitude").getValue().toString());
                        catLongitude = Double.parseDouble(next.child("catLongitude").getValue().toString());
                        catAdopted = next.child("catAdopted").getValue()=="true"?true:false;

                        Cat cat = new Cat(catID, catName, catRace, catDescription, catUserOwner, catLatitude, catLongitude, catAdopted);
                        System.out.println(cat);

                        arrListCat.add(cat);
                        System.out.println(arrListCat.size());
                    }
                    //Just after getting the cats, show them on the map
                    try {
                        showCatsOnMap();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                arrListCat.clear();
            }
        };

        query.addValueEventListener(eventListener);
    }

    private void baixarFoto(Cat cat, final GoogleMap mMap){
        final LatLng latlng;
        catName = cat.getCatName();
        userName = cat.getCatUserOwner().getUserName();
        catLatitude = cat.getCatLatitude();
        catLongitude = cat.getCatLongitude();
        String id = cat.getId();
        latlng = new LatLng(catLatitude,catLongitude);

        try{
            StorageReference fotoReference = FirebaseStorage.getInstance().getReference().child("cat/" + id + ".png");
            final File temp = File.createTempFile(id,null,null);

            fotoReference.getFile(temp).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    catPic = (BitmapFactory.decodeFile(temp.getPath()));
                    GoogleMaps.addPointerWithBitmap(mMap,latlng,catName,userName,catPic);
                }
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Get a list of cats
        getCatListFromQuery();

        //Floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMapReady) {
                    updateCatList();}
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isMapReady) {
            updateCatList();
            try {
                showCatsOnMap();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Permissions.checkGPSPermission(this,this,true);
        if (Permissions.isGPSPermitted) {
            isMapReady= true;
        } else {
            Notify.showNotify(this, getString(R.string.message_gps_needed));
            MapsActivity.this.finishActivity(0);
            this.finish();
        }

        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);
    }
    @Override
    public void onInfoWindowClick(Marker marker) {
        //Notify.showNotify(this,"Info Window Click");

        //Set the cat profile for viewing
        //Notify.showNotify(this,"Cat object: " + marker.getTag());
        CatDAO.selectedCat = (Cat)marker.getTag();

        //Open the Cat profile screen
        Intent catIntent = new Intent(MapsActivity.this, CatInfoActivity.class);
        startActivity(catIntent);
    }
    @Override
    public boolean onMarkerClick(final Marker marker) {
        try {
            //Notify.showNotify(this,"ID: " + marker.getId());
            //Notify.showNotify(this,"Snippet: " + marker.getSnippet());
            //Notify.showNotify(this,"Title: " + marker.getTitle());
            //Notify.showNotify(this,"Tag: " + marker.getTag());

            marker.showInfoWindow();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
