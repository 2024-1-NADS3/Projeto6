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

public class InstituicaoParceira implements Serializable {
    private static final String URL_CRIAR_PARCEIRO = "https://dzlj63-4550.csb.app/parceiro/cadastrar";
    private Context mContext;
    protected String instituitionName;
    protected String cnpj;
    protected String email;
    protected String cellnumber;
    protected String password;

    public String getInstituitionName() {
        return instituitionName;
    }

    public String getCnpj() {
        return cnpj;
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

    public void setInstituitionName(String instituitionName) {
        this.instituitionName = instituitionName;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
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
        return "Nome da instituição: " + instituitionName + ", CNPJ: " + cnpj + ", Email: " + email + ", Número: " + cellnumber + ", Senha: " + password;
    }
    public void criarParceiroNoServidor(InstituicaoParceira parceiro, final VolleyCallback callback) {

        // Criar um objeto JSON com os dados do usuário
        JSONObject jsonUsuario = new JSONObject();
        try {
            jsonUsuario.put("instituitionName", parceiro.getInstituitionName());
            jsonUsuario.put("cnpj", parceiro.getCnpj());
            jsonUsuario.put("email", parceiro.getEmail());
            jsonUsuario.put("cellnumber", parceiro.getCellnumber());
            jsonUsuario.put("password", parceiro.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Enviar a requisição POST usando Volley
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_CRIAR_PARCEIRO, jsonUsuario,
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

    public InstituicaoParceira(String nome_instituicao, String cnpj, String email, String numero, String senha, Context context) {
        this.instituitionName = nome_instituicao;
        this.cnpj = cnpj;
        this.email = email;
        this.cellnumber = numero;
        this.password = senha;
        this.mContext = context;
    }
}
