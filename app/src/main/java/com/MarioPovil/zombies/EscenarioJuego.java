package com.MarioPovil.zombies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Random;

public class EscenarioJuego extends AppCompatActivity {
    String UID, NOMBRE, ZOMBIE;
    TextView TvContador,TvNombre,TvTiempo,AnchoTv,AltoTv;
    ImageView IvZombie;
    int contador = 0;
    int AnchoPantalla;
    int AltoPantalla;
    Random aleatorio;
    Boolean GameOver = false;
    Dialog miDialog;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference Jugadores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escenario_juego);
        TvContador = findViewById(R.id.TvContador);
        TvNombre = findViewById(R.id.TvNombre);
        TvTiempo = findViewById(R.id.TvTiempo);
        AltoTv = findViewById(R.id.AltoTv);
        AnchoTv = findViewById(R.id.AnchoTv);
        IvZombie = findViewById(R.id.IvZombie);
        //Inicializando Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser(); //obtiene los datos de usuario
        firebaseDatabase = FirebaseDatabase.getInstance();
        Jugadores = firebaseDatabase.getReference("JugadoresZombies"); //Referencia a la base de datos

        miDialog = new Dialog(EscenarioJuego.this);

        Bundle intent = getIntent().getExtras();
        UID = intent.getString("UID");
        NOMBRE = intent.getString("NOMBRE");
        ZOMBIE = intent.getString("ZOMBIE");
        TvContador.setText(ZOMBIE);
        TvNombre.setText(NOMBRE);
        Pantalla();
        CuentaAtras();

        //Al hacer click en la imagen
        IvZombie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!GameOver){

                contador++; //Aumenta el valor en 1
                TvContador.setText(String.valueOf(contador)); //Se seea el valor, y cambia a tipo string
                IvZombie.setImageResource(R.drawable.zombieaplastado);
                //NOS PERMITE EJECTUAR CODIGO EN CIERTO TIEMPO
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        IvZombie.setImageResource(R.drawable.zombie);
                        Movimiento();
                    }
                },150);
             }
          }
        });
    }
    //METODO PARA OBTENER EL TAMAÑO DE LA PANTALLA
    private void Pantalla(){
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        AnchoPantalla = point.x;
        AltoPantalla = point.y;
        String ANCHOS = String.valueOf(AnchoPantalla);
        String ALTOS = String.valueOf(AltoPantalla);
        AnchoTv.setText(ANCHOS);
        AltoTv.setText(ALTOS);
        aleatorio = new Random();
    }
    private void Movimiento(){
        int min = 0;
        int MaximoX=AnchoPantalla - IvZombie.getWidth();
        int MaximoY = AltoPantalla - IvZombie.getHeight();

        int randomX = aleatorio.nextInt(((MaximoX-min)+1)+min);
        int randomY = aleatorio.nextInt(((MaximoY-min)+1)+min);
        IvZombie.setX(randomX);
        IvZombie.setY(randomY);
    }
    private void CuentaAtras(){
        new CountDownTimer(30000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                long segundosRestantes = millisUntilFinished/1000;
                TvTiempo.setText(segundosRestantes+"S");
            }

            @Override
            public void onFinish() {
                TvTiempo.setText("0S");
                GameOver=true;
                MensajeGameOver();
                GuardarResultados("Zombies",contador);
            }
        }.start();
    }
    public void MensajeGameOver(){

        TextView TimeOut,Kills,NKills;
        Button JugarDeNuevo,IrAMenu,Puntajes;

        miDialog.setContentView(R.layout.gameover);

        TimeOut = miDialog.findViewById(R.id.TimeOut);
        Kills = miDialog.findViewById(R.id.Kills);
        NKills = miDialog.findViewById(R.id.NKills);

        JugarDeNuevo = miDialog.findViewById(R.id.JugarDeNuevo);
        IrAMenu = miDialog.findViewById(R.id.IrAMenu);
        Puntajes = miDialog.findViewById(R.id.Puntajes);

        String zombies= String.valueOf(contador);
        NKills.setText(zombies);

        JugarDeNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contador=0;
                miDialog.dismiss();
                TvContador.setText("0");
                GameOver=false;
                CuentaAtras();
                Movimiento();
            }
        });
        IrAMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EscenarioJuego.this, Menu.class));
            }
        });
        Puntajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Toast.makeText(EscenarioJuego.this,"Puntuajes", Toast.LENGTH_SHORT).show();
            }
        });
        miDialog.show();
    }
    private void GuardarResultados(String key, int zombies){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put(key,zombies);
        Jugadores.child(user.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EscenarioJuego.this, "El puntuaje cambio", Toast.LENGTH_SHORT);
            }
        });
    }
}