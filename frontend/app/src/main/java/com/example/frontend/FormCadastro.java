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
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class FormCadastro extends AppCompatActivity implements VolleyCallback {

    private ImageView voltarLogin;

    private EditText campo_nome, campo_email, campo_numero, campo_senha, campo_confirmacao_senha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        getSupportActionBar().hide();

        voltarLogin = findViewById(R.id.voltar_login);

        voltarLogin.setOnClickListener(v -> voltarTelaEscolhaCadastro());

        campo_nome = findViewById(R.id.campo_nome);
        campo_email = findViewById(R.id.campo_email);
        campo_numero = findViewById(R.id.campo_numero);
        campo_senha = findViewById(R.id.campo_senha);
        campo_confirmacao_senha = findViewById(R.id.campo_confirmacao_senha);
    }

    public void cadastrarUsuario(View view){
        final String name = campo_nome.getText().toString().trim();
        final String email = campo_email.getText().toString().trim();
        final String cellnumber = campo_numero.getText().toString().trim();
        final String password = campo_senha.getText().toString().trim();
        final String confirmPassword = campo_confirmacao_senha.getText().toString().trim();

        // Verificar se os campos estão vazios
        if (name.isEmpty() || email.isEmpty() || cellnumber.isEmpty() || password.isEmpty()) {
            if (name.isEmpty()) campo_nome.setError("Nome é obrigatório");
            if (email.isEmpty()) campo_email.setError("Email é obrigatório");
            if (cellnumber.isEmpty()) campo_numero.setError("Número é obrigatório");
            if (password.isEmpty()) campo_senha.setError("Senha é obrigatória");
            return;
        }


        if (!ValidacaoFormCadastro.isValidName(name)) {
            campo_nome.setError("Nome inválido");
        }
        // Verificar se o email é válido
        if (!ValidacaoFormCadastro.isValidEmail(email)) {
            campo_email.setError("Email inválido");
            return;
        }

        if (!ValidacaoFormCadastro.isValidPhoneNumber(cellnumber)) {
            campo_numero.setError("Número de telefone inválido");
            return;
        }

        // Verificar se a senha tem pelo menos 4 dígitos
        if (!ValidacaoFormCadastro.isValidPassword(password)) {
            campo_senha.setError("Senha deve ter pelo menos 4 dígitos");
            return;
        }

        // Verificar se as senhas coincidem
        if (!password.equals(confirmPassword)) {
            campo_confirmacao_senha.setError("As senhas não coincidem");
            return;
        }

        Usuario usuario = new Usuario(name, email, cellnumber, password, this);

        Log.d("dados do usuario", usuario.obterDadosAsString());

        usuario.criarUsuarioNoServidor(usuario, this);
    }

    @Override
    public void onSuccess(JSONObject response) {
        // Ajustar depois para mostrar resposta do servidor

        Log.e("VolleySucess", "Resquisição recebida: " + response);

        // Criação do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Registro bem-sucedido");
        builder.setMessage("Clique no botão abaixo para fazer o login.");
        builder.setPositiveButton("Ir para Login", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Ação ao clicar no botão "Ir para Login"
                // Aqui você pode iniciar a atividade de login
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

    @Override
    public void onError(VolleyError error) {
        // Ajustar depois para mostrar resposta do servidor
        String errorMessage = "Desculpe, ocorreu um erro. Por favor, tente novamente mais tarde.";


        if (error.networkResponse != null && error.networkResponse.statusCode != 0) {
            if (error.networkResponse.statusCode == 409) {
                errorMessage = "Email já cadastrado. Por favor, use um email diferente.";
            }
        } else if (error instanceof NetworkError) {
            errorMessage = "Sem conexão com a internet. Por favor, verifique sua conexão.";
        } else if (error instanceof ServerError) {
            errorMessage = "O servidor está enfrentando problemas. Por favor, tente novamente mais tarde.";
        } else if (error instanceof ParseError) {
            errorMessage = "Houve um problema ao processar a resposta do servidor.";
        } else if (error instanceof TimeoutError) {
            errorMessage = "A solicitação demorou muito para ser processada. Por favor, tente novamente mais tarde.";
        }

        // Exibir a mensagem de erro em um AlertDialog ou Toast
        new AlertDialog.Builder(this)
                .setTitle("Erro")
                .setMessage(errorMessage)
                .setPositiveButton("OK", null)
                .show();
    }

    // Método que retorna para o formulário de login
    private void voltarTelaEscolhaCadastro() {
        Intent voltarTelaLogin = new Intent(FormCadastro.this, TelaEscolhaCadastro.class);
        startActivity(voltarTelaLogin);
    }
}