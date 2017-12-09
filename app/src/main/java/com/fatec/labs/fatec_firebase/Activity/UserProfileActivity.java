package com.fatec.labs.fatec_firebase.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fatec.labs.fatec_firebase.Common.Notify;
import com.fatec.labs.fatec_firebase.DAO.ConfiguracaoFirebase;
import com.fatec.labs.fatec_firebase.DAO.UserDAO;
import com.fatec.labs.fatec_firebase.Model.User;
import com.fatec.labs.fatec_firebase.R;
import com.fatec.labs.fatec_firebase.Utils.Base64Custom;
import com.fatec.labs.fatec_firebase.Utils.Preferencias;
import com.google.android.gms.internal.ex;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class UserProfileActivity extends AppCompatActivity {

    /* Variaveis */
    private EditText nomePerflUsuario;
    private EditText emaiPerflUsuario;
    private EditText phonePerflUsuario;
    private Button btnAlterPerfUsuario;
    private ImageView fotoDoUsuario;
    private FirebaseUser user;
    private User usuario;
    private String userCod;
    private boolean verFoto = false;
    public FirebaseAuth mAuth;
    private Bitmap foto;
    public static Bitmap newPhoto;//Alterdo 07/12/2017
    private static final int REQUEST_CAMERA = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();

        nomePerflUsuario = findViewById(R.id.edtUserPerfName);
        emaiPerflUsuario = findViewById(R.id.edtUserPerfEmail);
        phonePerflUsuario = findViewById(R.id.edtUserPerfCelular);
        fotoDoUsuario = findViewById(R.id.imgPerfUsuario);
        btnAlterPerfUsuario = findViewById(R.id.btnAlterarUsuario);
        newPhoto = null;//Alterdo 07/12/2017

        nomePerflUsuario.setText(UserDAO.loggedCatUser.getUserName());
        emaiPerflUsuario.setText(UserDAO.loggedCatUser.getUserEmail());
        phonePerflUsuario.setText(UserDAO.loggedCatUser.getUserPhone());
        fotoDoUsuario.setImageBitmap(UserDAO.loggedCatUser.getUserImage());

        user = FirebaseAuth.getInstance().getCurrentUser();
        userCod = user.getUid();

        btnAlterPerfUsuario.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                boolean cancel = false;
                View focusView = null;
                String nome = nomePerflUsuario.getText().toString();
                String celular = phonePerflUsuario.getText().toString();
                String email = emaiPerflUsuario.getText().toString();

                if (TextUtils.isEmpty(nome)) {
                    nomePerflUsuario.setError(getString(R.string.error_field_required));
                    focusView = nomePerflUsuario;
                    cancel = true;
                }

                if (TextUtils.isEmpty(email)) {
                    emaiPerflUsuario.setError(getString(R.string.error_field_required));
                    focusView = emaiPerflUsuario;
                    cancel = true;

                } if (TextUtils.isEmpty(celular)) {
                    phonePerflUsuario.setError(getString(R.string.error_field_required));
                    focusView = phonePerflUsuario;
                    cancel = true;
                }
                if (cancel) {
                    focusView.requestFocus();
                } else {

                    usuario = new User();
                    usuario.setId(userCod);
                    usuario.setUserSenha(UserDAO.loggedCatUser.getUserSenha());
                    usuario.setUserName(nomePerflUsuario.getText().toString());
                    usuario.setUserPhone(phonePerflUsuario.getText().toString());
                    usuario.setUserEmail(emaiPerflUsuario.getText().toString());

                    AlterarPerfilUsuario();
                    if (verFoto) {
                        UserDAO.loggedCatUser.setUserImage(newPhoto);//Alterdo 07/12/2017
                        MenuActivity.imgUser.setImageBitmap(Bitmap.createScaledBitmap(newPhoto, 100, 100, true));
                        salvarFoto();
                    }
                    user.updateEmail(emaiPerflUsuario.getText().toString());
                    Toast.makeText(UserProfileActivity.this, "Dados salvos com sucesso", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void tirarFoto(View v){ usarCamera(); }
    public void usarCamera(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String [] {android.Manifest.permission.CAMERA},REQUEST_CAMERA );
        } else{
            verFoto = true;
            tirarFoto();
        }
    }
    public void tirarFoto() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, REQUEST_CAMERA);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            foto = (Bitmap) data.getExtras().get("data");
            newPhoto = foto;//Alterdo 07/12/2017
            fotoDoUsuario.setImageBitmap(newPhoto);//Alterdo 07/12/2017
        }
    }
    public void salvarFoto(){
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        StorageReference fotoReference = storage.child("user/"+userCod+".png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        foto.compress(Bitmap.CompressFormat.PNG, 0, baos);
        byte[] fotoComprimida = baos.toByteArray();
        fotoReference.putBytes(fotoComprimida).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {}
        });
    }


    private void AlterarPerfilUsuario(){
        try {
            if (user == null) {
                Notify.showNotify(UserProfileActivity.this, "Nothing");
            } else {
                try {
                    UserDAO.loggedCatUser.setUserEmail(emaiPerflUsuario.getText().toString());
                    UserDAO.loggedCatUser.setUserPhone(phonePerflUsuario.getText().toString());
                    UserDAO.loggedCatUser.setUserName(nomePerflUsuario.getText().toString());

                    MenuActivity.tEmail.setText(UserDAO.loggedCatUser.getUserEmail());
                    MenuActivity.tNome.setText(UserDAO.loggedCatUser.getUserName());

                    usuario.setId(userCod);
                    DatabaseReference referenciaDataBase = FirebaseDatabase.getInstance().getReference();
                    referenciaDataBase.child("user").child(String.valueOf(userCod)).setValue(usuario);

                    Preferencias preferencias = new Preferencias(UserProfileActivity.this);
                    preferencias.salvarUsuarioPreferencias(userCod,usuario.getUserName());

                    FecharActivity();
                } catch(Exception e){
                    throw  new Exception( "Erro ao efetuar o cadastro!");
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void FecharActivity(){
        finish();
    }
}
