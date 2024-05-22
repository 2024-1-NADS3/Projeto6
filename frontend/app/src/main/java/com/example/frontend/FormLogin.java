package com.example.frontend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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

        //Separando a string e tornando uma parte dela clicável
        String textoCompleto = "Ainda não está na Educaliza? Crie sua conta";

        SpannableString spannableString = new SpannableString(textoCompleto);

        int startIndex = textoCompleto.indexOf("Crie sua conta");
        int endIndex = startIndex + "Crie sua conta".length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent MudarTelaCadatros= new Intent(FormLogin.this,TelaInicio.class);
                startActivity(MudarTelaCadatros);
            }
        };
        spannableString.setSpan(clickableSpan, startIndex, endIndex, 0);

        // Defina a SpannableString no TextView
        text_tela_cadastro.setText(spannableString);
        // Permita que o link seja clicável
        text_tela_cadastro.setMovementMethod(LinkMovementMethod.getInstance());
        text_tela_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mudarTelaCadastro = new Intent(FormLogin.this,TelaInicio.class);
                startActivity(mudarTelaCadastro);
            }
        });
        esqueci_senha.setPaintFlags(esqueci_senha.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        esqueci_senha.setOnClickListener(v -> {

            Intent mudarTelaRecSenha= new Intent(FormLogin.this,TelaRecuperarSenha.class);
            startActivity(mudarTelaRecSenha);
        });
        voltar_tela_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent voltarTelaInicio = new Intent(FormLogin.this,TelaInicio.class);
                startActivity(voltarTelaInicio);
            }
        });
    }

    /**
     * Método para realizar o login do usuário.
     * Valida os campos de email e senha antes de enviar a requisição.
     */
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
            campo_senha.setError("Senha deve ter pelo menos 8 dígitos");
            return;
        }

        requestLogin(email, password);
    }

    /**
     * Método para enviar a requisição de login ao servidor.
     */
    public void requestLogin(String email, String password) {
        String urlFinalParaLogar = Constants.BASE_URL + "/login" ;

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

    /**
     * Método para redirecionar o usuário após o login bem-sucedido.
     * Determina o tipo de usuário e redireciona para a tela correspondente.
     */
    public void RedirecionarUsuario(GerenciadorToken GToken) {
        String urlInfo = Constants.BASE_URL + "/user-type";
        String token = GToken.getToken(); // Obtenha o token JWT armazenado

        JsonObjectRequest jsonObjectRequestInfo = new JsonObjectRequest(Request.Method.GET, urlInfo, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Aqui você recebe o tipo de usuário na resposta
                        String userType = response.optString("userType");

                        if (userType.equals("partner")) {
                            pegarNomeDoUsuarioOuParceiro("partner", token);

                            Intent mudarTelaPerfilParceiro = new Intent(FormLogin.this,PerfilPaceiro.class);
                            startActivity(mudarTelaPerfilParceiro);

                        } else {
                            pegarNomeDoUsuarioOuParceiro("user", token);
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

    public void pegarNomeDoUsuarioOuParceiro(String userType, String token) {
        String urlInfo;

        if (userType.equals("partner")) {
            urlInfo = Constants.BASE_URL + "/parceiro/pegar-nome-instituicao";
        } else {
            urlInfo = Constants.BASE_URL + "/usuario/pegar-nome-do-usuario";
        }


        JsonObjectRequest jsonObjectRequestInfo = new JsonObjectRequest(Request.Method.GET, urlInfo, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Aqui você recebe o tipo de usuário na resposta
                        if (userType.equals("partner")) {
                            String instituitionName = response.optString("instituitionName");
                            Log.d("nome instituicao", instituitionName);
                            Constants.userName = instituitionName;
                        } else {
                            Constants.userName = response.optString("name");
                            Log.d("nome usuário", response.optString("name"));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("erro na requisição do nome", "onErrorResponse: " + error);
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

    /**
     * Método para inicializar o TextView que leva ao cadastro.
     */
    private void MudarTelaCadatros() {
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);
    }

    /**
     * Método para inicializar o TextView que leva à recuperação de senha.
     */
    private void MudarTelaRecSenha() {
        esqueci_senha = findViewById(R.id.esqueci_senha);
    }

    /**
     * Método para inicializar o ImageView que leva ao início.
     */
    private void Voltartela() {
        voltar_tela_inicio = findViewById(R.id.voltar_tela_inicio);
    }
}