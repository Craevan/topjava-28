package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository {
    // null if updated meal does not belong to userId
    Meal save(final Meal meal, final int userId);

    // false if meal does not belong to userId
    boolean delete(final int id, final int userId);

    // null if meal does not belong to userId
    Meal get(final int id, final int userId);

    // ORDERED dateTime desc
    List<Meal> getAll(final int userId);

    List<Meal> getFiltered(final LocalDate start, final LocalDate end, final int userId);
}
