package com.example.vacationscheduler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;

public class LoginActivity extends Activity {

    private EditText pinEditText;
    private Button loginButton;
    private static final String DEFAULT_PIN = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pinEditText = findViewById(R.id.pinEditText);
        loginButton = findViewById(R.id.loginButton);

        SharedPreferences prefs = getSharedPreferences("security", MODE_PRIVATE);

        if (!prefs.contains("pinHash")) {
            prefs.edit().putString("pinHash", hash(DEFAULT_PIN)).apply();
        }

        loginButton.setOnClickListener(v -> {
            String input = pinEditText.getText().toString().trim();
            String storedHash = prefs.getString("pinHash", "");

            if (hash(input).equals(storedHash)) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid PIN.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String hash(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encoded = digest.digest(value.getBytes());

            StringBuilder hex = new StringBuilder();
            for (byte b : encoded) {
                hex.append(String.format("%02x", b));
            }

            return hex.toString();
        } catch (Exception e) {
            return "";
        }
    }
}