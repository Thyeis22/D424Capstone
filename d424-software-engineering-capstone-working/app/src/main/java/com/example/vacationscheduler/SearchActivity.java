package com.example.vacationscheduler;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationscheduler.entities.Excursion;
import com.example.vacationscheduler.entities.Vacation;
import com.example.vacationscheduler.repository.Repository;

import java.util.List;

public class SearchActivity extends Activity {

    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView vacationResults;
    private RecyclerView excursionResults;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        repository = new Repository(getApplication());

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        vacationResults = findViewById(R.id.vacationSearchRecyclerView);
        excursionResults = findViewById(R.id.excursionSearchRecyclerView);

        vacationResults.setLayoutManager(new LinearLayoutManager(this));
        excursionResults.setLayoutManager(new LinearLayoutManager(this));

        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();

            List<Vacation> vacations = repository.searchVacations(query);
            List<Excursion> excursions = repository.searchExcursions(query);

            vacationResults.setAdapter(new VacationAdapter(this, vacations));
            excursionResults.setAdapter(new ExcursionAdapter(this, excursions, -1));
        });
    }
}