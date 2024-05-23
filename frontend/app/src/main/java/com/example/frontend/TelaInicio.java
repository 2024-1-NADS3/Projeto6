package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Activity para exibir a tela inicial do aplicativo.
 */
public class TelaInicio extends AppCompatActivity {

    private TextView sobre_nos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicio);

        sobre_nos = findViewById(R.id.sobre_nos);
        sobre_nos.setPaintFlags(sobre_nos.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        sobre_nos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mudarTelaInfo = new Intent(TelaInicio.this,TelaInfo.class);
                startActivity(mudarTelaInfo);
            }
        });
    }

    /**
     * Método para navegar para a tela principal da aplicação.
     * Este método é chamado quando um botão é clicado na tela inicial.
     */
    public void Navegar(View view) {
        Intent mudatTelaMain = new Intent(TelaInicio.this, MainActivity.class);
        startActivity(mudatTelaMain);
    }

    /**
     * Método para acessar a tela de login.
     * Este método é chamado quando um botão é clicado na tela inicial para acessar a tela de login.
     */
    public void Acessar(View view)
    {
        Intent mudarTelaLogin = new Intent(TelaInicio.this, FormLogin.class);
        startActivity(mudarTelaLogin);
    }

    /**
     * Método para acessar a tela de cadastro.
     * Este método é chamado quando um botão é clicado na tela inicial para acessar a tela de cadastro.
     */
    public void Cadastrar(View view)
    {
        Intent mudarTelaCadastro = new Intent(TelaInicio.this, FormCadastro.class);
        startActivity(mudarTelaCadastro);
    }

    /**
     * Método para acessar a tela de cadastro de parceiro.
     * Este método é chamado quando um botão é clicado na tela inicial para acessar a tela de cadastro de parceiro.
     */
    public void VirarParceiro(View view)
    {
        Intent mudarTelaParceiro = new Intent(TelaInicio.this, FormCadastroPJ.class);
        startActivity(mudarTelaParceiro);
    }
}