package com.example.frontend;

import android.annotation.SuppressLint;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Esta classe é responsável por criar a interface de entrada de endereço para um novo curso presencial.
 */
public class NovoCursoPresencialEndereco extends AppCompatActivity {

    private Spinner campo_zona;

    GerenciadorToken token;

    private String nomeCurso, vagas, dataInicial, dataFinal, categoria, descricao;

    private EditText campo_bairro, campo_rua, campo_numero;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_curso_presencial_endereco);
        token = new GerenciadorToken(this);
        Log.d("O token está aqui?", token.getToken());

        // Recebendo os dados da intent
        Intent intent = getIntent();
        nomeCurso = intent.getStringExtra("nome_curso");
        vagas = intent.getStringExtra("vagas");
        dataInicial = intent.getStringExtra("data_inicial");
        dataFinal = intent.getStringExtra("data_final");
        categoria = intent.getStringExtra("categoria");
        descricao = intent.getStringExtra("descricao");

        campo_bairro = findViewById(R.id.campo_bairro);
        campo_rua = findViewById(R.id.campo_rua);
        campo_numero = findViewById(R.id.campo_numero);
        campo_zona = findViewById(R.id.campo_zona);

// Crie um ArrayAdapter com os itens da categoria
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.zona, android.R.layout.simple_spinner_item);

// Especifique o layout a ser usado quando a lista de opções aparecer
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Adicionando um item vazio no início da lista para servir como hint
        List<String> zona = new ArrayList<>(Arrays.asList("Zona:"));
        zona.addAll(Arrays.asList(getResources().getStringArray(R.array.zona)));

// Configurando o ArrayAdapter personalizado no Spinner
        campo_zona.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, zona));
    }

    /**
     * Este método é chamado quando o usuário deseja criar um novo curso presencial após preencher o endereço.
     * Ele obtém os valores dos campos de endereço, valida-os e cria um objeto JSON com todas as informações do curso.
     * Em seguida, envia uma requisição POST usando Volley para cadastrar o curso no servidor.
     *
     * @param view A view que acionou o método.
     */
    public void CriarNovoCursoPresencial(View view)
    {
        // Obtendo os valores dos campos de endereço
        String bairro = campo_bairro.getText().toString();
        String rua = campo_rua.getText().toString();
        String numero = campo_numero.getText().toString();
        String zona = campo_zona.getSelectedItem().toString();
        String tipo = "Presencial";
        Integer occupiedSlots = 0;

        String endereco = rua + ", " + numero + ", " + bairro;

        if (rua.isEmpty() || numero.isEmpty() || bairro.isEmpty()) {
            if (bairro.isEmpty()) campo_bairro.setError("Bairro é obrigatório");
            if (rua.isEmpty()) campo_rua.setError("Rua é obrigatório");
            if (numero.isEmpty()) campo_numero.setError("Número é obrigatório");
            return;
        }

        // Criando um objeto JSON com todas as informações
        JSONObject jsonCurso = new JSONObject();
        try {
            jsonCurso.put("title", nomeCurso);
            jsonCurso.put("type", tipo);
            jsonCurso.put("category", categoria);
            jsonCurso.put("description", descricao);
            jsonCurso.put("address", endereco);
            jsonCurso.put("zone", zona);
            jsonCurso.put("occupiedSlots", occupiedSlots);
            jsonCurso.put("maxCapacity", vagas);
            jsonCurso.put("initialDate", dataInicial);
            jsonCurso.put("endDate", dataFinal);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Enviando a requisição POST usando Volley
        String URL = Constants.BASE_URL + "/cursos/cadastrar";
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
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token.getToken()); // Substitua "Bearer " + token pelo seu token JWT
                return headers;
            }
        };
        // Adicionando a requisição à fila do Volley
        Volley.newRequestQueue(this).add(request);
    }

    /**
     * Este método é usado para retornar à tela de criação de um novo curso presencial.
     */
    private void voltarNovoCursoPresencial() {
        Intent voltarTelaLogin = new Intent(NovoCursoPresencialEndereco.this, NovoCursoPresencial.class);
        startActivity(voltarTelaLogin);
    }
}