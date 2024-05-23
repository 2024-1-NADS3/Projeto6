package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Activity para exibir informações e voltar à tela inicial.
 */
public class TelaInfo extends AppCompatActivity {

    private ScrollView scrollView;

    private ImageView voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_info);

        VoltarInicio();

        scrollView = findViewById(R.id.scrollView);
        ImageView scrolltop = findViewById(R.id.scrolltop);

        scrolltop.setOnClickListener(v -> scrollView.smoothScrollTo(0, 0));

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent voltarInicio = new Intent(TelaInfo.this,TelaInicio.class);
                startActivity(voltarInicio  );
            }
        });
    }

    private void VoltarInicio() {voltar = findViewById(R.id.voltar);}
}