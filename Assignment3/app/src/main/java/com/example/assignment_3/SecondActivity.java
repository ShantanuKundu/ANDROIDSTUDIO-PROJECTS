package com.example.assignment_3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.assignment_3.EXTRA_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.second_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tvReceivedMessage = findViewById(R.id.tvReceivedMessage);
        Button btnBack = findViewById(R.id.btnBack);

        // Retrieve data passed via Explicit Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_MESSAGE)) {
            String message = intent.getStringExtra(EXTRA_MESSAGE);
            tvReceivedMessage.setText(message);
        } else {
            tvReceivedMessage.setText("No message received.");
        }

        btnBack.setOnClickListener(v -> finish());
    }
}
