package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryDao implements MealDao {
    private static final Map<Integer, Meal> REPOSITORY = new ConcurrentHashMap<>();
    private static final AtomicInteger ID = new AtomicInteger();

    {
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    public void add(Meal meal) {
        meal.setId(ID.incrementAndGet());
        REPOSITORY.put(meal.getId(), meal);
    }

    @Override
    public void delete(int mealId) {
        REPOSITORY.remove(mealId);
    }

    @Override
    public void update(Meal meal) {
        REPOSITORY.computeIfPresent(meal.getId(), (id, cureentMeal) -> meal);
    }

    @Override
    public Meal getById(int id) {
        return REPOSITORY.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(REPOSITORY.values());
    }
}
