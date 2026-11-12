package com.example.rakshalink;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDeleteHospitalActivity extends AppCompatActivity {

    private EditText etName;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_hospital);

        dbHelper = new DatabaseHelper(this);
        etName = findViewById(R.id.etTargetDeleteName);
        Button btnDelete = findViewById(R.id.btnSubmitDelete);

        btnDelete.setOnClickListener(v -> confirmDeletion());
    }

    private void confirmDeletion() {
        String name = etName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Enter hospital name", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to remove " + name + " from the offline directory?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    boolean isDeleted = dbHelper.deleteHospitalByName(name);
                    if (isDeleted) {
                        Toast.makeText(this, "Hospital deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "No matching hospital found", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}