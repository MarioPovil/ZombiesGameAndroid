package com.MarioPovil.zombies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    EditText correoLog, passLog;
    Button BtnLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //INICIALIZAR VARIABLES
        correoLog = findViewById(R.id.correoLog);
        passLog = findViewById(R.id.passLog);
        BtnLog=findViewById(R.id.BtnLog);


    }
}