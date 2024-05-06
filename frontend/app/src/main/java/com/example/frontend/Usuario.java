package com.example.frontend;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Usuario {
    private static final String URL_CRIAR_USUARIO = Constants.BASE_URL + "/usuario/cadastrar";
    private Context mContext;

    protected String name;
    protected String email;
    protected String cellnumber;
    protected String password;

    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getCellnumber() {
        return cellnumber;
    }
    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCellnumber(String cellnumber) {
        this.cellnumber = cellnumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String obterDadosAsString() {
        return "Nome: " + name + ", Email: " + email + ", Número: " + cellnumber + ", Senha: " + password;
    }
    public void criarUsuarioNoServidor(Usuario usuario, final VolleyCallback callback) {

        // Criar um objeto JSON com os dados do usuário
        JSONObject jsonUsuario = new JSONObject();
        try {
            jsonUsuario.put("name", usuario.getName());
            jsonUsuario.put("email", usuario.getEmail());
            jsonUsuario.put("cellnumber", usuario.getCellnumber());
            jsonUsuario.put("password", usuario.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Enviar a requisição POST usando Volley
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_CRIAR_USUARIO, jsonUsuario,
                new Response.Listener<JSONObject>() {

                    public void onResponse(JSONObject response) {
                        // Resposta recebida do servidor
                        if (callback != null) {
                            callback.onSuccess(response);
                        }
                    }
                },
                new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error) {
                        // Tratar erro
                        if (callback != null) {
                            callback.onError(error);
                        }
                    }
                });

        // Adicionar a requisição à fila do Volley
        Volley.newRequestQueue(mContext).add(request);
    }

    public  Usuario(String name, String email, String cellnumber, String password, Context context) { // Adicionando Context ao construtor
        this.name = name;
        this.email = email;
        this.cellnumber = cellnumber;
        this.password = password;
        this.mContext = context; // Atribuindo o contexto à variável mContext
    }
}
