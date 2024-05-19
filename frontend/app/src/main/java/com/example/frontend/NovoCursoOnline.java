package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NovoCursoOnline extends AppCompatActivity implements VolleyCallback {

    private Spinner campo_categoria;

    GerenciadorToken token;

    private static final String URL_CRIAR_CURSO= Constants.BASE_URL + "/cursos/cadastrar";

    private EditText campo_nome_curso, campo_vagas, campo_data_inicial, campo_data_final, campo_local, campo_descricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_curso_online);
        token = new GerenciadorToken(this);
        Log.d("O token está aqui?", token.getToken());

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

    public void CriarNovoCursoOnline(View view) throws ParseException {

        String nomeCurso = campo_nome_curso.getText().toString();
        String vagas = campo_vagas.getText().toString();
        String dataInicial = campo_data_inicial.getText().toString();
        String dataFinal = campo_data_final.getText().toString();
        String categoria = campo_categoria.getSelectedItem().toString();
        String tipo = "Online";
        Integer occupiedSlots = 0;
        String local = campo_local.getText().toString();
        String descricao = campo_descricao.getText().toString();

        // Verificar se os campos estão vazios
        if (nomeCurso.isEmpty() || vagas.isEmpty() || dataInicial.isEmpty() || dataFinal.isEmpty() || local.isEmpty() || descricao.isEmpty()) {
            if (nomeCurso.isEmpty()) campo_nome_curso.setError("Nome do curso é obrigatório");
            if (vagas.isEmpty()) campo_vagas.setError("Vagas é obrigatório");
            if (dataInicial.isEmpty()) campo_data_inicial.setError("Data Inicial é obrigatória");
            if (dataFinal.isEmpty()) campo_data_final.setError("Data Final é obrigatória");
            if (local.isEmpty()) campo_local.setError("Local é obrigatório");
            if (descricao.isEmpty()) campo_descricao.setError("Descrição é obrigatória");
            return;
        }

        int limiteNome = 20; // Definir o limite de caracteres para o nome
        int limiteDescricao = 50; // Definir o limite de caracteres para a descrição
        String formato = "dd/MM/yyyy";

        if (!ValidacaoNewCurso.validarLimiteNome(nomeCurso, limiteNome)) {
            campo_nome_curso.setError("Nome do curso só pode ter 20 caracteres");
            return;
        }

        if (!ValidacaoNewCurso.validarLimiteDescricao(descricao, limiteDescricao)) {
            campo_descricao.setError("Descrição só pode ter 100 caracteres");
            return;
        }

        if (!ValidacaoNewCurso.validarData(dataFinal, formato) || !ValidacaoNewCurso.validarData(dataInicial, formato)){
            if(!ValidacaoNewCurso.validarData(dataFinal, formato)){
                campo_data_final.setError("Data invalida, necessário inserir no seguinte formato dd/mm/yyyy");
            }
            if (!ValidacaoNewCurso.validarData(dataInicial, formato)){
                campo_data_inicial.setError("Data invalida, necessário inserir no seguinte formato dd/mm/yyyy");
            }
            return;
        }

        if(!ValidacaoNewCurso.dataMaiorQueHoje(dataFinal, formato) || !ValidacaoNewCurso.dataMaiorQueHoje(dataInicial, formato)){
            if (!ValidacaoNewCurso.dataMaiorQueHoje(dataFinal, formato)) {
                campo_data_final.setError("Data inferior ao dia de hoje");
            }
            if (!ValidacaoNewCurso.dataMaiorQueHoje(dataInicial, formato)) {
                campo_data_inicial.setError("Data inferior ao dia de hoje");
            }
            return;
        }

        if (ValidacaoNewCurso.compararData(dataInicial, dataFinal, formato)) {
            campo_data_final.setError("A data final deve ser posterior à data inicial");
            return;
        }

        if(!ValidacaoNewCurso.validarDataFormatoCorreto(dataFinal) || !ValidacaoNewCurso.validarDataFormatoCorreto(dataInicial)){
            if (!ValidacaoNewCurso.validarDataFormatoCorreto(dataFinal)) {
                campo_data_final.setError("Por favor, lembre-se de incluir o zero à esquerda para o dia ou mês caso não ultrapassem a unidade.");
            }
            if (!ValidacaoNewCurso.validarDataFormatoCorreto(dataInicial)) {
                campo_data_inicial.setError("Por favor, lembre-se de incluir o zero à esquerda para o dia ou mês caso não ultrapassem a unidade.");
            }
            return;
        }
        JSONObject jsonCurso = new JSONObject();
        try {
            jsonCurso.put("title", nomeCurso);
            jsonCurso.put("type", tipo);
            jsonCurso.put("category", categoria);
            jsonCurso.put("description", descricao);
            jsonCurso.put("address", local);
            jsonCurso.put("occupiedSlots", occupiedSlots);
            jsonCurso.put("maxCapacity", vagas);
            jsonCurso.put("initialDate", dataInicial);
            jsonCurso.put("endDate", dataFinal);
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
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token.getToken()); // Substitua "Bearer " + token pelo seu token JWT
                return headers;
            }
        };

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