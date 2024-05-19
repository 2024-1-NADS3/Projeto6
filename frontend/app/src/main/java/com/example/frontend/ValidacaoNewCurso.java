package com.example.frontend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ValidacaoNewCurso {

    // Método para validar o limite de caracteres para o nome
    public static boolean validarLimiteNome(String nome, int limite) {
        return nome.length() <= limite;
    }

    // Método para validar o limite de caracteres para a descrição
    public static boolean validarLimiteDescricao(String descricao, int limite) {
        return descricao.length() <= limite;
    }

    // Método para converter uma string em uma data
    public static Date parseData(String data, String formato) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        return sdf.parse(data);
    }

    public static boolean compararData(String dataInicial, String dataFinal, String formato) throws ParseException {
        Date data1 = parseData(dataInicial, formato);
        Date data2 = parseData(dataFinal, formato);
        return data1.after(data2);
    }

    // Método para validar se a data é válida
    public static boolean validarData(String data, String formato) {
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        sdf.setLenient(false); // Define para não aceitar datas inválidas
        try {
            sdf.parse(data);
            return true; // Se não lançar exceção, a data é válida
        } catch (ParseException e) {
            return false; // Se lançar exceção, a data é inválida
        }
    }

    // Método para verificar se a data é maior que o dia de hoje
    public static boolean dataMaiorQueHoje(String dataString, String formato) throws ParseException {
        Date data = parseData(dataString, formato);
        Date hoje = new Date(); // Obtém a data de hoje
        return data.after(hoje); // Verifica se a data é depois de hoje
    }

    public static boolean validarDataFormatoCorreto(String data) {
        // Expressão regular para validar o formato dd/MM/yyyy
        String regex = "^\\d{2}/\\d{2}/\\d{4}$"; // Adicionando barra adicional para escapar as barras
        if (!data.matches(regex)) {
            return false; // Data não está no formato correto
        }

        // Validação adicional com SimpleDateFormat para verificar a validade da data
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false); // Não aceita datas inválidas
        try {
            sdf.parse(data);
            return true; // Data está no formato correto e é válida
        } catch (ParseException e) {
            return false; // Data não é válida
        }
    }
}
