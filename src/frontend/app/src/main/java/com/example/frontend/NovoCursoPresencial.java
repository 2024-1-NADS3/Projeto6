package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Esta classe é responsável por criar um novo curso presencial.
 */
public class NovoCursoPresencial extends AppCompatActivity {

    private Spinner campo_categoria;

    private static final String URL_CRIAR_CURSO= Constants.BASE_URL + "/curso/cadastrar";

    private EditText campo_nome_curso, campo_vagas, campo_data_inicial, campo_data_final, campo_descricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_curso_presencial);

        campo_nome_curso = findViewById(R.id.campo_nome_curso);
        campo_vagas = findViewById(R.id.campo_vagas);
        campo_data_inicial = findViewById(R.id.campo_data_inicial);
        campo_data_final = findViewById(R.id.campo_data_final);
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

    /**
     * Este método é chamado quando o usuário deseja prosseguir para a próxima etapa ao criar um novo curso presencial.
     * Ele valida os campos preenchidos, incluindo nome do curso, vagas, datas, descrição e categoria.
     * Se todos os campos estiverem preenchidos corretamente, ele passa os valores para a próxima tela.
     * Caso contrário, exibe mensagens de erro nos campos correspondentes.
     *
     * @param view A view que acionou o método.
     * @throws ParseException Exceção lançada se houver erro de análise de data.
     */
    public void Prosseguir(View view) throws ParseException {
        // Obtendo os valores dos campos
        String nomeCurso = campo_nome_curso.getText().toString();
        String vagas = campo_vagas.getText().toString();
        String dataInicial = campo_data_inicial.getText().toString();
        String dataFinal = campo_data_final.getText().toString();
        String categoria = campo_categoria.getSelectedItem().toString();
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

        List<String> categoriasValidas = Arrays.asList("Música", "Diversos", "Esportes", "Esportes"); // Lista de categorias válidas

        boolean categoriaValida = ValidacaoNewCurso.validarCategoriaSelecionada(categoria, categoriasValidas);
        if (categoriaValida) {
            // Passando os valores para a próxima tela
            Intent intent = new Intent(NovoCursoPresencial.this, NovoCursoPresencialEndereco.class);
            intent.putExtra("nome_curso", nomeCurso);
            intent.putExtra("vagas", vagas);
            intent.putExtra("data_inicial", dataInicial);
            intent.putExtra("data_final", dataFinal);
            intent.putExtra("categoria", categoria);
            intent.putExtra("descricao", descricao);
            startActivity(intent);

        } else {
            Toast.makeText(this, "Por favor, selecione uma categoria.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Este método é chamado quando o usuário deseja cancelar a criação de um novo curso presencial.
     * Ele retorna à tela do perfil do parceiro.
     *
     * @param view A view que acionou o método.
     */
    public void Cancelar(View view)
    {
        Intent Cancelar = new Intent(NovoCursoPresencial.this, PerfilPaceiro.class);
        startActivity(Cancelar);
    }
}