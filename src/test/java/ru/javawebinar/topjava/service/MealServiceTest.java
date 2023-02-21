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
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
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
        final Meal actual = service.get(MealTestData.FIRST_USER_MEAL_ID, UserTestData.USER_ID);
        assertMatch(actual, MealTestData.userMeal1);
    }

    @Test
    public void getAll() {
        final List<Meal> actualUserMeals = service.getAll(UserTestData.USER_ID);
        final List<Meal> expectedUserMeals = Arrays.asList(
                MealTestData.userMeal7,
                MealTestData.userMeal6,
                MealTestData.userMeal5,
                MealTestData.userMeal4,
                MealTestData.userMeal3,
                MealTestData.userMeal2,
                MealTestData.userMeal1
        );
        assertMatch(actualUserMeals, expectedUserMeals);

        final List<Meal> actualAdminMeals = service.getAll(UserTestData.ADMIN_ID);
        final List<Meal> expectedAdminMeals = Arrays.asList(
                MealTestData.adminMeal2,
                MealTestData.adminMeal1
        );
        assertMatch(actualAdminMeals, expectedAdminMeals);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.FIRST_USER_MEAL_ID, UserTestData.NOT_FOUND));
    }

    @Test
    public void getForeignMeal() {
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.FIRST_USER_MEAL_ID, UserTestData.ADMIN_ID));
    }

    @Test
    public void getNonExistingMeal() {
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.NON_EXISTING_MEAL_ID, UserTestData.USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        assertMatch(service.getBetweenInclusive(
                        LocalDate.of(2020, Month.JANUARY, 30),
                        LocalDate.of(2020, Month.JANUARY, 30), UserTestData.USER_ID),
                MealTestData.userMeal3,
                MealTestData.userMeal2,
                MealTestData.userMeal1);
    }

    @Test
    public void getFilteredWithoutDate() {
        final List<Meal> expected = Arrays.asList(
                MealTestData.userMeal7,
                MealTestData.userMeal6,
                MealTestData.userMeal5,
                MealTestData.userMeal4,
                MealTestData.userMeal3,
                MealTestData.userMeal2,
                MealTestData.userMeal1
        );
        assertMatch(service.getBetweenInclusive(null, null, UserTestData.USER_ID), expected);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, MealTestData.userMeal1.getDateTime(), "DuplicateDateTime", 100), UserTestData.USER_ID));
    }

    @Test
    public void delete() {
        service.delete(MealTestData.FIRST_USER_MEAL_ID, UserTestData.USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.FIRST_USER_MEAL_ID, UserTestData.USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(MealTestData.FIRST_USER_MEAL_ID, UserTestData.NOT_FOUND));
    }

    @Test
    public void deleteForeignMeal() {
        assertThrows(NotFoundException.class, () -> service.delete(MealTestData.FIRST_USER_MEAL_ID, UserTestData.ADMIN_ID));
    }

    @Test
    public void deleteNonExistingMeal() {
        assertThrows(NotFoundException.class, () -> service.delete(MealTestData.NON_EXISTING_MEAL_ID, UserTestData.USER_ID));
    }

    @Test
    public void update() {
        final Meal updated = MealTestData.getUpdated();
        service.update(updated, UserTestData.USER_ID);
        assertMatch(service.get(MealTestData.FIRST_USER_MEAL_ID, UserTestData.USER_ID), MealTestData.getUpdated());
    }

    @Test
    public void updateForeignMeal() {
        assertThrows(NotFoundException.class, () -> service.update(MealTestData.getUpdated(), UserTestData.ADMIN_ID));
    }

    @Test
    public void updateNotFound() {
        assertThrows(NotFoundException.class, () -> service.update(MealTestData.getUpdated(), UserTestData.NOT_FOUND));
    }

    @Test
    public void create() {
        final Meal created = service.create(MealTestData.getNew(), UserTestData.USER_ID);
        final Integer id = created.getId();
        final Meal newMeal = MealTestData.getNew();
        newMeal.setId(id);
        assertMatch(created, newMeal);
        assertMatch(service.get(id, UserTestData.USER_ID), newMeal);
    }
}
