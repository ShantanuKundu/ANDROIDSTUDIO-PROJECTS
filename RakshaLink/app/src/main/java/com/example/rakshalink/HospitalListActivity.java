package com.example.rakshalink;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HospitalListActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    // Fallback baseline: CST / South Mumbai Center
    private double currentLatitude = 18.9322;
    private double currentLongitude = 72.8347;

    private EditText etSearchArea;
    private CheckBox cbIcuOnly;
    private TextView tvDefaultReference;
    private TextView tvLocationStatus;
    private LinearLayout layoutHospitalContainer;
    private DatabaseHelper dbHelper;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private static class HospitalItem implements Comparable<HospitalItem> {
        String name, area, phone;
        int icu;
        float rating;
        double distance;
        double lat, lng;

        HospitalItem(String name, String area, String phone, int icu, float rating, double distance, double lat, double lng) {
            this.name = name;
            this.area = area;
            this.phone = phone;
            this.icu = icu;
            this.rating = rating;
            this.distance = distance;
            this.lat = lat;
            this.lng = lng;
        }

        @Override
        public int compareTo(HospitalItem other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_list);

        dbHelper = new DatabaseHelper(this);

        tvDefaultReference = findViewById(R.id.tvDefaultReference);
        tvLocationStatus = findViewById(R.id.tvLocationStatus);
        etSearchArea = findViewById(R.id.etSearchArea);
        cbIcuOnly = findViewById(R.id.cbIcuOnly);
        layoutHospitalContainer = findViewById(R.id.layoutHospitalContainer);

        etSearchArea.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadHospitals(s.toString().trim(), cbIcuOnly.isChecked());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        cbIcuOnly.setOnCheckedChangeListener((buttonView, isChecked) -> {
            loadHospitals(etSearchArea.getText().toString().trim(), isChecked);
        });

        checkAndPromptLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationEngine();
        }
    }

    private void checkAndPromptLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            loadHospitals("", false);
        } else {
            verifyGpsEnabledAndStart();
        }
    }

    private void verifyGpsEnabledAndStart() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ignored) {}

        if (!gpsEnabled && !networkEnabled) {
            tvLocationStatus.setText("⚠️ Location Service Disabled on Phone");
            tvLocationStatus.setTextColor(Color.RED);
            new AlertDialog.Builder(this)
                    .setTitle("Location Disabled")
                    .setMessage("Enable device location to calculate distances from where you are standing right now.")
                    .setPositiveButton("Settings", (dialog, which) -> {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    })
                    .setNegativeButton("Keep Default", (dialog, which) -> {
                        loadHospitals("", false);
                    })
                    .show();
        } else {
            startLocationEngine();
        }
    }

    private void startLocationEngine() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Location bestLoc = null;
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            bestLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (bestLoc == null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            bestLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if (bestLoc != null) {
            updateLocationAndRefresh(bestLoc);
        } else {
            tvLocationStatus.setText("⏳ Fetching live phone coordinates...");
            tvLocationStatus.setTextColor(Color.parseColor("#E65100"));
            loadHospitals("", false);
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationAndRefresh(location);
            }
            @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override public void onProviderEnabled(@NonNull String provider) {}
            @Override public void onProviderDisabled(@NonNull String provider) {
                tvLocationStatus.setText("⚠️ GPS Switched Off");
                tvLocationStatus.setTextColor(Color.RED);
            }
        };

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 5, locationListener);
        }
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 5, locationListener);
        }
    }

    private void updateLocationAndRefresh(Location loc) {
        currentLatitude = loc.getLatitude();
        currentLongitude = loc.getLongitude();
        tvLocationStatus.setText("✅ Live Phone GPS Active");
        tvLocationStatus.setTextColor(Color.parseColor("#2E7D32"));
        loadHospitals(etSearchArea.getText().toString().trim(), cbIcuOnly.isChecked());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                verifyGpsEnabledAndStart();
            } else {
                tvLocationStatus.setText("❌ Permission Denied (Using Baseline)");
                tvLocationStatus.setTextColor(Color.RED);
                loadHospitals("", false);
            }
        }
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

        Cursor cursor = db.rawQuery(query.toString(), null);
        List<HospitalItem> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME));
            String area = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_AREA));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONE));
            int icu = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ICU));
            float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RATING));
            double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LAT));
            double lng = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LNG));

            double dist = DistanceCalculator.calculateDistance(currentLatitude, currentLongitude, lat, lng);
            list.add(new HospitalItem(name, area, phone, icu, rating, dist, lat, lng));
        }
        cursor.close();

        Collections.sort(list);

        if (list.isEmpty()) {
            TextView tvEmpty = new TextView(this);
            tvEmpty.setText("No hospitals found matching criteria.");
            tvEmpty.setPadding(16, 32, 16, 16);
            tvEmpty.setTextSize(16);
            tvEmpty.setTextColor(Color.GRAY);
            layoutHospitalContainer.addView(tvEmpty);
            return;
        }

        for (HospitalItem item : list) {
            addHospitalCard(item);
        }
    }

    private void addHospitalCard(HospitalItem item) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundColor(Color.WHITE);
        card.setPadding(24, 20, 24, 20);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 16);
        card.setLayoutParams(params);

        TextView tvName = new TextView(this);
        tvName.setText(item.name);
        tvName.setTextSize(17);
        tvName.setTextColor(Color.parseColor("#C62828"));
        tvName.getPaint().setFakeBoldText(true);
        card.addView(tvName);

        TextView tvArea = new TextView(this);
        tvArea.setText("📍 " + item.area + "  •  " + item.distance + " km away");
        tvArea.setTextSize(13);
        tvArea.setTextColor(Color.DKGRAY);
        card.addView(tvArea);

        TextView tvDetails = new TextView(this);
        String icuStatus = (item.icu == 1) ? "✅ 24/7 ICU Available" : "⚠️ Basic Care";
        tvDetails.setText("⭐ Rating: " + item.rating + "/5.0  |  " + icuStatus);
        tvDetails.setTextSize(12);
        tvDetails.setTextColor((item.icu == 1) ? Color.parseColor("#2E7D32") : Color.parseColor("#E65100"));
        tvDetails.setPadding(0, 4, 0, 12);
        card.addView(tvDetails);

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
            intent.setData(Uri.parse("tel:" + item.phone));
            startActivity(intent);
        });
        btnLayout.addView(btnCall);

        Button btnMap = new Button(this);
        btnMap.setText("🗺️ View Map");
        btnMap.setBackgroundColor(Color.parseColor("#1976D2"));
        btnMap.setTextColor(Color.WHITE);
        btnMap.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        btnMap.setOnClickListener(v -> {
            Uri navUri = Uri.parse("google.navigation:q=" + item.lat + "," + item.lng + "&mode=d");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, navUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getPackageManager()) == null) {
                navUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + item.lat + "," + item.lng);
                mapIntent = new Intent(Intent.ACTION_VIEW, navUri);
            }
            startActivity(mapIntent);
        });
        btnLayout.addView(btnMap);

        card.addView(btnLayout);
        layoutHospitalContainer.addView(card);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}