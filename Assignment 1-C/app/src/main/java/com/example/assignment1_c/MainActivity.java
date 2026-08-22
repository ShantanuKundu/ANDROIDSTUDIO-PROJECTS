package com.example.assignment1_c;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private int wrongAttempts = 0;

    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "admin123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        if (savedInstanceState != null) {
            wrongAttempts = savedInstanceState.getInt("wrongAttempts", 0);
            if (wrongAttempts >= 3) {
                btnLogin.setEnabled(false);
            }
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showDialog("Error", "Please enter both username and password.");
            return;
        }

        if (username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)) {
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
            finish();
        } else {
            wrongAttempts++;
            if (wrongAttempts >= 3) {
                btnLogin.setEnabled(false);
                showDialog("Login Failed", "Invalid username or password. Attempt " + wrongAttempts + " of 3.\n\nYou have exceeded the maximum number of login attempts (3). The login button has been disabled.");
            } else {
                showDialog("Login Failed", "Invalid username or password. Attempt " + wrongAttempts + " of 3.");
            }
        }
    }

    private void showDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("wrongAttempts", wrongAttempts);
    }
}