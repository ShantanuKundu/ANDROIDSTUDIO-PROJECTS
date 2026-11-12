package com.example.rakshalink;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminManageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage);

        Button btnNavAdd = findViewById(R.id.btnNavAddHospital);
        Button btnNavDelete = findViewById(R.id.btnNavDeleteHospital);

        btnNavAdd.setOnClickListener(v -> {
            Intent intent = new Intent(AdminManageActivity.this, AdminAddHospitalActivity.class);
            startActivity(intent);
        });

        btnNavDelete.setOnClickListener(v -> {
            Intent intent = new Intent(AdminManageActivity.this, AdminDeleteHospitalActivity.class);
            startActivity(intent);
        });
    }
}