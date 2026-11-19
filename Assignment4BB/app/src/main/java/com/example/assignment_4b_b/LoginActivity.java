package com.example.assignment_4b_b;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    public static final String PREF_NAME = "UserPrefs";
    public static final String KEY_REGISTERED_EMAIL = "registered_email";
    public static final String KEY_REGISTERED_PASSWORD = "registered_password";
    public static final String KEY_REMEMBER_ME = "remember_me";
    public static final String KEY_SAVED_EMAIL = "saved_email";
    public static final String KEY_SAVED_PASSWORD = "saved_password";

    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private CheckBox cbRememberMe;
    private MaterialButton btnSignIn;
    private MaterialButton btnRegister;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        loadSavedCredentials();

        btnRegister.setOnClickListener(v -> handleRegistration());
        btnSignIn.setOnClickListener(v -> handleSignIn());
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void loadSavedCredentials() {
        boolean isRemembered = sharedPreferences.getBoolean(KEY_REMEMBER_ME, false);
        if (isRemembered) {
            String savedEmail = sharedPreferences.getString(KEY_SAVED_EMAIL, "");
            String savedPassword = sharedPreferences.getString(KEY_SAVED_PASSWORD, "");
            etEmail.setText(savedEmail);
            etPassword.setText(savedPassword);
            cbRememberMe.setChecked(true);
        }
    }

    private void handleRegistration() {
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, getString(R.string.err_empty_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_REGISTERED_EMAIL, email);
        editor.putString(KEY_REGISTERED_PASSWORD, password);
        editor.apply();

        Toast.makeText(this, getString(R.string.msg_register_success), Toast.LENGTH_LONG).show();
    }

    private void handleSignIn() {
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, getString(R.string.err_empty_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        String regEmail = sharedPreferences.getString(KEY_REGISTERED_EMAIL, null);
        String regPassword = sharedPreferences.getString(KEY_REGISTERED_PASSWORD, null);

        if (regEmail == null || regPassword == null) {
            Toast.makeText(this, getString(R.string.err_no_user), Toast.LENGTH_LONG).show();
            return;
        }

        if (email.equals(regEmail) && password.equals(regPassword)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (cbRememberMe.isChecked()) {
                editor.putBoolean(KEY_REMEMBER_ME, true);
                editor.putString(KEY_SAVED_EMAIL, email);
                editor.putString(KEY_SAVED_PASSWORD, password);
            } else {
                editor.putBoolean(KEY_REMEMBER_ME, false);
                editor.remove(KEY_SAVED_EMAIL);
                editor.remove(KEY_SAVED_PASSWORD);
            }
            editor.apply();

            Toast.makeText(this, getString(R.string.msg_login_success), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("user_email", email);
            intent.putExtra("remember_me", cbRememberMe.isChecked());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, getString(R.string.err_invalid_credentials), Toast.LENGTH_LONG).show();
        }
    }
}