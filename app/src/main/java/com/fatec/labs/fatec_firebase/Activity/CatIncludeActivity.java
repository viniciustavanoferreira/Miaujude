package com.fatec.labs.fatec_firebase.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.fatec.labs.fatec_firebase.DAO.UserDAO;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.fatec.labs.fatec_firebase.Common.GPSTracker;
import com.fatec.labs.fatec_firebase.Common.Notify;
import com.fatec.labs.fatec_firebase.Model.Cat;
import com.fatec.labs.fatec_firebase.Model.CatUser;
import com.fatec.labs.fatec_firebase.Utils.Base64Custom;
import com.fatec.labs.fatec_firebase.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import static com.fatec.labs.fatec_firebase.Common.Permissions.REQUEST_CAMERA;

public class CatIncludeActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private EditText edtCatName;
    private EditText edtCatRace;
    private EditText edtCatDescription;
    private double dblLatitude;
    private double dblLongitude;
    private Button btnSubmitCat;
    private Button btnCancelCat;
    private ImageView fotoDoGato;
    private Bitmap pic;
    private boolean chkPic = false;

    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_include);

        mAuth = FirebaseAuth.getInstance();

        //Debug - exibir os dados do usuário logado
        //Notify.showNotify(this,"Logged ID: " + UserDAO.loggedCatUser.getId());
        //Notify.showNotify(this,"Logged Username: " + UserDAO.loggedCatUser.getUserName());
        //Notify.showNotify(this,"Logged Email: " + UserDAO.loggedCatUser.getUserEmail());
        //Notify.showNotify(this,"Logged Phone: " + UserDAO.loggedCatUser.getUserPhone());

        //Elementos da tela
        edtCatName = (EditText) findViewById(R.id.edtCatName);
        edtCatRace = (EditText) findViewById(R.id.edtCatRace);
        edtCatDescription = (EditText) findViewById(R.id.edtCatDescription);
        btnSubmitCat = (Button) findViewById(R.id.btnSubmitCat);
        fotoDoGato = findViewById(R.id.imgTirarGatoAdocao);
        btnSubmitCat.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //is there some unfilled fields?
                boolean blnValid;
                //Notify.showNotify(CatIncludeActivity.this,"tem texto no nome: " + (edtCatName.getText().toString() == ""));
                //Notify.showNotify(CatIncludeActivity.this,"tem texto na raça: " + (edtCatRace.getText().toString() == ""));
                blnValid = true;
                //blnValid = !(edtCatName.getText().toString() == "" || edtCatRace.getText().toString() == "");
                if (!blnValid) {
                    Notify.showNotify(CatIncludeActivity.this,getString(R.string.error_invalid_cat));
                } else {
                    //initiate insert process
                    addCat();
                }
            }
        });

    }

    public void takePic(View v){
        usarCamera();
    }

    public boolean usarCamera(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String [] {android.Manifest.permission.CAMERA},REQUEST_CAMERA );
        }
        else{
            tirarFoto();
            return true;
        }
        return false;
    }
    public void tirarFoto() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            pic = (Bitmap) data.getExtras().get("data");
            fotoDoGato.setImageBitmap(pic);
            chkPic = true;
        }
    }

    public void savePicCat(String strCatId){
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        StorageReference fotoReference = storage.child("cat/"+strCatId+".png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        pic.compress(Bitmap.CompressFormat.PNG, 0, baos);
        byte[] picCompressed = baos.toByteArray();
        fotoReference.putBytes(picCompressed).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }



    private void addCat(){
        //prepare cat data to be inserted
        String strCatName = edtCatName.getText().toString();
        String strCatRace = edtCatRace.getText().toString();
        String strCatDescription = edtCatDescription.getText().toString();
        CatUser usrCatOwner = new CatUser();
        usrCatOwner.setId(UserDAO.loggedCatUser.getId());
        usrCatOwner.setUserName(UserDAO.loggedCatUser.getUserName());
        usrCatOwner.setUserPhone(UserDAO.loggedCatUser.getUserPhone());
        usrCatOwner.setUserEmail(UserDAO.loggedCatUser.getUserEmail());

        //get gps coordinates
        GPSTracker gps = new GPSTracker(this);
        dblLatitude = gps.getLatitude();
        dblLongitude = gps.getLongitude();

        //build the cat id
        String strCatId = Base64Custom.codificarBase64(strCatName + strCatRace + usrCatOwner.getId());

        //create instance and send to database
        Cat cat = new Cat(strCatName,strCatRace,strCatDescription,usrCatOwner,dblLatitude,dblLongitude);
        cat.setId(strCatId);
        if (cat.addCat()) {
            if(chkPic){
                savePicCat(strCatId);
            }
            Notify.showNotify(this,getString(R.string.message_cat_included_success));
            this.finish();
            CatIncludeActivity.this.finishActivity(0);
        } else {
            Notify.showNotify(this,getString(R.string.message_cat_included_failure));
        }
    }
}
