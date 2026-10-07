package com.example.rakshalink;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvUserWelcome, tvUserRoleBadge, tvDatabaseStatus;
    private Button btnSos, btnFindHospitals, btnAdminManage, btnLogout;
    private SessionManager sessionManager;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        dbHelper = new DatabaseHelper(this);

        tvUserWelcome = findViewById(R.id.tvUserWelcome);
        tvUserRoleBadge = findViewById(R.id.tvUserRoleBadge);
        tvDatabaseStatus = findViewById(R.id.tvDatabaseStatus);
        btnSos = findViewById(R.id.btnSos);
        btnFindHospitals = findViewById(R.id.btnFindHospitals);
        btnAdminManage = findViewById(R.id.btnAdminManage);
        btnLogout = findViewById(R.id.btnLogout);

        // Populate personalized details
        tvUserWelcome.setText("Welcome, " + sessionManager.getUserName() + "!");
        tvUserRoleBadge.setText("Account Type: " + sessionManager.getUserRole().toUpperCase());

        int count = dbHelper.getHospitalCount();
        tvDatabaseStatus.setText("Active Mumbai Hospitals: " + count);

        // Role-Based Control: Only show the Admin button if logged in as Admin
        if (sessionManager.isAdmin()) {
            btnAdminManage.setVisibility(View.VISIBLE);
        } else {
            btnAdminManage.setVisibility(View.GONE);
        }

        // SOS Button
        btnSos.setOnClickListener(v -> showSosConfirmDialog("108"));

        // Hospital Locator Screen
        btnFindHospitals.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, HospitalListActivity.class));
        });

        // Admin Management Screen
        btnAdminManage.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AdminManageActivity.class));
        });

        // Logout
        btnLogout.setOnClickListener(v -> {
            sessionManager.logoutUser();
            Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void showSosConfirmDialog(String helplineNumber) {
        new AlertDialog.Builder(this)
                .setTitle("Emergency Call Confirmation")
                .setMessage("Are you sure you want to dial Mumbai Emergency Helpline (" + helplineNumber + ")?")
                .setPositiveButton("Call Now", (dialog, which) -> {
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                    dialIntent.setData(Uri.parse("tel:" + helplineNumber));
                    startActivity(dialIntent);
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}