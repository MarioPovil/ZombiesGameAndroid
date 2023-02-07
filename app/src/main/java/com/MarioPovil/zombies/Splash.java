package com.MarioPovil.zombies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends AppCompatActivity {
    TextView textView,textView2;
    ImageView splashZombie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //Se pone en FullScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Animaciones
        Animation animacion1 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_arriba);
        Animation animacion2 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_abajo);
        //declarar por ID
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        splashZombie = findViewById(R.id.splashZombie);
        //Settear animaciones
        textView.setAnimation(animacion2);
        textView2.setAnimation(animacion2);
        splashZombie.setAnimation(animacion1);


        int Duracion = 2500;
        /*-- Handler, ejecuta lineas de codigo en un tiempo determinado --*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this , MainActivity.class);
                startActivity(intent);
                finish();
              /*  startActivity(new Intent(Splash.this, MainActivity.class));  Forma 2 de ejecutar el intent*/
            }
        }, Duracion);
    }
}