package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.InMemoryMealDao;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealDao dataBase;

    @Override
    public void init() {
        dataBase = new InMemoryMealDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action == null ? "" : action) {
            case ("create"):
            case ("edit"):
                Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", MealsUtil.DEFAULT_CALORIES_COUNT) :
                        dataBase.getById(getId(request.getParameter("id")));
                log.info("Create/Update {}", meal);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/editMeal.jsp").forward(request, response);
                break;
            case("delete"):
                log.info("Delete meal with id = {}", request.getParameter("id"));
                dataBase.delete(getId(request.getParameter("id")));
                response.sendRedirect("meals");
                break;
            default:
                log.info("Get all meals");
                request.setAttribute("meals", MealsUtil.filteredByStreams(
                        dataBase.getAll(),
                        LocalTime.MIN,
                        LocalTime.MAX,
                        MealsUtil.DEFAULT_CALORIES_COUNT));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        LocalDateTime ldt = LocalDateTime.parse(request.getParameter("date"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        if (id.isEmpty()) {
            Meal meal = new Meal(ldt, description, calories);
            log.info("Create {}", meal);
            dataBase.add(meal);
        } else {
            Meal meal = new Meal(getId(id), ldt, description, calories);
            log.info("Update {}", meal);
            dataBase.update(meal);
        }
        response.sendRedirect("meals");
    }

    private int getId(final String param) {
        return Integer.parseInt(param);
    }
}
