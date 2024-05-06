package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class TelaRecuperarSenha extends AppCompatActivity {
    private ImageView voltar_tela_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_recuperar_senha);
        VoltarTela();

        voltar_tela_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent voltarTelaInicio = new Intent(TelaRecuperarSenha.this,FormLogin.class);
                startActivity(voltarTelaInicio);
            }
        });
    }
    private void VoltarTela(){
        voltar_tela_login = findViewById(R.id.voltar_tela_login);
    }
}