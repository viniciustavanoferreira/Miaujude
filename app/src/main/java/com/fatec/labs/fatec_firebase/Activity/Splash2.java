package com.fatec.labs.fatec_firebase.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.fatec.labs.fatec_firebase.R;

/**
 * Created by roberto on 08/12/17.
 */

public class Splash2 extends AppCompatActivity {

    private ProgressBar login_progress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);



        // Esconde tanto a barra de navegação e a barra de status .
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //Barra de progresso
        login_progress = (ProgressBar) findViewById(R.id.login_progress);

        final Thread tempoDaTread = new Thread(){
            @Override
            public void run(){
                while(LoginActivity.verRequisicao == false){
                    login_progress.setIndeterminate(true);
                    login_progress.setVisibility(View.VISIBLE);
                }
                Intent intentMenu = new Intent(Splash2.this, MenuActivity.class);
                startActivity(intentMenu);
                finish();
            }
        };
        tempoDaTread.start();
    }
}


