package com.example.frontend;

import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidacaoFormCadastro {
    /**
     * Verifica se o valor fornecido é um endereço de email válido.
     * @return true se o valor é um email válido, false caso contrário.
     */
    public static boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    /**
     * Verifica se o valor fornecido é um número de telefone válido.
     * @return true se o valor é um número de telefone válido, false caso contrário.
     */
    public static boolean isValidPhoneNumber(CharSequence target) {
        if (target == null || target.length() < 11 || target.length() > 11) {
            return false;
        } else {
            // Verifica se o número de telefone começa com dois dígitos seguidos por nove dígitos.
            return target.toString().matches("^\\d{2}\\d{9}$");
        }
    }

    /**
     * Verifica se o valor fornecido é um nome válido.
     * @return true se o valor é um nome válido, false caso contrário.
     */
    public static boolean isValidName(String name) {
        Pattern pattern;
        Matcher matcher;
        final String Name = "^[a-zA-ZÀ-ú\\s]*$"; // Expressão regular para validar nomes que podem conter letras maiúsculas, minúsculas, espaços e caracteres especiais de acentuação.
        pattern = Pattern.compile(Name);
        matcher = pattern.matcher(name);
        return matcher.matches();
    }

    /**
     * Verifica se o valor fornecido é uma senha válida.
     * @return true se o valor é uma senha válida (não nula e com pelo menos 8 caracteres), false caso contrário.
     */
    public static boolean isValidPassword(String password) {
        return password!= null && password.length() >= 8;
    }
}