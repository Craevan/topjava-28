package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    void add(final Meal meal);

    void delete(final int mealId);

    void update(final Meal meal);

    Meal getById(final int id);

    List<Meal> getAll();
}
