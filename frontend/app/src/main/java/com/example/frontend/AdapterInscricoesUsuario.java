package com.example.frontend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adaptador personalizado para RecyclerView que exibe uma lista de cursos em um perfil.
 *
 */
public class AdapterInscricoesUsuario extends RecyclerView.Adapter<AdapterInscricoesUsuario.ViewHolder> {
    private Context context;
    private int courseId;
    private List<Usuario> users;
    GerenciadorToken token;


    /**
     * Inicializa o adaptador com o contexto e a lista de cursos.
     * Cria uma cópia da lista para filtragem.
     */
    public AdapterInscricoesUsuario(Context context, List<Usuario> users, int courseId, GerenciadorToken token) {
        this.courseId = courseId;
        this.context = context;
        this.users = users;
        this.token = token;
    }

    /**
     * Cria e retorna um novo ViewHolder para cada item da lista.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_usuario_inscrito, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Vincula os dados do curso ao ViewHolder, incluindo imagem, título, tipo, categoria, endereço e vagas.
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("users", String.valueOf(getItemCount()));

        Usuario user = users.get(position);
        Log.d("Adapter", String.valueOf(user.getUserId()));

        holder.userName.setText("Nome: " + user.getName());
        holder.userEmail.setText("Email: " +user.getEmail());
        holder.userCellphone.setText("Celular: " + user.getCellnumber());

        holder.closeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int userId = user.getUserId();
                // Mostra o diálogo de confirmação
                new AlertDialog.Builder(context)
                        .setTitle("Confirmação")
                        .setMessage("Você realmente deseja deletar a inscrição deste usuário?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Se o usuário confirmar, chama a função de deletar
                                deletarUsuarioDoCurso(userId, position);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });



    }

    /**
     * Retorna o número total de itens na lista de cursos.
     */
    @Override
    public int getItemCount() {
        return users.size();
    }

    /**
     * Classe interna ViewHolder para armazenar referências de views.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView closeButton;
        TextView userName, userEmail, userCellphone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            closeButton = itemView.findViewById(R.id.closeButton);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
            userCellphone = itemView.findViewById(R.id.userCellphone);

        }
    }

    public void deletarUsuarioDoCurso(int userId, int position) {
        String deleteUserUrl = Constants.BASE_URL + "/parceiro/tirar-inscricao-do-usuario/curso/" + courseId + "/usuario/" + userId;

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, deleteUserUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Supondo que a requisição seja bem-sucedida, remova o item da lista
                        users.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Conta deletada com sucesso.", Toast.LENGTH_SHORT).show();

                        if (users.isEmpty()) {
                            // Se a lista estiver vazia, exibe a mensagem de erro
                            ((PefilParceiroCursoExpandido) context).errorPartnerExpandidoTextView.setVisibility(View.VISIBLE);
                            ((PefilParceiroCursoExpandido) context).errorPartnerExpandidoTextView.setText("Não há usuários cadastrados!");
                        } else {
                            // Caso contrário, esconde a mensagem de erro
                            ((PefilParceiroCursoExpandido) context).errorPartnerExpandidoTextView.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Trate o erro aqui
                Toast.makeText(context, "Erro ao deletar conta.", Toast.LENGTH_SHORT).show();
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
