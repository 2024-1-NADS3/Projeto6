package com.example.frontend;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class TelaCursoExpandido extends AppCompatActivity {

    ImageView courseImgExpandend;

    TextView descriptionExpandend,
            courseTitleExpanded,
            courseSlotsAndCapacityExpanded,
            courseCategoryExpandend,
            courseAdressExpanded,
            courseZoneExpanded,
            courseTypeExpanded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_curso_expandido);

        Curso curso = (Curso) getIntent().getSerializableExtra("curso");

        filledWithTheData(curso);
    }


    @SuppressLint("SetTextI18n")
    void filledWithTheData(Curso curso) {
        courseImgExpandend = findViewById(R.id.courseImgExpandend);
        courseTitleExpanded = findViewById(R.id.courseTitleExpanded);
        courseTypeExpanded = findViewById(R.id.courseTypeExpanded);

        courseSlotsAndCapacityExpanded = findViewById(R.id.courseSlotsAndCapacityExpanded);


        courseCategoryExpandend = findViewById(R.id.courseCategoryExpandend);
        courseAdressExpanded = findViewById(R.id.courseAdressExpanded);
        courseZoneExpanded = findViewById(R.id.courseZoneExpanded);
        descriptionExpandend = findViewById(R.id.descriptionExpandend);

        // Supondo que você tenha uma variável 'curso' que contém os dados do curso
        // Você deve configurar os elementos da UI com os dados do curso aqui
        // Por exemplo:

        String baseUrl = "https://l8d9q4-4550.csb.app";
        String imagePath = curso.getImg();
        String imageUrl = baseUrl + imagePath;
        Picasso.get().load(imageUrl).into(courseImgExpandend);

        courseTitleExpanded.setText(curso.getTitle());
        courseTypeExpanded.setText(curso.getType());

        courseSlotsAndCapacityExpanded.setText(String.valueOf(curso.getOccupiedSlots()) + " / " + String.valueOf(curso.getMaxCapacity()));

        courseCategoryExpandend.setText(curso.getCategory());
        courseAdressExpanded.setText(curso.getAddress());
        courseZoneExpanded.setText(curso.getZone());
        descriptionExpandend.setText(curso.getDescription());

        System.out.println(curso.getId());
    }

}