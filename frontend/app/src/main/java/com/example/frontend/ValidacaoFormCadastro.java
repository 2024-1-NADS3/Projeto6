package com.example.frontend;

import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidacaoFormCadastro {
    public static boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidPhoneNumber(CharSequence target) {
        if (target == null || target.length() < 11 || target.length() > 11) {
            return false;
        } else {
            return target.toString().matches("^\\d{2}\\d{9}$");
        }
    }

    public static boolean isValidName(String name) {
        Pattern pattern;
        Matcher matcher;
        final String Name = "^[a-zA-ZÀ-ú\\s]*$";
        pattern = Pattern.compile(Name);
        matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 4;
    }
}
