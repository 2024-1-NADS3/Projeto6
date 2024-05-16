package com.example.frontend;

import android.os.Bundle;
import android.widget.ScrollView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.R;

public class TelaInfoApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScrollView scrollView = findViewById(R.id.scrowView);

        // Exemplo de rolar para o topo do ScrollView
        scrollView.scrollTo(0, 0);

        // Exemplo de rolar para o final do ScrollView
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
}
