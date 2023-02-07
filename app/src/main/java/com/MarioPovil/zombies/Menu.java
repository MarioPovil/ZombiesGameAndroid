package com.MarioPovil.zombies;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Menu extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference JUGADORES;
    Button CerrarSesion,AcercaDeBtn, JugarBtn,PuntuacionesBtn,CPswBtn,EditarBtn;
    CircleImageView fotoPerfil;
    TextView Menutxt,nombre,correo,uid,Zombies,MiPuntuaciontxt,fechareg, edadEt, paisEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        setContentView(R.layout.activity_menu);

        firebaseDatabase = FirebaseDatabase.getInstance();
        JUGADORES = firebaseDatabase.getReference("JugadoresZombies");

        Menutxt = findViewById(R.id.Menutxt);
        nombre = findViewById(R.id.nombre);
        correo = findViewById(R.id.correo);
        uid = findViewById(R.id.uid);
        fechareg = findViewById(R.id.fechareg);
        Zombies = findViewById(R.id.Zombies);
        edadEt = findViewById(R.id.edadEt);
        paisEt = findViewById(R.id.paisEt);
        MiPuntuaciontxt = findViewById(R.id.MiPuntuaciontxt);
        fotoPerfil= findViewById(R.id.fotoPerfil);

        AcercaDeBtn = findViewById(R.id.AcercaDeBtn);
        JugarBtn = findViewById(R.id.JugarBtn);
        PuntuacionesBtn = findViewById(R.id.PuntuacionesBtn);
        CerrarSesion = findViewById(R.id.CerrarSesion);
        CPswBtn = findViewById(R.id.CPswBtn);
        EditarBtn = findViewById(R.id.EditarBtn);

        JugarBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Menu.this, EscenarioJuego.class);

            String UidS = uid.getText().toString();
            String NombresS = nombre.getText().toString();
            String ZombieS = Zombies.getText().toString();

            intent.putExtra("UID", UidS);
            intent.putExtra("NOMBRE", NombresS);
            intent.putExtra("ZOMBIE", ZombieS);
            startActivity(intent);

        });
        EditarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu.this, "Editar", Toast.LENGTH_SHORT).show();
            }
        });
        CPswBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu.this, "Cambiar De Contraseña", Toast.LENGTH_SHORT).show();
            }
        });
        AcercaDeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu.this, "Acerca De", Toast.LENGTH_SHORT).show();
            }
        });
        PuntuacionesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Menu.this, "Su puntuacion", Toast.LENGTH_SHORT).show();
            }
        });
        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CerrarSesion();
            }
        });

    }
    //Este metodo se ejecuta cuando se abre el minijuego
    @Override
    protected void onStart() {
        UsuarioLogeado();
        super.onStart();
    }

    // METODO COMPRUEBA SI EL JUGAR HA INICIADO SESIÓN
    private void UsuarioLogeado(){
        if(user != null){
            Consulta();
        }else{
            startActivity(new Intent(Menu.this, MainActivity.class));
            finish();
        }
    }

    private void CerrarSesion(){
        auth.signOut();
        startActivity(new Intent(Menu.this, MainActivity.class));
        Toast.makeText(this, "Has cerrado sesión", Toast.LENGTH_SHORT).show();
    }
    private void Consulta(){
        /*CONSULTA*/
        Query query = JUGADORES.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for( DataSnapshot ds : snapshot.getChildren()){
                    //Se Hacen las querys de zombies, uid, nombre, email
                    String zombiesString = ""+ds.child("Zombies").getValue();
                    String uidString = ""+ds.child("Uid").getValue();
                    String fechaString = ""+ds.child("Fecha").getValue();
                    String edadString = ""+ds.child("Edad").getValue();
                    String paisString = ""+ds.child("Pais").getValue();
                    String imagen = ""+ds.child("Imagen").getValue();
                    String nombresString = ""+ds.child("Nombres").getValue();
                    String emailString = ""+ds.child("Email").getValue();
                    //Se colocan en sus textView
                    Zombies.setText(zombiesString);
                    uid.setText(uidString);
                    edadEt.setText(edadString);
                    paisEt.setText(paisString);
                    correo.setText(emailString);
                    nombre.setText(nombresString);
                    fechareg.setText(fechaString);
                    try {
                        Picasso.get().load(imagen).into(fotoPerfil);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.soldado).into(fotoPerfil);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}