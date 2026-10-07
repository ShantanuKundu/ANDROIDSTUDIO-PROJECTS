package com.example.rakshalink;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvUserWelcome, tvDatabaseStatus;
    private Button btnSos, btnLogout;
    private SessionManager sessionManager;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        dbHelper = new DatabaseHelper(this);

        tvUserWelcome = findViewById(R.id.tvUserWelcome);
        tvDatabaseStatus = findViewById(R.id.tvDatabaseStatus);
        btnSos = findViewById(R.id.btnSos);
        btnLogout = findViewById(R.id.btnLogout);

        tvUserWelcome.setText("User: " + sessionManager.getUserEmail());

        int hospitalCount = dbHelper.getHospitalCount();
        tvDatabaseStatus.setText("Active Mumbai Hospitals: " + hospitalCount);

        btnSos.setOnClickListener(v -> showSosConfirmDialog("108"));

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
                .setPositiveButton("Call Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                        dialIntent.setData(Uri.parse("tel:" + helplineNumber));
                        startActivity(dialIntent);
                    }
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}