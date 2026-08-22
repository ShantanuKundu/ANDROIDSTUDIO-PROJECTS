package com.example.assignment1_a;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText etMovieTitle;
    private RatingBar rbMovieRating;
    private CheckBox cbRecommend;
    private RadioGroup rgGenre;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        etMovieTitle = findViewById(R.id.etMovieTitle);
        rbMovieRating = findViewById(R.id.rbMovieRating);
        cbRecommend = findViewById(R.id.cbRecommend);
        rgGenre = findViewById(R.id.rgGenre);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview();
            }
        });
    }

    private void submitReview() {
        String title = etMovieTitle.getText().toString().trim();
        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a movie title", Toast.LENGTH_SHORT).show();
            return;
        }

        float rating = rbMovieRating.getRating();
        boolean isRecommended = cbRecommend.isChecked();

        int selectedGenreId = rgGenre.getCheckedRadioButtonId();
        String genre = "Not specified";
        if (selectedGenreId != -1) {
            RadioButton rbSelected = findViewById(selectedGenreId);
            genre = rbSelected.getText().toString();
        }

        // Create intent and pass data
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra("MOVIE_TITLE", title);
        intent.putExtra("MOVIE_RATING", rating);
        intent.putExtra("IS_RECOMMENDED", isRecommended);
        intent.putExtra("MOVIE_GENRE", genre);
        
        startActivity(intent);
    }
}