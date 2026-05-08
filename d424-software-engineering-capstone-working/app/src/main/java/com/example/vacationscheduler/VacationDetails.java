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

import com.example.vacationscheduler.entities.Vacation;
import com.example.vacationscheduler.receivers.AlertReceiver;
import com.example.vacationscheduler.repository.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VacationDetails extends Activity {

    private EditText titleEditText, hotelEditText, startDateEditText, endDateEditText;
    private Button saveButton, deleteButton, shareButton, startAlertButton, endAlertButton, excursionsButton;

    private Repository repository;
    private Vacation currentVacation;
    private int vacationID = -1;

    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);

        repository = new Repository(getApplication());

        titleEditText = findViewById(R.id.titleEditText);
        hotelEditText = findViewById(R.id.hotelEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);

        saveButton = findViewById(R.id.saveVacationButton);
        deleteButton = findViewById(R.id.deleteVacationButton);
        shareButton = findViewById(R.id.shareVacationButton);
        startAlertButton = findViewById(R.id.startAlertButton);
        endAlertButton = findViewById(R.id.endAlertButton);
        excursionsButton = findViewById(R.id.excursionsButton);

        vacationID = getIntent().getIntExtra("id", -1);

        if (vacationID != -1) {
            currentVacation = repository.getVacationByID(vacationID);

            if (currentVacation != null) {
                titleEditText.setText(currentVacation.getVacationTitle());
                hotelEditText.setText(currentVacation.getHotel());
                startDateEditText.setText(currentVacation.getStartDate());
                endDateEditText.setText(currentVacation.getEndDate());
            }
        }

        saveButton.setOnClickListener(v -> saveVacation());
        deleteButton.setOnClickListener(v -> deleteVacation());
        shareButton.setOnClickListener(v -> shareVacation());

        startAlertButton.setOnClickListener(v -> setAlert(
                startDateEditText.getText().toString(),
                titleEditText.getText().toString() + " is starting today.",
                vacationID + 1000
        ));

        endAlertButton.setOnClickListener(v -> setAlert(
                endDateEditText.getText().toString(),
                titleEditText.getText().toString() + " is ending today.",
                vacationID + 2000
        ));

        excursionsButton.setOnClickListener(v -> {
            if (vacationID == -1) {
                Toast.makeText(this, "Save vacation before adding excursions.", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(VacationDetails.this, ExcursionList.class);
                intent.putExtra("vacationID", vacationID);
                startActivity(intent);
            }
        });
    }

    private void saveVacation() {
        String title = titleEditText.getText().toString().trim();
        String hotel = hotelEditText.getText().toString().trim();
        String start = startDateEditText.getText().toString().trim();
        String end = endDateEditText.getText().toString().trim();

        if (title.isEmpty() || hotel.isEmpty() || start.isEmpty() || end.isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!isValidDate(start) || !isValidDate(end)) {
            Toast.makeText(this, "Dates must be in MM/dd/yyyy format.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!isEndDateAfterStartDate(start, end)) {
            Toast.makeText(this, "End date must be after start date.", Toast.LENGTH_LONG).show();
            return;
        }

        if (vacationID == -1) {
            Vacation vacation = new Vacation(0, title, hotel, start, end);
            repository.insert(vacation);
            Toast.makeText(this, "Vacation added.", Toast.LENGTH_SHORT).show();
        } else {
            Vacation vacation = new Vacation(vacationID, title, hotel, start, end);
            repository.update(vacation);
            Toast.makeText(this, "Vacation updated.", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void deleteVacation() {
        if (vacationID == -1 || currentVacation == null) {
            Toast.makeText(this, "Vacation has not been saved yet.", Toast.LENGTH_SHORT).show();
            return;
        }

        int count = repository.getExcursionCount(vacationID);

        if (count > 0) {
            Toast.makeText(this, "Cannot delete vacation with associated excursions.", Toast.LENGTH_LONG).show();
        } else {
            repository.delete(currentVacation);
            Toast.makeText(this, "Vacation deleted.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void shareVacation() {
        String details =
                "Vacation: " + titleEditText.getText().toString() + "\n" +
                        "Hotel: " + hotelEditText.getText().toString() + "\n" +
                        "Start Date: " + startDateEditText.getText().toString() + "\n" +
                        "End Date: " + endDateEditText.getText().toString();

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, details);
        sendIntent.setType("text/plain");

        startActivity(Intent.createChooser(sendIntent, "Share vacation details"));
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

    private boolean isEndDateAfterStartDate(String startDate, String endDate) {
        try {
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            return end.after(start);
        } catch (Exception e) {
            return false;
        }
    }

    private void setAlert(String date, String message, int requestCode) {
        if (vacationID == -1) {
            Toast.makeText(this, "Save vacation before setting alerts.", Toast.LENGTH_LONG).show();
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

            Toast.makeText(this, "Alert set.", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Alert could not be set.", Toast.LENGTH_SHORT).show();
        }
    }
}