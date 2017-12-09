package com.fatec.labs.fatec_firebase.Common;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fatec.labs.fatec_firebase.Activity.LoginActivity;
import com.fatec.labs.fatec_firebase.Activity.MenuActivity;
import com.fatec.labs.fatec_firebase.Activity.StartActivity;
import com.fatec.labs.fatec_firebase.R;

public class SplashLoginActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 4000;

    ImageView ImageView;
    ImageView BarraView;
    AnimationDrawable animation;
    AnimationDrawable animabarra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_login);


        ImageView = (ImageView) findViewById(R.id.ImageViewSplash);
        if(ImageView == null) throw new AssertionError();
        ImageView.setBackgroundResource(R.drawable.animation_loading);
        ImageView.setVisibility(View.VISIBLE);

        BarraView = (ImageView) findViewById(R.id.barraView);
        if(BarraView == null) throw new AssertionError();
        BarraView.setBackgroundResource(R.drawable.animation_barraloading);
        BarraView.setVisibility(View.VISIBLE);

        animation = (AnimationDrawable) ImageView.getBackground();
        animation.start();

        animabarra = (AnimationDrawable) BarraView.getBackground();
        animabarra.start();

        new Handler().postDelayed(new Runnable(){



            @Override
            public void run() {

                Intent intentMenu = new Intent(SplashLoginActivity.this, MenuActivity.class);
                startActivity(intentMenu);
                finish();

            }

        },SPLASH_TIME_OUT);


    }
}
