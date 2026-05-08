package com.example.vacationscheduler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button vacationButton;
    private Button searchButton;
    private Button reportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vacationButton = findViewById(R.id.viewVacationsButton);
        searchButton = findViewById(R.id.searchScreenButton);
        reportButton = findViewById(R.id.reportScreenButton);

        vacationButton.setOnClickListener(v ->
                startActivity(new Intent(this, VacationList.class)));

        searchButton.setOnClickListener(v ->
                startActivity(new Intent(this, SearchActivity.class)));

        reportButton.setOnClickListener(v ->
                startActivity(new Intent(this, ReportActivity.class)));
    }
}