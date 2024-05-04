package com.example.frontend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class FormLogin extends AppCompatActivity {

    private TextView esqueci_senha;
    private TextView text_tela_cadastro;
    private ImageView voltar_tela_inicio;

    String urlBase;
    private EditText campo_email, campo_senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        getSupportActionBar();

        MudarTelaCadatros();
        Voltartela();
        MudarTelaRecSenha();

        campo_email = findViewById(R.id.email);
        campo_senha = findViewById(R.id.senha);
        // URL do endpoint de login
        urlBase = "http://192.168.0.10:4550";

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

    public void Login(View view) {
        final String email = campo_email.getText().toString().trim();
        final String password = campo_senha.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            if (email.isEmpty()) campo_email.setError("Email é obrigatório");
            if (password.isEmpty()) campo_senha.setError("Senha é obrigatória");
            return;
        }

        if (!ValidacaoFormCadastro.isValidEmail(email)) {
            campo_email.setError("Email inválido");
            return;
        }

        if (!ValidacaoFormCadastro.isValidPassword(password)) {
            campo_senha.setError("Senha deve ter pelo menos 4 dígitos");
            return;
        }

        requestLogin(email, password);
    }

    public void requestLogin(String email, String password) {
        String urlFinalParaLogar = urlBase + "/login" ;

        // Criação do RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Parâmetros da requisição
        JSONObject params = new JSONObject();
        try {
            params.put("email", email);
            params.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // JsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlFinalParaLogar, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("LoginResponse", response.toString());
                        String token = null;
                        try {
                            token = response.getString("token");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        GerenciadorToken tokenPreferenceManager = new GerenciadorToken(FormLogin.this);
                        tokenPreferenceManager.saveToken(token);
                        RedirecionarUsuario(tokenPreferenceManager);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Trata o erro
                String errorMessage = "Desculpe, ocorreu um erro. Por favor, tente novamente mais tarde.";

                if (error.networkResponse != null && error.networkResponse.statusCode != 0) {
                    if (error.networkResponse.statusCode == 401) {
                        try {
                            String errorBody = new String(error.networkResponse.data, "UTF-8");
                            JSONObject errorJson = new JSONObject(errorBody);
                            errorMessage = errorJson.getString("error");
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (error instanceof NetworkError) {
                    errorMessage = "Sem conexão com a internet. Por favor, verifique sua conexão.";
                } else if (error instanceof ServerError) {
                    errorMessage = "O servidor está enfrentando problemas. Por favor, tente novamente mais tarde.";
                } else if (error instanceof TimeoutError) {
                    errorMessage = "A solicitação demorou muito para ser processada. Por favor, tente novamente mais tarde.";
                }
                Log.e("LoginError", error.toString());

                // Exibe o AlertDialog com a mensagem de erro
                new AlertDialog.Builder(FormLogin.this)
                        .setTitle("Erro")
                        .setMessage(errorMessage)
                        .setPositiveButton("OK", null)
                        .show();
            }
        });

        // Adiciona a requisição à fila
        queue.add(jsonObjectRequest);
    }

    public void RedirecionarUsuario(GerenciadorToken GToken) {
        String urlInfo = urlBase + "/user-type";
        String token = GToken.getToken(); // Obtenha o token JWT armazenado

        JsonObjectRequest jsonObjectRequestInfo = new JsonObjectRequest(Request.Method.GET, urlInfo, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Aqui você recebe o tipo de usuário na resposta
                        String userType = response.optString("userType");

                        if (userType.equals("partner")) {
                            Intent mudarTelaPerfilParceiro = new Intent(FormLogin.this,PerfilPaceiro.class);
                            startActivity(mudarTelaPerfilParceiro);
                        } else {
                            Intent mudarTelaPerfilUsuario = new Intent(FormLogin.this,PerfilUsuario.class);
                            startActivity(mudarTelaPerfilUsuario);
                        }
                        // Redirecionar para a activity correta com base no tipo de usuário
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Tratar erro
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue requestQueueInfo = Volley.newRequestQueue(getApplicationContext());
        requestQueueInfo.add(jsonObjectRequestInfo);
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