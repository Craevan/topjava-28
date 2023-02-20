package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.assertMatch;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        final Meal actual = service.get(MealTestData.FIRST_MEAL_ID, MealTestData.USER_ID);
        System.out.println(actual);
        assertMatch(actual, MealTestData.userMeals.get(0));
    }

    @Test
    public void getAll() {
        final List<Meal> actualUserMeals = service.getAll(MealTestData.USER_ID);
        final List<Meal> expectedUserMeals = new ArrayList<>(MealTestData.userMeals);
        Collections.reverse(expectedUserMeals);
        assertMatch(actualUserMeals, expectedUserMeals);

        final List<Meal> actualAdminMeals = service.getAll(MealTestData.ADMIN_ID);
        final List<Meal> expectedAdminMeals = new ArrayList<>(MealTestData.adminMeals);
        Collections.reverse(expectedAdminMeals);
        assertMatch(actualAdminMeals, expectedAdminMeals);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.FIRST_MEAL_ID, MealTestData.NOT_FOUND));
    }

    @Test
    public void getForeignMeal() {
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.FIRST_MEAL_ID, MealTestData.ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        assertMatch(service.getBetweenInclusive(
                        LocalDate.of(2020, Month.JANUARY, 31),
                        LocalDate.of(2020, Month.JANUARY, 31), MealTestData.ADMIN_ID),
                MealTestData.adminMeals.get(1),
                MealTestData.adminMeals.get(0));
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, MealTestData.userMeals.get(0).getDateTime(), "DuplicateDateTime", 100), MealTestData.USER_ID));
    }

    @Test
    public void delete() {
        service.delete(MealTestData.FIRST_MEAL_ID, MealTestData.USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.FIRST_MEAL_ID, MealTestData.USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(MealTestData.FIRST_MEAL_ID, MealTestData.NOT_FOUND));
    }

    @Test
    public void deleteForeignMeal() {
        assertThrows(NotFoundException.class, () -> service.delete(MealTestData.FIRST_MEAL_ID, MealTestData.ADMIN_ID));
    }

    @Test
    public void update() {
        final Meal updated = MealTestData.getUpdated();
        service.update(updated, MealTestData.USER_ID);
        assertMatch(service.get(MealTestData.userMeals.get(0).getId(), MealTestData.USER_ID), MealTestData.getUpdated());

    }

    @Test
    public void updateForeignMeal() {
        assertThrows(NotFoundException.class, () -> service.update(MealTestData.userMeals.get(0), MealTestData.ADMIN_ID));
    }

    @Test
    public void updateNotFound() {
        assertThrows(NotFoundException.class, () -> service.update(MealTestData.userMeals.get(0), MealTestData.NOT_FOUND));
    }

    @Test
    public void create() {
        final Meal created = service.create(MealTestData.getNew(), MealTestData.USER_ID);
        final Integer id = created.getId();
        final Meal newMeal = MealTestData.getNew();
        newMeal.setId(id);
        assertMatch(created, newMeal);
        assertMatch(service.get(id, MealTestData.USER_ID), newMeal);
    }
}
