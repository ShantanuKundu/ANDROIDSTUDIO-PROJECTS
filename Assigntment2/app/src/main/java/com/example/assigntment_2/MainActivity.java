package com.example.assigntment_2;

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

public class MainActivity extends AppCompatActivity {

    private EditText etNum1, etNum2;
    private TextView tvResult;
    private Button btnAdd, btnSub, btnMul, btnDiv, btnClear;

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

        etNum1 = findViewById(R.id.etNum1);
        etNum2 = findViewById(R.id.etNum2);
        tvResult = findViewById(R.id.tvResult);
        btnAdd = findViewById(R.id.btnAdd);
        btnSub = findViewById(R.id.btnSub);
        btnMul = findViewById(R.id.btnMul);
        btnDiv = findViewById(R.id.btnDiv);
        btnClear = findViewById(R.id.btnClear);

        btnAdd.setOnClickListener(v -> calculate('+'));
        btnSub.setOnClickListener(v -> calculate('-'));
        btnMul.setOnClickListener(v -> calculate('*'));
        btnDiv.setOnClickListener(v -> calculate('/'));
        btnClear.setOnClickListener(v -> clearFields());
    }

    private void calculate(char operator) {
        String str1 = etNum1.getText().toString().trim();
        String str2 = etNum2.getText().toString().trim();

        if (TextUtils.isEmpty(str1) || TextUtils.isEmpty(str2)) {
            Toast.makeText(this, R.string.error_empty_input, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double num1 = Double.parseDouble(str1);
            double num2 = Double.parseDouble(str2);
            double result = 0;

            switch (operator) {
                case '+':
                    result = num1 + num2;
                    break;
                case '-':
                    result = num1 - num2;
                    break;
                case '*':
                    result = num1 * num2;
                    break;
                case '/':
                    if (num2 == 0) {
                        Toast.makeText(this, R.string.error_divide_by_zero, Toast.LENGTH_LONG).show();
                        tvResult.setText(getString(R.string.result_placeholder) + "Error");
                        return;
                    }
                    result = num1 / num2;
                    break;
            }

            // Format result to drop trailing .0 if integer
            String formattedResult;
            if (result == (long) result) {
                formattedResult = String.format("%d", (long) result);
            } else {
                formattedResult = String.valueOf(result);
            }

            tvResult.setText(getString(R.string.result_placeholder) + formattedResult);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input number format", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        etNum1.setText("");
        etNum2.setText("");
        tvResult.setText(getString(R.string.result_placeholder));
        etNum1.requestFocus();
    }
}
