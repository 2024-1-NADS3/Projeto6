package com.example.frontend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Esta classe fornece métodos estáticos para validação de dados relacionados a cursos.
 * Inclui métodos para validar o limite de caracteres em nomes e descrições, converter strings em objetos Date,
 * comparar datas, validar o formato de datas e verificar se uma categoria selecionada está presente na lista de categorias válidas.
 */
public class ValidacaoNewCurso {

    /**
     * Verifica se o nome possui um número de caracteres igual ou inferior ao limite especificado.
     *
     * @param nome O nome a ser verificado.
     * @param limite O limite máximo de caracteres permitido para o nome.
     * @return true se o nome tiver um número de caracteres igual ou inferior ao limite, caso contrário, false.
     */
    public static boolean validarLimiteNome(String nome, int limite) {
        return nome.length() <= limite;
    }

    /**
     * Verifica se a descrição possui um número de caracteres igual ou inferior ao limite especificado.
     *
     * @param descricao A descrição a ser verificada.
     * @param limite O limite máximo de caracteres permitido para a descrição.
     * @return true se a descrição tiver um número de caracteres igual ou inferior ao limite, caso contrário, false.
     */
    public static boolean validarLimiteDescricao(String descricao, int limite) {
        return descricao.length() <= limite;
    }

    /**
     * Converte uma string em um objeto Date de acordo com o formato especificado.
     *
     * @param data A string que representa a data a ser convertida.
     * @param formato O formato da data.
     * @return Um objeto Date representando a data convertida.
     * @throws ParseException Se ocorrer um erro durante o parsing da string para data.
     */
    public static Date parseData(String data, String formato) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        return sdf.parse(data);
    }

    /**
     * Compara duas datas para verificar se a data inicial é posterior à data final.
     *
     * @param dataInicial A primeira data a ser comparada, representada como uma string.
     * @param dataFinal A segunda data a ser comparada, representada como uma string.
     * @param formato O formato das datas.
     * @return true se a data inicial for posterior à data final, caso contrário, false.
     * @throws ParseException Se ocorrer um erro ao fazer o parsing das datas.
     */

    public static boolean compararData(String dataInicial, String dataFinal, String formato) throws ParseException {
        Date data1 = parseData(dataInicial, formato);
        Date data2 = parseData(dataFinal, formato);
        return data1.after(data2);
    }

    /**
     * Valida se a data fornecida está no formato especificado.
     *
     * @param data A data a ser validada, representada como uma string.
     * @param formato O formato da data a ser validada.
     * @return true se a data estiver no formato correto, caso contrário, false.
     */
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

    /**
     * Verifica se a data é posterior à data de hoje.
     *
     * @param dataString A data a ser verificada, representada como uma string.
     * @param formato O formato da data.
     * @return true se a data for posterior à data de hoje, caso contrário, false.
     * @throws ParseException Se ocorrer um erro ao fazer o parsing da data.
     */
    public static boolean dataMaiorQueHoje(String dataString, String formato) throws ParseException {
        Date data = parseData(dataString, formato);
        Date hoje = new Date(); // Obtém a data de hoje
        return data.after(hoje); // Verifica se a data é depois de hoje
    }

    /**
     * Verifica se a data está no formato correto (dd/MM/yyyy).
     *
     * @param data A data a ser verificada.
     * @return true se a data estiver no formato correto, caso contrário, false.
     */
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

    /**
     * Verifica se a categoria selecionada está presente na lista de categorias válidas.
     *
     * @param categoria         A categoria selecionada a ser verificada.
     * @param categoriasValidas A lista de categorias válidas.
     * @return true se a categoria selecionada estiver na lista de categorias válidas, caso contrário, false.
     */
    public static boolean validarCategoriaSelecionada(String categoria, List<String> categoriasValidas) {
        return categoriasValidas.contains(categoria);
    }
}
