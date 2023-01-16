package com.MarioPovil.zombies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        int Duracion = 1500;
        /*-- Handler, ejecuta lineas de codigo en un tiempo determinado --*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this , MainActivity.class);
                startActivity(intent);

              /*  startActivity(new Intent(Splash.this, MainActivity.class));  Forma 2 de ejecutar el intent*/
            }
        }, Duracion);
    }
}