package com.example.assignment1_a;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultActivity extends AppCompatActivity {

    private TextView tvSummary;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvSummary = findViewById(R.id.tvSummary);
        btnBack = findViewById(R.id.btnBack);

        // Receive data from Intent
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("MOVIE_TITLE");
            float rating = intent.getFloatExtra("MOVIE_RATING", 0.0f);
            boolean recommended = intent.getBooleanExtra("IS_RECOMMENDED", false);
            String genre = intent.getStringExtra("MOVIE_GENRE");

            // Format summary
            String summary = "Movie Title: " + title + "\n\n" +
                    "Rating: " + rating + " Stars\n\n" +
                    "Genre: " + genre + "\n\n" +
                    "Recommend: " + (recommended ? "Yes" : "No");

            tvSummary.setText(summary);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to MainActivity
            }
        });
    }
}