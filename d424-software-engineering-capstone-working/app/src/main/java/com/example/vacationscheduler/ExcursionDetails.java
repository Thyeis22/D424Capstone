package com.example.vacationscheduler;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vacationscheduler.entities.Excursion;
import com.example.vacationscheduler.entities.Vacation;
import com.example.vacationscheduler.receivers.AlertReceiver;
import com.example.vacationscheduler.repository.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends Activity {

    private EditText titleEditText, dateEditText;
    private Button saveButton, deleteButton, alertButton;

    private Repository repository;
    private Excursion currentExcursion;
    private Vacation vacation;

    private int excursionID = -1;
    private int vacationID = -1;

    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);

        repository = new Repository(getApplication());

        titleEditText = findViewById(R.id.excursionTitleEditText);
        dateEditText = findViewById(R.id.excursionDateEditText);

        saveButton = findViewById(R.id.saveExcursionButton);
        deleteButton = findViewById(R.id.deleteExcursionButton);
        alertButton = findViewById(R.id.excursionAlertButton);

        excursionID = getIntent().getIntExtra("id", -1);
        vacationID = getIntent().getIntExtra("vacationID", -1);

        vacation = repository.getVacationByID(vacationID);

        if (excursionID != -1) {
            currentExcursion = repository.getExcursionByID(excursionID);

            if (currentExcursion != null) {
                titleEditText.setText(currentExcursion.getExcursionTitle());
                dateEditText.setText(currentExcursion.getExcursionDate());
            }
        }

        saveButton.setOnClickListener(v -> saveExcursion());
        deleteButton.setOnClickListener(v -> deleteExcursion());

        alertButton.setOnClickListener(v -> setAlert(
                dateEditText.getText().toString(),
                titleEditText.getText().toString() + " is scheduled today.",
                excursionID + 3000
        ));
    }

    private void saveExcursion() {
        String title = titleEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();

        if (title.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!isValidDate(date)) {
            Toast.makeText(this, "Date must be in MM/dd/yyyy format.", Toast.LENGTH_LONG).show();
            return;
        }

        if (vacation == null) {
            Toast.makeText(this, "Associated vacation not found.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!isExcursionDuringVacation(date, vacation.getStartDate(), vacation.getEndDate())) {
            Toast.makeText(this, "Excursion date must be during the vacation.", Toast.LENGTH_LONG).show();
            return;
        }

        if (excursionID == -1) {
            Excursion excursion = new Excursion(0, title, date, vacationID);
            repository.insert(excursion);
            Toast.makeText(this, "Excursion added.", Toast.LENGTH_SHORT).show();
        } else {
            Excursion excursion = new Excursion(excursionID, title, date, vacationID);
            repository.update(excursion);
            Toast.makeText(this, "Excursion updated.", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void deleteExcursion() {
        if (excursionID == -1 || currentExcursion == null) {
            Toast.makeText(this, "Excursion has not been saved yet.", Toast.LENGTH_SHORT).show();
            return;
        }

        repository.delete(currentExcursion);
        Toast.makeText(this, "Excursion deleted.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean isValidDate(String date) {
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isExcursionDuringVacation(String excursionDate, String startDate, String endDate) {
        try {
            Date excursion = sdf.parse(excursionDate);
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            return !excursion.before(start) && !excursion.after(end);
        } catch (Exception e) {
            return false;
        }
    }

    private void setAlert(String date, String message, int requestCode) {
        if (excursionID == -1) {
            Toast.makeText(this, "Save excursion before setting alert.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!isValidDate(date)) {
            Toast.makeText(this, "Invalid alert date.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Date alertDate = sdf.parse(date);

            Intent intent = new Intent(this, AlertReceiver.class);
            intent.putExtra("message", message);

            PendingIntent sender = PendingIntent.getBroadcast(
                    this,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, alertDate.getTime(), sender);

            Toast.makeText(this, "Excursion alert set.", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Alert could not be set.", Toast.LENGTH_SHORT).show();
        }
    }
}