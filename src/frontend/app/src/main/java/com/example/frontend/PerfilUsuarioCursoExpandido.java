package com.example.frontend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity que exibe os detalhes de um curso para o usuário.
 */
public class PerfilUsuarioCursoExpandido extends AppCompatActivity {

    ImageView courseImgExpandedUser;
    GerenciadorToken token;
    String instituitionNameUser;
    Curso curso;
    TextView descriptionExpandendUser,
            courseTitleExpandedUser,
            courseSlotsAndCapacityExpandedUser,
            courseCategoryExpandendUser,
            courseAdressExpandedUser,
            courseZoneExpandedUser,
            courseTypeExpandedUser,
            instituitionNameExpandedUser,
            courseInitialDateExpandedUser,
            courseEndDateExpandedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario_curso_expandido);
        actionBar();

        token = new GerenciadorToken(this);

        curso = (Curso) getIntent().getSerializableExtra("curso");

        pegarNomeDaInstituicao();

        Log.d("id do curso", String.valueOf(curso.getCourseId()));

        preencherComDado(curso);
    }

    /**
     * Configura a barra de ação com um botão para voltar à tela principal.
     */
    public void actionBar() {
        TextView botaoTitulo = findViewById(R.id.titulo_CursoExpandidoUsuario);
        botaoTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilUsuarioCursoExpandido.this, MainActivity.class);
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
                            instituitionNameUser = message;
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

            instituitionNameExpandedUser = findViewById(R.id.instituitionNameExpandedUser);
            courseInitialDateExpandedUser = findViewById(R.id.courseInitialDateExpandedUser);
            courseEndDateExpandedUser = findViewById(R.id.courseEndDateExpandedUser);
            courseImgExpandedUser = findViewById(R.id.courseImgExpandedUser);
            courseTitleExpandedUser = findViewById(R.id.courseTitleExpandedUser);
            courseTypeExpandedUser = findViewById(R.id.courseTypeExpandedUser);
            courseSlotsAndCapacityExpandedUser = findViewById(R.id.courseSlotsAndCapacityExpandedUser);
            courseCategoryExpandendUser = findViewById(R.id.courseCategoryExpandendUser);
            courseAdressExpandedUser = findViewById(R.id.courseAdressExpandedUser);
            courseZoneExpandedUser = findViewById(R.id.courseZoneExpandedUser);
            descriptionExpandendUser = findViewById(R.id.descriptionExpandendUser);

            String imagePath = curso.getImg();
            String imageUrl = Constants.BASE_URL + imagePath;
            Picasso.get().load(imageUrl).into(courseImgExpandedUser);

            courseTitleExpandedUser.setText(curso.getTitle());
            instituitionNameExpandedUser.setText(instituitionNameUser);
            courseInitialDateExpandedUser.setText(curso.getInitialDate()); // Define o texto do TextView com a data inicial formatada
            courseEndDateExpandedUser.setText(curso.getEndDate()); // Define o texto do TextView com a data final formatada
            courseTypeExpandedUser.setText(curso.getType());
            courseSlotsAndCapacityExpandedUser.setText(String.valueOf(curso.getOccupiedSlots()) + " / " + String.valueOf(curso.getMaxCapacity()));
            courseCategoryExpandendUser.setText(curso.getCategory());
            courseAdressExpandedUser.setText(curso.getAddress());
            if(curso.getZone().equals("null")) {
                courseZoneExpandedUser.setText("-------------");
            } else {
                courseZoneExpandedUser.setText(curso.getZone());
            }
            descriptionExpandendUser.setText(curso.getDescription());

            Log.d("nome do curso", "Message: " + instituitionNameUser);
    }


    /** Método para fazer a inscrição do usuário no backend */
    public void cancelarInscricao(View view) {
        String deleteUrl = Constants.BASE_URL + "/usuario/anular-inscricao/curso/" + curso.getCourseId();

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, deleteUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Trate a resposta JSON aqui
                        Log.d("Na resposta do desinscrição", response.toString());
                        Intent voltarTelaUsuario = new Intent(PerfilUsuarioCursoExpandido.this, PerfilUsuario.class);
                        startActivity(voltarTelaUsuario);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Trate o erro aqui
                Log.d("Na resposta do desinscrição", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token.getToken()); // Substitua "Bearer " + token pelo seu token JWT
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }

}