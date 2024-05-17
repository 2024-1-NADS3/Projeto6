package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NovoCursoPresencial extends AppCompatActivity {

    private static final String URL_CRIAR_CURSO= Constants.BASE_URL + "/curso/cadastrar";

    private EditText campo_nome_curso, campo_vagas, campo_data_inicial, campo_date_final, campo_categoria, campo_descricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_curso_presencial);

        campo_nome_curso = findViewById(R.id.campo_nome_curso);
        campo_vagas = findViewById(R.id.campo_vagas);
        campo_data_inicial = findViewById(R.id.campo_data_inicial);
        campo_date_final = findViewById(R.id.campo_data_final);
        campo_descricao = findViewById(R.id.descricao_curso);

        Spinner campoCategoria = findViewById(R.id.campo_categoria);

// Crie um ArrayAdapter com os itens da categoria
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categoria, android.R.layout.simple_spinner_item);

// Especifique o layout a ser usado quando a lista de opções aparecer
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Adicionando um item vazio no início da lista para servir como hint
        List<String> categorias = new ArrayList<>(Arrays.asList("Categorias:"));
        categorias.addAll(Arrays.asList(getResources().getStringArray(R.array.categoria)));

// Configurando o ArrayAdapter personalizado no Spinner
        campoCategoria.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categorias));
    }

    public void ProximaPagina(View view) {
        // Obtendo os valores dos campos
        String nomeCurso = campo_nome_curso.getText().toString();
        String vagas = campo_vagas.getText().toString();
        String dataInicial = campo_data_inicial.getText().toString();
        String dataFinal = campo_date_final.getText().toString();
        String categoria = campo_categoria.getText().toString();
        String descricao = campo_descricao.getText().toString();

        // Passando os valores para a próxima tela
        Intent intent = new Intent(this, NovoCursoPresencialEndereco.class);
        intent.putExtra("nome_curso", nomeCurso);
        intent.putExtra("vagas", vagas);
        intent.putExtra("data_inicial", dataInicial);
        intent.putExtra("data_final", dataFinal);
        intent.putExtra("categoria", categoria);
        intent.putExtra("descricao", descricao);
        startActivity(intent);
    }

    public void Cancelar(View view)
    {
        Intent Cancelar = new Intent(NovoCursoPresencial.this, PerfilPaceiro.class);
        startActivity(Cancelar);
    }
}