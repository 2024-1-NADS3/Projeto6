package com.example.frontend;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity que exibe os detalhes de um curso na tela expandida.
 */
public class TelaCursoExpandido extends AppCompatActivity {

    ImageView courseImgExpandend;
    GerenciadorToken token;
    Button inscricaoButton;
    String instituitionName;
    Curso curso;
    TextView descriptionExpandend,
            courseTitleExpanded,
            courseSlotsAndCapacityExpanded,
            courseCategoryExpandend,
            courseAdressExpanded,
            courseZoneExpanded,
            courseTypeExpanded,
            instituitionNameExpanded,
            courseInitialDateExpanded,
            courseEndDateExpanded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_curso_expandido);
        actionBar();
        curso = (Curso) getIntent().getSerializableExtra("curso");
        pegarNomeDaInstituicao();

        token = new GerenciadorToken(this);
        inscricaoButton = findViewById(R.id.inscricaoButton);

        preencherComDado(curso);

        Log.d("zona", String.valueOf(curso.getZone()));
        Log.d("zona", String.valueOf( curso.getZone()));
        Log.d("id do curso", String.valueOf(curso.getCourseId()));
    }

    /**
     * Configura a barra de ação com um botão para voltar à tela principal.
     */
    public void actionBar() {
        TextView botaoTitulo = findViewById(R.id.titulo_CursoExpandido);
        botaoTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (TelaCursoExpandido.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Obtém o nome da instituição associada ao curso através de uma requisição GET.
     */
    void pegarNomeDaInstituicao() {
        RequestQueue mRequestQueue;
        String courseId = String.valueOf(curso.getCourseId());
        String finalURL = Constants.BASE_URL + "/parceiro/nome-instituicao?id=" + courseId;

        mRequestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            Log.d("Resposta", "onResponse: " + message);
                            instituitionName = message;
                            // Atualiza a UI aqui
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    preencherComDado(curso);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Logando o erro
                Log.d("Erro", "onErrorResponse: " + error.getMessage());
            }
        });

        mRequestQueue.add(stringRequest);
    }


    /**
     * Preenche a tela com os dados do curso, incluindo imagem, título, descrição, etc.
     */
    void preencherComDado(Curso curso) {
        instituitionNameExpanded = findViewById(R.id.instituitionNameExpanded);
        courseInitialDateExpanded = findViewById(R.id.courseInitialDateExpanded);
        courseEndDateExpanded = findViewById(R.id.courseEndDateExpanded);
        courseImgExpandend = findViewById(R.id.courseImgExpandend);
        courseTitleExpanded = findViewById(R.id.courseTitleExpanded);
        courseTypeExpanded = findViewById(R.id.courseTypeExpanded);
        courseSlotsAndCapacityExpanded = findViewById(R.id.courseSlotsAndCapacityExpanded);
        courseCategoryExpandend = findViewById(R.id.courseCategoryExpandend);
        courseAdressExpanded = findViewById(R.id.courseAdressExpanded);
        courseZoneExpanded = findViewById(R.id.courseZoneExpanded);
        descriptionExpandend = findViewById(R.id.descriptionExpandend);

        String imagePath = curso.getImg();
        String imageUrl = Constants.BASE_URL + imagePath;
        Picasso.get().load(imageUrl).into(courseImgExpandend);

        courseTitleExpanded.setText(curso.getTitle());
        instituitionNameExpanded.setText(instituitionName);
        courseInitialDateExpanded.setText(curso.getInitialDate()); // Define o texto do TextView com a data inicial formatada
        courseEndDateExpanded.setText(curso.getEndDate()); // Define o texto do TextView com a data final formatada
        courseTypeExpanded.setText(curso.getType());
        courseSlotsAndCapacityExpanded.setText(String.valueOf(curso.getOccupiedSlots()) + " / " + String.valueOf(curso.getMaxCapacity()));
        courseCategoryExpandend.setText(curso.getCategory());
        courseAdressExpanded.setText(curso.getAddress());
        if(curso.getZone().equals("null")) {
            courseZoneExpanded.setText("-------------");
        } else {
            courseZoneExpanded.setText(curso.getZone());
        }
        descriptionExpandend.setText(curso.getDescription());

        Log.d("nome do curso", "Message: " + instituitionName);
    }

    /** Método para fazer a inscrição do usuário no backend */
    public void inscricao (View view) {
        String tokenString = token.getToken();
        Log.d("Token", tokenString);
        if (tokenString.equals("")) {
            Log.d("Cai dentro do if para verificar o login", "token: " + tokenString);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Aviso");
            builder.setMessage("É necessário estar logado para fazer inscrições na plataforma");
            builder.setPositiveButton("Ir para Login", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(TelaCursoExpandido.this, FormLogin.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Ação ao clicar no botão "Cancelar"
                    dialog.cancel();
                }
            });

            // Exibição do AlertDialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return;
        }

        String urlFinalParaInscricao = Constants.BASE_URL + "/usuario/inscricao-curso" ;
        Log.d("Clicou", "Clicou");

        // Criação do RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Parâmetros da requisição
        JSONObject params = new JSONObject();
        try {
            params.put("courseId", curso.getCourseId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // JsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlFinalParaInscricao, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        new AlertDialog.Builder(TelaCursoExpandido.this)
                                .setTitle("Sucesso!")
                                .setMessage("Inscrição efetuada com sucesso! A instituição provedora do cuso entrara em contato para mais detalhes!")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = "Desculpe, ocorreu um erro. Por favor, tente novamente mais tarde.";

                if (error.networkResponse != null && error.networkResponse.statusCode != 0) {
                    if (error.networkResponse.statusCode == 400) {
                        try {
                            String errorBody = new String(error.networkResponse.data, "UTF-8");
                            JSONObject errorJson = new JSONObject(errorBody);
                            errorMessage = errorJson.getString("message");
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (error.networkResponse.statusCode == 401) {
                        errorMessage = "Sessão de login expirada. Faça o login novamente";
                        token.clearToken();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(TelaCursoExpandido.this, FormLogin.class);
                                startActivity(intent);
                            }
                        }, 1500);

                    }
                    if (error.networkResponse.statusCode == 403) {
                        try {
                            String errorBody = new String(error.networkResponse.data, "UTF-8");
                            JSONObject errorJson = new JSONObject(errorBody);
                            errorMessage = errorJson.getString("message");
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (error instanceof NetworkError) {
                    errorMessage = "Sem conexão com a internet. Por favor, verifique sua conexão.";
                } else if (error instanceof ServerError) {
                    errorMessage = "O servidor está enfrentando problemas. Por favor, tente novamente mais tarde.";
                } else if (error instanceof TimeoutError) {
                    errorMessage = "A solicitação demorou muito para ser processada. Por favor, tente novamente mais tarde.";
                }

                Log.e("Inscription error", error.toString());

                new AlertDialog.Builder(TelaCursoExpandido.this)
                        .setTitle("Erro")
                        .setMessage(errorMessage)
                        .setPositiveButton("OK", null)
                        .show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token.getToken()); // Substitua "Bearer " + token pelo seu token JWT
                return headers;
            }
        };


        // Adiciona a requisição à fila
        queue.add(jsonObjectRequest);
    }

}