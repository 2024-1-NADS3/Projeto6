package com.example.frontend;

import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidacaoFormCadastro {
    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isValidPhoneNumber(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.PHONE.matcher(target).matches();
        }
    }

    public static boolean isValidName(String name) {
        Pattern pattern;
        Matcher matcher;
        final String Name = "^[a-zA-Z\\\\s]+";
        pattern = Pattern.compile(Name);
        matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 4;
    }
}
