package com.example.vacationscheduler.repository;

import android.app.Application;

import com.example.vacationscheduler.dao.ExcursionDAO;
import com.example.vacationscheduler.dao.VacationDAO;
import com.example.vacationscheduler.database.VacationDatabase;
import com.example.vacationscheduler.entities.Excursion;
import com.example.vacationscheduler.entities.Vacation;

import java.util.List;

public class Repository {

    private VacationDAO vacationDAO;
    private ExcursionDAO excursionDAO;

    public Repository(Application application) {
        VacationDatabase db = VacationDatabase.getDatabase(application);
        vacationDAO = db.vacationDAO();
        excursionDAO = db.excursionDAO();
    }

    // Vacation methods
    public List<Vacation> getAllVacations() {
        return vacationDAO.getAllVacations();
    }

    public Vacation getVacationByID(int id) {
        return vacationDAO.getVacationByID(id);
    }

    public void insert(Vacation vacation) {
        vacationDAO.insert(vacation);
    }

    public void update(Vacation vacation) {
        vacationDAO.update(vacation);
    }

    public void delete(Vacation vacation) {
        vacationDAO.delete(vacation);
    }

    public List<Vacation> searchVacations(String query) {
        return vacationDAO.searchVacations(query);
    }

    // Excursion methods
    public List<Excursion> getAllExcursions() {
        return excursionDAO.getAllExcursions();
    }

    public List<Excursion> getAssociatedExcursions(int vacationID) {
        return excursionDAO.getAssociatedExcursions(vacationID);
    }

    public Excursion getExcursionByID(int id) {
        return excursionDAO.getExcursionByID(id);
    }

    public int getExcursionCount(int vacationID) {
        return excursionDAO.getExcursionCount(vacationID);
    }

    public void insert(Excursion excursion) {
        excursionDAO.insert(excursion);
    }

    public void update(Excursion excursion) {
        excursionDAO.update(excursion);
    }

    public void delete(Excursion excursion) {
        excursionDAO.delete(excursion);
    }

    public List<Excursion> searchExcursions(String query) {
        return excursionDAO.searchExcursions(query);
    }
}
