package com.example.frontend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NovoCursoOnline extends AppCompatActivity implements VolleyCallback {

    private Spinner campo_categoria;
    private static final String URL_CRIAR_CURSO= Constants.BASE_URL + "/cursos/cadastrar";

    private EditText campo_nome_curso, campo_vagas, campo_data_inicial, campo_data_final, campo_local, campo_descricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_curso_online);

        campo_nome_curso = findViewById(R.id.campo_nome_curso);
        campo_vagas = findViewById(R.id.campo_vagas);
        campo_data_inicial = findViewById(R.id.campo_data_inicial);
        campo_data_final= findViewById(R.id.campo_data_final);
        campo_local = findViewById(R.id.campo_local);
        campo_descricao = findViewById(R.id.descricao_curso);
        campo_categoria = findViewById(R.id.campo_categoria);

// Crie um ArrayAdapter com os itens da categoria
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categoria, android.R.layout.simple_spinner_item);

// Especifique o layout a ser usado quando a lista de opções aparecer
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Adicionando um item vazio no início da lista para servir como hint
        List<String> categorias = new ArrayList<>(Arrays.asList("Categorias:"));
        categorias.addAll(Arrays.asList(getResources().getStringArray(R.array.categoria)));

// Configurando o ArrayAdapter personalizado no Spinner
        campo_categoria.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categorias));
    }

    public void CriarNovoCursoOnline(View view) {

        String nomeCurso = campo_nome_curso.getText().toString();
        String vagas = campo_vagas.getText().toString();
        String dataInicial = campo_data_inicial.getText().toString();
        String dataFinal = campo_data_final.getText().toString();
        String categoria = campo_categoria.getSelectedItem().toString();
        String tipo = "Online";
        String local = campo_local.getText().toString();
        String descricao = campo_descricao.getText().toString();

        JSONObject jsonCurso = new JSONObject();
        try {
            jsonCurso.put("title", nomeCurso);
            jsonCurso.put("maxCapacity", vagas);
            jsonCurso.put("initialDate", dataInicial);
            jsonCurso.put("endDate", dataFinal);
            jsonCurso.put("type", tipo);
            jsonCurso.put("address", local);
            jsonCurso.put("category", categoria);
            jsonCurso.put("description", descricao);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_CRIAR_CURSO, jsonCurso,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Resposta recebida do servidor
                        Toast.makeText(NovoCursoOnline.this, "Curso criado com sucesso!", Toast.LENGTH_SHORT).show();
                        Log.d("VolleySuccess", "Resposta recebida: " + response.toString());
                        Intent intent = new Intent(NovoCursoOnline.this, PerfilPaceiro.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tratar erro
                        Toast.makeText(NovoCursoOnline.this, "Erro ao criar curso: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("VolleyError", "Erro na requisição: " + error.getMessage());
                    }
                });

        Volley.newRequestQueue(this).add(request);
    }

    public void onSuccess(JSONObject response) {
        // Ajustar depois para mostrar resposta do servidor
        Log.e("VolleySucess", "Resquisição recebida: " + response);
    }

    @Override
    public void onError(VolleyError error) {
        // Ajustar depois para mostrar resposta do servidor
        Log.e("VolleyError", "Erro na requisição: " + error.getMessage());
    }

    public void Cancelar(View view)
    {
        Intent Cancelar = new Intent(NovoCursoOnline.this, PerfilPaceiro.class);
        startActivity(Cancelar);
    }
}