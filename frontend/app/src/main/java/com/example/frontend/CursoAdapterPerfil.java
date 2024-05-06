package com.example.frontend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

public class CursoAdapterPerfil extends RecyclerView.Adapter<CursoAdapterPerfil.ViewHolder> {

    private Context context;
    private List<Curso> courses;

    private List<Curso> allCursos;

    // Inicializa o adaptador com o contexto e a lista de cursos, criando uma cópia para filtragem
    public CursoAdapterPerfil(Context context, List<Curso> courses) {
        this.context = context;
        this.courses = courses;
        this.allCursos = new ArrayList<>(courses);
    }

    // Cria e retorna um novo ViewHolder para cada item da lista
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_curso, parent, false);
        return new ViewHolder(view);
    }

    // Vincula os dados do curso ao ViewHolder, incluindo imagem, título, tipo, categoria, endereço e vagas
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Curso curso = courses.get(position);
        String imagePath = curso.getImg();
        String imageUrl = Constants.BASE_URL + imagePath;

        // Carrega a imagem do curso e preenche os campos de texto
        Picasso.get().load(imageUrl).into(holder.courseImg);
        holder.courseTitle.setText(curso.getTitle());
        holder.courseType.setText(curso.getType());
        holder.courseCategory.setText("Categoria: " + curso.getCategory());
        holder.courseAddress.setText("Endereço: " + curso.getAddress());
        holder.slotsAndMax.setText("Vagas ocupadas: " + curso.getOccupiedSlots() + " / " + curso.getMaxCapacity());
        holder.textView6.setText("Editar ↓");

        // Configura um listener de clique para abrir a tela detalhada do curso
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PefilParceiroCursoExpandido.class);
                intent.putExtra("curso", curso);
                context.startActivity(intent);
            }
        });
    }

    // Retorna o número total de itens na lista de cursos
    @Override
    public int getItemCount() {
        return courses.size();
    }

    // Classe interna ViewHolder para armazenar referências de views
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView courseImg;
        TextView courseTitle, courseType, courseCategory, courseAddress, slotsAndMax, textView6;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseImg = itemView.findViewById(R.id.courseImg);
            courseTitle = itemView.findViewById(R.id.courseTitle);
            courseType = itemView.findViewById(R.id.courseType);
            courseCategory = itemView.findViewById(R.id.courseCategory);
            courseAddress = itemView.findViewById(R.id.courseAddress);
            slotsAndMax = itemView.findViewById(R.id.slotsAndMax);
            textView6 = itemView.findViewById(R.id.textView6);
        }
    }

    public Filter getSearchBarFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Curso> filteredList = new ArrayList<>();
                List<Curso> sourceList = allCursos;

                if (constraint == null || constraint.length() == 0) {
                    results.values = sourceList; // Retorna todos os cursos se a busca estiver vazia
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Curso curso : sourceList) {
                        if (curso.getTitle().toLowerCase().contains(filterPattern)) {
                            filteredList.add(curso);
                        }
                    }
                    results.values = filteredList;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                courses.clear();
                courses.addAll((List) results.values);

                if (courses.isEmpty()) {
                    // Se a lista estiver vazia, exibe a mensagem de erro
                    ((PerfilPaceiro) context).errorPartnerTextView.setVisibility(View.VISIBLE);
                    ((PerfilPaceiro) context).errorPartnerTextView.setText("Não existe cursos com o nome pesquisado");
                } else {
                    // Caso contrário, esconde a mensagem de erro
                    ((PerfilPaceiro) context).errorPartnerTextView.setVisibility(View.GONE);
                }

                notifyDataSetChanged();
            }
        };
    }
}