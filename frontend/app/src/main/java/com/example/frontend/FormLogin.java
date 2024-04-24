package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
public class FormLogin extends AppCompatActivity {

    private TextView esqueci_senha;
    private TextView text_tela_cadastro;
    private ImageView voltar_tela_inicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        getSupportActionBar().hide();

        MudarTelaCadatros();
        Voltartela();
        MudarTelaRecSenha();

        text_tela_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mudarTelaCadastro = new Intent(FormLogin.this,TelaEscolhaCadastro.class);
                startActivity(mudarTelaCadastro);
            }
        });
        esqueci_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mudarTelaRecSenha= new Intent(FormLogin.this,TelaRecuperarSenha.class);
                startActivity(mudarTelaRecSenha);
            }
        });
        voltar_tela_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent voltarTelaInicio = new Intent(FormLogin.this,TelaInicio.class);
                startActivity(voltarTelaInicio);
            }
        });
    }
    private void MudarTelaCadatros() {
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);
    }

    private void MudarTelaRecSenha() {
        esqueci_senha = findViewById(R.id.esqueci_senha);
    }

    private void Voltartela() {
        voltar_tela_inicio = findViewById(R.id.voltar_tela_inicio);
    }
}