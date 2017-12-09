package com.fatec.labs.fatec_firebase.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.fatec.labs.fatec_firebase.DAO.ConfiguracaoFirebase;
import com.fatec.labs.fatec_firebase.Model.User;
import com.fatec.labs.fatec_firebase.Utils.Base64Custom;
import com.fatec.labs.fatec_firebase.Utils.Preferencias;
import com.fatec.labs.fatec_firebase.R;

import java.io.ByteArrayOutputStream;

public class UserCreationActivity extends AppCompatActivity {

    private EditText nomeCadlUsuario;
    private EditText emaiCadlUsuario;
    private EditText phoneCadlUsuario;
    private EditText senhaCadlUsuario;
    private EditText confirmaSenhaCadlUsuario;
    private Button btnCadSalvarUsuario;
    private ImageView fotoDoUsuario;
    private User user;
    private FirebaseAuth autenticacao;
    private Bitmap foto;

    private boolean verificarFoto = false;
    private static final int REQUEST_CAMERA = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_creation);

        //teste pra atualizar git
        nomeCadlUsuario = findViewById(R.id.nomeCadlUsuario);
        emaiCadlUsuario = findViewById(R.id.emaiCadlUsuario);
        phoneCadlUsuario = findViewById(R.id.phoneCadlUsuario);
        senhaCadlUsuario = findViewById(R.id.senhaCadlUsuario);
        confirmaSenhaCadlUsuario = findViewById(R.id.confirmaSenhaCadlUsuario);
        fotoDoUsuario = findViewById(R.id.imgCadUsuario);
        btnCadSalvarUsuario = findViewById(R.id.btnCadSalvarUsuario);

        btnCadSalvarUsuario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (senhaCadlUsuario.getText().toString().equals(confirmaSenhaCadlUsuario.getText().toString())) {
                    user = new User();
                    user.setUserName(nomeCadlUsuario.getText().toString());
                    user.setUserPhone(phoneCadlUsuario.getText().toString());
                    user.setUserEmail(emaiCadlUsuario.getText().toString());
                    user.setUserSenha(senhaCadlUsuario.getText().toString());
                    cadastrarUsuario();
                } else {
                    Toast.makeText(UserCreationActivity.this, "Senhas não são iguais, digite novamente!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void tirarFoto(View v) {
        usarCamera();
    }

    public void usarCamera() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA);
        } else {
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
            fotoDoUsuario.setImageBitmap(foto);
            verificarFoto = true;
        }
    }

    public void salvarFoto(String idUsusario) {
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        StorageReference fotoReference = storage.child("user/" + idUsusario + ".png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        foto.compress(Bitmap.CompressFormat.PNG, 0, baos);
        byte[] fotoComprimida = baos.toByteArray();
        fotoReference.putBytes(fotoComprimida).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }

    //Alterado 03/12/2017 17:02
    private void cadastrarUsuario() {

        if (verificarFoto) {//Alterado 03/12/2017 17:02

            boolean cancel = false;
            View focusView = null;

            String nome = nomeCadlUsuario.getText().toString();
            String celular = phoneCadlUsuario.getText().toString();
            String email = emaiCadlUsuario.getText().toString();
            String senha = senhaCadlUsuario.getText().toString();

            if (TextUtils.isEmpty(nome)) {
                nomeCadlUsuario.setError(getString(R.string.error_field_required));
                focusView = nomeCadlUsuario;
                cancel = true;
            }

            if (TextUtils.isEmpty(email)) {
                emaiCadlUsuario.setError(getString(R.string.error_field_required));
                focusView = emaiCadlUsuario;
                cancel = true;

            } if (TextUtils.isEmpty(celular)) {
                phoneCadlUsuario.setError(getString(R.string.error_field_required));
                focusView = phoneCadlUsuario;
                cancel = true;
            }
            if (TextUtils.isEmpty(senha)) {
                senhaCadlUsuario.setError(getString(R.string.error_field_required));
                focusView = senhaCadlUsuario;
                cancel = true;
            }

            if (cancel) {
                focusView.requestFocus();
            } else {

                autenticacao = ConfiguracaoFirebase.getAutenticacao();
                autenticacao.createUserWithEmailAndPassword(
                        user.getUserEmail(),
                        user.getUserSenha()
                ).addOnCompleteListener(UserCreationActivity.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserCreationActivity.this, "Usuario Cadastrado com Sucesso!", Toast.LENGTH_SHORT).show();

                            String identificadorUsuario = task.getResult().getUser().getUid();//Alterado 03/12/2017 17:02
                            //FirebaseUser usuarioFirebase = task.getResult().getUser();  //Alterado 03/12/2017 17:02

                            user.setId(identificadorUsuario);
                            user.salvarUsuario();
                            salvarFoto(identificadorUsuario);
                            Preferencias preferencias = new Preferencias(UserCreationActivity.this);
                            preferencias.salvarUsuarioPreferencias(identificadorUsuario, user.getUserName());

                            AbrirLoginusuario();

                        } else {
                            String erroExcecao = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                erroExcecao = "Digite uma senha de 8 caracteres sendo letras e numeros";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erroExcecao = "O E-mail digitado é invalido, digite um novo e-mail";
                            } catch (FirebaseAuthUserCollisionException e) {
                                erroExcecao = "Este e-mail ja esta cadastrado no sistema";
                            } catch (Exception e) {
                                erroExcecao = "Erro ao efetuar o cadastro!";
                                e.printStackTrace();
                            }

                            Toast.makeText(UserCreationActivity.this, "Erro:" + erroExcecao, Toast.LENGTH_SHORT).show();
                        }
                    }

                });

            }

        }

        else {//Alterado 03/12/2017 17:02
            Toast.makeText(UserCreationActivity.this, "Foto requerida ao criar usuário", Toast.LENGTH_SHORT).show();
        }
    }

    public void AbrirLoginusuario() {

        Intent intentAbrirLoginUsuario = new Intent(UserCreationActivity.this, LoginActivity.class);
        startActivity(intentAbrirLoginUsuario);
        finish();
    }


}
