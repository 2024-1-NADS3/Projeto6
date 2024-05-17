package com.example.frontend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class NovoCursoPresencialEndereco extends AppCompatActivity {
    private ImageView voltarNovoCurso;

    private String nomeCurso, vagas, dataInicial, dataFinal, categoria, descricao;

    private EditText campo_cep, campo_rua, campo_numero, campo_zona;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_curso_presencial_endereco);
        getSupportActionBar().hide();

        // Recebendo os dados da intent
        Intent intent = getIntent();
        nomeCurso = intent.getStringExtra("nome_curso");
        vagas = intent.getStringExtra("vagas");
        dataInicial = intent.getStringExtra("data_inicial");
        dataFinal = intent.getStringExtra("data_final");
        categoria = intent.getStringExtra("categoria");
        descricao = intent.getStringExtra("descricao");

        campo_cep = findViewById(R.id.campo_cep);
        campo_rua = findViewById(R.id.campo_rua);
        campo_numero = findViewById(R.id.campo_numero);
        campo_zona = findViewById(R.id.campo_zona);

        voltarNovoCurso = findViewById(R.id.voltar_novo_curso_presencial);

        voltarNovoCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltarNovoCursoPresencial();
            }
        });
    }

    public void CriarNovoCursoPresencial(View view)
    {
        // Obtendo os valores dos campos de endereço
        String cep = campo_cep.getText().toString();
        String rua = campo_rua.getText().toString();
        String numero = campo_numero.getText().toString();
        String zona = campo_zona.getText().toString();
        String tipo = "Presencial";



        // Criando um objeto JSON com todas as informações
        JSONObject jsonCurso = new JSONObject();
        try {
            jsonCurso.put("nome_curso", nomeCurso);
            jsonCurso.put("vagas", vagas);
            jsonCurso.put("data_inicial", dataInicial);
            jsonCurso.put("data_final", dataFinal);
            jsonCurso.put("categoria", categoria);
            jsonCurso.put("descricao", descricao);
            jsonCurso.put("cep", cep);
            jsonCurso.put("rua", rua);
            jsonCurso.put("numero", numero);
            jsonCurso.put("zona", zona);
            jsonCurso.put("tipo", zona);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Enviando a requisição POST usando Volley
        String URL = Constants.BASE_URL + "/curso/cadastrar";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, jsonCurso,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(NovoCursoPresencialEndereco.this, "Curso criado com sucesso!", Toast.LENGTH_SHORT).show();
                        Log.d("VolleySuccess", "Resposta recebida: " + response.toString());
                        Intent intent = new Intent(NovoCursoPresencialEndereco.this, PerfilPaceiro.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tratar erro
                        Toast.makeText(NovoCursoPresencialEndereco.this, "Erro ao criar curso: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("POST Error", error.toString());
                    }
                });

        // Adicionando a requisição à fila do Volley
        Volley.newRequestQueue(this).add(request);
    }

    private void voltarNovoCursoPresencial() {
        Intent voltarTelaLogin = new Intent(NovoCursoPresencialEndereco.this, NovoCursoPresencial.class);
        startActivity(voltarTelaLogin);
    }
}