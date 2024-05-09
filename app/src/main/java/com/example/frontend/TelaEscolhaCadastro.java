package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class TelaEscolhaCadastro extends AppCompatActivity {

    private ImageView voltarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_escolha_cadastro);
        getSupportActionBar();

        voltarLogin = findViewById(R.id.voltar_login);

        voltarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltarLogin();
            }
        });
    }

    /**
     * Navega de volta para a tela de login.
     */
    private void voltarLogin() {
        Intent voltarTelaLogin = new Intent(TelaEscolhaCadastro.this, FormLogin.class);
        startActivity(voltarTelaLogin);
    }

    /**
     * Navega para a tela de cadastro de usuário.
     */
    public void formUsuario(View view) {
        Intent mudarTelaCdastro = new Intent(TelaEscolhaCadastro.this, FormCadastro.class);
        startActivity(mudarTelaCdastro);
    }

    /**
     * Navega para a tela de cadastro de parceiro.
     */
    public void formParceiro(View view) {
        Intent mudarTelaCadastroPJ= new Intent(TelaEscolhaCadastro.this, FormCadastroPJ.class);
        startActivity(mudarTelaCadastroPJ);
    }
}