package com.example.rakshalink;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HospitalListActivity extends AppCompatActivity {

    private EditText etSearchArea;
    private CheckBox cbIcuOnly;
    private LinearLayout layoutHospitalContainer;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_list);

        dbHelper = new DatabaseHelper(this);

        etSearchArea = findViewById(R.id.etSearchArea);
        cbIcuOnly = findViewById(R.id.cbIcuOnly);
        layoutHospitalContainer = findViewById(R.id.layoutHospitalContainer);

        // Load all 50 hospitals initially
        loadHospitals("", false);

        // Search text change listener
        etSearchArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadHospitals(s.toString().trim(), cbIcuOnly.isChecked());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // ICU Checkbox filter listener
        cbIcuOnly.setOnCheckedChangeListener((buttonView, isChecked) -> {
            loadHospitals(etSearchArea.getText().toString().trim(), isChecked);
        });
    }

    private void loadHospitals(String filterText, boolean icuOnly) {
        layoutHospitalContainer.removeAllViews();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        StringBuilder query = new StringBuilder("SELECT * FROM " + DatabaseHelper.TABLE_HOSPITALS + " WHERE 1=1");
        if (!filterText.isEmpty()) {
            query.append(" AND (area LIKE '%").append(filterText).append("%' OR name LIKE '%").append(filterText).append("%')");
        }
        if (icuOnly) {
            query.append(" AND icu_available = 1");
        }
        query.append(" ORDER BY distance_km ASC");

        Cursor cursor = db.rawQuery(query.toString(), null);

        if (cursor.getCount() == 0) {
            TextView tvEmpty = new TextView(this);
            tvEmpty.setText("No hospitals found matching your criteria.");
            tvEmpty.setPadding(16, 32, 16, 16);
            tvEmpty.setTextSize(16);
            tvEmpty.setTextColor(Color.GRAY);
            layoutHospitalContainer.addView(tvEmpty);
            cursor.close();
            return;
        }

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME));
            String area = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_AREA));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONE));
            int icu = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ICU));
            float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RATING));
            double distance = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DISTANCE));

            addHospitalCard(name, area, phone, icu, rating, distance);
        }
        cursor.close();
    }

    private void addHospitalCard(String name, String area, String phone, int icu, float rating, double distance) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundColor(Color.WHITE);
        card.setPadding(24, 20, 24, 20);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 16);
        card.setLayoutParams(params);

        // Hospital Name
        TextView tvName = new TextView(this);
        tvName.setText(name);
        tvName.setTextSize(17);
        tvName.setTextColor(Color.parseColor("#C62828"));
        tvName.getPaint().setFakeBoldText(true);
        card.addView(tvName);

        // Area & Distance
        TextView tvArea = new TextView(this);
        tvArea.setText("📍 " + area + " (" + distance + " km away)");
        tvArea.setTextSize(13);
        tvArea.setTextColor(Color.DKGRAY);
        card.addView(tvArea);

        // Rating and ICU tag
        TextView tvDetails = new TextView(this);
        String icuStatus = (icu == 1) ? "✅ 24/7 ICU Available" : "⚠️ Basic Care (No ICU)";
        tvDetails.setText("⭐ Rating: " + rating + "/5.0  |  " + icuStatus);
        tvDetails.setTextSize(12);
        tvDetails.setTextColor((icu == 1) ? Color.parseColor("#2E7D32") : Color.parseColor("#E65100"));
        tvDetails.setPadding(0, 4, 0, 12);
        card.addView(tvDetails);

        // Buttons Layout (Call & View on Map)
        LinearLayout btnLayout = new LinearLayout(this);
        btnLayout.setOrientation(LinearLayout.HORIZONTAL);

        Button btnCall = new Button(this);
        btnCall.setText("📞 Call");
        btnCall.setBackgroundColor(Color.parseColor("#388E3C"));
        btnCall.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        btnParams.setMargins(0, 0, 8, 0);
        btnCall.setLayoutParams(btnParams);
        btnCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phone));
            startActivity(intent);
        });
        btnLayout.addView(btnCall);

        Button btnMap = new Button(this);
        btnMap.setText("🗺️ View Map");
        btnMap.setBackgroundColor(Color.parseColor("#1976D2"));
        btnMap.setTextColor(Color.WHITE);
        btnMap.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        btnMap.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(name + ", " + area + ", Mumbai"));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            startActivity(mapIntent);
        });
        btnLayout.addView(btnMap);

        card.addView(btnLayout);
        layoutHospitalContainer.addView(card);
    }
}