package com.example.assignment_2b;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerCategory, spinnerFrom, spinnerTo;
    EditText etInputValue;
    Button btnConvert;
    TextView tvResult;

    String[] categories = {"Currency", "Temperature", "Distance"};
    String[] currencyUnits = {"INR", "USD", "EUR"};
    String[] tempUnits = {"Celsius", "Fahrenheit", "Kelvin"};
    String[] distanceUnits = {"Meters", "Kilometers", "Miles"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        etInputValue = findViewById(R.id.etInputValue);
        btnConvert = findViewById(R.id.btnConvert);
        tvResult = findViewById(R.id.tvResult);

        // Set up Category Spinner
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(categoryAdapter);

        // Update From/To Spinners dynamically when Category changes
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] selectedUnits;
                if (position == 0) {
                    selectedUnits = currencyUnits;
                } else if (position == 1) {
                    selectedUnits = tempUnits;
                } else {
                    selectedUnits = distanceUnits;
                }

                ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
                        MainActivity.this, android.R.layout.simple_spinner_dropdown_item, selectedUnits);
                spinnerFrom.setAdapter(unitAdapter);
                spinnerTo.setAdapter(unitAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Convert Button Click Listener
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputStr = etInputValue.getText().toString().trim();
                if (inputStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a value", Toast.LENGTH_SHORT).show();
                    return;
                }

                double input = Double.parseDouble(inputStr);
                String category = spinnerCategory.getSelectedItem().toString();
                String fromUnit = spinnerFrom.getSelectedItem().toString();
                String toUnit = spinnerTo.getSelectedItem().toString();
                double result = 0.0;

                if (category.equals("Currency")) {
                    result = convertCurrency(input, fromUnit, toUnit);
                } else if (category.equals("Temperature")) {
                    result = convertTemperature(input, fromUnit, toUnit);
                } else if (category.equals("Distance")) {
                    result = convertDistance(input, fromUnit, toUnit);
                }

                tvResult.setText(String.format("Result: %.2f %s", result, toUnit));
            }
        });
    }

    // 1. Currency Conversion (Base: INR)
    private double convertCurrency(double val, String from, String to) {
        double inINR = val;
        if (from.equals("USD")) inINR = val * 84.0;
        else if (from.equals("EUR")) inINR = val * 90.0;

        if (to.equals("USD")) return inINR / 84.0;
        else if (to.equals("EUR")) return inINR / 90.0;
        return inINR;
    }

    // 2. Temperature Conversion (Base: Celsius)
    private double convertTemperature(double val, String from, String to) {
        double inCelsius = val;
        if (from.equals("Fahrenheit")) inCelsius = (val - 32) * 5.0 / 9.0;
        else if (from.equals("Kelvin")) inCelsius = val - 273.15;

        if (to.equals("Fahrenheit")) return (inCelsius * 9.0 / 5.0) + 32;
        else if (to.equals("Kelvin")) return inCelsius + 273.15;
        return inCelsius;
    }

    // 3. Distance Conversion (Base: Meters)
    private double convertDistance(double val, String from, String to) {
        double inMeters = val;
        if (from.equals("Kilometers")) inMeters = val * 1000.0;
        else if (from.equals("Miles")) inMeters = val * 1609.34;

        if (to.equals("Kilometers")) return inMeters / 1000.0;
        else if (to.equals("Miles")) return inMeters / 1609.34;
        return inMeters;
    }
}