package com.example.assignment_4a;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etId, etName, etCourse;
    Button btnInsert, btnView, btnUpdate, btnDelete;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etId = findViewById(R.id.etId);
        etName = findViewById(R.id.etName);
        etCourse = findViewById(R.id.etCourse);
        btnInsert = findViewById(R.id.btnInsert);
        btnView = findViewById(R.id.btnView);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        dbHelper = new DBHelper(this);

        // CREATE
        btnInsert.setOnClickListener(v -> {
            String id = etId.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String course = etCourse.getText().toString().trim();

            if (id.isEmpty() || name.isEmpty() || course.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean inserted = dbHelper.insertStudent(id, name, course);
            Toast.makeText(this, inserted ? "Record Inserted" : "Insert Failed", Toast.LENGTH_SHORT).show();
        });

        // READ
        btnView.setOnClickListener(v -> {
            Cursor cursor = dbHelper.getAllStudents();
            if (cursor.getCount() == 0) {
                Toast.makeText(this, "No Records Found", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder buffer = new StringBuilder();
            while (cursor.moveToNext()) {
                buffer.append("ID: ").append(cursor.getString(0)).append("\n");
                buffer.append("Name: ").append(cursor.getString(1)).append("\n");
                buffer.append("Course: ").append(cursor.getString(2)).append("\n\n");
            }
            cursor.close();

            new AlertDialog.Builder(this)
                    .setTitle("Student Records")
                    .setMessage(buffer.toString())
                    .setPositiveButton("OK", null)
                    .show();
        });

        // UPDATE
        btnUpdate.setOnClickListener(v -> {
            String id = etId.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String course = etCourse.getText().toString().trim();

            if (id.isEmpty()) {
                Toast.makeText(this, "Please enter Roll No", Toast.LENGTH_SHORT).show();
                return;
            }

            if (name.isEmpty() && course.isEmpty()) {
                Toast.makeText(this, "Please enter Name or Course to update", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean updated = dbHelper.updateStudent(id, name, course);
            Toast.makeText(this, updated ? "Record Updated" : "Record Not Found", Toast.LENGTH_SHORT).show();
        });

        // DELETE
        btnDelete.setOnClickListener(v -> {
            String id = etId.getText().toString().trim();

            if (id.isEmpty()) {
                Toast.makeText(this, "Please enter Roll ID", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean deleted = dbHelper.deleteStudent(id);
            Toast.makeText(this, deleted ? "Record Deleted" : "Record Not Found", Toast.LENGTH_SHORT).show();
        });
    }
}