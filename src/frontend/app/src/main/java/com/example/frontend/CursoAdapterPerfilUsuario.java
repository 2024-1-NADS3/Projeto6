package com.example.frontend;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CursoAdapterPerfilUsuario extends RecyclerView.Adapter<CursoAdapterPerfilUsuario.ViewHolder> {

    private Context context;
    private List<Curso> courses;
    private List<Curso> allCursos;
    private List<Curso> filteredByDropDown;

    private boolean isFilteredByDropDownActive = false;


    /**
     * Inicializa o adaptador com o contexto e a lista de cursos.
     * Cria uma cópia da lista para filtragem.
     */
    public CursoAdapterPerfilUsuario(Context context, List<Curso> courses) {
        this.context = context;
        this.courses = courses;
        this.allCursos = new ArrayList<>(courses);
        this.filteredByDropDown = new ArrayList<>(courses);
    }


    public void filtrarCursos (String filtroSelecionado) {

        List<Curso> cursosFiltrados = new ArrayList<>();
        Date hoje = new Date();

        Log.d("Filtro selecionado", filtroSelecionado);
        Log.d("Data hoje", String.valueOf(hoje));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Curso curso : allCursos) {
            Log.d("Data atual", String.valueOf(hoje));
            Log.d("Curso data inicial", String.valueOf(curso.getInitialDate()));
            Log.d("Curso data final ", String.valueOf(curso.getEndDate()));

            try {
                Date dataInicio = sdf.parse(curso.getInitialDate());
                Date dataFim = sdf.parse(curso.getEndDate());

                Log.d("Curso data inicio transformada", String.valueOf(dataInicio));
                Log.d("Curso data final transformada", String.valueOf(dataFim));

                // Verifica se o filtro selecionado é "Todos"
                if (filtroSelecionado.equals("Cursos: Todos")) {
                    cursosFiltrados.add(curso);
                }
                else if(filtroSelecionado.equals("Cursos: Em andamento") && dataInicio.before(hoje) && dataFim.after(hoje)) {
                    cursosFiltrados.add(curso);
                }
                // Verifica se o filtro selecionado é "Não iniciados"
                else if (filtroSelecionado.equals("Cursos: Não iniciados") && dataInicio.after(hoje)) {
                    cursosFiltrados.add(curso);
                }
                // Verifica se o filtro selecionado é "Finalizados"
                else if (filtroSelecionado.equals("Cursos: Finalizados") && dataFim.before(hoje)) {
                    cursosFiltrados.add(curso);
                }
            } catch (ParseException e) {
                // Trate a exceção de forma adequada
                Log.e("CursoAdapterPerfilUsuario", "Erro ao parsear data", e);
            }
        }

        if (cursosFiltrados.isEmpty()) {
            ((PerfilUsuario) context).errorUserTextView.setVisibility(View.VISIBLE);
            ((PerfilUsuario) context).errorUserTextView.setText("Não existe cursos com os filtros aplicados");
        } else {
            ((PerfilUsuario) context).errorUserTextView.setVisibility(View.GONE);
        }

        this.courses = cursosFiltrados;
        this.filteredByDropDown = cursosFiltrados;
        this.courses = new ArrayList<>(cursosFiltrados);
        this.isFilteredByDropDownActive =!filteredByDropDown.isEmpty();
        notifyDataSetChanged();
    }


    /**
     * Cria e retorna um novo ViewHolder para cada item da lista.
     */
    @NonNull
    @Override
    public CursoAdapterPerfilUsuario.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_curso, parent, false);
        return new CursoAdapterPerfilUsuario.ViewHolder(view);
    }

    /**
     * Vincula os dados do curso ao ViewHolder, incluindo imagem, título, tipo, categoria, endereço e vagas.
     */

    @Override
    public void onBindViewHolder(@NonNull CursoAdapterPerfilUsuario.ViewHolder holder, int position) {
        Curso curso = courses.get(position);
        String imagePath = curso.getImg();
        String imageUrl = Constants.BASE_URL + imagePath;

        // Carrega a imagem do curso e preenche os campos de texto
        Picasso.get().load(imageUrl).into(holder.courseImg);
        holder.courseTitle.setText(curso.getTitle());
        holder.courseType.setText(curso.getType());
        holder.courseCategory.setText("Categoria: " + curso.getCategory());
        holder.courseAddress.setText("Local: " + curso.getAddress());
        holder.slotsAndMax.setText("Vagas ocupadas: " + curso.getOccupiedSlots() + " / " + curso.getMaxCapacity());
        holder.textView6.setText("Desinscrever-se ↓");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aqui você pode definir o que acontece quando o item é clicado
                // Por exemplo, redirecionando para outra atividade
                Intent intent = new Intent(context, PerfilUsuarioCursoExpandido.class); // Substitua SuaNovaAtividade pelo nome da sua atividade
                intent.putExtra("curso", curso);
                context.startActivity(intent);
            }
        });

    }

    /**
     * Retorna o número total de itens na lista de cursos.
     */
    @Override
    public int getItemCount() {
        return courses.size();
    }

    /**
     * Classe interna ViewHolder para armazenar referências de views.
     */
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
                List<Curso> sourceList = isFilteredByDropDownActive ? filteredByDropDown : allCursos;

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
                    ((PerfilUsuario) context).errorUserTextView.setVisibility(View.VISIBLE);
                    ((PerfilUsuario) context).errorUserTextView.setText("Não existe cursos com o nome pesquisado");
                } else {
                    // Caso contrário, esconde a mensagem de erro
                    ((PerfilUsuario) context).errorUserTextView.setVisibility(View.GONE);
                }

                notifyDataSetChanged();
            }
        };
    }

}
