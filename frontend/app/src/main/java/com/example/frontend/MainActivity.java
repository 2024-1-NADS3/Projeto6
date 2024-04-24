package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    // Inicializa variáveis
    RequestQueue filaRequest;
    RecyclerView recyclerView;
    CursoAdapter adapter;
    List<Curso> courses = new ArrayList<>();
    ProgressBar progressBar;
    String finalURL = "https://dzlj63-4550.csb.app/cursos/todos";
    TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inicializa a Activity, define o layout e configura os componentes da UI.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        errorTextView = findViewById(R.id.errorTextView);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        filaRequest = Volley.newRequestQueue(this);
        fetchCoursesData(); // Inicia a busca de dados dos cursos.

        // Configura o SearchView para permitir a busca por título de curso.
        SearchView searchView = findViewById(R.id.searchBar);
        searchView.setIconifiedByDefault(false);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getSearchBarFilter().filter(newText); // Filtra os cursos com base no texto de busca.
                return false;
            }
        });
    }

    // Método para buscar os dados dos cursos da API.
    public void fetchCoursesData() {
        progressBar.setVisibility(View.VISIBLE); // Mostra o ProgressBar enquanto os dados são carregados.
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, finalURL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.GONE); // Esconde o ProgressBar após o carregamento dos dados.
                        processCoursesResponse(response); // Processa a resposta dos dados dos cursos.
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE); // Esconde o ProgressBar após um erro.
                        errorTextView.setVisibility(View.VISIBLE); // Exibe a mensagem de erro.
                        errorTextView.setText("Não foi possível carregar os cursos");
                        Log.e("Volley", error.toString()); // Registra o erro.
                    }
                });
        filaRequest.add(request); // Adiciona a requisição à fila de requisições.
    }

    // Processa a resposta dos dados dos cursos, criando objetos Curso e adicionando-os à lista.
    private void processCoursesResponse(JSONArray response) {
        if (response.length() > 0) {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject cursoJson = response.getJSONObject(i);
                    Curso curso = new Curso();
                    curso.setId(cursoJson.getInt("id"));
                    curso.setTitle(cursoJson.getString("title"));
                    curso.setType(cursoJson.getString("type"));
                    curso.setCategory(cursoJson.getString("category"));
                    curso.setImg(cursoJson.getString("img"));
                    curso.setDescription(cursoJson.getString("description"));
                    curso.setAddress(cursoJson.getString("address"));
                    curso.setZone(cursoJson.getString("zone"));
                    curso.setOccupiedSlots(cursoJson.getInt("occupiedSlots"));
                    curso.setMaxCapacity(cursoJson.getInt("maxCapacity"));
                    courses.add(curso);
                } catch (JSONException e) {
                    Log.e("Volley", "Erro no JSON", e); // Registra um erro no JSON.
                }
            }
            setupRecyclerView(); // Configura o RecyclerView com os dados dos cursos.
            applyFiltersIfAvailable(); // Aplica filtros se disponíveis.
        } else {
            Log.d("Data", "Sem Dados");
        }
    }

    // Configura o RecyclerView com o adaptador e os dados dos cursos.
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new CursoAdapter(MainActivity.this, courses);
        recyclerView.setAdapter(adapter);
    }

    // Método para mudar para a Activity de filtros.
    public void changeActivityToFilters(View view) {
        Intent intent = new Intent(getApplicationContext(), Filtros.class);
        startActivity(intent);
    }

    // Aplica filtros se disponíveis, obtidos da Intent anterior.
    private void applyFiltersIfAvailable() {
        String categorySelected = getIntent().getStringExtra("categorySelectedOptionTxt");
        String typeSelected = getIntent().getStringExtra("typeSelectedOptionTxt");
        String zoneSelected = getIntent().getStringExtra("zoneSelectedOptionTxt");

        if (categorySelected != null && typeSelected != null && zoneSelected != null) {
            filterDataWithFiltersPage(categorySelected, typeSelected, zoneSelected); // Aplica os filtros.
        }
    }

    // Aplica os filtros selecionados aos cursos e atualiza o RecyclerView.
    public void filterDataWithFiltersPage(String categorySelected, String typeSelected, String zoneSelected) {
        List<Curso> filteredCursos = courses.stream()
                .filter(curso -> (categorySelected.isEmpty() || curso.getCategory().equals(categorySelected)) &&
                        (zoneSelected.isEmpty() || curso.getZone().equals(zoneSelected)) &&
                        (typeSelected.isEmpty() || curso.getType().equals(typeSelected)))
                .collect(Collectors.toList());

        adapter.updateDataByFiltersPage(filteredCursos); // Atualiza o adaptador com os cursos filtrados.
        // Verifica se a lista de cursos filtrados está vazia
        if (filteredCursos.isEmpty()) {
            // Se a lista estiver vazia, exibe a mensagem de erro
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText("Não existe cursos com os filtros aplicados");
        } else {
            // Caso contrário, esconde a mensagem de erro
            errorTextView.setVisibility(View.GONE);
        }

        Log.d("array", String.valueOf(filteredCursos));
    }




}