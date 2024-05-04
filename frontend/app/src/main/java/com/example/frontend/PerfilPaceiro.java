package com.example.frontend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PerfilPaceiro extends AppCompatActivity {

    GerenciadorToken token;

    String urlBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_paceiro);
        actionBar();

        urlBase = "http://192.168.0.10:4550";

        token = new GerenciadorToken(this);
        Log.d("O token está aqui?", token.getToken());
    }


    /* Método que aciona o verficarSeEstáLogado toda vez que a activity estiver em primeiro plano */
    @Override
    protected void onResume() {
        super.onResume();
        verificarSeEstaLogado();
    }

    public void actionBar() {
        //Button botaoUsuario = findViewById(R.id.ic_usuario);
//        botaoUsuario.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent (MainActivity.this, TelaUsuario.class);
//                startActivity(intent);
//            }
//        });

        TextView botaoTitulo = findViewById(R.id.titulo_PerfilParceiro);
        botaoTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (PerfilPaceiro.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    /* Método para deslogar no perfil do parceiro */
    public void sair(View view) {
        token.clearToken();
        Intent mudarTelaParaMainAcitivity = new Intent(PerfilPaceiro.this, MainActivity.class);
        startActivity(mudarTelaParaMainAcitivity);
    }

    /* Método para verificar se tem um token valido */
    public void verificarSeEstaLogado() {
        if (token != null) {
            String tokenString = token.getToken();
            if (tokenString.equals("")) {
                // Se o token estiver vazio, redireciona para a tela de login
                Intent mudarTelaParaLogin = new Intent(PerfilPaceiro.this, FormLogin.class);
                startActivity(mudarTelaParaLogin);
            }
        }
    }

    public void deletarConta(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PerfilPaceiro.this);
        builder.setTitle("Deletar Conta")
                .setMessage("Você tem certeza que deseja deletar a conta?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("CAIU NO IF SIM", "DELETAR CONTA E MUDA DE ACTIVITY");
                        deletarContaReq();
                        // Aqui você pode adicionar a lógica para deletar a conta
                        // Por exemplo, chamar um método que faz uma requisição ao servidor
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("CAIU NO IF NÃO", "FECHAR DIALOG SEM FAZER NADA");
                        // Fechar o dialog sem fazer nada
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void deletarContaReq() {
        String deleteUrl = urlBase + "/parceiro/deletarParceiro";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, deleteUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Trate a resposta JSON aqui
                        System.out.println(response.toString());
                        Toast.makeText(PerfilPaceiro.this, "Conta deletada com sucesso.", Toast.LENGTH_SHORT).show();

                        // Redireciona para a tela inicial após 3 segundos
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent retornarTelaInicio = new Intent(PerfilPaceiro.this, TelaInicio.class);
                                startActivity(retornarTelaInicio);
                                finish(); // Encerra a atividade atual
                            }
                        }, 3000); // 3000 milissegundos = 3 segundos
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Trate o erro aqui
                System.out.println(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token.getToken()); // Substitua "Bearer " + token pelo seu token JWT
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }


}