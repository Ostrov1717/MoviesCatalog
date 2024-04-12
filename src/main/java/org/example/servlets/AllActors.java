package org.example.servlets;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@WebServlet("/actors")
public class AllActors extends HttpServlet {
    private static final int PAGE_SIZE = 10;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");
        resp.setHeader("Cache-Control", "max-age=240");
        PrintWriter printWriter = resp.getWriter();
        String strPage= req.getParameter("page");
        JsonArray actors=new JsonArray();
        try{
            actors = allActors(req);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(strPage==null){
            printWriter.print(actors);
        } else {
            int page = Integer.parseInt(strPage);
            int startIndex = (page - 1) * PAGE_SIZE;
            int endIndex = Math.min(startIndex + PAGE_SIZE, actors.size());
            JsonArray subActors=new JsonArray();
            for(int i=startIndex;i<endIndex;i++){
                subActors.add(actors.get(i));
            }
            printWriter.println(subActors);
        }
    }

    private JsonArray arrayFilms(String filmsInfo) {
        JsonArray filmsArray = new JsonArray();
        String[] stringsArray = filmsInfo.split(",");
        for (String film : stringsArray) {
            if (film.contains(":")) {
                String title = film.split(":")[1].trim();
                filmsArray.add(title);
            } else {
                filmsArray.add(film.trim());
            }
        }
        return filmsArray;
    }

    private JsonArray allActors(HttpServletRequest req) throws SQLException {
        Connection connection = (Connection) req.getServletContext().getAttribute("dbconnection");
        JsonArray actors = new JsonArray();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM actor_info ORDER BY actor_id");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet != null && resultSet.next()) {
            JsonObject actor = new JsonObject();
            int actorId = resultSet.getInt("actor_id");
            actor.addProperty("id", actorId);
            actor.addProperty("firstName", resultSet.getString("first_name"));
            actor.addProperty("lastName", resultSet.getString("last_name"));
            String filmInfo = resultSet.getString("film_info");
            actor.add("movies", arrayFilms(filmInfo));
            actors.add(actor);
        }
        return actors;
    }
}
