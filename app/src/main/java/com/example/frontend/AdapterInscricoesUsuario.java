package com.example.frontend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador personalizado para RecyclerView que exibe uma lista de cursos em um perfil.
 *
 */
public class AdapterInscricoesUsuario extends RecyclerView.Adapter<AdapterInscricoesUsuario.ViewHolder> {
    private Context context;

    private List<Usuario> users;

    /**
     * Inicializa o adaptador com o contexto e a lista de cursos.
     * Cria uma cópia da lista para filtragem.
     */
    public AdapterInscricoesUsuario(Context context, List<Usuario> users) {
        this.context = context;
        this.users = users;
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
        Log.d("Adapter", user.getName());

        holder.userName.setText("Nome: " + user.getName());
        holder.userEmail.setText("Email: " +user.getEmail());
        holder.userCellphone.setText("Celular: " + user.getCellnumber());


//        holder.itemView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, PefilParceiroCursoExpandido.class);
//                intent.putExtra("curso", curso);
//                context.startActivity(intent);
//            }
//        });
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
}
