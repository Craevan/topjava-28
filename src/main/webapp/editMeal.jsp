<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${param.action == 'create' ? 'Add Meal' : 'Edit meal'}</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>${param.action == 'create' ? 'Add Meal' : 'Edit meal'}</h2>
<form method="post">
    <input type="hidden" name="id" value="${meal.id}">

    <label>
        Date and Time: <input type="datetime-local" name="date" value="${meal.dateTime}"/>
    </label>
    <label>
        Description: <input type="text" name="description" value="${meal.description}"/>
    </label>
    <label>
        Calories: <input type="number" name="calories" value="${meal.calories}"/>
    </label>
    <button type="submit">Save</button>
    <button type="button" onclick="history.back();">Back</button>
</form>
</body>
</html>
