package com.example.rakshalink;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminAddHospitalActivity extends AppCompatActivity {

    private EditText etName, etArea, etPhone, etLat, etLng, etRating;
    private CheckBox cbIcu;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_hospital);

        dbHelper = new DatabaseHelper(this);

        etName = findViewById(R.id.etAddName);
        etArea = findViewById(R.id.etAddArea);
        etPhone = findViewById(R.id.etAddPhone);
        etLat = findViewById(R.id.etAddLat);
        etLng = findViewById(R.id.etAddLng);
        etRating = findViewById(R.id.etAddRating);
        cbIcu = findViewById(R.id.cbAddIcu);
        Button btnSubmit = findViewById(R.id.btnSubmitAdd);

        btnSubmit.setOnClickListener(v -> saveRecord());
    }

    private void saveRecord() {
        String name = etName.getText().toString().trim();
        String area = etArea.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String latStr = etLat.getText().toString().trim();
        String lngStr = etLng.getText().toString().trim();
        String ratingStr = etRating.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(area) || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(latStr) || TextUtils.isEmpty(lngStr)) {
            Toast.makeText(this, "Please fill in all mandatory fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double lat = Double.parseDouble(latStr);
        double lng = Double.parseDouble(lngStr);
        double rating = TextUtils.isEmpty(ratingStr) ? 4.0 : Double.parseDouble(ratingStr);
        int hasIcu = cbIcu.isChecked() ? 1 : 0;

        boolean isInserted = dbHelper.addHospital(name, area, phone, lat, lng, hasIcu, rating);
        if (isInserted) {
            Toast.makeText(this, "Hospital added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to insert record", Toast.LENGTH_SHORT).show();
        }
    }
}