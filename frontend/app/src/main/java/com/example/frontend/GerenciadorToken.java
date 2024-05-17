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

    /**
     * Método para salvar o token JWT nas preferências compartilhadas.
     */
    public void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }

    /**
     * Método para recuperar o token JWT das preferências compartilhadas.
     */
    public String getToken() {
        return sharedPreferences.getString(TOKEN_KEY, "");
    }

    /**
     * Método para limpar o token JWT das preferências compartilhadas.
     * Isso é útil para deslogar o usuário ou limpar dados sensíveis.
     */
    public void clearToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TOKEN_KEY);
        editor.apply();
    }
}

