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
    }

    /**
     * Navega para a tela principal da aplicação.
     */
    public void Navegar(View view) {
        Intent mudatTelaMain = new Intent(TelaInicio.this, MainActivity.class);
        startActivity(mudatTelaMain);
    }

    /**
     * Navega para a tela de login.
     */
    public void Acessar(View view)
    {
        Intent mudarTelaLogin = new Intent(TelaInicio.this, FormLogin.class);
        startActivity(mudarTelaLogin);
    }

    /**
     * Navega para a tela de escolha de cadastro.
     */
    public void Cadastrar(View view)
    {
        Intent mudarTelaCadastro = new Intent(TelaInicio.this, FormCadastro.class);
        startActivity(mudarTelaCadastro);
    }
    public void VirarParceiro(View view)
    {
        Intent mudarTelaParceiro = new Intent(TelaInicio.this, FormCadastroPJ.class);
        startActivity(mudarTelaParceiro);
    }

}