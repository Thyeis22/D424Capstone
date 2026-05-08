package com.example.vacationscheduler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationscheduler.entities.Vacation;
import com.example.vacationscheduler.repository.Repository;

import java.util.List;

public class VacationList extends Activity {

    private Repository repository;
    private RecyclerView recyclerView;
    private Button addVacationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        repository = new Repository(getApplication());

        recyclerView = findViewById(R.id.vacationRecyclerView);
        addVacationButton = findViewById(R.id.addVacationButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addVacationButton.setOnClickListener(v -> {
            Intent intent = new Intent(VacationList.this, VacationDetails.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Vacation> vacations = repository.getAllVacations();
        VacationAdapter adapter = new VacationAdapter(this, vacations);
        recyclerView.setAdapter(adapter);
    }
}