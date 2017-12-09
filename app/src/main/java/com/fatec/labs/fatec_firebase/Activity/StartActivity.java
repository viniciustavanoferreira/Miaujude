package com.fatec.labs.fatec_firebase.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fatec.labs.fatec_firebase.R;

public class StartActivity extends AppCompatActivity {

    private Button btnAbrirActivityLogin;
    private TextView tvAbreCadastroUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btnAbrirActivityLogin = findViewById(R.id.BtnFazerLogin);
        tvAbreCadastroUsuario = findViewById(R.id.txtViewAbreCadastroUsuario);

        btnAbrirActivityLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intentAbrirLoginActivity = new Intent(StartActivity.this,LoginActivity.class);
                startActivity(intentAbrirLoginActivity);
            }
        });

        tvAbreCadastroUsuario.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AbrirCadastroUsuario();
            }
        });

    }

    protected void onStart(Bundle savedInstanceState){
        super.onStart();
        btnAbrirActivityLogin.callOnClick();
    }

    public void AbrirCadastroUsuario(){
        Intent intentAbrirCadastroUsuario = new Intent(StartActivity.this,UserCreationActivity.class);
        startActivity(intentAbrirCadastroUsuario);
        finish();
    }
}
