package com.example.assignment_3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText etMessage;
    private EditText etUrl;
    private Button btnExplicitIntent;
    private Button btnOpenWeb;
    private Button btnShareText;

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

        etMessage = findViewById(R.id.etMessage);
        etUrl = findViewById(R.id.etUrl);
        btnExplicitIntent = findViewById(R.id.btnExplicitIntent);
        btnOpenWeb = findViewById(R.id.btnOpenWeb);
        btnShareText = findViewById(R.id.btnShareText);

        // 1. Explicit Intent: Start SecondActivity directly with data
        btnExplicitIntent.setOnClickListener(v -> {
            String message = etMessage.getText().toString().trim();
            if (message.isEmpty()) {
                message = "Hello from MainActivity!";
            }

            Intent explicitIntent = new Intent(MainActivity.this, SecondActivity.class);
            explicitIntent.putExtra(SecondActivity.EXTRA_MESSAGE, message);
            startActivity(explicitIntent);
        });

        // 2. Implicit Intent: Open a web page in browser
        btnOpenWeb.setOnClickListener(v -> {
            String url = etUrl.getText().toString().trim();
            if (url.isEmpty()) {
                Toast.makeText(this, "Please enter a valid URL", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }

            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(webIntent);
        });

        // 3. Implicit Intent: Share text to other apps
        btnShareText.setOnClickListener(v -> {
            String textToShare = etMessage.getText().toString().trim();
            if (textToShare.isEmpty()) {
                textToShare = "Sharing sample message via Implicit Intent!";
            }

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
            startActivity(Intent.createChooser(shareIntent, "Share text via"));
        });
    }
}
