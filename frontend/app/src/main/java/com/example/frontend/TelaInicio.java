package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TelaInicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicio);
        getSupportActionBar().hide();
    }



    public void Navegar(View view) {
        Intent mudatTelaMain = new Intent(TelaInicio.this, MainActivity.class);
        startActivity(mudatTelaMain);
    }

    public void Acessar(View view)
    {
        Intent mudarTelaLogin = new Intent(TelaInicio.this, FormLogin.class);
        startActivity(mudarTelaLogin);
    }

    public void Cadastrar(View view)
    {
        Intent mudarTelaCadastro = new Intent(TelaInicio.this, TelaEscolhaCadastro.class);
        startActivity(mudarTelaCadastro);
    }

}