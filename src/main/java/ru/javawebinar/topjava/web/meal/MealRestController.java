package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {
    private static final Logger logger = LoggerFactory.getLogger(MealRestController.class);

    private final MealService service;

    public MealRestController(final MealService service) {
        this.service = service;
    }

    public Meal get(final int id) {
        logger.info("get {}", id);
        int userId = SecurityUtil.authUserId();
        return service.get(id, userId);
    }

    public List<MealTo> getAll() {
        final int userId = SecurityUtil.authUserId();
        logger.info("user with ID = {} geting all meals", userId);
        return MealsUtil.getTos(service.getAll(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public Meal create(final Meal meal) {
        final int userId = SecurityUtil.authUserId();
        ValidationUtil.checkNew(meal);
        logger.info("user with ID = {} creating {}", userId, meal);
        return service.create(meal, userId);
    }

    public void delete(final int id) {
        logger.info("delete {}", id);
        int userId = SecurityUtil.authUserId();
        service.delete(id, userId);
    }

    public void update(final Meal meal, final int id) {
        final int userId = SecurityUtil.authUserId();
        ValidationUtil.assureIdConsistent(meal, id);
        logger.info("user with ID = {} updating {}", userId, meal);
        service.update(meal, userId);
    }

    public List<MealTo> getFilteredByDateTime(final LocalDate startDate, final LocalDate endDate,
                                              final LocalTime startTime, final LocalTime endTime) {
        final int userId = SecurityUtil.authUserId();
        logger.info("user with ID = {} getting filtered by date {} - {} and time {} - {}", userId, startDate, endDate, startTime, endTime);
        List<Meal> meals = service.getFiltered(startDate, endDate, userId);
        return MealsUtil.getFilteredTos(meals, MealsUtil.DEFAULT_CALORIES_PER_DAY, startTime, endTime);
    }
}
