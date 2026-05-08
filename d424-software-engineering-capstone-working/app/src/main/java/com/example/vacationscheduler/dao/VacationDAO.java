package com.example.vacationscheduler.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vacationscheduler.entities.Vacation;

import java.util.List;

@Dao
public interface VacationDAO {

    @Insert
    void insert(Vacation vacation);

    @Update
    void update(Vacation vacation);

    @Delete
    void delete(Vacation vacation);

    @Query("SELECT * FROM vacations ORDER BY vacationID ASC")
    List<Vacation> getAllVacations();

    @Query("SELECT * FROM vacations WHERE vacationID = :id")
    Vacation getVacationByID(int id);

    @Query("SELECT * FROM vacations WHERE vacationTitle LIKE '%' || :query || '%' OR hotel LIKE '%' || :query || '%' OR startDate LIKE '%' || :query || '%' OR endDate LIKE '%' || :query || '%' ORDER BY vacationID ASC")
    List<Vacation> searchVacations(String query);
}