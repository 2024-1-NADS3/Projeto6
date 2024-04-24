package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

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
        final String name = campo_nome.getText().toString();
        final String email = campo_email.getText().toString();
        final String cellnumber = campo_numero.getText().toString();
        final String password = campo_senha.getText().toString();
        final String confirmPassword = campo_confirmacao_senha.getText().toString();

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
    }

    @Override
    public void onError(VolleyError error) {
        // Ajustar depois para mostrar resposta do servidor
        Log.e("VolleyError", "Erro na requisição: " + error.getMessage());
    }


    // Método que retorna para o formulário de login
    private void voltarTelaEscolhaCadastro() {
        Intent voltarTelaLogin = new Intent(FormCadastro.this, TelaEscolhaCadastro.class);
        startActivity(voltarTelaLogin);
    }
}