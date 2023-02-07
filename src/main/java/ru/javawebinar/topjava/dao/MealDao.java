package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    Meal add(final Meal meal);

    void delete(final int id);

    Meal update(final Meal meal);

    Meal getById(final int id);

    List<Meal> getAll();
}
