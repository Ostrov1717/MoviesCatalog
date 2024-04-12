package org.example.servlets;

import com.google.gson.Gson;
import org.example.Actor;

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

@WebServlet("/actor")
public class PostActor extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");
        String data = getReqBody(req);
        Gson g=new Gson();
        Actor actor=g.fromJson(data,Actor.class);
        Connection connection=(Connection)req.getServletContext().getAttribute("dbconnection");
        try {
            PreparedStatement statement = connection.prepareStatement("insert into actor (actor_id,first_name,last_name) values (?,?,?)");
            statement.setInt(1, actor.id);
            statement.setString(2, actor.firstName);
            statement.setString(3, actor.lastName);

            int rs=statement.executeUpdate();
            int[] movies= actor.movies;
            for(int i=0;i<movies.length;i++){
                PreparedStatement statement1 = connection.prepareStatement("insert into film_actor (actor_id,film_id) values (?,?)");
                statement1.setInt(1, actor.id);
                statement1.setInt(2,movies[i]);
                int rs1=statement1.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        HttpServletRequestWrapper requestWrapper = changeMethod(req);
        req.getRequestDispatcher("/actor/"+actor.id).forward(requestWrapper,resp);
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

    private static HttpServletRequestWrapper changeMethod(HttpServletRequest req) {
        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(req) {
            @Override
            public String getMethod() {
                return "GET";
            }
        };
        return requestWrapper;
    }

}
