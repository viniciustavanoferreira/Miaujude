package com.fatec.labs.fatec_firebase.Model;

import android.graphics.Bitmap;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.fatec.labs.fatec_firebase.DAO.ConfiguracaoFirebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cat {
    private String id;
    private String catName;
    private String catRace;
    private String catDescription;
    private CatUser catUserOwner;
    private double catLatitude;
    private double catLongitude;
    private boolean catAdopted;
    private Bitmap catPicture;
    private ArrayList<Bitmap> catPictureGallery;

    public Cat(){
    }

    public Cat(String catName, String catRace, String catDescription, CatUser catUserOwner, double catLatitude, double catLongitude){
        setCatName(catName);
        setCatRace(catRace);
        setCatDescription(catDescription);
        setCatUserOwner(catUserOwner);
        setCatLatitude(catLatitude);
        setCatLongitude(catLongitude);
    }

    public Cat(String id, String catName, String catRace, String catDescription, CatUser catUserOwner,
                    double catLatitude, double catLongitude, boolean catAdopted){
        setId(id);
        setCatName(catName);
        setCatRace(catRace);
        setCatDescription(catDescription);
        setCatUserOwner(catUserOwner);
        setCatLatitude(catLatitude);
        setCatLongitude(catLongitude);
        setCatAdopted(catAdopted);
    }

    public boolean addCat(){
        try{
            String strCatID = this.getId();
            DatabaseReference referenciaDataBase = ConfiguracaoFirebase.getFireBase();
            referenciaDataBase.child("cat").child(strCatID).setValue(this);
            return true;
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage().toString());
            e.printStackTrace();
            return false;
        }
    }

    @Exclude
    public Map<String, Object> ToMap(){
        HashMap<String, Object> hashMapCat = new HashMap<>();
        hashMapCat.put("id", getId());
        hashMapCat.put("catName", getCatName());
        hashMapCat.put("catRace", getCatRace());
        hashMapCat.put("carDescription", getCatDescription());
        hashMapCat.put("catUserOwner", getCatUserOwner());
        hashMapCat.put("catLatitude", getCatLatitude());
        hashMapCat.put("catLongitude", getCatLongitude());
        return hashMapCat;
    }

    //Controls whether cat is or not adopted
    public boolean isCatAdopted() { return catAdopted;}
    public void setCatAdopted(boolean isCatAdopted) {this.catAdopted = isCatAdopted;}

    //Added to make it easier to link photo
    public Bitmap getCatPicture()           {   return catPicture;    }
    public void setCatPicture(Bitmap catPicture)    {   this.catPicture = catPicture;    }

    //Default Cat elements
    public String getId()           {   return id;    }
    public void setId(String id)    {   this.id = id;    }
    public String getCatName()      {   return catName;    }
    public void setCatName(String catName) {        this.catName = catName;    }
    public String getCatRace() {        return catRace;    }
    public void setCatRace(String catRace) {        this.catRace = catRace;    }
    public String getCatDescription() {        return catDescription;    }
    public void setCatDescription(String catDescription) {        this.catDescription = catDescription;    }
    public CatUser getCatUserOwner() {        return catUserOwner;    }
    public void setCatUserOwner(CatUser catUserOwner) {        this.catUserOwner = catUserOwner;    }
    public double getCatLatitude() {        return catLatitude;    }
    public void setCatLatitude(double catLatitude) {        this.catLatitude = catLatitude;    }
    public double getCatLongitude() {        return catLongitude;    }
    public void setCatLongitude(double catLongitude) {        this.catLongitude = catLongitude;    }
}