package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Esta classe é responsável por permitir a edição do endereço de um curso presencial.
 */
public class EditarCursoPresencialEndereco extends AppCompatActivity {
    private String nomeCurso, vagas, dataInicial, dataFinal, descricao, endereco, zona;

    private Spinner campo_zona;

    private EditText campo_bairro, campo_rua, campo_numero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_curso_presencial_endereco);

        // Recebendo os dados da intent
        Intent intent = getIntent();
        nomeCurso = intent.getStringExtra("nome_curso");
        vagas = intent.getStringExtra("vagas");
        dataInicial = intent.getStringExtra("data_inicial");
        dataFinal = intent.getStringExtra("data_final");
        descricao = intent.getStringExtra("descricao");
        endereco = intent.getStringExtra("endereco");
        zona = intent.getStringExtra("zona");

        Log.d("endereco do curso", String.valueOf(endereco));
        Log.d("zona do curso", String.valueOf(zona));

        campo_bairro = findViewById(R.id.campo_bairro);
        campo_rua = findViewById(R.id.campo_rua);
        campo_numero = findViewById(R.id.campo_numero);
        campo_zona = findViewById(R.id.campo_zona);

        if (endereco != null && !endereco.isEmpty()) {
            // Dividindo o endereço em partes usando espaços em branco
            String[] partes = endereco.split(",");
            if (partes.length >= 3) { // Certificando-se de que há pelo menos rua, número e bairro
                String rua = partes[0].trim();
                String numero = partes[1].trim();
                String bairro = partes[2].trim();

                campo_rua.setText(rua);
                campo_numero.setText(numero);
                campo_bairro.setText(bairro);
            }
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.zona, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campo_zona.setAdapter(adapter);

        // Se a zona não for nula e não estiver vazia, selecione-a no Spinner
        if (zona != null && !zona.isEmpty()) {
            // Obtenha o índice da zona no array de recursos
            String[] zonaArray = getResources().getStringArray(R.array.zona);
            int position = Arrays.asList(zonaArray).indexOf(zona);

            // Se o índice for encontrado, selecione a zona no Spinner
            if (position != -1) {
                campo_zona.setSelection(position);
            }
        }
    }

    /**
     * Este método é chamado quando o botão de atualização de endereço é clicado.
     * Ele obtém o ID do curso da intent e chama o método AtualizandoCursoPresencial para atualizar o endereço do curso presencial.
     *
     * @param view A view que acionou o clique no botão.
     */
    public void onClickAtualizarEndereco(View view) {
        // Obtenha o courseId novamente, pois ele pode ter mudado desde a criação da atividade
        Intent intent = getIntent();
        int defaultValue = 0;
        int courseId = intent.getIntExtra("courseId", defaultValue);

        // Chame o método AtualizandoCurso
        try {
            AtulizandoCursoPresencial(view, courseId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Este método é responsável por atualizar o endereço de um curso presencial no servidor.
     *
     * @param view     A view que acionou a ação de atualização.
     * @param courseId O ID do curso que está sendo atualizado.
     * @throws ParseException Exceção que pode ser lançada durante a análise dos dados.
     */
    public void AtulizandoCursoPresencial(View view, int courseId) throws ParseException
    {
        // Obtendo os valores dos campos de endereço
        String bairro = campo_bairro.getText().toString();
        String rua = campo_rua.getText().toString();
        String numero = campo_numero.getText().toString();
        String zona = campo_zona.getSelectedItem().toString();
        Log.d("zona do curso", String.valueOf(zona));
        String endereco = rua + ", " + numero + ", " + bairro;

        if (rua.isEmpty() || numero.isEmpty() || bairro.isEmpty()) {
            if (bairro.isEmpty()) campo_bairro.setError("Bairro é obrigatório");
            if (rua.isEmpty()) campo_rua.setError("Rua é obrigatório");
            if (numero.isEmpty()) campo_numero.setError("Número é obrigatório");
            return;
        }

        JSONObject jsonCurso = new JSONObject();
        try {
            jsonCurso.put("courseId", courseId);
            jsonCurso.put("title", nomeCurso);
            jsonCurso.put("description", descricao);
            jsonCurso.put("address", endereco);
            jsonCurso.put("maxCapacity", vagas);
            jsonCurso.put("initialDate", dataInicial);
            jsonCurso.put("endDate", dataFinal);
            jsonCurso.put("zone", zona);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String finalURL = Constants.BASE_URL + "/cursos/update" ;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, finalURL, jsonCurso,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Resposta recebida do servidor
                        Toast.makeText(EditarCursoPresencialEndereco.this, "Curso atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        Log.d("VolleySuccess", "Resposta recebida: " + response.toString());
                        Intent intent = new Intent(EditarCursoPresencialEndereco.this, PerfilPaceiro.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tratar erro
                        Toast.makeText(EditarCursoPresencialEndereco.this, "Erro ao criar curso: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("VolleyError", "Erro na requisição: " + error.getMessage());
                    }
                }){
        };

        Volley.newRequestQueue(this).add(request);
    }

    /**
     * Este método é responsável por voltar para a tela de edição de curso presencial.
     */
    private void voltarNovoCursoPresencial() {
        Intent voltarTelaLogin = new Intent(EditarCursoPresencialEndereco.this, EditarCursoPresencial.class);
        startActivity(voltarTelaLogin);
    }
}