package com.example.frontend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class TelaCursoExpandido extends AppCompatActivity {

    ImageView courseImgExpandend;

    GerenciadorToken token;
    Button inscricaoButton;

    String urlBase, instituitionName;
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
        urlBase = "http://192.168.0.10:4550";
        curso = (Curso) getIntent().getSerializableExtra("curso");
        pegarNomeDaInstituicao();

        token = new GerenciadorToken(this);
        String tokenString = token.getToken();
        inscricaoButton = findViewById(R.id.inscricaoButton);

        if (!tokenString.equals("")) {
            // Se houver um token, habilita o botão e define a cor de fundo para verde
            inscricaoButton.setEnabled(true);
            inscricaoButton.setBackgroundColor(Color.parseColor("#759E54"));
        } else {
            // Se não houver token, desabilita o botão e define a cor de fundo para cinza
            inscricaoButton.setEnabled(false);
            inscricaoButton.setBackgroundColor(Color.GRAY);
        }

        preencherComDado(curso);

        Log.d("id do curso", String.valueOf(curso.getCourseid()));
    }

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
    void pegarNomeDaInstituicao() {
        RequestQueue mRequestQueue;
        String courseId = String.valueOf(curso.getCourseid());
        String finalURL = urlBase + "/parceiro/nomeInstituicao?id=" + courseId;

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
    @SuppressLint("SetTextI18n")
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
        String imageUrl = urlBase + imagePath;
        Picasso.get().load(imageUrl).into(courseImgExpandend);

        courseTitleExpanded.setText(curso.getTitle());
        instituitionNameExpanded.setText(instituitionName);
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yy"); // Formato da data original
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yy"); // Formato desejado

            // Formatação da data inicial
            Date initialDate = inputFormat.parse(curso.getInitialDate()); // Converte a string de data para um objeto Date
            String formattedInitialDate = outputFormat.format(initialDate); // Formata o objeto Date para a string desejada
            courseInitialDateExpanded.setText(formattedInitialDate); // Define o texto do TextView com a data inicial formatada

            // Formatação da data final
            Date endDate = inputFormat.parse(curso.getEndDate()); // Converte a string de data para um objeto Date
            String formattedEndDate = outputFormat.format(endDate); // Formata o objeto Date para a string desejada
            courseEndDateExpanded.setText(formattedEndDate); // Define o texto do TextView com a data final formatada
        } catch (ParseException e) {
            e.printStackTrace();
            // Trate o erro conforme necessário
        }
        courseTypeExpanded.setText(curso.getType());
        courseSlotsAndCapacityExpanded.setText(String.valueOf(curso.getOccupiedSlots()) + " / " + String.valueOf(curso.getMaxCapacity()));
        courseCategoryExpandend.setText(curso.getCategory());
        courseAdressExpanded.setText(curso.getAddress());
        courseZoneExpanded.setText(curso.getZone());
        descriptionExpandend.setText(curso.getDescription());

        Log.d("nome do curso", "Message: " + instituitionName);
    }

    // Terminar dps
    public void inscreverse (View view) {
        String urlFinalParaLogar = urlBase + "/inscriverseCurso" ;
        Log.d("Clicou", "Clicou");
//
//        // FALTA COMEÇAR ESSA AQUI
//
//        // Criação do RequestQueue
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        // Parâmetros da requisição
//        JSONObject params = new JSONObject();
//        try {
//            params.put("cursoId", curso.getId());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        // JsonObjectRequest
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlFinalParaLogar, params,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
////                // Trata o erro
////                String errorMessage = "Desculpe, ocorreu um erro. Por favor, tente novamente mais tarde.";
////
////                if (error.networkResponse != null && error.networkResponse.statusCode != 0) {
////                    if (error.networkResponse.statusCode == 401) {
////                        try {
////                            String errorBody = new String(error.networkResponse.data, "UTF-8");
////                            JSONObject errorJson = new JSONObject(errorBody);
////                            errorMessage = errorJson.getString("error");
////                        } catch (UnsupportedEncodingException | JSONException e) {
////                            e.printStackTrace();
////                        }
////                    }
////                } else if (error instanceof NetworkError) {
////                    errorMessage = "Sem conexão com a internet. Por favor, verifique sua conexão.";
////                } else if (error instanceof ServerError) {
////                    errorMessage = "O servidor está enfrentando problemas. Por favor, tente novamente mais tarde.";
////                } else if (error instanceof TimeoutError) {
////                    errorMessage = "A solicitação demorou muito para ser processada. Por favor, tente novamente mais tarde.";
////                }
////                Log.e("LoginError", error.toString());
////
////                // Exibe o AlertDialog com a mensagem de erro
//
//            }
//        });
//
//        // Adiciona a requisição à fila
//        queue.add(jsonObjectRequest);
    }

}