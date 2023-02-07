package com.MarioPovil.zombies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Registro extends AppCompatActivity {
    //Declaracion de Variables
    EditText correoEt, passEt, nombreEt, paisEt, edadEt;
    TextView fechaTxt;
    Button Registrar;

    FirebaseAuth auth; //FireBase Authentication

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //Inicializacion de las variables
        correoEt = findViewById(R.id.correoEt);
        passEt = findViewById(R.id.passEt);
        nombreEt = findViewById(R.id.nombreEt);
        fechaTxt = findViewById(R.id.fechaTxt);
        Registrar = findViewById(R.id.Registrar);
        edadEt = findViewById(R.id.edadEt);
        paisEt = findViewById(R.id.paisEt);

        //Inicializar Firebase
        auth = FirebaseAuth.getInstance();

        Date date = new Date();
        SimpleDateFormat fecha = new SimpleDateFormat("d 'de' MMMM 'del' yyyy" ); /* 15 de Mayo del 2023 Formato de fecha*/
        String StringFecha = fecha.format(date);
        fechaTxt.setText(StringFecha);

        Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = correoEt.getText().toString();
                String password = passEt.getText().toString();

                /*Validacion*/
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    correoEt.setError("Correo no Valido!");
                    correoEt.setFocusable(true);
                } else if (password.length()<6) {
                    passEt.setError("Contraseña muy debil");
                    passEt.setFocusable(true);
                }else{
                    registrarJugador(email,password);
                    }
                }
        });
    }
    /*Registrar jugador*/
    private void registrarJugador(String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                /*SI EL REGISTRO ES EXITOSO*/
                if(task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();

                    int contador = 0;
                    assert user != null;

                    String uidString = user.getUid();
                    String correoString = correoEt.getText().toString();
                    String passString = passEt.getText().toString();
                    String nombreString = nombreEt.getText().toString();
                    String fechaString = fechaTxt.getText().toString();
                    String edadString = edadEt.getText().toString();
                    String paisString = paisEt.getText().toString();

                    HashMap<Object, Object> DatosJugador = new HashMap<>(); /*HashMap sirve para dar clases a nuestro servidor*/
                    DatosJugador.put("Uid", uidString);
                    DatosJugador.put("Edad", edadString);
                    DatosJugador.put("Pais", paisString);
                    DatosJugador.put("imagen","");
                    DatosJugador.put("Email", correoString);
                    DatosJugador.put("Password", passString);
                    DatosJugador.put("Nombres", nombreString);
                    DatosJugador.put("Fecha", fechaString);
                    DatosJugador.put("Zombies", contador);

                    FirebaseDatabase database = FirebaseDatabase.getInstance(); //Instancia de DB
                    DatabaseReference reference = database.getReference("JugadoresZombies");//Nombre DB
                    reference.child(uidString).setValue(DatosJugador);
                    startActivity(new Intent(Registro.this, Menu.class));
                    Toast.makeText(Registro.this, "Usuario Registrado Exitosamente", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                        Toast.makeText(Registro.this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                }
            }
        })      /*SI FALLA EL REGISTRO*/
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Registro.this, ""+e.getMessage(), Toast.LENGTH_SHORT);
                    }
                });
}
}