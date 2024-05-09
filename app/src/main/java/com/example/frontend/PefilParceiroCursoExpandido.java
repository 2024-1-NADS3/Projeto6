package com.example.frontend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PefilParceiroCursoExpandido extends AppCompatActivity {


    AdapterInscricoesUsuario adapterUsuariosCadastrados;
    RecyclerView recyclerViewUsuariosCadastrados;
    Curso curso;
    RequestQueue filaRequest;
    GerenciadorToken token;
    List<Usuario> userInformationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pefil_parceiro_curso_expandido);

        token = new GerenciadorToken(this);
        Log.d("O token está aqui no perfil parceiro curso expandido?", token.getToken());

        curso = (Curso) getIntent().getSerializableExtra("curso");
        Log.d("id do curso", String.valueOf(curso.getCourseId()));

        filaRequest = Volley.newRequestQueue(this);

        fetchPartnerCoursesData();
    }

    public void deletarCurso(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PefilParceiroCursoExpandido.this);
        builder.setTitle("Deletar Curso")
                .setMessage("Você tem certeza que deseja deletar o curso?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("CAIU NO IF SIM", "DELETAR CONTA E MUDA DE ACTIVITY");
                        deletarCursoReq();
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

    public void deletarCursoReq() {
        String courseId = String.valueOf(curso.getCourseId());
        Log.d("id do curso", courseId);
        String deleteUrl = Constants.BASE_URL + "/parceiro/deletar-curso/" + courseId;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, deleteUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Trate a resposta JSON aqui
                        Log.d("Estou na resposta", "deu certo");

                        final Intent retornarTelaParceiro = new Intent(PefilParceiroCursoExpandido.this, PerfilPaceiro.class);
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(retornarTelaParceiro);
                                finish(); // Encerra a atividade atual
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Trate o erro aqui
                if (error.networkResponse.statusCode == 401) {
                    token.clearToken();


                    Toast.makeText(PefilParceiroCursoExpandido.this, "Sessão expirada, faça o login novamente!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PefilParceiroCursoExpandido.this, FormLogin.class);
                    startActivity(intent);
                }
                Toast.makeText(PefilParceiroCursoExpandido.this, "Não foi possível deletar o curso, tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
                Log.d("message", ":", error);
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

    /** Ajustar abaixo */

    public void fetchPartnerCoursesData() {
        String finalURL = Constants.BASE_URL + "/parceiro/usuarios-cadastrados-no-curso/" + curso.getCourseId();
        //progressBarPerfilParceiro.setVisibility(View.VISIBLE); // Mostra o ProgressBar enquanto os dados são carregados.

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, finalURL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //progressBarPerfilParceiro.setVisibility(View.GONE);
                        if (response.length() == 0) {
                            //progressBarPerfilParceiro.setVisibility(View.GONE);
                            //errorPartnerTextView.setVisibility(View.VISIBLE); // Exibe a mensagem de erro.
                            //errorPartnerTextView.setText("Não há cursos cadastrados!");
                            return;
                        }
                        Log.d("Response", ": " + response);
                        processCoursesResponse(response); // Processa a resposta dos dados dos cursos.
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressBarPerfilParceiro.setVisibility(View.GONE);
                        //errorPartnerTextView.setVisibility(View.VISIBLE); // Exibe a mensagem de erro.
                        //errorPartnerTextView.setText("Não foi possível carregar os cursos");
                        Log.e("Volley", error.toString()); // Registra o erro.
                    }
                })  {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token.getToken()); // Substitua "Bearer " + token pelo seu token JWT
                return headers;
            }
        };

        filaRequest.add(request); // Adiciona a requisição à fila de requisições.
    }

    private void processCoursesResponse(JSONArray response) {
        if (response.length() > 0) {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject userJson = response.getJSONObject(i);
                    Usuario user = new Usuario(userJson.getString("name"), userJson.getString("email"), userJson.getString("cellnumber"));
                    userInformationList.add(user);
                } catch (JSONException e) {
                    Log.e("Volley", "Erro no JSON", e); // Registra um erro no JSON.
                }
            }
            setupRecyclerView(); // Configura o RecyclerView com os dados dos cursos.
        } else {
            Log.d("Data", "Sem Dados");
        }
    }


    private void setupRecyclerView() {
        recyclerViewUsuariosCadastrados = findViewById(R.id.recyclerViewUsuariosCadastrados);
        recyclerViewUsuariosCadastrados.setLayoutManager(new LinearLayoutManager(PefilParceiroCursoExpandido.this));
        adapterUsuariosCadastrados = new AdapterInscricoesUsuario(PefilParceiroCursoExpandido.this, userInformationList);
        recyclerViewUsuariosCadastrados.setAdapter(adapterUsuariosCadastrados);
    }

}




