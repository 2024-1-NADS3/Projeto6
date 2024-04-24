package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Filtros extends AppCompatActivity {

    // Inicializa variáveis
    RadioGroup radioGroup_categories1,
            radioGroup_categories2,
            radioGroup_types,
            radioGroup_zones1,
            radioGroup_zones2;
    boolean isProgrammaticCategoriesCheck = false;
    boolean isProgrammaticZoneCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inicializa a Activity, define o layout e configura os componentes da UI.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

        // Configura os RadioGroups para as categorias, tipos e zonas.
        radioGroup_types = findViewById(R.id.radioGroup_types);
        radioGroup_zones1 = findViewById(R.id.radioGroup_zones1);
        radioGroup_zones2 = findViewById(R.id.radioGroup_zones2);
        radioGroup_categories1 = findViewById(R.id.radioGroup_categories1);
        radioGroup_categories2 = findViewById(R.id.radioGroup_categories2);

        // Configura os listeners para os RadioGroups de categorias e zonas para garantir que apenas um RadioButton possa ser selecionado em cada grupo.
        radioGroup_categories1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (!isProgrammaticCategoriesCheck && checkedId != radioGroup_categories2.getCheckedRadioButtonId()) {
                    isProgrammaticCategoriesCheck = true;
                    radioGroup_categories2.clearCheck();
                    isProgrammaticCategoriesCheck = false;
                }
            }
        });
        radioGroup_categories2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (!isProgrammaticCategoriesCheck && checkedId != radioGroup_categories1.getCheckedRadioButtonId()) {
                    isProgrammaticCategoriesCheck = true;
                    radioGroup_categories1.clearCheck();
                    isProgrammaticCategoriesCheck = false;
                }
            }
        });
        radioGroup_zones1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (!isProgrammaticZoneCheck && checkedId != radioGroup_zones2.getCheckedRadioButtonId()) {
                    isProgrammaticZoneCheck = true;
                    radioGroup_zones2.clearCheck();
                    isProgrammaticZoneCheck = false;
                }
            }
        });
        radioGroup_zones2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (!isProgrammaticZoneCheck && checkedId != radioGroup_zones1.getCheckedRadioButtonId()) {
                    isProgrammaticZoneCheck = true;
                    radioGroup_zones1.clearCheck();
                    isProgrammaticZoneCheck = false;
                }
            }
        });
    }

    // Método para aplicar os filtros selecionados e iniciar a Activity de cursos com os filtros aplicados.
    public void onFilter(View view) {
        // Obtém os IDs dos RadioButtons selecionados para as categorias, tipos e zonas.
        int selectedRadioButtonCategoriesId1 = radioGroup_categories1.getCheckedRadioButtonId();
        int selectedRadioButtonCategoriesId2 = radioGroup_categories2.getCheckedRadioButtonId();
        int selectedRadioButtonTypesId = radioGroup_types.getCheckedRadioButtonId();
        int selectedRadioButtonZoneId1 = radioGroup_zones1.getCheckedRadioButtonId();
        int selectedRadioButtonZoneId2 = radioGroup_zones2.getCheckedRadioButtonId();

        String categorySelectedOptionTxt = "";
        String typeSelectedOptionTxt = "";
        String zoneSelectedOptionTxt = "";

        // Obtém os textos dos RadioButtons selecionados para as categorias, tipos e zonas.
        if (selectedRadioButtonCategoriesId1 != -1) {
            RadioButton categorySelectedOption1 = findViewById(selectedRadioButtonCategoriesId1);
            categorySelectedOptionTxt = categorySelectedOption1.getText().toString().trim();
        } else if (selectedRadioButtonCategoriesId2 != -1) {
            RadioButton categorySelectedOption2 = findViewById(selectedRadioButtonCategoriesId2);
            categorySelectedOptionTxt = categorySelectedOption2.getText().toString().trim();
        }
        if (selectedRadioButtonTypesId != -1) {
            RadioButton typeSelectedOption = findViewById(selectedRadioButtonTypesId);
            typeSelectedOptionTxt = typeSelectedOption.getText().toString().trim();
        }
        if (selectedRadioButtonZoneId1 != -1) {
            RadioButton zoneSelectedOption1 = findViewById(selectedRadioButtonZoneId1);
            zoneSelectedOptionTxt = zoneSelectedOption1.getText().toString().trim();
        } else if (selectedRadioButtonZoneId2 != -1) {
            RadioButton zoneSelectedOption2 = findViewById(selectedRadioButtonZoneId2);
            zoneSelectedOptionTxt = zoneSelectedOption2.getText().toString().trim();
        }

        // Cria uma Intent para iniciar a Activity de cursos com os filtros aplicados.
        Intent intent = new Intent(Filtros.this, MainActivity.class);
        intent.putExtra("categorySelectedOptionTxt", categorySelectedOptionTxt);
        intent.putExtra("typeSelectedOptionTxt", typeSelectedOptionTxt);
        intent.putExtra("zoneSelectedOptionTxt", zoneSelectedOptionTxt);

        startActivity(intent); // Inicia a Activity de cursos com os filtros aplicados.
    }

    // Método para limpar todos os filtros selecionados.
    public void clearFilters(View view) {
        // Limpa a seleção de todos os RadioGroups.
        radioGroup_types.clearCheck();
        radioGroup_zones1.clearCheck();
        radioGroup_zones2.clearCheck();
        radioGroup_categories1.clearCheck();
        radioGroup_categories2.clearCheck();
    }
}