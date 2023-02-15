package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, InMemoryUserRepository.USER_ID));
        save(new Meal(LocalDateTime.now(), "TEST RECORD", SecurityUtil.authUserCaloriesPerDay()), InMemoryUserRepository.ADMIN_ID);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        final Map<Integer, Meal> mealMap = repository.computeIfAbsent(userId, id -> new ConcurrentHashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealMap.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return mealMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        final Map<Integer, Meal> mealMap = repository.get(userId);
        return mealMap != null && mealMap.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        final Map<Integer, Meal> mealMap = repository.get(userId);
        return mealMap == null ? null : mealMap.get(id);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        final Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap == null || mealMap.isEmpty()) {
            return Collections.emptyList();
        }
        return mealMap.values()
                .stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getFiltered(final LocalDate start, final LocalDate end, final int userId) {
        final Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap == null || mealMap.isEmpty()) {
            return Collections.emptyList();
        }
        return mealMap.values()
                .stream()
                .filter(meal -> DateTimeUtil.isInside(meal.getDate(), start, end))
                .collect(Collectors.toList());
    }
}
