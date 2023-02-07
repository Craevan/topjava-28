package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.InMemoryDao;
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
    private static final Logger log = getLogger(UserServlet.class);
    private static final MealDao dataBase = new InMemoryDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            log.info("Get all meals");
            request.setAttribute("meals", MealsUtil.filteredByStreams(
                    dataBase.getAll(),
                    LocalTime.MIN,
                    LocalTime.MAX,
                    MealsUtil.DEFAULT_CALORIES_COUNT));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else if ("create".equals(action) || "edit".equals(action)) {
            Meal meal = "create".equals(action) ?
                    new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", MealsUtil.DEFAULT_CALORIES_COUNT) :
                    dataBase.getById(Integer.parseInt(request.getParameter("id")));
            log.info("Create/Update {}", meal);
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("/edit_meal.jsp").forward(request, response);
        } else if ("delete".equals(action)) {
            log.info("Delete meal with id = {}", request.getParameter("id"));
            dataBase.delete(Integer.parseInt(request.getParameter("id")));
            response.sendRedirect("meals");
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
            Meal meal = new Meal(Integer.parseInt(id), ldt, description, calories);
            log.info("Update {}", meal);
            dataBase.update(meal);
        }
        response.sendRedirect("meals");
    }
}
