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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
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

/**
 * Método chamado quando a activity é criada.
 *
 * Este método é chamado quando a activity é criada pela primeira vez. Ele configura os elementos
 * de interface do usuário, como o RecyclerView e a barra de progresso. Além disso, obtém o token
 * JWT do usuário, recupera os dados do curso expandido do parceiro e exibe-os na interface do usuário.
 *
 * @param savedInstanceState Um objeto Bundle contendo o estado anterior da activity,
 *                           que é usado para reconstruir a activity após uma mudança
 *                           de configuração, como uma rotação de tela.
 */
public class PefilParceiroCursoExpandido extends AppCompatActivity {

    AdapterInscricoesUsuario adapterUsuariosCadastrados;
    RecyclerView recyclerViewUsuariosCadastrados;
    Curso curso;
    RequestQueue filaRequest;
    GerenciadorToken token;
    ProgressBar progressBarPerfilParceiroExpandido;
    TextView errorPartnerExpandidoTextView;
    List<Usuario> userInformationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pefil_parceiro_curso_expandido);

        TextView botaoTitulo = findViewById(R.id.titulo_TelaParceiroCursoExpandido);
        botaoTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (PefilParceiroCursoExpandido.this, MainActivity.class);
                startActivity(intent);
            }
        });

        errorPartnerExpandidoTextView = findViewById(R.id.errorPartnerExpandidoTextView);
        progressBarPerfilParceiroExpandido = findViewById(R.id.progressBarPerfilParceiroExpandido);


        token = new GerenciadorToken(this);
        Log.d("O token está aqui no perfil parceiro curso expandido?", token.getToken());

        curso = (Curso) getIntent().getSerializableExtra("curso");
        Log.d("id do curso", String.valueOf(curso.getCourseId()));
        Log.d("tipo do curso", String.valueOf(curso.getType()));
        Log.d("endereco do curso", String.valueOf(curso.getAddress()));

        filaRequest = Volley.newRequestQueue(this);

        fetchPartnerCoursesData();
    }

    /**
     * Método para confirmar a exclusão de um curso.
     *
     * Este método exibe um diálogo de confirmação para garantir que o usuário
     * realmente deseja excluir o curso. Se o usuário confirmar a exclusão,
     * o método chama `deletarCursoReq()` para fazer a solicitação de exclusão do curso.
     *
     * @param view A view que foi clicada para acionar o método.
     */
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

    /**
     * Método para solicitar a exclusão de um curso.
     *
     * Este método faz uma solicitação DELETE para excluir o curso específico do parceiro.
     * Ele trata as respostas e os erros da solicitação e executa ações apropriadas, como
     * redirecionar para a tela do perfil do parceiro após a exclusão bem-sucedida do curso
     * ou mostrar uma mensagem de erro em caso de falha na solicitação.
     */
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

            // ESSE AQUI É O ERRO QUE DEU BOM PARA PEGAR 401
            public void onErrorResponse(VolleyError error) {
                // Trate o erro aqui
                String errorMessage = "error";

                if (error.networkResponse!= null && error.networkResponse.statusCode == 401) {
                    token.clearToken();
                    errorMessage = "Sessão expirou. Por favor, faça login novamente.";
                    new AlertDialog.Builder(PefilParceiroCursoExpandido.this)
                            .setTitle("Erro")
                            .setMessage(errorMessage)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(PefilParceiroCursoExpandido.this, FormLogin.class);
                                    startActivity(intent);
                                }
                            })
                            .show();
                } else {
                    if (error instanceof NetworkError) {
                        errorMessage = "Sem conexão com a internet. Por favor, verifique sua conexão.";
                    } else if (error instanceof ServerError) {
                        errorMessage = "O servidor está enfrentando problemas. Por favor, tente novamente mais tarde.";
                    } else if (error instanceof ParseError) {
                        errorMessage = "Houve um problema ao processar a resposta do servidor.";
                    } else if (error instanceof TimeoutError) {
                        errorMessage = "A solicitação demorou muito para ser processada. Por favor, tente novamente mais tarde.";
                    }
                    new AlertDialog.Builder(PefilParceiroCursoExpandido.this)
                            .setTitle("Erro")
                            .setMessage(errorMessage)
                            .setPositiveButton("OK", null)
                            .show();
                }
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

    /**
     * Método para buscar os dados dos usuários cadastrados em um curso específico do parceiro.
     *
     * Este método faz uma solicitação GET para recuperar os dados dos usuários cadastrados em um
     * curso específico do parceiro. Ele trata as respostas e os erros da solicitação e executa
     * ações apropriadas, como exibir os dados dos usuários ou mostrar mensagens de erro caso ocorra
     * algum problema durante a solicitação.
     */

    public void fetchPartnerCoursesData() {
        String finalURL = Constants.BASE_URL + "/parceiro/usuarios-cadastrados-no-curso/" + curso.getCourseId();
        progressBarPerfilParceiroExpandido.setVisibility(View.VISIBLE); // Mostra o ProgressBar enquanto os dados são carregados.

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, finalURL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() == 0) {
                            progressBarPerfilParceiroExpandido.setVisibility(View.GONE);
                            errorPartnerExpandidoTextView.setVisibility(View.VISIBLE); // Exibe a mensagem de erro.
                            errorPartnerExpandidoTextView.setText("Não há usuários cadastrados!");
                            return;
                        }
                        progressBarPerfilParceiroExpandido.setVisibility(View.GONE);
                        processCoursesResponse(response); // Processa a resposta dos dados dos cursos.
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse!= null && error.networkResponse.statusCode == 401) {
                            token.clearToken();
                            new AlertDialog.Builder(PefilParceiroCursoExpandido.this)
                                    .setTitle("Erro")
                                    .setMessage("Sessão expirou. Por favor, faça login novamente.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(PefilParceiroCursoExpandido.this, FormLogin.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .show();
                        } else {

                            progressBarPerfilParceiroExpandido.setVisibility(View.GONE);
                            errorPartnerExpandidoTextView.setVisibility(View.VISIBLE); // Exibe a mensagem de erro.
                            errorPartnerExpandidoTextView.setText("Não foi possível carregar os usuários.");
                            Log.e("Volley", error.toString()); // Registra o erro.
                        }
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

    /**
     * Método para processar a resposta contendo os dados dos usuários cadastrados em um curso.
     *
     * Este método analisa a resposta JSON recebida do servidor, extrai os dados dos usuários
     * cadastrados no curso e os adiciona à lista de informações do usuário. Em seguida, chama
     * o método `setupRecyclerView()` para configurar o RecyclerView com os dados dos usuários.
     *
     * @param response A resposta JSON contendo os dados dos usuários cadastrados no curso.
     */
    private void processCoursesResponse(JSONArray response) {
        if (response.length() > 0) {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject userJson = response.getJSONObject(i);
                    Usuario user = new Usuario(userJson.getInt("userId") ,userJson.getString("name"), userJson.getString("email"), userJson.getString("cellnumber"));
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

    /**
     * Método para configurar o RecyclerView com os dados dos usuários cadastrados no curso.
     *
     * Este método configura o RecyclerView para exibir os dados dos usuários cadastrados
     * no curso expandido do parceiro. Ele define um LinearLayoutManager e define o
     * AdapterInscricoesUsuario personalizado para exibir os dados dos usuários.
     */
    private void setupRecyclerView() {
        recyclerViewUsuariosCadastrados = findViewById(R.id.recyclerViewUsuariosCadastrados);
        recyclerViewUsuariosCadastrados.setLayoutManager(new LinearLayoutManager(PefilParceiroCursoExpandido.this));
        adapterUsuariosCadastrados = new AdapterInscricoesUsuario(PefilParceiroCursoExpandido.this, userInformationList, curso.getCourseId(), token);
        recyclerViewUsuariosCadastrados.setAdapter(adapterUsuariosCadastrados);
    }

    /**
     * Método para editar um curso.
     *
     * Este método é chamado quando o usuário deseja editar um curso específico.
     * Dependendo do tipo de curso (Online ou Presencial), ele inicia a atividade
     * correspondente para editar o curso, passando os dados necessários através
     * dos extras.
     *
     * @param view A view que foi clicada para acionar o método.
     */
    public void EditarCurso(View view)
    {
        String endereco = curso.getAddress();
        String zona = curso.getZone();
        String tipo = curso.getType();
        int courseId = curso.getCourseId();

        if(tipo.equals("Online")){
            Intent EditarCursoOnline = new Intent(PefilParceiroCursoExpandido.this, EditarCursoOnline.class);
            EditarCursoOnline.putExtra("courseId", courseId);
            startActivity(EditarCursoOnline);
        }
        else{
            Intent EditarCursoPresencial = new Intent(PefilParceiroCursoExpandido.this, EditarCursoPresencial.class);
            EditarCursoPresencial.putExtra("courseId", courseId);
            EditarCursoPresencial.putExtra("endereco",endereco);
            EditarCursoPresencial.putExtra("zona",zona);
            startActivity(EditarCursoPresencial);
        }
    }
}




