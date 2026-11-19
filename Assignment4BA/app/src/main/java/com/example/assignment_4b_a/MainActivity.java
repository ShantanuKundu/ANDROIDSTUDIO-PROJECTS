package com.example.assignment_4b_a;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private EditText etDirName, etFileName, etFileContent;
    private Button btnWrite, btnRead, btnDelete;
    private TextView tvOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        etDirName = findViewById(R.id.etDirName);
        etFileName = findViewById(R.id.etFileName);
        etFileContent = findViewById(R.id.etFileContent);
        btnWrite = findViewById(R.id.btnWrite);
        btnRead = findViewById(R.id.btnRead);
        btnDelete = findViewById(R.id.btnDelete);
        tvOutput = findViewById(R.id.tvOutput);

        // Set click listeners
        btnWrite.setOnClickListener(v -> writeToFile());
        btnRead.setOnClickListener(v -> readFromFile());
        btnDelete.setOnClickListener(v -> deleteFile());
    }

    /**
     * Creates directory (if not exists), creates file, and writes content into it.
     */
    private void writeToFile() {
        String dirName = etDirName.getText().toString().trim();
        String fileName = etFileName.getText().toString().trim();
        String content = etFileContent.getText().toString();

        if (TextUtils.isEmpty(dirName) || TextUtils.isEmpty(fileName)) {
            Toast.makeText(this, "Please enter both Directory and File name", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Get internal directory
            File dir = new File(getFilesDir(), dirName);
            if (!dir.exists()) {
                boolean createdDir = dir.mkdirs();
                if (!createdDir) {
                    tvOutput.setText("Failed to create directory: " + dir.getAbsolutePath());
                    return;
                }
            }

            File file = new File(dir, fileName);
            boolean isNewFile = !file.exists();

            // Write text content to file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();

            String status = "Operation: WRITE INTO FILE\n" +
                    "Status: Success\n" +
                    "Directory Created/Used: " + dir.getName() + "\n" +
                    "File " + (isNewFile ? "Created" : "Modified") + ": " + file.getName() + "\n" +
                    "Full Path: " + file.getAbsolutePath() + "\n" +
                    "File Size: " + file.length() + " bytes";

            tvOutput.setText(status);
            Toast.makeText(this, "File saved successfully!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            tvOutput.setText("Error writing file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Reads content from the specified file in the directory.
     */
    private void readFromFile() {
        String dirName = etDirName.getText().toString().trim();
        String fileName = etFileName.getText().toString().trim();

        if (TextUtils.isEmpty(dirName) || TextUtils.isEmpty(fileName)) {
            Toast.makeText(this, "Please enter both Directory and File name", Toast.LENGTH_SHORT).show();
            return;
        }

        File dir = new File(getFilesDir(), dirName);
        File file = new File(dir, fileName);

        if (!file.exists()) {
            tvOutput.setText("Operation: READ FROM FILE\n" +
                    "Status: File does NOT exist!\n" +
                    "Target File: " + file.getAbsolutePath());
            Toast.makeText(this, "File does not exist!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            fis.close();

            String status = "Operation: READ FROM FILE\n" +
                    "Status: Success\n" +
                    "File Name: " + file.getName() + "\n" +
                    "Full Path: " + file.getAbsolutePath() + "\n" +
                    "-----------------------------------\n" +
                    "File Content:\n" + sb.toString().trim();

            tvOutput.setText(status);
            Toast.makeText(this, "File read successfully!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            tvOutput.setText("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Deletes the specified file from the directory.
     */
    private void deleteFile() {
        String dirName = etDirName.getText().toString().trim();
        String fileName = etFileName.getText().toString().trim();

        if (TextUtils.isEmpty(dirName) || TextUtils.isEmpty(fileName)) {
            Toast.makeText(this, "Please enter both Directory and File name", Toast.LENGTH_SHORT).show();
            return;
        }

        File dir = new File(getFilesDir(), dirName);
        File file = new File(dir, fileName);

        if (!file.exists()) {
            tvOutput.setText("Operation: DELETE FILE\n" +
                    "Status: File does NOT exist!\n" +
                    "Target File: " + file.getAbsolutePath());
            Toast.makeText(this, "File does not exist!", Toast.LENGTH_SHORT).show();
            return;
        }

        String deletedFileName = file.getName();
        String deletedFilePath = file.getAbsolutePath();
        boolean deleted = file.delete();

        if (deleted) {
            String status = "Operation: DELETE FILE\n" +
                    "Status: Success\n" +
                    "Deleted File: " + deletedFileName + "\n" +
                    "Deleted Path: " + deletedFilePath;

            tvOutput.setText(status);
            Toast.makeText(this, "File deleted successfully!", Toast.LENGTH_SHORT).show();
        } else {
            tvOutput.setText("Operation: DELETE FILE\n" +
                    "Status: Failed to delete file\n" +
                    "Target File: " + deletedFilePath);
            Toast.makeText(this, "Failed to delete file!", Toast.LENGTH_SHORT).show();
        }
    }
}