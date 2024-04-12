package org.example.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

@WebServlet("/movies")
public class AllMovies extends HttpServlet {

    private static final int PAGE_SIZE = 10;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");
        resp.setHeader("Cache-Control", "max-age=240");
        PrintWriter printWriter = resp.getWriter();
        String strPage = req.getParameter("page");
        JsonArray movies = new JsonArray();
        try {
            movies = allMovies(req);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (strPage == null) {
            printWriter.print(movies);
        } else {
            int page = Integer.parseInt(strPage);
            int startIndex = (page - 1) * PAGE_SIZE;
            int endIndex = Math.min(startIndex + PAGE_SIZE, movies.size());
            JsonArray subMovies = new JsonArray();
            for (int i = startIndex; i < endIndex; i++) {
                subMovies.add(movies.get(i));
            }
            printWriter.println(subMovies);
        }
    }
    private JsonArray arrayActors(String actors){
        JsonArray actorsArray=new JsonArray();
        String[] stringsArray=actors.split(",");
        for(String actor:stringsArray){
            actorsArray.add(actor.trim());
        }
        return actorsArray;
    }
    private JsonArray allMovies (HttpServletRequest req) throws SQLException {
        JsonArray movies=new JsonArray();
        Connection connection = (Connection) req.getServletContext().getAttribute("dbconnection");
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM film_list ORDER BY fid");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet != null && resultSet.next()) {
            JsonObject movie = new JsonObject();
            movie.addProperty("id", resultSet.getInt("fid"));
            movie.addProperty("title", resultSet.getString("title"));
            movie.addProperty("description", resultSet.getString("description"));
            String actors = resultSet.getString("actors");
            movie.add("actors", arrayActors(actors));
            movies.add(movie);
        }
        return movies;
}
}
