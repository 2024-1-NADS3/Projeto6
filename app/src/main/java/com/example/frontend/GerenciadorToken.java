package com.example.frontend;
import android.content.Context;
import android.content.SharedPreferences;


public class GerenciadorToken {

    // é uma constante que define o nome do arquivo de preferências compartilhadas usado para armazenar o token JWT.
    private static final String PREF_NAME = "PrefsDoApp";
    private static final String TOKEN_KEY = "access_token";

    private SharedPreferences sharedPreferences;

    public GerenciadorToken(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(TOKEN_KEY, "");
    }

    public void clearToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TOKEN_KEY);
        editor.apply();
    }
}

