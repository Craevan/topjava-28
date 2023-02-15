package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(final MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(final Meal meal, final int userId) {
        return repository.save(meal, userId);
    }

    public void delete(final int id, final int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public Meal get(final int id, final int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public List<Meal> getAll(final int userId) {
        return (List<Meal>) repository.getAll(userId);
    }

    public void update(final Meal meal, final int userId) {
        checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    public List<Meal> getFiltered(final LocalDate start, final LocalDate end, final int userId) {
        return (List<Meal>) repository.getFiltered(start, end, userId);
    }
}
