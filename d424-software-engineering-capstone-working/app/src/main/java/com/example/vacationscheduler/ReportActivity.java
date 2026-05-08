package com.example.vacationscheduler;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.vacationscheduler.entities.Excursion;
import com.example.vacationscheduler.entities.Vacation;
import com.example.vacationscheduler.repository.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportActivity extends Activity {

    private TableLayout reportTable;
    private TextView reportTitle;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        repository = new Repository(getApplication());
        reportTable = findViewById(R.id.reportTable);
        reportTitle = findViewById(R.id.reportTitle);

        String timestamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US).format(new Date());
        reportTitle.setText("Vacation Scheduler Report\nGenerated: " + timestamp);

        addRow("Type", "Title", "Date / Hotel", "Related ID");

        List<Vacation> vacations = repository.getAllVacations();
        for (Vacation vacation : vacations) {
            addRow("Vacation", vacation.getVacationTitle(), vacation.getHotel(), vacation.getStartDate() + " - " + vacation.getEndDate());
        }

        List<Excursion> excursions = repository.getAllExcursions();
        for (Excursion excursion : excursions) {
            addRow("Excursion", excursion.getExcursionTitle(), excursion.getExcursionDate(), "Vacation ID: " + excursion.getVacationID());
        }
    }

    private void addRow(String col1, String col2, String col3, String col4) {
        TableRow row = new TableRow(this);

        TextView t1 = makeCell(col1);
        TextView t2 = makeCell(col2);
        TextView t3 = makeCell(col3);
        TextView t4 = makeCell(col4);

        row.addView(t1);
        row.addView(t2);
        row.addView(t3);
        row.addView(t4);

        reportTable.addView(row);
    }

    private TextView makeCell(String text) {
        TextView cell = new TextView(this);
        cell.setText(text);
        cell.setPadding(8, 8, 8, 8);
        return cell;
    }
}