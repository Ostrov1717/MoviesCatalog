package org.example.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/movie/*")
public class MovieServlet extends HttpServlet {
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter=resp.getWriter();
        Connection connection=(Connection)req.getServletContext().getAttribute("dbconnection");
        String key = req.getRequestURI().substring(7);
        try {
            PreparedStatement statement = connection.prepareStatement("select * from film where film_id=?");
            statement.setInt(1, Integer.parseInt(key));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet!=null&&resultSet.isBeforeFirst()){
                resultSet.next();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", resultSet.getInt("film_id"));
                jsonObject.addProperty("title", resultSet.getString("title"));
                PreparedStatement statement1=connection.prepareStatement("delete from film_actor where film_id=?;\n" +
                        "delete from film where film_id=?");
                statement1.setInt(1, Integer.parseInt(key));
                statement1.setInt(2, Integer.parseInt(key));
                int rs=statement1.executeUpdate();
                printWriter.println(jsonObject);
                printWriter.println("Удален из каталога"+" (Изменено записей: "+rs+" )");
            } else {
                resp.setStatus(404);
                printWriter.println("Фильма с id="+key+" в каталоге нет");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(500);
            printWriter.println("Ошибка сервера");
        }
    }

    @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.setContentType("application/json; charset=utf-8");
            PrintWriter printWriter=resp.getWriter();
            resp.setHeader("Cache-Control", "max-age=120");
            Connection connection=(Connection)req.getServletContext().getAttribute("dbconnection");
            String key = req.getRequestURI().substring(7);
            try {
                PreparedStatement statement=connection.prepareStatement("SELECT film.film_id,film.title,film.description, film_actor.actor_id, actor.first_name, actor.last_name\n" +
                        "FROM film\n" +
                        "LEFT JOIN film_actor ON film.film_id = film_actor.film_id\n" +
                        "LEFT JOIN actor ON film_actor.actor_id=actor.actor_id\n" +
                        "where film.film_id=?");
                statement.setInt(1,Integer.parseInt(key));
                ResultSet resultSet=statement.executeQuery();
                int k=0;
                JsonObject jsonObject = new JsonObject();
                JsonArray actors=new JsonArray();
                if(resultSet!=null&&resultSet.isBeforeFirst()) {
                    while (resultSet.next()) {
                        if (k++ == 0) {
                            jsonObject.addProperty("id", resultSet.getInt("film_id"));
                            jsonObject.addProperty("title", resultSet.getString("title"));
                            jsonObject.addProperty("description", resultSet.getString("description"));
                        }
                        actors.add(resultSet.getString("first_name") + " " + resultSet.getString("last_name"));
                    }
                    jsonObject.add("actors", actors);
                    printWriter.print(jsonObject);
                } else {
                    printWriter.print("Фильм с введенным id не найден");
                    resp.setStatus(404);
                }
            } catch (SQLException e) {
                resp.setStatus(500);
                printWriter.print("Ошибка сервера");
                e.printStackTrace();
            }
        }
    }
