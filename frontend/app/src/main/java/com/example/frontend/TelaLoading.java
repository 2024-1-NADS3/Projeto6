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

        // Função que atribui um delay de 2 segundos ao mudar de tela
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mudarTela();
            }
        }, 2000);
    }

    /**
     * Muda para a tela de início após um delay de 2 segundos.
     */
    public void mudarTela()
    {
        Intent intent = new Intent(TelaLoading.this, TelaInicio.class);
        startActivity(intent);
    }
}