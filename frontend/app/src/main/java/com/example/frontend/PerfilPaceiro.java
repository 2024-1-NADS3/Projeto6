package com.example.frontend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerfilPaceiro extends AppCompatActivity {

    GerenciadorToken token;
    CursoAdapterPerfil adapterPartner;
    RecyclerView recyclerViewPartner;
    RequestQueue filaRequest;
    ProgressBar progressBarPerfilParceiro;

    TextView errorPartnerTextView;

    List<Curso> registeredCourses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_paceiro);
        actionBar();


        token = new GerenciadorToken(this);
        Log.d("O token está aqui?", token.getToken());


        recyclerViewPartner = findViewById(R.id.recyclerViewPartner);

        filaRequest = Volley.newRequestQueue(this);

        errorPartnerTextView = findViewById(R.id.errorPartnerTextView);
        progressBarPerfilParceiro = findViewById(R.id.progressBarPerfilParceiro);

        fetchPartnerCoursesData();




        //Configura o SearchView para permitir a busca por título de curso.
        SearchView searchBarPerfilParceiro = findViewById(R.id.searchBarPerfilParceiro);
        searchBarPerfilParceiro.setIconifiedByDefault(false);
        searchBarPerfilParceiro.clearFocus();
        searchBarPerfilParceiro.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterPartner.getSearchBarFilter().filter(newText); // Filtra os cursos com base no texto de busca.
                return false;
            }
        });

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
        String deleteUrl = Constants.BASE_URL + "/parceiro/deletar-parceiro";

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

    public void fetchPartnerCoursesData() {
        String finalURL = Constants.BASE_URL + "/parceiro/cursos-cadastrados";
        progressBarPerfilParceiro.setVisibility(View.VISIBLE); // Mostra o ProgressBar enquanto os dados são carregados.

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, finalURL, null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    progressBarPerfilParceiro.setVisibility(View.GONE);
                    processCoursesResponse(response); // Processa a resposta dos dados dos cursos.
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBarPerfilParceiro.setVisibility(View.GONE);
                    errorPartnerTextView.setVisibility(View.VISIBLE); // Exibe a mensagem de erro.
                    errorPartnerTextView.setText("Não foi possível carregar os cursos");
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
                    JSONObject cursoJson = response.getJSONObject(i);
                    Curso curso = new Curso();
                    curso.setCourseId(cursoJson.getInt("courseId"));
                    curso.setTitle(cursoJson.getString("title"));
                    curso.setType(cursoJson.getString("type"));
                    curso.setCategory(cursoJson.getString("category"));
                    curso.setImg(cursoJson.getString("img"));
                    curso.setDescription(cursoJson.getString("description"));
                    curso.setAddress(cursoJson.getString("address"));
                    curso.setZone(cursoJson.getString("zone"));
                    curso.setOccupiedSlots(cursoJson.getInt("occupiedSlots"));
                    curso.setMaxCapacity(cursoJson.getInt("maxCapacity"));
                    curso.setInitialDate(cursoJson.getString("initialDate"));
                    curso.setEndDate(cursoJson.getString("endDate"));
                    registeredCourses.add(curso);
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
        recyclerViewPartner.setLayoutManager(new LinearLayoutManager(PerfilPaceiro.this));
        adapterPartner = new CursoAdapterPerfil(PerfilPaceiro.this, registeredCourses);
        recyclerViewPartner.setAdapter(adapterPartner);
    }

}