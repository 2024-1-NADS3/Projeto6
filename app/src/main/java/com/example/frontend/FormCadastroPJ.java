package com.example.frontend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class FormCadastroPJ extends AppCompatActivity implements VolleyCallback {

    private ImageView voltarLogin;

    private EditText campo_nome_pj, campo_cnpj, campo_email_pj, campo_numero_pj, senha_pj, confirmar_senha_pj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro_pj);
        getSupportActionBar();

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

    /**
     * Método para cadastrar um parceiro, validando os campos e enviando os dados para o servidor.
     */
    public void cadastrarParceiro(View view) {
        final String cnpj = campo_cnpj.getText().toString().trim();
        final String instituitionName = campo_nome_pj.getText().toString().trim();
        final String email = campo_email_pj.getText().toString().trim();
        final String cellnumber = campo_numero_pj.getText().toString().trim();
        final String password = senha_pj.getText().toString().trim();
        final String confirmPassword = confirmar_senha_pj.getText().toString().trim();

        // Verificar se os campos estão vazios
        if (instituitionName.isEmpty() || cnpj.isEmpty() || email.isEmpty() || cellnumber.isEmpty() || password.isEmpty()) {
            if (instituitionName.isEmpty()) campo_nome_pj.setError("Nome é obrigatório");
            if (email.isEmpty()) campo_email_pj.setError("Email é obrigatório");
            if (cellnumber.isEmpty()) campo_numero_pj.setError("Número é obrigatório");
            if (password.isEmpty()) senha_pj.setError("Senha é obrigatória");
            if (cnpj.isEmpty()) campo_cnpj.setError("CNPJ é obrigatório");
            return;
        }

//        if (!ValidacaoFormCadastro.isValidName(instituitionName)) {
//            campo_nome_pj.setError("Nome inválido");
//        }

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

        InstituicaoParceira parceiro = new InstituicaoParceira(instituitionName.trim(), cnpj, email, cellnumber, password, this);

        Log.d("dados do parceiro", parceiro.obterDadosAsString());

        parceiro.criarParceiroNoServidor(parceiro, (VolleyCallback) this);
    }

    /**
     * Método chamado quando a requisição de cadastro é bem-sucedida.
     * Exibe um AlertDialog informando o sucesso e redireciona o usuário para a tela de login.
     */
    @Override
    public void onSuccess(JSONObject response) {
        // Criação do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Registro bem-sucedido");
        builder.setMessage("Clique no botão abaixo para fazer o login.");
        builder.setPositiveButton("Ir para Login", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(getApplicationContext(), FormLogin.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Ação ao clicar no botão "Cancelar"
                dialog.cancel();
            }
        });

        // Exibição do AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Método chamado quando ocorre um erro na requisição de cadastro.
     * Trata diferentes tipos de erros de rede e de servidor, exibindo um AlertDialog com a mensagem de erro.
     */
    @Override
    public void onError(VolleyError error) {
        // Ajustar depois para mostrar resposta do servidor
        String errorMessage = "Desculpe, ocorreu um erro. Por favor, tente novamente mais tarde.";

        // Verifica se há uma resposta de rede e um código de status
        if (error.networkResponse!= null && error.networkResponse.statusCode!= 0) {
            if (error.networkResponse.statusCode == 409) {
                errorMessage = "Email já cadastrado. Por favor, use um email diferente.";
            }
        }
        else if (error instanceof NetworkError) {
            errorMessage = "Sem conexão com a internet. Por favor, verifique sua conexão.";
        } else if (error instanceof ServerError) {
            errorMessage = "O servidor está enfrentando problemas. Por favor, tente novamente mais tarde.";
        } else if (error instanceof ParseError) {
            errorMessage = "Houve um problema ao processar a resposta do servidor.";
        } else if (error instanceof TimeoutError) {
            errorMessage = "A solicitação demorou muito para ser processada. Por favor, tente novamente mais tarde.";
        }

        // Exibe a mensagem de erro em um AlertDialog ou Toast
        new AlertDialog.Builder(this)
                .setTitle("Erro")
                .setMessage(errorMessage)
                .setPositiveButton("OK", null)
                .show();
    }

    private void voltarTelaEscolhaCadastro() {
        Intent voltarTelaLogin = new Intent(FormCadastroPJ.this, TelaInicio.class);
        startActivity(voltarTelaLogin);
    }

    public void IrLogin(View view)
    {
        Intent irTelaLogin = new Intent(FormCadastroPJ.this, FormLogin.class);
        startActivity(irTelaLogin);
    }
}