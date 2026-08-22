package com.example.assignment1_b;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etConfirmPassword;
    private Button btnSignUp;
    private TextView tvLoginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUsername = findViewById(R.id.etSignUpUsername);
        etPassword = findViewById(R.id.etSignUpPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUpSubmit);
        tvLoginLink = findViewById(R.id.tvLoginLink);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignUp();
            }
        });

        tvLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to LoginActivity
            }
        });
    }

    private void handleSignUp() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlertDialog("Error", "All fields are required.");
            return;
        }

        if (UserPrefsHelper.isUsernameTaken(this, username)) {
            showAlertDialog("Error", "Username already exists. Please choose another.");
            return;
        }

        if (password.length() < 6) {
            showAlertDialog("Error", "Password must be at least 6 characters.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlertDialog("Error", "Passwords do not match.");
            return;
        }

        // Save user
        UserPrefsHelper.saveUser(this, username, password);

        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Account created successfully! Please log in.")
                .setPositiveButton("OK", (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.putExtra("REGISTERED_USERNAME", username);
                    setResult(RESULT_OK, intent);
                    finish();
                })
                .show();
    }

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}