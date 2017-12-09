package com.fatec.labs.fatec_firebase.DAO;

import android.graphics.BitmapFactory;

import com.fatec.labs.fatec_firebase.Common.GoogleMaps;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.fatec.labs.fatec_firebase.Activity.MenuActivity;
import com.fatec.labs.fatec_firebase.Common.Notify;
import com.fatec.labs.fatec_firebase.Model.Cat;
import com.fatec.labs.fatec_firebase.Model.CatUser;
import com.fatec.labs.fatec_firebase.Model.User;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class CatDAO {
    public static FirebaseAuth mAuth;
    private static DatabaseReference refFirebase;
    public static ArrayList<Cat> arrListCat = new ArrayList<>();        //cat list
    public static ArrayList<Cat> arrMyCatList = new ArrayList<>();      //my cat list
    public static ArrayList<Cat> arrAdoptedCatList = new ArrayList<>();  //adopted cat list

    static String userID;
    static String userName;
    static String userPhone;
    static String userEmail;
    static String catID;
    static String catName;
    static String catRace;
    static String catDescription;
    static CatUser catUserOwner;
    static double catLatitude;
    static double catLongitude;

    public static Cat selectedCat;

    /*

    public static void getCatListFromQuery() {
        //Instancia o Firebase
        refFirebase = FirebaseDatabase
                .getInstance()
                .getReference("/cat");

        Query query = refFirebase.orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //arrListCat = new ArrayList<>();

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                while (iterator.hasNext()) {
                    DataSnapshot next = (DataSnapshot) iterator.next();
                    System.out.println("Value = " + next.child("id").getValue());

                    userID = next.child("catUserOwner").child("id").getValue().toString();
                    userName="";
                    userPhone="";
                    userEmail= next.child("catUserOwner").child("userEmail").getValue().toString();
                    catUserOwner = new CatUser(userID,userName,userPhone,userEmail);

                    catID = next.child("id").getValue().toString();
                    catName = next.child("catName").getValue().toString();
                    catRace = next.child("catRace").getValue().toString();
                    catDescription = next.child("catDescription").getValue().toString();
                    catLatitude = Double.parseDouble(next.child("catLatitude").getValue().toString());
                    catLongitude = Double.parseDouble(next.child("catLongitude").getValue().toString());

                    Cat cat = new Cat(catID, catName, catRace, catDescription, catUserOwner, catLatitude, catLongitude);
                    System.out.println(cat);

                    arrListCat.add(cat);
                    System.out.println(arrListCat.size());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                arrListCat.clear();
            }
        });
    }
    public static ArrayList<Cat> getCatList(){
        //Lista todos os gatos que não são do usuário logado
        final ArrayList<Cat> arrCatList = new ArrayList<>();

        try {
            //Instancia o Firebase
            refFirebase = FirebaseDatabase
                    .getInstance()
                    //.getReference("cat");
                    .getReference()
                    .child("cat");

            System.out.println("Reference: " + refFirebase.toString());

            refFirebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        //Gera a lista de gatos com as informações
                        //String userID;
                        //String userName;
                        //String userPhone;
                        //String userEmail;
                        //String catID;
                        //String catName;
                        //String catRace;
                        //String catDescription;
                        //CatUser catUserOwner;
                        //double catLatitude;
                        //double catLongitude;

                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                            userID = childDataSnapshot.child("catUserOwner").child("id").getValue().toString();
                            userName="";
                            userPhone="";
                            userEmail=childDataSnapshot.child("catUserOwner").child("userEmail").getValue().toString();
                            catUserOwner = new CatUser(userID,userName,userPhone,userEmail);

                            System.out.println("userID: " + userID);
                            System.out.println("userName: " + userName);
                            System.out.println("userPhone: " + userPhone);
                            System.out.println("userEmail: " + userEmail);

                            catID = childDataSnapshot.child("id").getValue().toString();
                            catName = childDataSnapshot.child("catName").getValue().toString();
                            catRace = childDataSnapshot.child("catRace").getValue().toString();
                            catDescription = childDataSnapshot.child("catDescription").getValue().toString();
                            catLatitude = Double.parseDouble(childDataSnapshot.child("catLatitude").getValue().toString());
                            catLongitude = Double.parseDouble(childDataSnapshot.child("catLongitude").getValue().toString());

                            System.out.println("catName: " + catName);
                            System.out.println("catRace: " + catRace);
                            System.out.println("catDescription: " + catDescription);
                            System.out.println("catLatitude: " + catLatitude);
                            System.out.println("catLongitude: " + catLongitude);

                            Cat cat = new Cat(catID, catName, catRace, catDescription, catUserOwner, catLatitude, catLongitude);
                            System.out.println(cat);

                            arrCatList.add(cat);
                            System.out.println(arrCatList.size());
                        }

                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("Erro: " + databaseError.getDetails());
                }
            });
            return arrCatList;

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }

    }

    public static void getAllCatsFromFirebase() {
            //Instancia o Firebase
            refFirebase = FirebaseDatabase
                    .getInstance()
                    .getReference("/cat");

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

                            Cat cat = new Cat(catID, catName, catRace, catDescription, catUserOwner, catLatitude, catLongitude);

                            arrListCat.add(cat);
                            System.out.println(arrListCat.size());
                        }
                        //Just after getting the cats, show them on the map
                        for (final Cat cat : arrListCat) {
                            downloadCatPic(cat);

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    arrListCat.clear();
                }
            };

        }
    private static void downloadCatPic(final Cat cat){
        String catID = cat.getId();

        try{
            StorageReference fotoReference = FirebaseStorage.getInstance().getReference().child("cat/" + catID + ".png");
            final File temp = File.createTempFile(catID,null,null);

            fotoReference.getFile(temp).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    cat.setCatPicture(BitmapFactory.decodeFile(temp.getPath()));
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

    public static void getCatsFromOwner(){
        if (!arrListCat.isEmpty()){

        }
    }
*/

}
