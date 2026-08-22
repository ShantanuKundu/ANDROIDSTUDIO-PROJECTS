package com.example.assignment1_b;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private CheckBox cbRememberMe;
    private Button btnLogin;
    private TextView tvForgotPassword, tvSignUp;

    private int wrongAttempts = 0;
    private static final String KEY_WRONG_ATTEMPTS = "wrong_attempts";
    private static final int SIGN_UP_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp = findViewById(R.id.tvSignUp);

        if (savedInstanceState != null) {
            wrongAttempts = savedInstanceState.getInt(KEY_WRONG_ATTEMPTS, 0);
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

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog("Forgot Password", "Password reset link would be sent to your registered email.");
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivityForResult(intent, SIGN_UP_REQUEST_CODE);
            }
        });
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlertDialog("Error", "Please enter both username and password.");
            return;
        }

        // Use UserPrefsHelper for validation (includes admin check)
        if (UserPrefsHelper.validateUser(this, username, password)) {
            Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
            finish();
        } else {
            wrongAttempts++;
            if (wrongAttempts >= 3) {
                btnLogin.setEnabled(false);
                showAlertDialog("Login Failed", "You have exceeded the maximum number of attempts (3). The login button has been disabled.");
            } else {
                showAlertDialog("Login Failed", "Invalid username or password. Attempt " + wrongAttempts + " of 3.");
            }
        }
    }

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_WRONG_ATTEMPTS, wrongAttempts);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_UP_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String registeredUser = data.getStringExtra("REGISTERED_USERNAME");
            if (registeredUser != null) {
                etUsername.setText(registeredUser);
                etPassword.requestFocus();
            }
        }
    }
}