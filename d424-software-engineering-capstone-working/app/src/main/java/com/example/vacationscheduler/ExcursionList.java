package com.example.vacationscheduler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationscheduler.entities.Excursion;
import com.example.vacationscheduler.repository.Repository;

import java.util.List;

public class ExcursionList extends Activity {

    private Repository repository;
    private RecyclerView recyclerView;
    private Button addButton;
    private int vacationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_list);

        repository = new Repository(getApplication());
        vacationID = getIntent().getIntExtra("vacationID", -1);

        recyclerView = findViewById(R.id.excursionRecyclerView);
        addButton = findViewById(R.id.addExcursionButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExcursionDetails.class);
            intent.putExtra("vacationID", vacationID);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Excursion> list = repository.getAssociatedExcursions(vacationID);
        recyclerView.setAdapter(new ExcursionAdapter(this, list, vacationID));
    }
}