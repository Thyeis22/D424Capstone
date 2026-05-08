package com.example.vacationscheduler.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.vacationscheduler.models.BaseEntity;
import com.example.vacationscheduler.models.Reportable;

@Entity(tableName = "vacations")
public class Vacation extends BaseEntity implements Reportable {

    @PrimaryKey(autoGenerate = true)
    private int vacationID;

    private String vacationTitle;
    private String hotel;
    private String startDate;
    private String endDate;

    public Vacation(int vacationID, String vacationTitle, String hotel, String startDate, String endDate) {
        this.vacationID = vacationID;
        this.vacationTitle = vacationTitle;
        this.hotel = hotel;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public int getId() {
        return vacationID;
    }

    @Override
    public String getDisplayName() {
        return vacationTitle;
    }

    @Override
    public String[] getReportRow() {
        return new String[]{vacationTitle, hotel, startDate, endDate};
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

    public String getVacationTitle() {
        return vacationTitle;
    }

    public void setVacationTitle(String vacationTitle) {
        this.vacationTitle = vacationTitle;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}