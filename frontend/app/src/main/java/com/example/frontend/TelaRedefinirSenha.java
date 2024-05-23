package com.example.frontend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.health.connect.datatypes.units.Length;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

/**
 * Classe para a tela de redefinição de senha.
 */
public class TelaRedefinirSenha extends AppCompatActivity {

    TextView email_usuario_recuperar;
    private EditText campo_codigoDigitadoUsuario, campo_novaSenha, campo_confirmarSenha;

    String email;
    int codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_redefinir_senha);

        Intent intent = getIntent();
        email = intent.getStringExtra("Email");
        codigo = intent.getIntExtra("Code", 0);

        campo_codigoDigitadoUsuario = findViewById(R.id.inserir_codigo);
        campo_novaSenha = findViewById(R.id.nova_senha);
        campo_confirmarSenha = findViewById(R.id.confirmar_nova_senha);

        email_usuario_recuperar = findViewById(R.id.email_usuario_recuperar);

        email_usuario_recuperar.setText(email);


        Log.d("Código chegou", String.valueOf(codigo));

    }

    /**
     * Método para enviar os dados de redefinição de senha para o backend.
     * @param view A view associada ao botão de enviar dados.
     */
    public void enviarDados(View view) {
        final String codigoDigitado = campo_codigoDigitadoUsuario.getText().toString().trim();
        final String novaSenha = campo_novaSenha.getText().toString().trim();
        final String confirmarSenha = campo_confirmarSenha.getText().toString().trim();


        if (codigoDigitado.isEmpty() || novaSenha.isEmpty() || confirmarSenha.isEmpty()) {
            if (codigoDigitado.isEmpty()) campo_codigoDigitadoUsuario.setError("O código é obrigatório");
            if (novaSenha.isEmpty()) campo_novaSenha.setError("A nova senha é obrigatório");
            if (confirmarSenha.isEmpty()) campo_confirmarSenha.setError("Confirmar senha é obrigatório");
            return;
        }

        String codigoDoBackEmString = String.valueOf(codigo);

        if(!codigoDigitado.equals(codigoDoBackEmString)) {
            campo_codigoDigitadoUsuario.setError("Código inválido.");
            return;
        }

        if (!novaSenha.equals(confirmarSenha)) {
            campo_novaSenha.setError("As senhas não coincidem");
            campo_confirmarSenha.setError("As senhas não coincidem");
            return;
        }

        // Verificar se a senha tem pelo menos 4 dígitos
        if (!ValidacaoFormCadastro.isValidPassword(novaSenha)) {
            campo_novaSenha.setError("Senha deve ter pelo menos 4 dígitos");
            return;
        }


        Log.d("Nova senha", novaSenha);
        recuperarSenhaReq(novaSenha);
    }

    /**
     * Método para enviar uma solicitação ao backend para atualizar a senha.
     * @param senha A nova senha a ser atualizada.
     */
    public void recuperarSenhaReq(String senha){

        RequestQueue requestQueue;

        String url = Constants.BASE_URL + "/atualizar-senha";

        requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("newPassword", senha);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TelaRedefinirSenha.this);
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

                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Desculpe, ocorreu um erro. Por favor, tente novamente mais tarde.";

                        // Verifica se há uma resposta de rede e um código de status
                        if (error.networkResponse!= null && error.networkResponse.statusCode!= 0) {
                            if (error.networkResponse.statusCode == 500) {
                                errorMessage = "Algo deu errado, tente novamente mais tarde.";
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
                        new AlertDialog.Builder(TelaRedefinirSenha.this)
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

