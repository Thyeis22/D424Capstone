package com.example.vacationscheduler.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vacationscheduler.entities.Excursion;

import java.util.List;

@Dao
public interface ExcursionDAO {

    @Insert
    void insert(Excursion excursion);

    @Update
    void update(Excursion excursion);

    @Delete
    void delete(Excursion excursion);

    @Query("SELECT * FROM excursions ORDER BY excursionID ASC")
    List<Excursion> getAllExcursions();

    @Query("SELECT * FROM excursions WHERE vacationID = :vacationID ORDER BY excursionID ASC")
    List<Excursion> getAssociatedExcursions(int vacationID);

    @Query("SELECT COUNT(*) FROM excursions WHERE vacationID = :vacationID")
    int getExcursionCount(int vacationID);

    @Query("SELECT * FROM excursions WHERE excursionID = :id")
    Excursion getExcursionByID(int id);

    @Query("SELECT * FROM excursions WHERE excursionTitle LIKE '%' || :query || '%' OR excursionDate LIKE '%' || :query || '%' ORDER BY excursionID ASC")
    List<Excursion> searchExcursions(String query);
}