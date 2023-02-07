package com.MarioPovil.zombies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
        // DECLARAR VARIABLES
        EditText correoLog, passLog;
        Button BtnLog;
        FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //INICIALIZAR VARIABLES
        correoLog = findViewById(R.id.correoLog);
        passLog = findViewById(R.id.passLog);
        BtnLog=findViewById(R.id.BtnLog);
        auth = FirebaseAuth.getInstance();

        BtnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = correoLog.getText().toString();
                String pass = passLog.getText().toString();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    correoLog.setError("correo invalido");
                    correoLog.setFocusable(true);
                } else if (pass.length()<6){
                    passLog.setError("Contraseña incorrecta");
                    passLog.setFocusable(true);
                }else{
                    LogeoDeJugador(email,pass);
                }
            }
        });
    }
    private void LogeoDeJugador(String email, String pass){
        auth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {  //En caso de logeo exitoso!
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            startActivity(new Intent(Login.this, Menu.class));
                            assert user !=null; //Afirmamos el usuario no es nulo!
                            Toast.makeText(Login.this, "BIENVENIDO(A)"+user.getEmail(),Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() { //Muestra mensaje de error
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}