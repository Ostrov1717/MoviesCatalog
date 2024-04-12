package org.example.servlets;

import com.google.gson.Gson;
import org.example.Movie;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/movie")
public class PostMovie extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.setContentType("application/json; charset=utf-8");
            String data = getReqBody(req);
            Gson g = new Gson();
            Movie movie = g.fromJson(data, Movie.class);
            Connection connection = (Connection) req.getServletContext().getAttribute("dbconnection");
            try {
                PreparedStatement statement = connection.prepareStatement("insert into film (film_id,title,description,language_id,length,release_year) values (?,?,?,2,90,2024)");
                statement.setInt(1, movie.id);
                statement.setString(2, movie.title);
                statement.setString(3, movie.description);
                int rs = statement.executeUpdate();
                int[] actors = movie.actors;
                for (int i = 0; i < actors.length; i++) {
                    PreparedStatement statement1 = connection.prepareStatement("insert into film_actor (film_id,actor_id) values (?,?)");
                    statement1.setInt(1, movie.id);
                    statement1.setInt(2, actors[i]);
                    int rs1 = statement1.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            HttpServletRequestWrapper requestWrapper = changeMethod(req);
            req.getRequestDispatcher("/movie/" + movie.id).forward(requestWrapper, resp);
    }

    private static HttpServletRequestWrapper changeMethod(HttpServletRequest req) {
        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(req) {
            @Override
            public String getMethod() {
                return "GET";
            }
        };
        return requestWrapper;
    }

    private static String getReqBody(HttpServletRequest req) throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
            buffer.append(System.lineSeparator());
        }
        String data = buffer.toString();
        return data;
    }

}
