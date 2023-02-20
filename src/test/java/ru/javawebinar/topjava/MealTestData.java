package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int FIRST_MEAL_ID = START_SEQ + 3;
    public static final int NOT_FOUND = 10;
    public static final List<Meal> userMeals = Arrays.asList(
            new Meal(FIRST_MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(FIRST_MEAL_ID + 1, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(FIRST_MEAL_ID + 2, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(FIRST_MEAL_ID + 3, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(FIRST_MEAL_ID + 4, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(FIRST_MEAL_ID + 5, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(FIRST_MEAL_ID + 6, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));

    public static final List<Meal> adminMeals = Arrays.asList(
            new Meal(FIRST_MEAL_ID + 7, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 600),
            new Meal(FIRST_MEAL_ID + 8, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 1000)
    );

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2023, Month.FEBRUARY, 18, 10, 0), "TEST RECORD", 1000);
    }

    public static Meal getUpdated() {
        return new Meal(
                FIRST_MEAL_ID,
                LocalDateTime.of(2023, Month.FEBRUARY, 19, 11, 11),
                "UPDATED TEST DESCRIPTION",
                1500);
    }
}
