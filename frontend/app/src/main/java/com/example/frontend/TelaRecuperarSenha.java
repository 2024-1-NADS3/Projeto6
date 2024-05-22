package com.example.frontend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class TelaRecuperarSenha extends AppCompatActivity {
    private ImageView voltar_tela_login;

    private EditText campo_email_rec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_recuperar_senha);
        VoltarTela();

        campo_email_rec = findViewById(R.id.campo_email_rec);

        voltar_tela_login.setOnClickListener(v -> {
            Intent voltarTelaInicio = new Intent(TelaRecuperarSenha.this, FormLogin.class);
            startActivity(voltarTelaInicio);
        });
    }

    private void VoltarTela() {
        voltar_tela_login = findViewById(R.id.voltar_tela_login);
    }

    public void EnviarEmailBackEnd(View view) {
        RequestQueue requestQueue;
        String url = Constants.BASE_URL + "/pegar-codigo";
        EditText emailEditText;
        emailEditText = findViewById(R.id.campo_email_rec);
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Insira um e-mail!!", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Insira um e-mail válido!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Email enviado!", Toast.LENGTH_SHORT).show();

            requestQueue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("email", email);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Tratar resposta do servidor
                            int codigo = response.optInt("code");
                            String email = response.optString("email");
                            Log.d("codigo", String.valueOf(codigo));
                            Log.d("email", email);

                            Intent mudarTela = new Intent(getApplicationContext(), TelaRedefinirSenha.class);
                            mudarTela.putExtra("Email", email);
                            mudarTela.putExtra("Code", codigo);
                            startActivity(mudarTela);
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String errorMessage = "Desculpe, ocorreu um erro. Por favor, tente novamente mais tarde.";
                            // Verifica se há uma resposta de rede e um código de status
                            if (error.networkResponse != null && error.networkResponse.statusCode != 0) {
                                if (error.networkResponse.statusCode == 400) {
                                    errorMessage = "Este e-mail não consta no banco de dados.";
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

                            // Exibe a mensagem de erro em um AlertDialog ou Toast
                            new AlertDialog.Builder(TelaRecuperarSenha.this)
                                    .setTitle("Erro")
                                    .setMessage(errorMessage)
                                    .setPositiveButton("OK", null)
                                    .show();

                        }
                    }
            );
            requestQueue.add(jsonObjectRequest);
        }
    }
}