package com.example.assignment1_b;

import android.text.TextUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    private EditText etName, etEmail;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale;
    private CheckBox cbAndroid, cbFlutter, cbJava;
    private RatingBar ratingBar;
    private TextView tvRatingValue;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        cbAndroid = findViewById(R.id.cbAndroid);
        cbFlutter = findViewById(R.id.cbFlutter);
        cbJava = findViewById(R.id.cbJava);
        ratingBar = findViewById(R.id.ratingBar);
        tvRatingValue = findViewById(R.id.tvRatingValue);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Pre-fill name from Intent
        String username = getIntent().getStringExtra("USERNAME");
        if (username != null) {
            etName.setText(username);
        }

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                updateRatingText(rating);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSubmit();
            }
        });
    }

    private void updateRatingText(float rating) {
        StringBuilder stars = new StringBuilder();
        int fullStars = (int) rating;
        for (int i = 0; i < 5; i++) {
            if (i < fullStars) {
                stars.append("★");
            } else {
                stars.append("☆");
            }
        }
        tvRatingValue.setText(stars.toString() + " (" + rating + "/5)");
    }

    private void validateAndSubmit() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        int selectedGenderId = rgGender.getCheckedRadioButtonId();

        List<String> missingFields = new ArrayList<>();
        if (name.isEmpty()) missingFields.add("Name");
        if (email.isEmpty()) missingFields.add("Email");
        if (selectedGenderId == -1) missingFields.add("Gender");

        if (!missingFields.isEmpty()) {
            showAlertDialog("Validation Error", "Please fill in the following fields: " + TextUtils.join(", ", missingFields));
            return;
        }

        // Collect subjects
        List<String> subjects = new ArrayList<>();
        if (cbAndroid.isChecked()) subjects.add("Android");
        if (cbFlutter.isChecked()) subjects.add("Flutter");
        if (cbJava.isChecked()) subjects.add("Java");

        String gender = ((RadioButton) findViewById(selectedGenderId)).getText().toString();
        float rating = ratingBar.getRating();

        String summary = "Name: " + name + "\n" +
                "Email: " + email + "\n" +
                "Gender: " + gender + "\n" +
                "Subjects: " + (subjects.isEmpty() ? "None" : TextUtils.join(", ", subjects)) + "\n" +
                "Rating: " + rating + "/5";

        showAlertDialog("Feedback Submitted", summary);
    }

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}