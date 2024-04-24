package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public class FormCadastroPJ extends AppCompatActivity implements VolleyCallback {


    private ImageView voltarLogin;

    private EditText campo_nome_pj, campo_cnpj, campo_email_pj, campo_numero_pj, senha_pj, confirmar_senha_pj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro_pj);
        getSupportActionBar().hide();

        voltarLogin = findViewById(R.id.voltar_login);

        voltarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltarTelaEscolhaCadastro();
            }
        });

        campo_cnpj = findViewById(R.id.campo_cnpj);
        campo_nome_pj = findViewById(R.id.campo_nome_pj);
        campo_email_pj = findViewById(R.id.campo_email_pj);
        campo_numero_pj = findViewById(R.id.campo_numero_pj);
        senha_pj = findViewById(R.id.senha_pj);
        confirmar_senha_pj = findViewById(R.id.confirmar_senha_pj);
    }

    public void cadastrarParceiro(View view) {
        final String cnpj = campo_cnpj.getText().toString();
        final String instituitionName = campo_nome_pj.getText().toString();
        final String email = campo_email_pj.getText().toString();
        final String cellnumber = campo_numero_pj.getText().toString();
        final String password = senha_pj.getText().toString();
        final String confirmPassword = confirmar_senha_pj.getText().toString();

        // Verificar se os campos estão vazios
        if (instituitionName.isEmpty() || cnpj.isEmpty() || email.isEmpty() || cellnumber.isEmpty() || password.isEmpty()) {
            if (instituitionName.isEmpty()) campo_nome_pj.setError("Nome é obrigatório");
            if (email.isEmpty()) campo_email_pj.setError("Email é obrigatório");
            if (cellnumber.isEmpty()) campo_numero_pj.setError("Número é obrigatório");
            if (password.isEmpty()) senha_pj.setError("Senha é obrigatória");
            if (cnpj.isEmpty()) campo_cnpj.setError("CNPJ é obrigatório");
            return;
        }


        if (!ValidacaoFormCadastro.isValidName(instituitionName)) {
            campo_nome_pj.setError("Nome inválido");
        }
        // Verificar se o email é válido
        if (!ValidacaoFormCadastro.isValidEmail(email)) {
            campo_email_pj.setError("Email inválido");
            return;
        }

        if (!ValidacaoFormCadastro.isValidPhoneNumber(cellnumber)) {
            campo_numero_pj.setError("Número de telefone inválido");
            return;
        }

        // Verificar se a senha tem pelo menos 4 dígitos
        if (!ValidacaoFormCadastro.isValidPassword(password)) {
            senha_pj.setError("Senha deve ter pelo menos 4 dígitos");
            return;
        }

        // Verificar se as senhas coincidem
        if (!password.equals(confirmPassword)) {
            confirmar_senha_pj.setError("As senhas não coincidem");
            return;
        }

        InstituicaoParceira parceiro = new InstituicaoParceira(instituitionName, cnpj, email, cellnumber, password, this);

        Log.d("dados do parceiro", parceiro.obterDadosAsString());

        parceiro.criarParceiroNoServidor(parceiro, (VolleyCallback) this);
    }

    @Override
    public void onSuccess(JSONObject response) {
        // Ajustar depois para mostrar resposta do servidor
        Log.e("VolleySucess", "Resquisição recebida: " + response);
    }
    @Override
    public void onError(VolleyError error) {
        // Ajustar depois para mostrar resposta do servidor
        Log.e("VolleyError", "Erro na requisição: " + error.getMessage());
    }

    private void voltarTelaEscolhaCadastro() {
        Intent voltarTelaLogin = new Intent(FormCadastroPJ.this, TelaEscolhaCadastro.class);
        startActivity(voltarTelaLogin);
    }
}