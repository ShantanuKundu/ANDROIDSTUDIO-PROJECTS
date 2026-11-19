package com.example.assignment_4b_b;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class HomeActivity extends AppCompatActivity {

    private TextView tvUserEmail;
    private TextView tvRememberStatus;
    private MaterialButton btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvRememberStatus = findViewById(R.id.tvRememberStatus);
        btnLogout = findViewById(R.id.btnLogout);

        String email = getIntent().getStringExtra("user_email");
        boolean rememberMe = getIntent().getBooleanExtra("remember_me", false);

        if (email != null) {
            tvUserEmail.setText(getString(R.string.logged_in_as, email));
        }
        tvRememberStatus.setText(getString(R.string.remember_status, rememberMe ? "Yes" : "No"));

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}