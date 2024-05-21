package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Classe que define a atividade para editar um curso presencial.
 */
public class EditarCursoPresencial extends AppCompatActivity {

    private EditText campo_nome_curso, campo_vagas, campo_data_inicial, campo_data_final, campo_descricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_curso_presencial);

        Intent intent = getIntent();
        int defaultValue = 0;
        int courseId = intent.getIntExtra("courseId", defaultValue);

        campo_nome_curso = findViewById(R.id.campo_nome_curso);
        campo_vagas = findViewById(R.id.campo_vagas);
        campo_data_inicial = findViewById(R.id.campo_data_inicial);
        campo_data_final= findViewById(R.id.campo_data_final);
        campo_descricao = findViewById(R.id.descricao_curso);

        obterDadosDoCursoPresencial(courseId);
    }

    /**
     * Método para obter os dados de um curso presencial específico do servidor.
     *
     * @param courseId O ID do curso a ser obtido.
     */
    private void obterDadosDoCursoPresencial(int courseId) {
        String finalURL = Constants.BASE_URL + "/cursos/" + courseId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, finalURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Extrair os dados do curso do JSON de resposta
                            String nomeCurso = response.getString("title");
                            int vagas = response.getInt("maxCapacity");
                            String dataInicial = response.getString("initialDate");
                            String dataFinal = response.getString("endDate");
                            String descricao = response.getString("description");

                            // Preencher os campos com os dados obtidos
                            campo_nome_curso.setText(nomeCurso);
                            campo_vagas.setText(String.valueOf(vagas));
                            campo_data_inicial.setText(dataInicial);
                            campo_data_final.setText(dataFinal);
                            campo_descricao.setText(descricao);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditarCursoPresencial.this, "Erro ao processar resposta do servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Tratar erro de requisição
                error.printStackTrace();
                Toast.makeText(EditarCursoPresencial.this, "Erro ao obter dados do curso", Toast.LENGTH_SHORT).show();
            }
        });

        // Adicionar a requisição à fila do Volley
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    /**
     * Método chamado quando o botão "Atualizar" é clicado na atividade para editar um curso presencial.
     *
     * @param view A view associada ao método.
     */
    public void onClickAtualizarCursoPresencial(View view) {
        // Obtenha o courseId novamente, pois ele pode ter mudado desde a criação da atividade
        Intent intent = getIntent();
        int defaultValue = 0;
        int courseId = intent.getIntExtra("courseId", defaultValue);
        String endereco = intent.getStringExtra("endereco");
        String zona = intent.getStringExtra("zona");

        // Chame o método AtualizandoCurso
        try {
            Prosseguir(view, courseId,endereco, zona);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método chamado para prosseguir para a próxima tela ao editar um curso presencial.
     *
     * @param view      A view associada ao método.
     * @param courseId  O ID do curso.
     * @param endereco  O endereço do curso.
     * @param zona      A zona do curso.
     * @throws ParseException Se ocorrer um erro de parsing.
     */
    public void Prosseguir(View view, int courseId, String endereco, String zona ) throws ParseException {
        // Obtendo os valores dos campos
        String nomeCurso = campo_nome_curso.getText().toString();
        String vagas = campo_vagas.getText().toString();
        String dataInicial = campo_data_inicial.getText().toString();
        String dataFinal = campo_data_final.getText().toString();
        String descricao = campo_descricao.getText().toString();

        if (nomeCurso.isEmpty() || vagas.isEmpty() || dataInicial.isEmpty() || dataFinal.isEmpty() || descricao.isEmpty()) {
            if (nomeCurso.isEmpty()) campo_nome_curso.setError("Nome do curso é obrigatório");
            if (vagas.isEmpty()) campo_vagas.setError("Vagas é obrigatório");
            if (dataInicial.isEmpty()) campo_data_inicial.setError("Data Inicial é obrigatória");
            if (dataFinal.isEmpty()) campo_data_final.setError("Data Final é obrigatória");
            if (descricao.isEmpty()) campo_descricao.setError("Descrição é obrigatória");
            if (descricao.isEmpty()) campo_descricao.setError("Descrição é obrigatória");
            return;
        }

        int limiteNome = 20; // Definir o limite de caracteres para o nome
        int limiteDescricao = 150; // Definir o limite de caracteres para a descrição
        String formato = "dd/MM/yyyy";

        if (!ValidacaoNewCurso.validarLimiteNome(nomeCurso, limiteNome)) {
            campo_nome_curso.setError("Nome do curso só pode ter 20 caracteres");
            return;
        }

        if (!ValidacaoNewCurso.validarLimiteDescricao(descricao, limiteDescricao)) {
            campo_descricao.setError("Descrição só pode ter 150 caracteres");
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

        // Passando os valores para a próxima tela
        Intent intent = new Intent(EditarCursoPresencial.this, EditarCursoPresencialEndereco.class);
        intent.putExtra("courseId", courseId);
        intent.putExtra("nome_curso", nomeCurso);
        intent.putExtra("vagas", vagas);
        intent.putExtra("data_inicial", dataInicial);
        intent.putExtra("data_final", dataFinal);
        intent.putExtra("descricao", descricao);
        intent.putExtra("endereco", endereco);
        intent.putExtra("zona", zona);
        startActivity(intent);
    }

    /**
     * Método chamado para cancelar a edição de um curso presencial e voltar para o perfil do usuário com o curso expandido.
     *
     * @param view A view associada ao método.
     */
    public void Cancelar(View view)
    {
        Intent Cancelar = new Intent(EditarCursoPresencial.this, PerfilUsuarioCursoExpandido.class);
        startActivity(Cancelar);
    }
}