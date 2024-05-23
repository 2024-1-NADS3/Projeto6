package com.example.frontend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.appcompat.widget.SearchView;

import android.widget.RelativeLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Método chamado quando a activity é criada.
 *
 * Este método é chamado quando a activity é criada pela primeira vez. Ele configura os elementos
 * de interface do usuário, como o RecyclerView e a barra de progresso. Além disso, obtém o token
 * JWT do usuário, recupera os cursos registrados pelo parceiro e exibe-os na interface do usuário.
 *
 * @param savedInstanceState Um objeto Bundle contendo o estado anterior da activity,
 *                           que é usado para reconstruir a activity após uma mudança
 *                           de configuração, como uma rotação de tela.
 */
public class PerfilPaceiro extends AppCompatActivity {

    GerenciadorToken token;
    CursoAdapterPerfil adapterPartner;
    RecyclerView recyclerViewPartner;
    RequestQueue filaRequest;
    ProgressBar progressBarPerfilParceiro;
    TextView errorPartnerTextView;
    List<Curso> registeredCourses = new ArrayList<>();

    private boolean drawerAberto = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_paceiro);
        token = new GerenciadorToken(this);
        Log.d("O token está aqui?", token.getToken());
        actionBar();


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


    /** Método que aciona o verficarSeEstáLogado toda vez que a activity estiver em primeiro plano */
    @Override
    protected void onResume() {
        super.onResume();
        verificarSeEstaLogado();
    }

    /**
     * Método para configurar a action bar.
     *
     * Este método configura a action bar para exibir um botão de usuário e um título.
     * Quando o botão de usuário é clicado, ele chama o método `abrirDrawer()` para
     * abrir o drawer do perfil do parceiro. Quando o título é clicado, ele retorna à
     * tela principal.
     */
    public void actionBar() {
     Button botaoUsuario = findViewById(R.id.ic_usuarioParceiro);
        botaoUsuario.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             abrirDrawer();
          }
      });

        TextView botaoTitulo = findViewById(R.id.titulo_PerfilParceiro);
        botaoTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (PerfilPaceiro.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    /** Método para deslogar no perfil do parceiro */
    public void sair(View view) {
        token.clearToken();
        Intent mudarTelaParaMainAcitivity = new Intent(PerfilPaceiro.this, MainActivity.class);
        startActivity(mudarTelaParaMainAcitivity);
    }

    /** Método para verificar se tem um token valido */
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

    /**
     * Método para abrir ou fechar o Navigation Drawer do perfil do parceiro.
     *
     * Este método verifica se o Navigation Drawer está aberto. Se estiver aberto, ele
     * remove o fragmento do Drawer. Caso contrário, adiciona o fragmento ao Drawer.
     * Ele também ajusta a elevação dos botões para que fiquem atrás do Drawer quando
     * estiver aberto.
     */
    public void abrirDrawer() {

        //Obtendo o FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        //Verifica se o Navigation Drawer está aberto
        if (drawerAberto) {
            //Se estiver aberto, remover o fragment
            Fragment fragment = fragmentManager.findFragmentById(R.id.containerLayout_PerfilParceiro);
            if (fragment instanceof NavigationDrawer){
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                transaction.remove(fragment).commit();
                drawerAberto = false;

                // Ajusta a elevação dos botões para que fiquem atrás do fragmento
                Button button = findViewById(R.id.add_curso_presencial);
                button.setEnabled(true);
                button.setElevation(0); // Define a elevação para o valor padrão (zero)
                Button buttonOnline = findViewById(R.id.add_curso_online);
                buttonOnline.setEnabled(true);
                buttonOnline.setElevation(0); // Define a elevação para o valor padrão (zero)
            }
        } else {
            //Se não estiver aberto, adiciona o fragment
            NavigationDrawer drawerFragment = new NavigationDrawer();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
            transaction.replace(R.id.containerLayout_PerfilParceiro, drawerFragment);
            transaction.addToBackStack(null); // Adiciona o fragment à pilha de BackStack
            transaction.commit();
            drawerAberto = true; //Atualiza a flag

            // Ajusta a elevação dos botões para que fiquem atrás do fragmento
            Button button = findViewById(R.id.add_curso_presencial);
            button.setEnabled(false);
            button.setElevation(-1); // Define uma elevação menor que zero para ficar atrás do fragmento
            Button buttonOnline = findViewById(R.id.add_curso_online);
            buttonOnline.setEnabled(false);
            buttonOnline.setElevation(-1); // Define uma elevação menor que zero para ficar atrás do fragmento
        }
    }

    /**
     * Método para exibir um diálogo de confirmação para deletar a conta do parceiro.
     * Este método exibe um diálogo de confirmação com as opções "Sim" e "Não". Se o usuário
     * selecionar "Sim", a conta do parceiro será deletada. Caso contrário, o diálogo será fechado.
     */
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

    /**
     * Método para fazer uma requisição para deletar a conta do parceiro no servidor.
     * Este método envia uma requisição DELETE para o endpoint responsável por deletar a conta
     * do parceiro. Se a requisição for bem-sucedida, a conta é deletada e o usuário é redirecionado
     * para a tela inicial. Se ocorrer algum erro, como falha de conexão ou erro no servidor, uma mensagem
     * de erro é exibida e tratada adequadamente.
     */
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
                String errorMessage = "error";

                if (error.networkResponse!= null && error.networkResponse.statusCode == 401) {
                    token.clearToken();
                    Intent intent = new Intent(PerfilPaceiro.this, FormLogin.class);
                    startActivity(intent);
                } else if (error instanceof NetworkError) {
                    errorMessage = "Sem conexão com a internet. Por favor, verifique sua conexão.";
                } else if (error instanceof ServerError) {
                    errorMessage = "O servidor está enfrentando problemas. Por favor, tente novamente mais tarde.";
                } else if (error instanceof ParseError) {
                    errorMessage = "Houve um problema ao processar a resposta do servidor.";
                } else if (error instanceof TimeoutError) {
                    errorMessage = "A solicitação demorou muito para ser processada. Por favor, tente novamente mais tarde.";
                }


                new AlertDialog.Builder(PerfilPaceiro.this)
                        .setTitle("Erro")
                        .setMessage(errorMessage)
                        .setPositiveButton("OK", null)
                        .show();
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

    /**
     * Método para buscar os cursos cadastrados pelo parceiro no servidor.
     * Este método faz uma requisição GET para o endpoint responsável por obter os cursos
     * cadastrados pelo parceiro. Se os dados forem recuperados com sucesso, eles são processados
     * e exibidos na interface do usuário. Caso contrário, uma mensagem de erro é exibida.
     */
    public void fetchPartnerCoursesData() {
        String finalURL = Constants.BASE_URL + "/parceiro/cursos-cadastrados";
        progressBarPerfilParceiro.setVisibility(View.VISIBLE); // Mostra o ProgressBar enquanto os dados são carregados.

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, finalURL, null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    progressBarPerfilParceiro.setVisibility(View.GONE);
                    if (response.length() == 0) {
                        progressBarPerfilParceiro.setVisibility(View.GONE);
                        errorPartnerTextView.setVisibility(View.VISIBLE); // Exibe a mensagem de erro.
                        errorPartnerTextView.setText("Não há cursos cadastrados!");
                        return;
                    }
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

    /**
     * Método para processar a resposta dos cursos cadastrados pelo parceiro.
     * Este método percorre a resposta JSON e extrai as informações de cada curso,
     * em seguida, cria objetos Curso e adiciona à lista de cursos cadastrados.
     * Por fim, configura o RecyclerView para exibir os cursos na interface do usuário.
     */
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

    /**
     * Método para configurar o RecyclerView com os cursos cadastrados pelo parceiro.
     * Este método define o layout do RecyclerView e cria um adaptador personalizado
     * para exibir os cursos na interface do usuário.
     */
    private void setupRecyclerView() {
        recyclerViewPartner.setLayoutManager(new LinearLayoutManager(PerfilPaceiro.this));
        adapterPartner = new CursoAdapterPerfil(PerfilPaceiro.this, registeredCourses);
        recyclerViewPartner.setAdapter(adapterPartner);
    }

    /**
     * Método para iniciar a atividade para adicionar um novo curso online.
     * Este método cria e inicia um intent para abrir a atividade NovoCursoOnline.
     */
    public void irParaNovoCursoOnline(View view)
    {
        Intent irNovoCursoOnline = new Intent(PerfilPaceiro.this, NovoCursoOnline.class);
        startActivity(irNovoCursoOnline);
    }

    /**
     * Método para iniciar a atividade para adicionar um novo curso presencial.
     * Este método cria e inicia um intent para abrir a atividade NovoCursoPresencial.
     */
    public void irParaNovoCursoPresencial(View view)
    {
        Intent irNovoCursoPresencial = new Intent(PerfilPaceiro.this, NovoCursoPresencial.class);
        startActivity(irNovoCursoPresencial);
    }

}