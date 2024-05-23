package com.example.frontend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Método para exibir um diálogo informativo sobre o ícone de advertência.
 * Este método cria um AlertDialog que mostra informações sobre as advertências do usuário ao clicar no ícone de advertência.
 */
public class PerfilUsuario extends AppCompatActivity {

    GerenciadorToken token;
    RecyclerView recyclerViewCursosUsuarios;
    CursoAdapterPerfilUsuario adapterUser;
    List<Curso> userCourses = new ArrayList<>();
    RequestQueue filaRequest;
    ProgressBar progressBarUser;
    TextView errorUserTextView, warnings;
    private boolean drawerAberto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        ImageView warningIcon = findViewById(R.id.warningIcon);
        warningIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PerfilUsuario.this);
                        builder.setTitle("Aviso")
                                .setMessage("Este campo mostra quantas advertências você tem. " +
                                        "Elas são dadas por não comparecer a compromissos presenciais ou online. " +
                                        "Lembre-se: 5 advertências significam não poder se inscrever mais em cursos pela plataforma. ")

                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Código para executar quando o usuário clicar em OK
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        token = new GerenciadorToken(this);

        actionBar();

        //Configura o SearchView para permitir a busca por título de curso.
        SearchView searchBarPerfilUser = findViewById(R.id.searchViewUser);
        searchBarPerfilUser.setIconifiedByDefault(false);
        searchBarPerfilUser.clearFocus();
        searchBarPerfilUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterUser.getSearchBarFilter().filter(newText); // Filtra os cursos com base no texto de busca.
                return false;
            }
        });

        warnings = findViewById(R.id.warnings);

        getUserWarnings();

        recyclerViewCursosUsuarios = findViewById(R.id.recyclerViewCursosUsuarios);

        filaRequest = Volley.newRequestQueue(this);

        errorUserTextView = findViewById(R.id.errorUserTextView);
        progressBarUser = findViewById(R.id.progressBarUser);


        fetchUserCoursesData();


        Log.d("O token está aqui no USER?", token.getToken());
        filtroSpinner();
    }

    /**
     * Método chamado quando a atividade é retomada. Verifica se o usuário está logado.
     */
    @Override
    protected void onResume() {
        super.onResume();
        verificarSeEstaLogado();
    }

    /**
     * Configura a ActionBar, incluindo o botão de usuário e o título, e define os comportamentos dos cliques nos botões.
     */
    public void actionBar() {
        Button botaoUsuario = findViewById(R.id.ic_usuarioPerfil);
        botaoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDrawer();
            }
        });

        TextView botaoTitulo = findViewById(R.id.titulo_PerfilUsuario);
        botaoTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (PerfilUsuario.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Abre ou fecha o Navigation Drawer, adicionando ou removendo o fragment correspondente.
     */
    public void abrirDrawer() {

        //Obtendo o FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        //Verifica se o Navigation Drawer está aberto
        if (drawerAberto) {
            //Se estiver aberto, remover o fragment
            Fragment fragment = fragmentManager.findFragmentById(R.id.containerLayout_PerfilUsuario);
            if (fragment instanceof NavigationDrawer){
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                transaction.remove(fragment).commit();
                drawerAberto = false;
            }


        } else {
            //Se não estiver aberto, adiciona o fragment
            NavigationDrawer drawerFragment = new NavigationDrawer();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
            transaction.replace(R.id.containerLayout_PerfilUsuario, drawerFragment);
            transaction.addToBackStack(null); // Adiciona o fragment à pilha de BackStack
            transaction.commit();
            drawerAberto = true; //Atualiza a flag
        }
    }

    /**
     * Configura o Spinner que permite filtrar os cursos com base em seu estado.
     */
    public void filtroSpinner() {
        Spinner spinnerFiltros = findViewById(R.id.spinner_filtros);

        // Definindo o array de opções diretamente no código
        String[] filtrosArray = {"Cursos: Todos", "Cursos: Não iniciados", "Cursos: Em andamento", "Cursos: Finalizados"};

        // Criando um ArrayAdapter com o array de opções
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, filtrosArray);

        // Especificando o layout a ser usado quando a lista de opções aparecer
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apliquando o adaptador ao spinner
        spinnerFiltros.setAdapter(adapter);

        // Manipulando a seleção do spinner
        spinnerFiltros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filtroSelecionado = parent.getItemAtPosition(position).toString();
                // Faça algo com o filtro selecionado
                if (adapterUser!= null) {
                    adapterUser.filtrarCursos(filtroSelecionado);
                } else {
                    Log.d("Erro", "adapterUser é null");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // O usuário não selecionou nada
            }
        });
    }

    /**
     * Obtém o número de advertências do usuário fazendo uma solicitação ao servidor e exibe o resultado na interface do usuário.
     */
    public void getUserWarnings() {
        RequestQueue mRequestQueue;

        String finalURL = Constants.BASE_URL + "/usuario/pegar-advertencias";

        mRequestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String warningsResult = jsonResponse.getString("warnings");
                            Log.d("Resposta", "onResponse: " + warningsResult);
                            warnings.setText(warningsResult);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Logando o erro
                Log.d("Erro", "onErrorResponse: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token.getToken()); // Substitua "Bearer " + token pelo seu token JWT
                return headers;
            }
        };;

        mRequestQueue.add(stringRequest);
    }

    /**
     * Verifica se o usuário está logado verificando se há um token válido.
     * Se não estiver logado, redireciona para a tela de login.
     */
    public void verificarSeEstaLogado() {
        Log.d("erro na activity Perfil Usuario", "aaaaa");
        if (token != null) {
            String tokenString = token.getToken();
            if (tokenString.equals("")) {
                // Se o token estiver vazio, redireciona para a tela de login
                Intent mudarTelaParaLogin = new Intent(PerfilUsuario.this, FormLogin.class);
                startActivity(mudarTelaParaLogin);
            }
        }
    }

    /**
     * Obtém os cursos em que o usuário está inscrito fazendo uma solicitação ao servidor e exibe os dados na interface do usuário.
     */
    public void fetchUserCoursesData() {
        String finalURL = Constants.BASE_URL + "/usuario/cursos-inscritos";
        progressBarUser.setVisibility(View.VISIBLE); // Mostra o ProgressBar enquanto os dados são carregados.

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, finalURL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBarUser.setVisibility(View.GONE);
                        if (response.length() == 0) {
                            progressBarUser.setVisibility(View.GONE);
                            errorUserTextView.setVisibility(View.VISIBLE); // Exibe a mensagem de erro.
                            errorUserTextView.setText("Você não está inscrito em nenhum curso");
                            Log.d("Erro", "Array vazia, sem cursos");
                            return;
                        }
                        processCoursesResponse(response); // Processa a resposta dos dados dos cursos.
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBarUser.setVisibility(View.GONE);
                        errorUserTextView.setVisibility(View.VISIBLE); // Exibe a mensagem de erro.
                        errorUserTextView.setText("Não foi possível carregar os cursos");
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
     * Processa a resposta JSON dos cursos do usuário e os adiciona à lista de cursos do usuário.
     * Em seguida, configura o RecyclerView com os dados dos cursos.
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
                    userCourses.add(curso);
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
     * Configura o RecyclerView para exibir os cursos do usuário.
     */
    private void setupRecyclerView() {
        recyclerViewCursosUsuarios.setLayoutManager(new LinearLayoutManager(PerfilUsuario.this));
        adapterUser = new CursoAdapterPerfilUsuario(PerfilUsuario.this, userCourses);
        recyclerViewCursosUsuarios.setAdapter(adapterUser);
    }
}


