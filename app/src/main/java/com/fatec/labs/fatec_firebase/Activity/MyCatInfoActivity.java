package com.fatec.labs.fatec_firebase.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fatec.labs.fatec_firebase.Common.Notify;
import com.fatec.labs.fatec_firebase.Common.Permissions;
import com.fatec.labs.fatec_firebase.DAO.CatDAO;
import com.fatec.labs.fatec_firebase.R;

import java.security.Permission;

import static com.fatec.labs.fatec_firebase.Common.Permissions.REQUEST_PHONE;

public class MyCatInfoActivity extends AppCompatActivity {

    private ImageView imgGalery;
    private TextView tvwMostrarCatName;
    private TextView tvwMostrarCatRaca;
    private TextView tvwMostrarCatDescription;

    @Override
    protected void onStart() {
        super.onStart();

        //If the Cat has picture, show it
        if (CatDAO.selectedCat.getCatPicture()!=null) {
            imgGalery.setImageBitmap(CatDAO.selectedCat.getCatPicture());
        }
        tvwMostrarCatName.setText(CatDAO.selectedCat.getCatName());
        tvwMostrarCatRaca.setText(CatDAO.selectedCat.getCatRace());
        tvwMostrarCatDescription.setText(CatDAO.selectedCat.getCatDescription());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cat_info);

        imgGalery = (ImageView) findViewById(R.id.imgGalery);
        tvwMostrarCatName = (TextView) findViewById(R.id.tvwMostrarCatName);
        tvwMostrarCatRaca = (TextView) findViewById(R.id.tvwMostrarCatRaca);
        tvwMostrarCatDescription = (TextView) findViewById(R.id.tvwMostrarCatDescription);
        //tvwMostrarUserName = (TextView) findViewById(R.id.tvwMostrarUserName);
    }

}
