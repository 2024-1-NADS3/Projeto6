package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class TelaRecuperarSenha extends AppCompatActivity {
    private ImageView voltar_tela_login;
    private EditText emailenv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_recuperar_senha);
        emailenv = findViewById(R.id.email);
        VoltarTela();

        voltar_tela_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent voltarTelaInicio = new Intent(TelaRecuperarSenha.this,FormLogin.class);
                startActivity(voltarTelaInicio);
            }
        });
    }

    public void EnviarEmailRecuperacao(View view) {
        String email = emailenv.getText().toString().trim();

        // Verifica se o campo de e-mail está vazio
        if (email.isEmpty()) {
            emailenv.setError("Digite seu e-mail");
            emailenv.requestFocus();
            return;
        }

        if (!ValidacaoFormCadastro.isValidEmail(email)) {
            emailenv.setError("Email inválido");
            return;
        }

        // Exemplo: enviar um Toast informando que o e-mail de recuperação foi enviado com sucesso
        Toast.makeText(this, "E-mail de recuperação enviado para " + email, Toast.LENGTH_SHORT).show();
    }
    private void VoltarTela(){
        voltar_tela_login = findViewById(R.id.voltar_tela_login);
    }
}