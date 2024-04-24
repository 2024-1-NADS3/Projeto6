package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class TelaLoading extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_loading);
        getSupportActionBar().hide();

        // Função que atribui um delay de 2 segundos ao mudar de tela
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mudarTela();
            }
        }, 2000);
    }

    // Função que chama a TelaInicio
    public void mudarTela()
    {
        Intent intent = new Intent(TelaLoading.this, TelaInicio.class);
        startActivity(intent);
    }
}